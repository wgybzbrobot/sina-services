package edu.fudan.ml.update;

import edu.fudan.ml.types.Instance;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;

public abstract class AbstractPAUpdate implements Update {

	protected double diffw;
	protected TIntIntHashMap diffvec = null;

	public AbstractPAUpdate() {
		diffw = 0;
		diffvec = new TIntIntHashMap();
	}

	public int update(Instance inst, double[] weights, Object predict, double c) {
		return update(inst, weights, inst.getTarget(), predict, c);
	}

	public int update(Instance inst, double[] weights, Object target,
			Object predict, double c) {

		diffvec.clear();
		diffw = 0;

		int loss = diff(inst, weights, target, predict);

		double lamda = 0;
		TIntIntIterator it = diffvec.iterator();
		for (int i = diffvec.size(); i-- > 0;) {
			it.advance();
			lamda += it.value() * it.value();
		}

		if (diffw <= loss) {
			double alpha = (loss - diffw) / lamda;
			alpha = Math.min(alpha, c);
			it = diffvec.iterator();
			for (int i = diffvec.size(); i-- > 0;) {
				it.advance();
				weights[it.key()] += it.value() * alpha;
			}
			return 1;
		} else
			return 0;
	}

	protected abstract int diff(Instance inst, double[] weights, Object target,
			Object predict);

	protected void adjust(double[] weights, int ts, int ps) {
		assert (ts != -1 && ps != -1);
		diffvec.adjustOrPutValue(ts, 1, 1);
		diffvec.adjustOrPutValue(ps, -1, -1);
		diffw += weights[ts] - weights[ps];
	}
}
