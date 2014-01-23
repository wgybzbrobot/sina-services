package edu.fudan.nlp.tag;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import edu.fudan.ml.classifier.Linear;
import edu.fudan.ml.classifier.ModelAnalysis;
import edu.fudan.ml.classifier.OnlineTrainer;
import edu.fudan.ml.classifier.struct.OnlineStructTrainer;
import edu.fudan.ml.data.SequenceReader;
import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.inf.struct.LinearViterbi;
import edu.fudan.ml.inf.struct.Viterbi;
import edu.fudan.ml.loss.Loss;
import edu.fudan.ml.loss.struct.HammingLoss;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.update.Update;
import edu.fudan.ml.update.struct.LinearViterbiPAUpdate;
import edu.fudan.ml.update.struct.WeightUpdate;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.Sequence2FeatureSequence;
import edu.fudan.nlp.pipe.SeriesPipes;
import edu.fudan.nlp.pipe.SplitDataAndTarget;
import edu.fudan.nlp.pipe.StringTokenizer;
import edu.fudan.nlp.pipe.Target2Label;
import edu.fudan.nlp.tag.Format.BasicFormatter;
import edu.fudan.nlp.tag.Format.SimpleFormatter;

/**
 * 序列标注入口程序
 * 
 * @author xpqiu
 * 
 */
public class Tagger {

	Linear cl;
	String train;
	String testfile = null;
	String output = null;
	String templateFile;
	private String model;
	private int iterNum = 40;
	private double c1 = 1;
	private double c2 = 0.1;
	private boolean useLoss = true;
	private String delimiter = "\\s+|\\t+";
	private boolean interim = false;
	private AlphabetFactory factory;
	private Pipe prePipe;
	private Pipe featurePipe;
	private TempletGroup templets;

	public Tagger() {
		prePipe = new SeriesPipes(new Pipe[] { new StringTokenizer(delimiter),
				new SplitDataAndTarget() });
	}

	public void setFile(String templateFile, String train, String model) {
		this.templateFile = templateFile;
		this.train = train;
		this.model = model;
	}

	/**
	 * 训练： java -classpath fudannlp.jar edu.fudan.nlp.tag.Tagger -train template train model 
	 * 测试： java -classpath fudannlp.jar edu.fudan.nlp.tag.Tagger model test [result]
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Options opt = new Options();

		opt.addOption("h", false, "Print help for this application");
		opt.addOption("iter", true, "iterative num, default 50");
		opt.addOption("c1", true, "parameters 1, default 1");
		opt.addOption("c2", true, "parameters 2, default 0.1");
		opt.addOption("train", false,
				"switch to training mode(Default: test model");
		opt.addOption("labelwise", false,
				"switch to labelwise mode(Default: viterbi model");
		opt.addOption("margin", false, "use hamming loss as margin threshold");
		opt.addOption("interim", false, "save interim model file");

		BasicParser parser = new BasicParser();
		CommandLine cl;
		try {
			cl = parser.parse(opt, args);
		} catch (Exception e) {
			System.err.println("Parameters format error");
			return;
		}

		if (args.length == 0 || cl.hasOption('h')) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp(
					"Tagger:\n"
							+ "tagger [option] -train templet_file train_file model_file [test_file];\n"
							+ "tagger [option] model_file test_file output_file\n",
							opt);
			return;
		}
		Tagger tagger = new Tagger();
		tagger.iterNum = Integer.parseInt(cl.getOptionValue("iter", "50"));
		tagger.c1 = Double.parseDouble(cl.getOptionValue("c1", "1"));
		tagger.c2 = Double.parseDouble(cl.getOptionValue("c2", "0.1"));
		tagger.useLoss = cl.hasOption("margin");
		tagger.interim = cl.hasOption("interim");

		String[] arg = cl.getArgs();
		if (cl.hasOption("train") && arg.length == 3) {
			tagger.templateFile = arg[0];
			tagger.train = arg[1];
			tagger.model = arg[2];
			System.out.println("Training model ...");
			tagger.train();
		} else if (cl.hasOption("train") && arg.length == 4) {
			tagger.templateFile = arg[0];
			tagger.train = arg[1];
			tagger.model = arg[2];
			tagger.testfile = arg[3];
			System.out.println("Training model ...");
			tagger.train();
		} else if (cl.hasOption("train") && arg.length == 5) {
			tagger.templateFile = arg[0];
			tagger.train = arg[1];
			tagger.model = arg[2];
			tagger.testfile = arg[3];
			System.out.println("Training model ...");
			tagger.train();
			System.gc();
			tagger.output = arg[4];
			tagger.test();
		} else if (arg.length == 3) {
			tagger.model = arg[0];
			tagger.testfile = arg[1];
			tagger.output = arg[2];
			tagger.test();
		} else if (arg.length == 2) {
			tagger.model = arg[0];
			tagger.testfile = arg[1];
			tagger.test();
		} else {
			System.err.println("paramenters format error!");
			System.err.println("Print option \"-h\" for help.");
			return;
		}

		System.gc();

	}

	/**
	 * @throws Exception
	 */
	public Pipe createProcessor(boolean flag) throws Exception {
		if (!flag) {
			templets = new TempletGroup();
			templets.load(templateFile);

			// Dictionary d = new Dictionary();
			// d.loadWithWeigth("D:/xpqiu/项目/自选/CLP2010/CWS/av-b-lut.txt",
			// "AV");
			// templets.add(new DictionaryTemplet(d, gid++, -1, 0));
			// templets.add(new DictionaryTemplet(d, gid++, 0, 1));
			// templets.add(new DictionaryTemplet(d, gid++, -1,0, 1));
			// templets.add(new DictionaryTemplet(d, gid++, -2,-1,0, 1));
			// templates.add(new CharRangeTemplet(templates.gid++,new
			// int[]{0}));
			// templates.add(new CharRangeTemplet(templates.gid++,new
			// int[]{-1,0}));
			// templates.add(new CharRangeTemplet(templates.gid++,new
			// int[]{-1,0,1}));
		}

		if (cl != null)
			factory = cl.getAlphabetFactory();
		else
			factory = AlphabetFactory.buildFactory();

		/**
		 * 标签转为0、1、2、...
		 */
		LabelAlphabet labels = factory.buildLabelAlphabet("labels");

		// 将样本通过Pipe抽取特征
		FeatureAlphabet features = factory.buildFeatureAlphabet("features");
		featurePipe = new Sequence2FeatureSequence(templets, features, labels);

		Pipe pipe = new SeriesPipes(new Pipe[] { prePipe,
				new Target2Label(labels), featurePipe });
		return pipe;
	}

