package edu.fudan.ml.classifier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;
import edu.fudan.nlp.pipe.Pipe;

/**
 * 线性分类器
 * 
 * @author xpqiu
 * 
 */
public class Linear implements Classifier	{

	private static final long serialVersionUID = -2626247109469506636L;

	protected Inferencer inferencer;
	protected AlphabetFactory factory;
	protected Pipe pipe;

	public Linear(Inferencer inferencer, AlphabetFactory factory) {
		this.inferencer = inferencer;
		this.factory = factory;
	}

	public Linear() {		
	}

	public Results predict(Instance instance) {

		Results pred = (Results) inferencer.getBest(instance, 1);

		return pred;
	}

	public Results predict(Instance instance, int n) {
		return inferencer.getBest(instance, n);
	}
	
	/**
	 * 得到类标签
	 * @param instance
	 * @return
	 */
	public String getLabel(Instance instance) {
		int idx =  (Integer) inferencer.getBest(instance, 1).getPredAt(0);
		return factory.DefaultLabelAlphabet().lookupString(idx);
	}

	
	public void saveTo(String file) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(
				new BufferedOutputStream(new FileOutputStream(file))));
		out.writeObject(this);
		out.close();
	}

	public static Linear loadFrom(String file) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(
				new BufferedInputStream(new FileInputStream(file))));
		Linear cl = (Linear) in.readObject();
		in.close();
		return cl;
	}

	public Inferencer getInferencer() {
		return inferencer;
	}
	
	public void setInferencer(Inferencer inferencer)	{
		this.inferencer = inferencer;
	}

	public AlphabetFactory getAlphabetFactory() {
		return factory;
	}

	public void setWeights(double[] weights) {
		inferencer.setWeights(weights);
	}

	public double[] getWeights() {
		return inferencer.getWeights();
	}

	public void setPipe(Pipe pipe) {
		this.pipe = pipe;		
	}
	public Pipe getPipe() {
		return pipe;		
	}

}
