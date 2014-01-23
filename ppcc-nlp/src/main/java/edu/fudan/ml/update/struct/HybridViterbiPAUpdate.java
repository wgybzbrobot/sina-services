package edu.fudan.ml.update.struct;

import edu.fudan.ml.feature.templet.HybridTemplet;
import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.update.AbstractPAUpdate;

public class HybridViterbiPAUpdate extends AbstractPAUpdate {

	private int ssize;
	private int tsize;
	private TempletGroup[] templets;
	private int[][][] data;
	private int[][] golds;
	private int[][] preds;

	public HybridViterbiPAUpdate(TempletGroup[] templets, int ssize, int tsize,
			double c) {
		super();
		this.ssize = ssize;
		this.tsize = tsize;
		this.templets = templets;
	}

	protected int diff(Instance inst, double[] weights, Object targets,
			Object predicts) {

		data = (int[][][]) inst.getData();
		if (targets == null)
			golds = (int[][]) inst.getTarget();
		else
			golds = (int[][]) targets;
		preds = (int[][]) predicts;

		int diff = 0;

		if (golds[0][0] != preds[0][0]) {
			diff++;
			diffUpClique(weights, templets[0], 0);
		}
		if (golds[0][1] != preds[0][1]) {
			diff++;
			diffDownClique(weights, templets[1], 0);
		}

		for (int i = 1; i < data.length; i++) {
			if (golds[i - 1][0] != preds[i - 1][0]
					|| golds[i - 1][1] != preds[i - 1][1]
					|| golds[i][0] != preds[i][0]) {
				diff++;
				diffUpClique(weights, templets[0], i);
			}
			if (golds[i - 1][1] != preds[i - 1][1]
					|| golds[i][0] != preds[i][0] || golds[i][1] != preds[i][1]) {
				diff++;
				diffDownClique(weights, templets[1], i);
			}
		}

		return diff;
	}

	private void diffDownClique(double[] weights, TempletGroup templets, int p) {
		for (int t = 0; t < templets.size(); t++) {
			if (data[p][1][t] == -1)
				continue;

			HybridTemplet ht = (HybridTemplet) templets.get(t);
			int base = data[p][1][t];
			if (ht.getOrder() == 0) {
				if (golds[p][1] != preds[p][1]) {
					int ts = base + golds[p][1];
					int ps = base + preds[p][1];
					adjust(weights, ts, ps);
				}
			}
			if (ht.getOrder() == -1) {
				if (golds[p][0] != preds[p][0] || golds[p][1] != preds[p][1]) {
					int ts = base + golds[p][0] * tsize + golds[p][1];
					int ps = base + preds[p][0] * tsize + preds[p][1];
					adjust(weights, ts, ps);
				}
			}
			if (p > 0) {
				if (ht.getOrder() == 1) {
					if (golds[p - 1][1] != preds[p - 1][1]
							|| golds[p][1] != preds[p][1]) {
						int ts = base + golds[p - 1][1] * tsize + golds[p][1];
						int ps = base + preds[p - 1][1] * tsize + preds[p][1];
						adjust(weights, ts, ps);
					}
				}
				if (ht.getOrder() == 2) {
					int ts = base + (golds[p - 1][1] * ssize + golds[p][0])
							* tsize + golds[p][1];
					int ps = base + (preds[p - 1][1] * ssize + preds[p][0])
							* tsize + preds[p][1];
					adjust(weights, ts, ps);
				}
			}
		}
	}

	private void diffUpClique(double[] weights, TempletGroup templets, int p) {
		for (int t = 0; t < templets.size(); t++) {
			if (data[p][0][t] == -1)
				continue;

			HybridTemplet ht = (HybridTemplet) templets.get(t);
			int base = data[p][0][t];
			if (ht.getOrder() == 0) {
				if (golds[p][0] != preds[p][0]) {
					int ts = base + golds[p][0];
					int ps = base + preds[p][0];
					adjust(weights, ts, ps);
				}
			}
			if (p > 0) {
				if (ht.getOrder() == -1) {
					if (golds[p - 1][1] != preds[p - 1][1]
							|| golds[p][0] != preds[p][0]) {
						int ts = base + golds[p - 1][1] * ssize + golds[p][0];
						int ps = base + preds[p - 1][1] * ssize + preds[p][0];
						adjust(weights, ts, ps);
					}
				}
				if (ht.getOrder() == 1) {
					if (golds[p - 1][0] != preds[p - 1][0]
							|| golds[p][0] != preds[p][0]) {
						int ts = base + golds[p - 1][0] * ssize + golds[p][0];
						int ps = base + preds[p - 1][0] * ssize + preds[p][0];
						adjust(weights, ts, ps);
					}
				}
				if (ht.getOrder() == 2) {
					int ts = base + (golds[p - 1][0] * tsize + golds[p - 1][1])
							* ssize + golds[p][0];
					int ps = base + (preds[p - 1][0] * tsize + preds[p - 1][1])
							* ssize + preds[p][0];
					adjust(weights, ts, ps);
				}
			}
		}
	}

}
