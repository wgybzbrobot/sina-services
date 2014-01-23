package edu.fudan.ml.classifier.hier;

import java.util.*;

import edu.fudan.ml.cluster.Kmeans;
import edu.fudan.ml.eval.Evaluation;
import edu.fudan.ml.feature.generator.BaseGenerator;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.inf.hier.LatentMultiLinearMax;
import edu.fudan.ml.loss.Loss;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.types.SparseVector;
import edu.fudan.ml.types.Tree;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.tc.Mean;

public class LatentPATrainer {

	// 特征权重
	private SparseVector[] weights;
	private LatentMultiLinearMax msolver;
	private BaseGenerator featureGen;
	private Loss loss;

	// 最大迭代次数
	private int maxIter = Integer.MAX_VALUE;
	// 最小错误
	private double eps = 1e-10;
	private Tree tree;
	private double c;
	int numLatent = 2;

	public LatentPATrainer(Inferencer msolver, BaseGenerator featureGen, Loss loss,
			int maxIter, double c, Tree tr, int numLatent) {
		this.msolver = (LatentMultiLinearMax) msolver;
		this.featureGen = featureGen;
		this.loss = loss;
		this.maxIter = maxIter;
		tree = tr;
		this.c = c;
		this.numLatent = numLatent;
	}

	/**
	 * 训练
	 * 
	 * @param eval
	 */
	public Linear train(InstanceSet trainingList, Evaluation eval) {
		LabelAlphabet labels = trainingList.getAlphabetFactory()
				.buildLabelAlphabet("labels");
		int numClass = labels.size();
		int numSamples = trainingList.size();

		if (tree == null) {
			return null;
		}
		// 总类个数
		int numNodeswithLatent = tree.size * numLatent;
		weights = new SparseVector[numNodeswithLatent];

		ArrayList<Instance>[] nodes = new ArrayList[tree.size];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new ArrayList<Instance>();
		}
		for (int i = 0; i < trainingList.size(); i++) {
			ArrayList<Integer> anc = tree.getPath((Integer) trainingList
					.getInstance(i).getTarget());
			for (int j = 0; j < anc.size(); j++) {
				nodes[anc.get(j)].add(trainingList.getInstance(i));
			}
		}
		// 初始化权重
		boolean way = true;
		if (way) {
			// 初始化权重向量到类中心
			SparseVector[] weights1 = Mean.mean(trainingList, tree);
			// 将每个类中心分为numLatent份
			weights = new SparseVector[weights1.length * numLatent];
			for (int i = 0; i < weights1.length; i++)
				for (int j = 0; j < numLatent; j++)
					weights[i * numLatent + j] = new SparseVector(weights1[i]);

		} else {
			// 对每个类聚类为numLatent个中心
			for (int i = 0; i < tree.size; i++) {
				// 聚类
				Kmeans km = new Kmeans(numLatent);
				km.cluster(nodes[tree.getNode(i)]);
				for (int j = 0; j < numLatent; j++) {
					weights[2 * tree.getNode(i) + j] = km.centroids[j];
				}
			}
		}
		msolver.setWeight(weights);
		int loops = 0;
		double oldErrorRate = Double.MAX_VALUE;

		int frac = numSamples / 10;

		// 开始循环
		System.out.println("Begin Training...");
		long beginTime = System.currentTimeMillis();
		while (loops++ < maxIter) {
			System.out.print("Loop:" + loops);
			double totalerror = 0.0;
			// trainingList.shuffle();
			long beginTimeInner = System.currentTimeMillis();
			int progress = frac;
			for (int ii = 0; ii < numSamples; ii++) {
				Instance inst = trainingList.getInstance(ii);
				// 原始最大正确、最大错误类别
				Integer maxEY;
				Integer maxCY;

				maxCY = (Integer) inst.getTarget();

				Results res = msolver.getBest(inst, 1);
				if (res == null) {
					System.err.println("Error: " + ii);
					continue;
				}

				int error;
				maxEY = (Integer) res.predList[0];

				error = tree.dist(maxEY, maxCY);
				double margin = res.oracleScores[0] - res.predScores[0];
				double loss = error - margin;
				Object[] maxlatent = (Object[]) res.other;
				int[] o = (int[]) maxlatent[0];
				int[] l = (int[]) maxlatent[1];
				if (loss > 0) {// 预测错误，更新权重

					totalerror += 1;
					// 计算含层次信息的内积
					// 计算步长
					double phi = featureGen.getVector(inst).squared();
					double alpha = Math.min(c, loss / (phi * error));
					ArrayList<Integer> anc = tree.getPath(maxCY);
					for (int j = 0; j < anc.size(); j++) {
						weights[o[j]].plus(featureGen.getVector(inst), alpha);
					}
					anc = tree.getPath(maxEY);
					for (int j = 0; j < anc.size(); j++) {
						weights[l[j]].plus(featureGen.getVector(inst), -alpha);
					}

				}
				if (ii % progress == 0) {// 显示进度
					System.out.print('.');
					progress += frac;
				}
			}
			double acc = 1 - totalerror / numSamples;
			System.out.print("\t Train Accuracy:" + acc);
			System.out.println("\t Train Time(s):"
					+ (System.currentTimeMillis() - beginTimeInner) / 1000);
			// weight.trim(0.5);

			if (eval != null) {
				System.out.println("Test:");
				Linear classifier = new Linear(weights, msolver);
				eval.eval(classifier);
				msolver.isUseTarget(true);
			}

			if (acc == 1 && Math.abs(oldErrorRate - acc) / oldErrorRate < eps)
				break;
		}
		System.out.println("Training End");
		System.out.println("Training Time(s):"
				+ (System.currentTimeMillis() - beginTime) / 1000);
		Pipe dataPipe = trainingList.getPipes();

		Linear classifier = new Linear(weights, msolver, featureGen, dataPipe,
				labels);
		return classifier;
	}

}
