package edu.fudan.nlp.tag;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import edu.fudan.ml.types.Instance;
import edu.fudan.nlp.tag.Format.Seq2ArrayWithTag;
import edu.fudan.nlp.tag.Format.Seq2StrWithTag;

public class POSTagger extends AbstractTagger {


	public POSTagger(String str) throws Exception {
		super(str);
	}

	public String tagShort(String src) {
		String[] sents = src.split("\\n");
		String tag = "";
		try {
			for (int i = 0; i < sents.length; i++) {
				if(sents[i].length()>0){
					Instance inst = new Instance(sents[i]);
					doProcess(inst);
					int[] pred = (int[]) cl.predict(inst).getPredAt(0);
					String[] target = labels.lookupString(pred);
					String s = Seq2StrWithTag.format(inst, target);
					tag += s;
				}
				if (i < sents.length - 1)
					tag += "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tag;
	}

	public String tagLong(String src) {
		src = src.replaceAll("。", "。 ");
		StringBuilder sb = new StringBuilder();
		String[] s = src.split("\\s+");
		for (int i = 0; i < s.length; i++) {
			sb.append(tagShort(s[i]));
		}
		return sb.toString();
	}

	public String[][] tag2Array(String src) {
		ArrayList words = new ArrayList<String>();
		ArrayList pos = new ArrayList<String>();
		src = src.replaceAll("。", "。 ");
		StringBuilder sb = new StringBuilder();
		String[] s = src.split("\\s+");
		try {
			for (int i = 0; i < s.length; i++) {
				Instance inst = new Instance(s[i]);
				doProcess(inst);
				int[] pred = (int[]) cl.predict(inst).getPredAt(0);
				String[] target = labels.lookupString(pred);
				List[] res = Seq2ArrayWithTag.format(inst, target);
				words.addAll(res[0]);
				pos.addAll(res[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[][] tag = new String[2][];
		tag[0] = (String[]) words.toArray(new String[words.size()]);
		tag[1] = (String[]) pos.toArray(new String[pos.size()]);
		return tag;
	}

	public String tag(String src) {
		if (src.length() > 200)
			return tagLong(src);
		else
			return tagShort(src);
	}

	public static void main(String[] args) throws Exception {
		Options opt = new Options();

		opt.addOption("h", false, "Print help for this application");
		opt.addOption("f", false, "segment file. Default string mode.");
		opt.addOption("s", false, "segment string");
		BasicParser parser = new BasicParser();
		CommandLine cl = parser.parse(opt, args);

		if (args.length == 0 || cl.hasOption('h')) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp(
					"Tagger:\n"
							+ "java edu.fudan.nlp.tag.POSTagger -f model_file input_file output_file;\n"
							+ "java edu.fudan.nlp.tag.POSTagger -s model_file string_to_segement",
							opt);
			return;
		}
		String[] arg = cl.getArgs();
		String modelFile;
		String input;
		String output = null;
		if (cl.hasOption("f") && arg.length == 3) {
			modelFile = arg[0];
			input = arg[1];
			output = arg[2];
		} else if (arg.length == 2) {
			modelFile = arg[0];
			input = arg[1];
		} else {
			System.err.println("paramenters format error!");
			System.err.println("Print option \"-h\" for help.");
			return;
		}
		POSTagger pos = new POSTagger(modelFile);
		if (cl.hasOption("f")) {
			String s = pos.tagFile(input);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
					output), "utf8");
			w.write(s);
			w.close();
		} else {
			String s = pos.tag(input);
			System.out.println(s);
		}
	}

	public String tagFile(String input) {
		StringBuilder res = new StringBuilder();
		try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(
					input), "utf-8");
			BufferedReader lbin = new BufferedReader(read);
			String str = lbin.readLine();
			while (str != null) {
				String s = tag(str);
				res.append(s);
				res.append("\n");
				str = lbin.readLine();
			}
			lbin.close();
			return res.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";

	}
}
