package edu.fudan.ml.classifier.struct;

import java.io.IOException;
import java.util.Arrays;

import edu.fudan.ml.classifier.Linear;
import edu.fudan.ml.classifier.OnlineTrainer;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.loss.Loss;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.update.Update;
import edu.fudan.utils.MyArrays;

public class OnlineStructTrainer extends OnlineTrainer {

	public OnlineStructTrainer(Inferencer msolver, Update update,
			Loss loss, int fsize, int iternum, double c) {
		super(msolver, update, loss, fsize, iternum, c);
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
			trainset.shuffle();

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

			int innerCount = 0;
			for (int ii = 0; ii < numSamples; ii++) {
				Instance inst = trainset.getInstance(ii);
				double l = inst.length();
				double dl = Double.MAX_VALUE;
				do {
					dl = l;
					Results pred = (Results) inferencer.getBest(inst, 1);
					l = loss.calc(pred.getPredAt(0), inst.getTarget());
					if (l > 0) {
						update.update(inst, weights, pred.getPredAt(0), c);
						innerCount++;
						if (DEBUG) {
							pred = (Results) inferencer.getBest(inst, 1);
							double nl = loss.calc(pred.getPredAt(0), inst.getTarget());
						}
					}
					dl -= l;
				} while (l != 0 && Math.abs(dl) > 0);
				
				cnt += inst.length();
				cnttot++;
				if (l > 0)	{
					err += l;
					errtot++;
				}

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

			if (method == TrainMethod.Average) {
				for (int i = 0; i < innerWeights.length; i++) {
					averageWeights[i] += innerWeights[i] / numSamples;
				}
			} else if (method == TrainMethod.FastAverage) {
				for (int i = 0; i < weights.length; i++) {
					averageWeights[i] += weights[i];
				}
			}

			double curErrRate = err / cnt;

			endTimeIter = System.currentTimeMillis();

			if (!simpleOutput) {
				System.out.println("\ttime:" + (endTimeIter - beginTimeIter)
						/ 1000.0 + "s");
				System.out.print("Train:");
				System.out.print("\tTag acc:");
			}
			System.out.print(1 - curErrRate);
			if (!simpleOutput) {
				System.out.print("\tSentence acc:");
				System.out.println(1 - errtot / cnttot);
			}
			if (devset != null) {
				evaluate(devset);
			}
			System.out.println();
			
			if (Math.abs(curErrRate - hisErrRate) < eps) {
				System.out.println("Convergence!");
				break;
			}
			hisErrRate = curErrRate;
			if (interim) {
				Linear p = new Linear(inferencer, trainset.getAlphabetFactory());
				try {
					p.saveTo("tmp.model");
				} catch (IOException e) {
					System.err.println("write model error!");
				}
			}
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
			System.out.print("Opt: weight numbers: "
					+ MyArrays.countNoneZero(weights));
			MyArrays.set(weights, idx, 0.0);
			System.out.println(" -> " + MyArrays.countNoneZero(weights));
		}
		System.out.println();

		endTime = System.currentTimeMillis();
		System.out.println("time escape:" + (endTime - beginTime) / 1000.0
				+ "s");
		Linear p = new Linear(inferencer, trainset.getAlphabetFactory());
		return p;
	}
}
