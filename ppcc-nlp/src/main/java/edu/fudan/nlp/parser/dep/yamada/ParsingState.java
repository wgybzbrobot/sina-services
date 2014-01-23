package edu.fudan.nlp.parser.dep.yamada;

import java.util.*;

import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.SparseVector;

/**
 * 句法分析过程中的状态，及在此状态上的一系列操作
 * 
 * 句法分析由状态的转换完成，转换操作涉及在当前状态提取特征，动作执行。 动作的预测在Parser 中完成
 * 
 * @author cshen
 * @version Feb 16, 2009
 * @see Parser
 */
public class ParsingState {
	/**
	 * 动作类型
	 * 
	 * @author xpqiu
	 * 
	 */
	public enum Action {
		SHIFT, LEFT, RIGHT
	};

	Sentence instance;
	ArrayList<Tree> subtrees;
	int leftFocus;

	// 非SHIFT动作中概率较大的动作的概率
	private double[] probsOfBuild;

	// 非SHIFT动作中概率较大的动作
	private Action[] actionsOfBuild;

	// 是否执行过非SHIFT动作
	private boolean isUpdated = false;

	private boolean isFinal = false;

	/**
	 * 构造函数
	 * 
	 * 由句子实例初始化状态
	 * 
	 * @param instance
	 *            句子实例
	 */
	public ParsingState(Sentence instance) {
		subtrees = new ArrayList<Tree>();
		for (int i = 0; i < instance.length(); i++) {
			Tree tree = new Tree(i);
			subtrees.add(tree);
		}
		this.instance = instance;

		probsOfBuild = new double[subtrees.size() - 1];
		actionsOfBuild = new Action[subtrees.size() - 1];
	}

	public boolean isFinalState() {
		return subtrees.size() == 1 || isFinal;
	}

	public int[] getFocusIndices() {
		assert (!isFinalState());

		int[] indices = new int[2];
		indices[0] = subtrees.get(leftFocus).root;
		indices[1] = subtrees.get(leftFocus + 1).root;
		return indices;
	}

