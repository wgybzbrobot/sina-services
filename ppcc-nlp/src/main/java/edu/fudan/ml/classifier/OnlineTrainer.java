package edu.fudan.ml.classifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.loss.Loss;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.update.Update;
import edu.fudan.utils.MyArrays;

public class OnlineTrainer extends AbstractTrainer {

	public TrainMethod method = TrainMethod.FastAverage;

	public boolean DEBUG = true;
	public boolean shuffle = true;
	public boolean finalOptimized = false;
	public boolean innerOptimized = false;
	public boolean simpleOutput = false;
	public boolean interim = false;

	public double c;

	public double threshold = 0.99;

	protected Linear classifier;
	protected Inferencer inferencer;
	protected Loss loss;
	protected Update update;
	protected Random random;

	public double eps = 1e-5;
	public int iternum;
	protected double[] weights;

	public enum TrainMethod {
		Perceptron, Average, FastAverage
	}

	public OnlineTrainer(Inferencer inferencer, Update update,
			Loss loss, int fsize, int iternum, double c) {
		this.inferencer = inferencer;
		this.update = update;
		this.loss = loss;
		this.iternum = iternum;
		this.c = c;
		weights = (double[]) inferencer.getWeights();
		if (weights == null) {
			weights = new double[fsize];
			inferencer.setWeights(weights);
		}
		random = new Random(1l);
	}

	public OnlineTrainer(Linear classifier, Update update, Loss loss,
			int fsize, int iternum, double c) {
		this(classifier.getInferencer(), update, loss, fsize, iternum, c);
	}

	public Linear train(InstanceSet trainset, InstanceSet devset) {
		int numSamples = trainset.size();

		System.out.println("Training Number: " + numSamples);

		double hisErrRate = Double.MAX_VALUE;

		long beginTime, endTime;
		long beginTimeIter, endTimeIter;
		int iter = 0;
		int frac = numSamples / 10;

		double[] averageWeights = null;
		if (method == TrainMethod.Average || method == TrainMethod.FastAverage) {
			averageWeights = new double[weights.length];
		}

		beginTime = System.currentTimeMillis();

		if (shuffle)
			trainset.shuffle(random);

		while (iter++ < iternum) {
			if (!simpleOutput) {
				System.out.print("iter:");
				System.out.print(iter + "\t");
			}
			double err = 0;
			double errtot = 0;
			int cnt = 0;
			int cnttot = 0;
			int progress = frac;

			beginTimeIter = System.currentTimeMillis();

			double[] innerWeights = null;
			if (method == TrainMethod.Average) {
				innerWeights = Arrays.copyOf(weights, weights.length);
			}

			for (int ii = 0; ii < numSamples; ii++) {
				Instance inst = trainset.getInstance(ii);
				Results pred = (Results) inferencer.getBest(inst);
				double l = loss.calc(pred.getPredAt(0), inst.getTarget());
				if (l > 0) {
					err += l;
					errtot++;
					update.update(inst, weights, pred.getPredAt(0), c);

					if (DEBUG) {
						pred = (Results) inferencer.getBest(inst, 1);
						l = loss.calc(pred.getPredAt(0), inst.getTarget());
					}
				}
				cnt += inst.length();
				cnttot++;
				if (method == TrainMethod.Average) {
					for (int i = 0; i < weights.length; i++) {
						innerWeights[i] += weights[i];
					}
				}

				if (!simpleOutput && progress != 0 && ii % progress == 0) {
					System.out.print('.');
					progress += frac;
				}
			}

			double curErrRate = err / cnt;

			endTimeIter = System.currentTimeMillis();

			if (!simpleOutput) {
				System.out.println("\ttime: " + (endTimeIter - beginTimeIter)
						/ 1000.0 + "s");
				System.out.print("Train: ");
				System.out.print("\tTag acc: ");
			}
			System.out.print(1 - curErrRate);
			if (!simpleOutput) {
				System.out.print("\tSentence acc: ");
				System.out.print(1 - errtot / cnttot);
				System.out.println();
			}

			System.out.print("Weight Numbers: "
					+ MyArrays.countNoneZero(weights));
			if (innerOptimized) {
				int[] idx = MyArrays.getTop(weights.clone(), threshold, false);
				MyArrays.set(weights, idx, 0.0);
				System.out.print("	After Optimized: "
						+ MyArrays.countNoneZero(weights));
			}
			System.out.println();

			if (devset != null) {
				evaluate(devset);
			}
			System.out.println();

			if (method == TrainMethod.Average) {
				for (int i = 0; i < innerWeights.length; i++) {
					averageWeights[i] += innerWeights[i] / numSamples;
				}
			} else if (method == TrainMethod.FastAverage) {
				for (int i = 0; i < weights.length; i++) {
					averageWeights[i] += weights[i];
				}
			}

			if (interim) {
				Linear p = new Linear(inferencer, trainset.getAlphabetFactory());
				try {
					p.saveTo("tmp.model");
				} catch (IOException e) {
					System.err.println("write model error!");
				}
			}

			if (Math.abs(curErrRate - hisErrRate) < eps) {
				System.out.println("Convergence!");
				break;
			}
			hisErrRate = curErrRate;
		}

		if (method == TrainMethod.Average || method == TrainMethod.FastAverage) {
			for (int i = 0; i < averageWeights.length; i++) {
				averageWeights[i] /= iternum;
			}
			weights = null;
			weights = averageWeights;
			inferencer.setWeights(weights);
		}

		System.out.print("Weight Numbers: " + MyArrays.countNoneZero(weights));
		if (finalOptimized) {
			int[] idx = MyArrays.getTop(weights.clone(), threshold, false);
			MyArrays.set(weights, idx, 0.0);
			System.out.print("	After Optimized: "
					+ MyArrays.countNoneZero(weights));
		}
		System.out.println();

		endTime = System.currentTimeMillis();
		System.out.println("time escape:" + (endTime - beginTime) / 1000.0
				+ "s");
		System.out.println();
		Linear p = new Linear(inferencer, trainset.getAlphabetFactory());
		return p;
	}

	protected void evaluate(InstanceSet devset) {
		double err = 0;
		double errtot = 0;
		int total = 0;
		for (int i = 0; i < devset.size(); i++) {
			Instance inst = devset.getInstance(i);
			total += inst.length();
			Results pred = (Results) inferencer.getBest(inst, 1);
			double l = loss.calc(pred.getPredAt(0), inst.getTarget());
			if (l > 0) {
				errtot += 1.0;
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
			System.out.println(1 - errtot / devset.size());

		}
	}

}
