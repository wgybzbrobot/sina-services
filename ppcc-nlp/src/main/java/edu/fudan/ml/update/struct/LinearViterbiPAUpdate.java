package edu.fudan.ml.update.struct;

import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.update.AbstractPAUpdate;

public class LinearViterbiPAUpdate extends AbstractPAUpdate {

	private int ysize;
	private int[] golds;
	private int[] preds;
	private int[][] data;
	private int[] orders;

	public LinearViterbiPAUpdate(TempletGroup templets, int ysize) {
		super();
		this.ysize = ysize;
		orders = new int[templets.size()];
		for(int i = 0; i < templets.size(); i++)	{
			orders[i] = templets.get(i).getOrder();
		}
	}

	protected int diff(Instance inst, double[] weights, Object targets,
			Object predicts) {

		data = (int[][]) inst.getData();

		if (targets == null)
			golds = (int[]) inst.getTarget();
		else
			golds = (int[]) targets;
		preds = (int[]) predicts;

		int diff = 0;

		if (golds[0] != preds[0]) {
			diff++;
			diffClique(weights, 0);
		}
		for (int p = 1; p < data.length; p++) {
			if (golds[p - 1] != preds[p - 1] || golds[p] != preds[p]) {
				diff++;
				diffClique(weights, p);
			}
		}

		return diff;
	}

	private void diffClique(double[] weights, int p) {
		for (int t = 0; t < orders.length; t++) {
			if (data[p][t] == -1)
				continue;

			if (orders[t] == 0) {
				if (golds[p] != preds[p]) {
					int ts = data[p][t] + golds[p];
					int ps = data[p][t] + preds[p];
					adjust(weights, ts, ps);
				}
			}
			if (p > 0 && orders[t] == 1) {
				int ts = data[p][t] + (golds[p - 1] * ysize + golds[p]);
				int ps = data[p][t] + (preds[p - 1] * ysize + preds[p]);
				adjust(weights, ts, ps);
			}
		}
	}

}