	/**
	 * 得到当前状态的特征
	 * 
	 * @return 特征表，其中key是有用的，value没有用
	 * @throws Exception
	 */
	public SparseVector getFeatures() {
		if (isFinalState())
			return null;

		AlphabetFactory factory = AlphabetFactory.buildFactory();
		FeatureAlphabet features = factory.buildFeatureAlphabet("feature");

		int rightFocus = leftFocus + 1;

		SparseVector vec = new SparseVector();

		// 设定上下文窗口大小
		int l = 2;
		int r = 2;
		int idx = -1;
		for (int i = 0; i <= l; i++) {
			// 词性特征前缀
			String posFeature = "-" + new Integer(i).toString() + "/pos/";
			String lexFeature = "-" + new Integer(i).toString() + "/lex/";

			String lcLexFeature = "-" + new Integer(i).toString()
					+ "/ch-L-lex/";
			String lcPosFeature = "-" + new Integer(i).toString()
					+ "/ch-L-pos/";
			String rcLexFeature = "-" + new Integer(i).toString()
					+ "/ch-R-lex/";
			String rcPosFeature = "-" + new Integer(i).toString()
					+ "/ch-R-pos/";

			if (leftFocus - i < 0) {
				addFeature(features, vec,
						lexFeature + "START" + String.valueOf(i - leftFocus));
				addFeature(features, vec,
						posFeature + "START" + String.valueOf(i - leftFocus));
			} else {
				addFeature(
						features,
						vec,
						lexFeature
								+ instance.forms[subtrees.get(leftFocus - i).root]);
				addFeature(features, vec, posFeature
						+ instance.postags[subtrees.get(leftFocus - i).root]);

				if (subtrees.get(leftFocus - i).leftChildren.size() == 0) {
					// features.put(lcLexFeature + "None", 1);
					// features.put(lcPosFeature + "None", 1);
				} else {
					for (int j = 0; j < subtrees.get(leftFocus - i).leftChildren
							.size(); j++) {
						int leftChildIndex = subtrees.get(leftFocus - i).leftChildren
								.get(j).root;
						addFeature(features, vec, lcLexFeature
								+ instance.forms[leftChildIndex]);
						addFeature(features, vec, lcPosFeature
								+ instance.postags[leftChildIndex]);
					}
				}

				if (subtrees.get(leftFocus - i).rightChildren.size() == 0) {
					// features.put(rcLexFeature + "None", 1);
					// features.put(rcPosFeature + "None", 1);
				} else {
					for (int j = 0; j < subtrees.get(leftFocus - i).rightChildren
							.size(); j++) {
						int rightChildIndex = subtrees.get(leftFocus - i).rightChildren
								.get(j).root;
						addFeature(features, vec, rcLexFeature
								+ instance.forms[rightChildIndex]);
						addFeature(features, vec, rcPosFeature
								+ instance.postags[rightChildIndex]);
					}
				}
			}
		}

		for (int i = 0; i <= r; i++) {
			String posFeature = "+" + new Integer(i).toString() + "/pos/";
			String lexFeature = "+" + new Integer(i).toString() + "/lex/";

			String lcLexFeature = "+" + new Integer(i).toString()
					+ "/ch-L-lex/";
			String lcPosFeature = "+" + new Integer(i).toString()
					+ "/ch-L-pos/";
			String rcLexFeature = "+" + new Integer(i).toString()
					+ "/ch-R-lex/";
			String rcPosFeature = "+" + new Integer(i).toString()
					+ "/ch-R-pos/";

			if (rightFocus + i >= subtrees.size()) {
				addFeature(
						features,
						vec,
						lexFeature
								+ "END"
								+ String.valueOf(rightFocus + i
										- subtrees.size() + 3));
				addFeature(
						features,
						vec,
						posFeature
								+ "END"
								+ String.valueOf(rightFocus + i
										- subtrees.size() + 3));
			} else {
				addFeature(
						features,
						vec,
						lexFeature
								+ instance.forms[subtrees.get(rightFocus + i).root]);
				addFeature(features, vec, posFeature
						+ instance.postags[subtrees.get(rightFocus + i).root]);

				if (subtrees.get(rightFocus + i).leftChildren.size() == 0) {
					// features.put(lcLexFeature + "None", 1);
					// features.put(lcPosFeature + "None", 1);
				} else {
					for (int j = 0; j < subtrees.get(rightFocus + i).leftChildren
							.size(); j++) {
						int leftChildIndex = subtrees.get(rightFocus + i).leftChildren
								.get(j).root;
						addFeature(features, vec, lcLexFeature
								+ instance.forms[leftChildIndex]);
						addFeature(features, vec, lcPosFeature
								+ instance.postags[leftChildIndex]);
					}
				}

				if (subtrees.get(rightFocus + i).rightChildren.size() == 0) {
					// features.put(rcLexFeature + "None", 1);
					// features.put(rcPosFeature + "None", 1);
				} else {
					for (int j = 0; j < subtrees.get(rightFocus + i).rightChildren
							.size(); j++) {
						int rightChildIndex = subtrees.get(rightFocus + i).rightChildren
								.get(j).root;
						addFeature(features, vec, rcLexFeature
								+ instance.forms[rightChildIndex]);
						addFeature(features, vec, rcPosFeature
								+ instance.postags[rightChildIndex]);
					}
				}
			}
		}

		return vec;
	}

	private void addFeature(FeatureAlphabet features, SparseVector vec,
			String str) {
		int idx = features.lookupIndex(str, 3);
		if (idx != -1) {
			vec.add(idx, 1);
		}
	}

	/**
	 * 状态转换，动作为SHIFT
	 * 
	 * 动作为SHIFT，但保存第二大可能的动作，当一列动作都是SHIFT时，执行概率最大的第二大动作
	 * 
	 * @param action
	 *            第二大可能的动作
	 * @param prob
	 *            第二大可能的动作的概率
	 */
	public void next(Action action, double prob) {
		probsOfBuild[leftFocus] = prob;
		actionsOfBuild[leftFocus] = action;
		leftFocus++;

		if (leftFocus >= subtrees.size() - 1) {
			if (!isUpdated) {
				int maxIndex = 0;
				double maxValue = 0;
				for (int i = 0; i < probsOfBuild.length; i++)
					if (probsOfBuild[i] > maxValue) {
						maxValue = probsOfBuild[i];
						maxIndex = i;
					}
				leftFocus = maxIndex;
				next(actionsOfBuild[leftFocus]);
			}

			back();
		}
	}