	public void train() throws Exception {

		System.out.print("Loading training data ...");
		long beginTime = System.currentTimeMillis();

		Pipe pipe = createProcessor(false);
		InstanceSet trainSet = new InstanceSet(pipe, factory);

		LabelAlphabet labels = factory.buildLabelAlphabet("labels");
		FeatureAlphabet features = factory.buildFeatureAlphabet("features");

		// 训练集
		trainSet.loadThruStagePipes(new SequenceReader(train, "utf8"));

		long endTime = System.currentTimeMillis();
		System.out.println(" done!");
		System.out
		.println("Time escape: " + (endTime - beginTime) / 1000 + "s");
		System.out.println();

		// 输出
		System.out.println("Training Number: " + trainSet.size());

		System.out.println("Label Number: " + labels.size()); // 标签个数
		System.out.println("Feature Number: " + features.size()); // 特征个数

		// 冻结特征集
		features.setStopIncrement(true);
		labels.setStopIncrement(true);

		InstanceSet testSet = null;
		// /////////////////
		if (testfile != null) {

			Pipe tpipe;
			if (false) {// 如果test data没有标注
				SeriesPipes tprePipe = new SeriesPipes(
						new Pipe[] { new StringTokenizer() });
				tpipe = new SeriesPipes(new Pipe[] { tprePipe, featurePipe });
			} else {
				tpipe = pipe;
			}

			// 测试集
			testSet = new InstanceSet(tpipe);

			testSet.loadThruStagePipes(new SequenceReader(testfile, "utf8"));
			System.out.println("Test Number: " + testSet.size()); // 样本个数
		}

		/**
		 * 
		 * 更新参数的准则
		 */
		Update update;
		// viterbi解码
		Inferencer inference;
		boolean standard = true;
		if (standard) {
			update = new LinearViterbiPAUpdate(templets, labels.size());
			inference = new LinearViterbi(templets, labels.size());
		} else {
			inference = new Viterbi(features, labels.size(), templets);
			update = new WeightUpdate(templets, labels.size(), true);
		}

		Loss loss = new HammingLoss();

		OnlineTrainer trainer = new OnlineStructTrainer(inference, update, loss,
				features.size(), iterNum, c1);

		cl = trainer.train(trainSet, testSet);

		ModelAnalysis ma = new ModelAnalysis(cl);
		ma.removeZero();

		saveTo(model);

	}

	protected void saveTo(String modelfile) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new GZIPOutputStream(
						new FileOutputStream(modelfile))));
		out.writeObject(templets);
		out.writeObject(cl);
		out.close();
	}

	protected void loadFrom(String modelfile) throws IOException,
	ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
				new GZIPInputStream(new FileInputStream(modelfile))));
		templets = (TempletGroup) in.readObject();
		cl = (Linear) in.readObject();
		in.close();
	}

	private void test() throws Exception {
		if (cl == null)
			loadFrom(model);

		long starttime = System.currentTimeMillis();
		// 将样本通过Pipe抽取特征
		Pipe pipe = createProcessor(true);

		// 测试集
		InstanceSet testSet = new InstanceSet(pipe);

		testSet.loadThruStagePipes(new SequenceReader(testfile, "utf8"));
		System.out.println("Test Number: " + testSet.size()); // 样本个数

		long featuretime = System.currentTimeMillis();

		boolean acc = true;
		double error = 0;
		int len = 0;
		Loss loss = new HammingLoss();

		String[][] labelsSet = new String[testSet.size()][];
		LabelAlphabet labels = cl.getAlphabetFactory().buildLabelAlphabet(
				"labels");
		for (int i = 0; i < testSet.size(); i++) {
			Instance carrier = testSet.get(i);
			int[] pred = (int[]) cl.predict(carrier).getPredAt(0);
			if (acc) {
				len += pred.length;
				error += loss.calc(carrier.getTarget(), pred);
			}
			labelsSet[i] = labels.lookupString(pred);
		}

		long endtime = System.currentTimeMillis();
		System.out.println("totaltime\t" + (endtime - starttime) / 1000.0);
		System.out.println("feature\t" + (featuretime - starttime) / 1000.0);
		System.out.println("predict\t" + (endtime - featuretime) / 1000.0);

		if (acc) {
			System.out.println("Test Accuracy:\t" + (1 - error / len));
		}

		if (output != null) {
			BufferedWriter prn = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(output), "utf8"));
			String s = BasicFormatter.format(testSet, labelsSet);
			prn.write(s.trim());
			prn.close();
		}
		System.out.println("Done");
	}

}
