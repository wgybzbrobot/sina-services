package edu.fudan.ml.classifier.struct;

import java.io.IOException;

import edu.fudan.ml.classifier.Linear;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.loss.Loss;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.update.Update;
import edu.fudan.utils.MyArrays;

public class PATrainer {

	/**
	 * 特征权重
	 */
	double[] weights;
	/**
	 * 推理算法
	 */
	Inferencer msolver;
	/**
	 * 权重更新
	 */
	public Update update;
	/**
	 * 损失函数
	 */
	Loss loss;

	/**
	 * 最大迭代次数
	 */
	int maxIter = Integer.MAX_VALUE;
	/**
	 * 最小错误
	 */
	private double eps = 1e-5;
	/**
	 * 字数
	 */
	public int count;
	/**
	 * PA算法参数C，用来调节约束强弱
	 */
	double c;
	/**
	 * 是否优化，在每次迭代时，将不显著的特征权重置为0
	 */
	public boolean isOptimized = false;
	/**
	 * 优化阈值，权重绝对值按大小排序，只保留前n个最大的。
	 * 要求sum_{i=1}^n{(w_i^2)/||w||}^2>threshold。threshold默认为0.99
	 */
	double threshold = 0.99;

	boolean simpleOutput = false;
	boolean usePerceptron = true;
	public boolean interim = false;

	public PATrainer(Inferencer msolver, Update update, Loss loss, 
			FeatureAlphabet features, int maxIter, double c) {
		this.msolver = msolver;
		this.update = update;
		this.loss = loss;
		this.maxIter = maxIter;
		this.c = c;
		weights = new double[features.size()];
		this.msolver.setWeights(weights);
	}

	/**
	 * 训练
	 */
	public Linear train(InstanceSet trainingList, InstanceSet testList) {
		int numSamples = trainingList.size();
		count = 0;
		for (int ii = 0; ii < trainingList.size(); ii++) {
			Instance inst = trainingList.getInstance(ii);
			count += ((int[]) inst.getTarget()).length;
		}

		System.out.println("Chars Number: " + count);

		double oldErrorRate = Double.MAX_VALUE;

		// 开始循环
		long beginTime, endTime;
		long beginTimeIter, endTimeIter;
		beginTime = System.currentTimeMillis();
		double pE = 0;
		int iter = 0;
		int frac = numSamples / 10;
		while (iter++ < maxIter) {
			if (!simpleOutput) {
				System.out.print("iter:");
				System.out.print(iter + "\t");
			}
			double err = 0;
			double errorAll = 0;
			beginTimeIter = System.currentTimeMillis();
			int progress = frac;
			for (int ii = 0; ii < numSamples; ii++) {
				Instance inst = trainingList.getInstance(ii);
				Results pred = (Results) msolver.getBest(inst, 1);
				double l = loss.calc(pred.getPredAt(0), inst.getTarget());
				if (l > 0) {// 预测错误，更新权重
					errorAll += 1.0;
					err += l;
					update.update(inst, weights, pred.getPredAt(0), c);
				} else {
					if (pred.size() > 1)
						update.update(inst, weights, pred.getPredAt(1), c);
				}
				if (!simpleOutput && ii % progress == 0) {// 显示进度
					System.out.print('.');
					progress += frac;
				}
			}
			double errRate = err / count;

			endTimeIter = System.currentTimeMillis();
			if (!simpleOutput) {
				System.out.println("\ttime:" + (endTimeIter - beginTimeIter)
						/ 1000.0 + "s");
				System.out.print("Train:");
				System.out.print("\tTag acc:");
			}
			System.out.print(1 - errRate);
			if (!simpleOutput) {
				System.out.print("\tSentence acc:");
				System.out.print(1 - errorAll / numSamples);
				System.out.println();
			}
			if (testList != null) {
				test(testList);
			}
			if (Math.abs(errRate - oldErrorRate) < eps) {
				System.out.println("Convergence!");
				break;
			}
			oldErrorRate = errRate;
			if (interim) {
				Linear p = new Linear(msolver, trainingList.getAlphabetFactory());
				try {
					p.saveTo("tmp.model");
				} catch (IOException e) {
					System.err.println("write model error!");
				}
			}
			if (isOptimized) {// 模型优化，去掉不显著的特征
				int[] idx = MyArrays.getTop(weights.clone(), threshold, false);
				System.out.print("Opt: weight numbers: "
						+ MyArrays.countNoneZero(weights));
				MyArrays.set(weights, idx, 0.0);
				System.out.println(" -> " + MyArrays.countNoneZero(weights));
			}
		}
		endTime = System.currentTimeMillis();
		System.out.println("done!");
		System.out.println("time escape:" + (endTime - beginTime) / 1000.0
				+ "s");
		Linear p = new Linear(msolver, trainingList.getAlphabetFactory());
		return p;
	}

	/**
	 * 用当前模型在测试集上进行测试 输出正确率
	 * 
	 * @param testSet
	 */
	public void test(InstanceSet testSet) {

		double err = 0;
		double errorAll = 0;
		int total = 0;
		for (int i = 0; i < testSet.size(); i++) {
			Instance inst = testSet.getInstance(i);
			total += ((int[]) inst.getTarget()).length;
			Results pred = (Results) msolver.getBest(inst, 1);
			double l = loss.calc(pred.getPredAt(0), inst.getTarget());
			if (l > 0) {// 预测错误
				errorAll += 1.0;
				err += l;
			}

		}
		if (!simpleOutput) {
			System.out.print("Test:\t");
			System.out.print(total - err);
			System.out.print('/');
			System.out.print(total);
			System.out.print("\tTag acc:");
		} else {
			System.out.print('\t');
		}
		System.out.print(1 - err / total);
		if (!simpleOutput) {
			System.out.print("\tSentence acc:");
			System.out.println(1 - errorAll / testSet.size());

		}
		System.out.println();
	}
}
