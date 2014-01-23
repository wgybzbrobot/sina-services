package edu.fudan.ml.inf.struct;

import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;

public abstract class AbstractViterbi extends Inferencer {

	private static final long serialVersionUID = 2627448350847639460L;

	public Results getBest(Instance inst) {
		return getBest(inst, 1);
	}
	
	public  abstract Results getBest(Instance carrier, int nbest);
	
}
