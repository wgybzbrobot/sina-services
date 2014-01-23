package edu.fudan.ml.classifier;

import edu.fudan.ml.types.InstanceSet;

public abstract class AbstractTrainer {

	public abstract Classifier train(InstanceSet trainset, InstanceSet devset);
	
	protected abstract void evaluate(InstanceSet devset);
	
}
