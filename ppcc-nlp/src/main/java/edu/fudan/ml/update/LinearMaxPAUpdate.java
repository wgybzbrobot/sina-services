package edu.fudan.ml.update;

import edu.fudan.ml.types.Instance;

public class LinearMaxPAUpdate extends AbstractPAUpdate {

	protected int diff(Instance inst, double[] weights, Object target,
			Object predict) {

		int[] data = (int[]) inst.getData();
		int gold;
		if (target == null)
			gold = (Integer) inst.getTarget();
		else
			gold = (Integer) target;
		int pred = (Integer) predict;

		for (int i = 0; i < data.length; i++) {
			if (data[i] != -1) {
				int ts = data[i] + gold;
				int ps = data[i] + pred;
				diffvec.adjustOrPutValue(ts, 1, 1);
				diffvec.adjustOrPutValue(ps, -1, -1);
				diffw += weights[ts]-weights[ps];  // w^T(f(x,y)-f(x,ybar))
			}
		}

		return 1;
	}

}
