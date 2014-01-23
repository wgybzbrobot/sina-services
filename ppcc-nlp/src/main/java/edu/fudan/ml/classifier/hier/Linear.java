package edu.fudan.ml.classifier.hier;

import java.io.Serializable;

import edu.fudan.ml.feature.generator.BaseGenerator;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.types.SparseVector;
import edu.fudan.nlp.pipe.Pipe;

public class Linear implements Serializable {

	private static final long serialVersionUID = -1599891626397888670L;
	public Pipe pipes;
	SparseVector[] weight;
	private Inferencer msolver;
	private BaseGenerator gen;
	private LabelAlphabet labelAlphabet;

	public Linear(SparseVector[] weights, Inferencer msolver) {	
		this.weight = weights;
		this.msolver = msolver;
		this.msolver.isUseTarget(false);
	}
	
	public Linear(SparseVector[] weights, Inferencer msolver, 
			BaseGenerator gen, Pipe pipes, LabelAlphabet labelAlphabet) {
		this.weight = weights;
		this.pipes = pipes;
		this.gen = gen;
		this.gen.setStopIncrement(true);
		this.msolver = msolver;
		this.msolver.isUseTarget(false);
		this.labelAlphabet = labelAlphabet;
		this.labelAlphabet.setStopIncrement(true);
	}

	public Object classify(Instance instance) {
		Results pred = (Results) msolver.getBest(instance, 1);		
		return pred.predList[0];
	}
	
	public int[] classify(InstanceSet instance) {
		int[] pred= new int[instance.size()];
		for(int i=0;i<instance.size();i++){
			pred[i]=  (Integer) classify(instance.getInstance(i));			
		}
		return pred;
	}
	
	public String getLabel(Instance instance){
		Results pred = (Results) msolver.getBest(instance, 1);
		return (String) labelAlphabet.lookupString((Integer)pred.predList[0]);
	}
	
}
