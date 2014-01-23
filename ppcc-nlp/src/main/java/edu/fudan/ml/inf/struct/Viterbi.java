package edu.fudan.ml.inf.struct;

import java.util.Arrays;

import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;
import edu.fudan.utils.MyArrays;

/**
 * 任意阶biterbi算法
 * 
 * @author xpqiu
 * 
 */
public class Viterbi extends Inferencer {

	private static final long serialVersionUID = 6023318778006156804L;

	/**
	 * 标记个数
	 */
	int numLabels;

	/**
	 * 模板个数
	 */
	int numTemplets;
	
	/**
	 * 模板组
	 */
	private TempletGroup templets;

	/**
	 * 状态组合个数
	 */
	private int numStates;

	/**
	 * 构造函数
	 * 
	 * @param features
	 *            特征
	 * @param numLabels
	 *            标记
	 * @param templets
	 *            模板
	 */
	public Viterbi(FeatureAlphabet features, int numLabels,
			TempletGroup templets) {
		this.numLabels = numLabels;
		this.templets = templets;
		this.templets.calc(numLabels);
		this.numTemplets = templets.size();
		numStates = templets.numStates;
	}

	/**
	 * 标记给定实例
	 * 
	 * @param instance
	 */
	public Results<int[]> getBest(Instance instance, int nbest) {
		int[][] data;
		/**
		 * 节点矩阵
		 */
		Node[][] lattice;

		data = (int[][]) instance.getData();
		// target = (int[]) instance.getTarget();
		lattice = new Node[data.length][templets.numStates];
		for (int ip = 0; ip < data.length; ip++)
			for (int s = 0; s < templets.numStates; s++)
				lattice[ip][s] = new Node(nbest);

		for (int ip = 0; ip < data.length; ip++) {
			// 对于每一个n阶的可能组合
			for (int s = 0; s < numStates; s++) {
				// 计算所有特征的权重和
				for (int t = 0; t < numTemplets; t++) {
					if (data[ip][t] == -1)
						continue;
					lattice[ip][s].weight += weights[data[ip][t]
							+ templets.offset[t][s]];
				}
			}
		}
		for (int s = 0; s < numLabels; s++) {
			lattice[0][s].best[0] = lattice[0][s].weight;
		}
		double[] best = new double[nbest];
		Integer[] prev = new Integer[nbest];
		for (int ip = 1; ip < data.length; ip++) {
			for (int s = 0; s < numStates; s += numLabels) {
				Arrays.fill(best, Double.NEGATIVE_INFINITY);
				for (int k = 0; k < numLabels; k++) {
					int sp = (k * templets.numStates + s) / numLabels;
					for (int ibest = 0; ibest < nbest; ibest++) {
						double b = lattice[ip - 1][sp].best[ibest];
						MyArrays.addBest(best, prev, b, sp * nbest + ibest);
					}
				}
				for (int r = s; r < s + numLabels; r++) {
					for (int n = 0; n < nbest; n++) {
						lattice[ip][r].best[n] = best[n]
								+ lattice[ip][r].weight;
						lattice[ip][r].prev[n] = prev[n];
					}
				}
			}
		}

		Results<int[]> res = getPath(lattice, nbest);

		return res;
	}
	
	public Results<int[]> getBest(Instance instance)	{
		return getBest(instance, 1);
	}

	private Results<int[]> getPath(Node[][] lattice, int nbest) {
		double best;
		Node lastNode = new Node(nbest);
		int last = lattice.length - 1;
		for (int s = 0; s < templets.numStates; s++) {
			for (int ibest = 0; ibest < nbest; ibest++) {
				best = lattice[last][s].best[ibest];
				lastNode.addBest(best, s * nbest + ibest);
			}
		}

		Results<int[]> res = new Results<int[]>(nbest);
		for (int k = 0; k < nbest; k++) {
			int[] path = new int[lattice.length];
			int p = last;
			int s = lastNode.prev[k];
			double score = lastNode.best[k];

			for (int d = s / nbest, i = 0; i < templets.maxOrder && p >= 0; i++, p--) {
				path[p] = d % numLabels;
				d = d / numLabels;
			}
			while (p >= 0) {
				path[p] = s / nbest / templets.base[templets.maxOrder];
				s = lattice[p + templets.maxOrder][s / nbest].prev[s % nbest];
				--p;
			}
			res.addPred(score, path);
		}
		return res;
	}

	public final class Node {

		int n;
		double weight = 0.0;
		double[] best;
		int[] prev;

		public Node(int n) {
			this.n = n;
			best = new double[n];
			prev = new int[n];

		}

		/**
		 * 记录之前的label和得分，保留前n个
		 * 
		 * @param score
		 * @param p
		 */
		public int addBest(double score, int p) {
			int i;
			for (i = 0; i < n; i++) {
				if (score > best[i])
					break;
			}
			if (i >= n)
				return -1;
			for (int k = n - 2; k >= i; k--) {
				best[k + 1] = best[k];
				prev[k + 1] = prev[k];
			}
			best[i] = score;
			prev[i] = p;
			return i;
		}

		public String toString() {
			return String.format("%f %f %d", weight, best[0], prev[0]);
		}
	}

}
