package edu.fudan.ml.inf;

import edu.fudan.ml.feature.generator.Generator;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.types.SparseVector;

/**
 * @author xpqiu
 * @version 1.0
 */
public class LinearMax extends Inferencer {

	private static final long serialVersionUID = -7602321210007971450L;
	
	private Generator generator;
	private int ysize;

	public LinearMax(Generator generator, int ysize) {
		this.generator = generator;
		this.ysize = ysize;
	}
	
	public Results getBest(Instance inst)	{
		return getBest(inst, 1);
	}

	public Results getBest(Instance inst, int n) {

		Integer target = null;
		if (isUseTarget && inst.getTarget() != null)
			target = (Integer) inst.getTarget();

		Results<Integer> res = new Results<Integer>(n);
		if (target != null) {
			res.buildOracle();
		}

		for (int i = 0; i < ysize; i++) {
			SparseVector fv = generator.getVector(inst, i);
			double score = fv.dotProduct(weights);
			if (target != null && target == i)
				res.addOracle(score, i);
			else
				res.addPred(score, i);
		}
		return res;
	}
	
}
