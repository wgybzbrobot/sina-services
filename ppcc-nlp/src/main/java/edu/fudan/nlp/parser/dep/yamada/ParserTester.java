package edu.fudan.nlp.parser.dep.yamada;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import edu.fudan.ml.types.Results;

/**
 * 性能测试类
 * 
 * @version Feb 16, 2009
 */
public class ParserTester {

	Parser parser;
	boolean finaltest = true;

	/**
	 * 构造函数
	 * 
	 * @param modelfile
	 *            模型目录
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public ParserTester(String modelfile) throws IOException, ClassNotFoundException {
		parser = new Parser(modelfile);
	}

	/**
	 * 测试阶段
	 * 
	 * 对于输入文件的所有句子作依赖文法分析
	 * 
	 * @param testFile
	 *            测试文件
	 * @param resultFile
	 *            结果文件
	 * @throws Exception
	 */
	public void test(String testFile, String resultFile, String charset)
			throws Exception {
		// HashMap<String, HashMap<String, Integer>> featureAlphabetByPos =
		// buildFeatureAlphabet(testFile);
		int error = 0;
		int total = 0;
		int errsent = 0;
		int totsent = 0;

		System.out.print("Beginning the test ... ");
		// 输入
		CoNLLReader reader = new CoNLLReader(testFile);

		// 输出

		BufferedWriter writer = null;
		if (finaltest)
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(resultFile), charset));

		long beginTime = System.currentTimeMillis();
		while (reader.hasNext()) {
			Sentence instance = (Sentence) reader.next();
			int[] golds = new int[instance.length()];
			System.arraycopy(instance.heads, 0, golds, 0, golds.length);
			instance.setSource(golds);

			Results pred = parser.getBest(instance);

			int[] preds = (int[]) pred.getPredAt(0);
			int curerr = diff(golds, preds);
			if (curerr != 0) {
				errsent++;
				error += curerr;
			}
			totsent++;
			total += golds.length;
			golds = null;

			if (finaltest)
				instance.writeInstance(writer);
		}
		if (finaltest)
			writer.close();
		
		long endTime = System.currentTimeMillis();

		parser = null;

		double time = (endTime - beginTime) / 1000.0;
		System.out.println("finish! =]");
		System.out.printf("total time:\t%.2f(s)\n", time);
		System.out.printf("errate(words):\t%.8f\ttotal(words):\t%d\n", 1.0
				* error / total, total);
		System.out.printf("errate(sents):\t%.8f\ttotal(sents):\t%d\n", 1.0
				* errsent / totsent, totsent);
		System.out.printf("average speed:\t%.2f(s/word)\t%.2f(s/sent)",
				total / time, totsent / time);
	}

	/**
	 * 比较函数
	 * 
	 * @param golds
	 *            标准依存关系树
	 * @param preds
	 *            预测的依存关系树
	 * @return 不同的依存关系的数量
	 */
	private int diff(int[] golds, int[] preds) {
		int ret = 0;

		int[] ref = golds;
		if (golds.length > preds.length)
			ref = preds;
		for (int i = 0; i < ref.length; i++)
			if (golds[i] != preds[i])
				ret++;

		return ret;
	}

	public static void main(String[] args) throws Exception {
		
		Options opt = new Options();

		opt.addOption("h", false, "Print help for this application");

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
							+ "ParserTester [option] model_file test_file result_file;\n",
					opt);
			return;
		}
		
		String modelfile = args[0];
		String testfile = args[1];
		String resultfile = args[2];
		
		ParserTester tester = new ParserTester(modelfile);
		tester.test(testfile, resultfile, "UTF-8");
	}
}
