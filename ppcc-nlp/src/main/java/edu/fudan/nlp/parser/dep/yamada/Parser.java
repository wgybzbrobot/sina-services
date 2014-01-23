package edu.fudan.nlp.parser.dep.yamada;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import edu.fudan.ml.classifier.Linear;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.types.SparseVector;

/**
 * 依赖句法分析器类
 * 
 * 输入单个分完词的句子(包含词性)，使用Yamada分析算法完成依存结构分析。
 * 
 * @author cshen
 * @version Feb 16, 2009
 */
public class Parser extends Inferencer {

	private static final long serialVersionUID = 7114734594734593632L;
	
	// 对于左焦点词的每个词性，保存一张特征名到特征ID的对应表
	LabelAlphabet postagAlphabet;
	// 对于左焦点词的每个词性，有一个分类模型
	Linear[] models;
	AlphabetFactory factory;

	/**
	 * 构造函数
	 * 
	 * @param modelfile
	 *            模型目录
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Parser(String modelfile) throws IOException, ClassNotFoundException {
		loadModel(modelfile);
		factory = AlphabetFactory.buildFactory();
	}

	/**
	 * 分析单个句子
	 * 
	 * @param instance
	 *            句子实例
	 * @return 整个句子的得分
	 */
	public Results<int[]> getBest(Instance carrier, int n) {
		Sentence instance = (Sentence) carrier;
		instance.clearDependency();
		
		Results<int[]> res = new Results<int[]>(n);

		double score = 0;

		// 分析中的状态
		ParsingState state = new ParsingState(instance);

		postagAlphabet = factory.buildLabelAlphabet("postag");

		while (!state.isFinalState()) {
			int[] lr = state.getFocusIndices();

			// 当前状态的特征
			SparseVector features = state.getFeatures();
			Instance inst = new Instance(features.indices());

			String pos = instance.getTag(lr[0]);
			int lpos = postagAlphabet.lookupIndex(pos);
			LabelAlphabet actionList = factory.buildLabelAlphabet(pos);

			double[][] estimates = estimateActions(models[lpos], actionList, inst);

			if ((int) estimates[0][0] == 1)
				state.next(ParsingState.Action.LEFT);
			else if ((int) estimates[0][0] == 2)
				state.next(ParsingState.Action.RIGHT);
			else if ((int) estimates[0][1] == 1)
				state.next(ParsingState.Action.LEFT, estimates[1][1]);
			else
				state.next(ParsingState.Action.RIGHT, estimates[1][1]);

			if (estimates[0][0] != 0)
				score += Math.log10(estimates[1][0]);
			else
				score += Math.log10(estimates[1][1]);

		}

		state.saveRelation();
		
		score = Math.exp(score);
		int[] preds = instance.heads;
		res.addPred(score, preds);

		return res;
	}

	/**
	 * 动作预测
	 * 
	 * 根据当前状态得到的特征，和训练好的模型，预测当前状态应采取的策略，用在测试中
	 * 
	 * @param featureAlphabet
	 *            特征名到特征ID的对应表，特征抽取时使用特征名，模型中使用特征ID，
	 * @param model
	 *            分类模型
	 * @param features
	 *            当前状态的特征
	 * @return 动作及其概率 ［［动作1，概率1］，［动作2，概率2］，［动作3，概率3］］ 动作： 1->LEFT; 2->RIGHT;
	 *         0->SHIFT
	 */
	private double[][] estimateActions(Linear model, LabelAlphabet actions, Instance inst) {
		int numOfClasses = actions.size();
		double[][] result = new double[2][numOfClasses];

		Results ret = model.predict(inst, numOfClasses);
		Object[] guess = ret.predList;

		double total = 0;
		for (int i = 0; i < guess.length; i++) {
			String action = actions.lookupString((Integer)guess[i]);
			result[0][i] = 0;
			if (action.matches("L"))
				result[0][i] = 1;
			else if (action.matches("R"))
				result[0][i] = 2;
			result[1][i] = Math.exp(ret.predScores[i]);
			total += result[1][i];
		}
		for (int i = 0; i < guess.length; i++) {
			result[1][i] = result[1][i] / total;
		}

		return result;
	}

	/**
	 * 加载模型
	 * 
	 * 以序列化方式加载模型
	 * 
	 * @param modelfile
	 *            模型路径
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadModel(String modelfile) throws IOException,
			ClassNotFoundException {
		ObjectInputStream instream = new ObjectInputStream(new GZIPInputStream(
				new FileInputStream(modelfile)));
		factory = (AlphabetFactory) instream.readObject();
		models = (Linear[]) instream.readObject();
		instream.close();
		FeatureAlphabet features = factory.buildFeatureAlphabet("feature");
		features.setStopIncrement(true);
	}

	public Results getBest(Instance inst) {
		return getBest(inst, 1);
	}
	
	public int[] parse(Instance inst)	{
		return (int[]) getBest(inst).getPredAt(0);
	}
	
	public int[] parse(String[] words, String[] pos)	{
		return parse(new Sentence(words, pos));
	}

}
