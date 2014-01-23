package edu.fudan.ml.inf.struct;

import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;
import edu.fudan.utils.MyArrays;

public class Labelwise extends Inferencer {

	private static final long serialVersionUID = 504088074088644518L;

	/**
	 * 标记
	 */
	/**
	 * 标记个数
	 */
	int numLabels;

	int numTemplets;

	private TempletGroup templets;

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
	public Labelwise(FeatureAlphabet features, int numLabels,
			TempletGroup templets) {
		// this.fvg = (SequenceGenerator)generator;
		// this.weights = weights;
		// this.labels = labels;
		// this.features = features;
		// this.features.setStopIncrement(true);
		this.numLabels = numLabels;
		this.templets = templets;
		this.templets.calc(numLabels);
		this.numTemplets = templets.size();
	}

	/**
	 * 标记给定实例
	 * 
	 * @param instance
	 */
	public Results<int[]> getBest(Instance instance, int nbest) {
		int[][] data;
		int[] target;

		data = (int[][]) instance.getData();

		int length = data.length;

		double[][] lattice = new double[data.length][numLabels];
		int[][][] maxState = new int[length][numLabels][2];

		for (int ip = 0; ip < data.length; ip++) {
			// 对于每一个n阶的可能组合
			double[] score0 = new double[numLabels];
			double[] maxscore1 = new double[numLabels];

			for (int s = 0; s < templets.numStates; s++) {
				// 计算所有特征的权重和
				int label = s % numLabels;
				int label1 = s / numLabels % numLabels;

				for (int t = 0; t < numTemplets; t++) {
					if (data[ip][t] == -1)
						continue;
					int ord = templets.get(t).getOrder();
					if (ord == 0) {
						score0[label] += weights[data[ip][t]
								+ templets.offset[t][s]];
					} else {
						double sc = weights[data[ip][t] + templets.offset[t][s]];
						if (sc > maxscore1[label]) {
							maxscore1[label] = sc;
							maxState[ip][label][0] = s;
						}
						if (ip < length - ord - 1) {
							if (data[ip + 1][t] == -1)
								continue;
							sc = weights[data[ip + 1][t]
									+ templets.offset[t][s]];
							if (sc > maxscore1[label1]) {
								maxscore1[label1] = sc;
								maxState[ip + 1][label][1] = s; // 记录到下一个
							}
						}
					}
				}
			}
			for (int i = 0; i < numLabels; i++) {
				lattice[ip][i] = score0[i] + maxscore1[i];
			}
		}
		instance.setTempData(maxState);
		Results<int[]> res = getPath(lattice, nbest);

		return res;
	}
	
	public Results<int[]> getBest(Instance instance)	{
		return getBest(instance, 1);
	}

	private Results<int[]> getPath(double[][] lattice, int nbest) {

		Results<int[]> res = new Results<int[]>(nbest);

		double[][] scores = new double[lattice.length][nbest];
		Integer[][] list = new Integer[lattice.length][nbest];
		for (int i = 0; i < lattice.length; i++) {
			for (int j = 0; j < numLabels; j++) {
				MyArrays.addBest(scores[i], list[i], lattice[i][j], j);
			}

		}
		for (int k = 0; k < nbest; k++) {
			int[] path = new int[lattice.length];
			double score = scores[lattice.length][k];
			for (int i = 0; i < lattice.length; i++) {
				path[i] = list[i][k];
			}
			res.addPred(score, path);
		}
		return res;
	}
}