	/**
	 * 将序列第一二个词设为焦点词
	 */
	private void back() {
		isUpdated = false;
		leftFocus = 0;

		probsOfBuild = new double[subtrees.size() - 1];
		actionsOfBuild = new Action[subtrees.size() - 1];
	}

	/**
	 * 状态转换, 执行动作
	 * 
	 * @param action
	 *            要执行的动作
	 */
	public void next(Action action) {
		// assert (action.equalsIgnoreCase("left")
		// || action.equalsIgnoreCase("right") || action
		// .equalsIgnoreCase("shift"));
		assert (!isFinalState());

		// 左焦点词在句子中的位置
		int lNode = subtrees.get(leftFocus).root;
		int rNode = subtrees.get(leftFocus + 1).root;

		switch (action) {
		case LEFT:
			// add for counting two types of errors
			// if (instance.heads[rNode] == lNode)
			// break;

			// end

			subtrees.get(leftFocus).addRightChild(subtrees.get(leftFocus + 1));
			subtrees.remove(leftFocus + 1);
			isUpdated = true;

			break;
		case RIGHT:

			// add for counting two types of errors
			// if (instance.heads[lNode] == rNode)
			// break;

			// end

			subtrees.get(leftFocus + 1).addLeftChild(subtrees.get(leftFocus));
			subtrees.remove(leftFocus);
			isUpdated = true;

			break;
		default:
			leftFocus++;
		}

		if (leftFocus >= subtrees.size() - 1) {
			if (!isUpdated) {
				isFinal = true;
			}
			back();
		}
	}

	/**
	 * 保存当前状态所包含的依赖关系到句子实例中
	 */
	public void saveRelation() {
		for (int i = 0; i < subtrees.size(); i++) {
			saveRelation(subtrees.get(i));
		}
	}

	private void saveRelation(Tree t) {
		for (int i = 0; i < t.leftChildren.size(); i++) {
			instance.heads[t.leftChildren.get(i).root] = t.root;
			saveRelation(t.leftChildren.get(i));
		}
		for (int i = 0; i < t.rightChildren.size(); i++) {
			instance.heads[t.rightChildren.get(i).root] = t.root;
			saveRelation(t.rightChildren.get(i));
		}
	}
}

/**
 * 依赖语法树
 * 
 * 依赖语法树，树节点的值 i 表示该节点为句子中的第 i 个词。 所有的树都是由某一个词开始建立。
 * 
 * @author cshen
 * @version Feb 16, 2009
 */
class Tree {
	int root;
	ArrayList<Tree> leftChildren;
	ArrayList<Tree> rightChildren;

	public Tree(int root) {
		this.root = root;
		leftChildren = new ArrayList<Tree>();
		rightChildren = new ArrayList<Tree>();
	}

	// public Tree(String str) {
	// str = str.substring(1, str.length() - 1);
	// int i = str.indexOf(' ');
	// String rootStr = str.substring(0, i);
	// root = Integer.parseInt(rootStr);
	// int startIndex = 0;
	// int balance = 0;
	// ArrayList<Tree> children = leftChildren;
	// for (i++; i < str.length(); i++) {
	// switch (str.charAt(i)) {
	// case '[':
	// if (balance == 0)
	// startIndex = i;
	// balance++;
	// break;
	// case ']':
	// balance--;
	// if (balance == 0)
	// children.add(new Tree(str.substring(startIndex, i + 1)));
	// break;
	// case '-':
	// if (balance == 0)
	// children = rightChildren;
	// default:
	// break;
	// }
	// }
	// }

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(new Integer(root).toString());
		sb.append(" ");
		for (int i = 0; i < leftChildren.size(); i++) {
			sb.append(leftChildren.get(i).toString());
		}
		sb.append("-");
		for (int i = 0; i < rightChildren.size(); i++) {
			sb.append(rightChildren.get(i).toString());
		}
		sb.append("]");
		return sb.toString();
	}

	public void addLeftChild(Tree lc) {
		leftChildren.add(0, lc);
	}

	public void addRightChild(Tree rc) {
		rightChildren.add(rc);
	}
}