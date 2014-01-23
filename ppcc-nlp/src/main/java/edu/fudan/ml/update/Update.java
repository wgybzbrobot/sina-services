package edu.fudan.ml.update;

import edu.fudan.ml.types.Instance;

public interface Update {
	
	public int update(Instance inst, double[] weights, Object predictLabel,
			double c);

	public int update(Instance inst, double[] weights, Object predictLabel,
			Object goldenLabel, double c);

}
