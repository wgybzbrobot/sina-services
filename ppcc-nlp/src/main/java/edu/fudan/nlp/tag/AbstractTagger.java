package edu.fudan.nlp.tag;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import edu.fudan.ml.classifier.Linear;
import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.Sequence2FeatureSequence;
import edu.fudan.nlp.pipe.String2Sequence;

public abstract class AbstractTagger {

	protected Linear cl;
	protected Pipe prePipe;
	protected Pipe featurePipe;
	protected AlphabetFactory factory;
	protected TempletGroup templets;
	protected LabelAlphabet labels;
	
	public abstract Object tag(String src);
	
	public AbstractTagger(String file) throws Exception	{
		try {
			loadFrom(file);
		} catch (IOException e) {
			System.err.println("模型文件路径错误。" + e.toString());
			throw e;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("模型文件版本错误。");
			throw e;
		}
		if(cl==null){
			System.err.println("模型为空");
			throw new Exception("模型为空");
		}
			
		factory = cl.getAlphabetFactory();
		labels = factory.buildLabelAlphabet("labels");
		FeatureAlphabet features = factory.buildFeatureAlphabet("features");
		
		prePipe = new String2Sequence(false);
		featurePipe = new Sequence2FeatureSequence(templets, features,
				labels);
	}
	
	public void doProcess(Instance carrier)	{
		try {
			prePipe.addThruPipe(carrier);
			carrier.setSource(carrier.getData());
			featurePipe.addThruPipe(carrier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void loadFrom(String modelfile) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
				new GZIPInputStream(new FileInputStream(modelfile))));
		templets = (TempletGroup) in.readObject();
		cl = (Linear) in.readObject();
		in.close();
	}
}
