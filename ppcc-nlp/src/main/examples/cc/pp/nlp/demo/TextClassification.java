package cc.pp.nlp.demo;

import edu.fudan.ml.classifier.Linear;
import edu.fudan.ml.classifier.OnlineTrainer;
import edu.fudan.ml.data.SimpleFileReader;
import edu.fudan.ml.data.StringReader;
import edu.fudan.ml.feature.generator.Generator;
import edu.fudan.ml.feature.generator.SFGenerator;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.inf.LinearMax;
import edu.fudan.ml.loss.ZeroOneLoss;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.update.LinearMaxPAUpdate;
import edu.fudan.ml.update.Update;
import edu.fudan.nlp.pipe.FeaturePipe;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.SeriesPipes;
import edu.fudan.nlp.pipe.String2NgramToken;
import edu.fudan.nlp.pipe.Target2Label;

/**
 * 文本分类示例
 * @author xpqiu
 *
 */

public class TextClassification {

	/**
	 * 训练文件
	 */
	private String trainFile = "./example-data/text-classification/train.txt";

	/**
	 * 模型文件
	 */
	private static String modelFile = "./example-data/text-classification/model.gz";

	public static void main(String[] args) throws Exception {

		TextClassification tc = new TextClassification();
		tc.train();
		Linear cl =Linear.loadFrom(modelFile);
		
		InstanceSet test = new InstanceSet(cl.getPipe());
		test.loadThruPipes(new StringReader(new String[] { "唱过什么歌?","张学友是谁？","在哪里？" }));
		for(int i=0;i<test.size();i++){
			String ss = cl.getLabel(test.getInstance(i));
			System.out.println(ss);
		}
		System.gc();
	}

	/**
	 * 训练
	 * @throws Exception
	 */
	public void train() throws Exception {

		//建立字典管理器
		AlphabetFactory af = AlphabetFactory.buildFactory();
		
		SeriesPipes prepipe = new SeriesPipes(new Pipe[]{new String2NgramToken(
				new int[] {2, 3 }),new Target2Label(af.DefaultLabelAlphabet())});
		SeriesPipes pp = new SeriesPipes(new Pipe[]{prepipe,new FeaturePipe(af)});

		InstanceSet instset = new InstanceSet(pp,af);
		instset.loadThruStagePipes(new SimpleFileReader(trainFile));

		Generator gen = new SFGenerator();
		ZeroOneLoss l = new ZeroOneLoss();
		Inferencer ms = new LinearMax(gen, af.getLabelSize());
		Update update = new LinearMaxPAUpdate();
		OnlineTrainer trainer = new OnlineTrainer(ms, update,l, af.getFeatureSize(), 20,0.01);
		Linear pclassifier = trainer.train(instset, instset);
		pp.removeTargetPipe();
		pclassifier.setPipe(pp);
		af.setStopIncrement(true);
		pclassifier.saveTo(modelFile);
	}
}