package edu.fudan.ml.feature.generator;

import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.SparseVector;

/**
 * 简单将data返回 特征不包含类别信息
 * 
 * @author xpqiu
 * 
 */
public class BaseGenerator extends Generator {

	private static final long serialVersionUID = 5209575930740335391L;
	
	protected FeatureAlphabet alphabet = null;

	public BaseGenerator(FeatureAlphabet alphabet) {
		this.alphabet = alphabet;
	}

	public void setStopIncrement(boolean stopIncrement) {
		alphabet.setStopIncrement(stopIncrement);
	}

	public FeatureAlphabet getAlphabet() {
		return this.alphabet;
	}

	public void setAlphabet(FeatureAlphabet alphabet) {
		this.alphabet = alphabet;
	}

	public SparseVector getVector(Instance inst) {

		return (SparseVector) inst.getData();
	}

	public SparseVector getVector(Instance inst, Object object) {
		return getVector(inst);
	}
}
