package edu.fudan.ml.classifier.hier;

import java.util.ArrayList;
import java.util.HashSet;

import edu.fudan.ml.eval.Evaluation;
import edu.fudan.ml.feature.generator.BaseGenerator;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.inf.hier.MultiLinearMax;
import edu.fudan.ml.loss.Loss;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.types.SparseVector;
import edu.fudan.ml.types.Tree;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.tc.Mean;

public class PATrainer {

	// 特征权重
	private SparseVector[] weights;
	private Linear classifier;
	private MultiLinearMax msolver;
	private BaseGenerator featureGen;
	private Loss loss;

	// 最大迭代次数
	private int maxIter = Integer.MAX_VALUE;
	// 最小错误
	private double eps = 1e-10;
	private Tree tree;
	private double c;

	public PATrainer(Inferencer msolver, BaseGenerator featureGen, Loss loss,
			int maxIter, double c, Tree tr) {
		this.msolver = (MultiLinearMax) msolver;
		this.featureGen = featureGen;
		this.loss = loss;
		this.maxIter = maxIter;
		tree = tr;
		this.c = c;
	}

	public Linear getClassifier() {
		return classifier;
	}

	/**
	 * 训练
	 * 
	 * @param eval
	 */
	public Linear train(InstanceSet trainingList, Evaluation eval) {
		System.out.println("Sample Size: " + trainingList.size());
		LabelAlphabet labels = trainingList.getAlphabetFactory()
				.buildLabelAlphabet("labels");
		int numClass = labels.size();

		// 初始化权重向量到类中心
		weights = Mean.mean(trainingList, tree);
		msolver.setWeight(weights);
		int loops = 0;
		double oldErrorRate = Double.MAX_VALUE;
		int numSamples = trainingList.size();
		int frac = numSamples / 10;

		// 开始循环
		System.out.println("Begin Training...");
		long beginTime = System.currentTimeMillis();
		while (loops++ < maxIter) {
			System.out.print("Loop: " + loops);
			double totalerror = 0.0;
			trainingList.shuffle();
			long beginTimeInner = System.currentTimeMillis();
			int progress = frac;
			for (int ii = 0; ii < numSamples; ii++) {

				Instance inst = trainingList.getInstance(ii);
				Integer maxC = (Integer) inst.getTarget();
				HashSet<Integer> t = new HashSet<Integer>();
				t.add(maxC);
				Results pred = (Results) msolver.getBest(inst, 1);
				Integer maxE = (Integer) pred.predList[0];
				int error;
				if (tree == null) {
					error = ((Integer) pred.predList[0] == maxC) ? 0 : 1;
				} else {
					error = tree.dist(maxE, maxC);
				}
				double loss = error
						- (pred.oracleScores[0] - ((Double) pred.predScores[0]));

				if (loss > 0) {// 预测错误，更新权重

					totalerror += 1;
					// 计算含层次信息的内积
					// 计算步长
					double phi = featureGen.getVector(inst).squared();
					double alpha = Math.min(c, loss / (phi * error));
					if (tree != null) {
						ArrayList<Integer> anc = tree.getPath(maxC);
						for (int j = 0; j < anc.size(); j++) {
							weights[anc.get(j)].plus(
									featureGen.getVector(inst), alpha);
						}
						anc = tree.getPath(maxE);
						for (int j = 0; j < anc.size(); j++) {
							weights[anc.get(j)].plus(
									featureGen.getVector(inst), -alpha);
						}
					} else {
						weights[maxC].plus(featureGen.getVector(inst), alpha);
						weights[maxE].plus(featureGen.getVector(inst), -alpha);
					}

				}
				if (ii % progress == 0) {// 显示进度
					System.out.print('.');
					progress += frac;
				}
			}
			double acc = 1 - totalerror / numSamples;
			System.out.print("\t Accuracy:" + acc);
			System.out.println("\t Time(s):"
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

		// trimAlphabet();

		classifier = new Linear(weights, msolver, featureGen, dataPipe, labels);
		return classifier;
	}

}
