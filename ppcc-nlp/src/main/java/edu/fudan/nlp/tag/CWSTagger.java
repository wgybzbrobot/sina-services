package edu.fudan.nlp.tag;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import edu.fudan.ml.inf.struct.DictViterbi;
import edu.fudan.ml.inf.struct.LinearViterbi;
import edu.fudan.ml.types.Dictionary;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.PreTokenByDict;
import edu.fudan.nlp.pipe.Sequence2FeatureSequence;
import edu.fudan.nlp.pipe.Sequence2FeatureSequence_Dict;
import edu.fudan.nlp.pipe.SeriesPipes;
import edu.fudan.nlp.tag.Format.Seq2String;

public class CWSTagger extends AbstractTagger {
	//考虑不同CWStagger可能使用不同dict，所以不使用静态
	private Dictionary dict;

	public CWSTagger(String str) throws Exception {
		super(str);
	}
	
	private void initDict() {
		FeatureAlphabet features = factory.buildFeatureAlphabet("features");
		LabelAlphabet labels = factory.buildLabelAlphabet("labels");
		featurePipe = new SeriesPipes(new Pipe[]{new PreTokenByDict(dict, labels), 
				new Sequence2FeatureSequence_Dict(templets, features, labels)});
		DictViterbi dv = new DictViterbi((LinearViterbi) cl.getInferencer());
		cl.setInferencer(dv);
	}
	
	public CWSTagger(String str, Dictionary dict) throws Exception {
		super(str);
		this.dict = dict;
		initDict();
	}
	
	public CWSTagger(String str, String dictPath) throws Exception {
		super(str);
		dict = new Dictionary(dictPath);
		initDict();
	}
	
	public CWSTagger(String str, ArrayList<String> al) throws Exception {
		super(str);
		dict = new Dictionary(al);
		initDict();
	}
	
	public String tag(String src){
		String[] sents = src.split("\n");
		String tag="";
		try {
			for(int i=0;i<sents.length;i++){
				if(sents[i].length()>0){
					Instance inst = new Instance(sents[i]);
					
					doProcess(inst);
					int[] pred = (int[]) cl.predict(inst).getPredAt(0);
					
//					for(int j = 0; j < pred.length; j++) 
//						System.out.print(pred[j] + " ");
//					System.out.println();
					
					String[] target = labels.lookupString(pred);
					String s = Seq2String.format(inst, target);
					tag +=s;
				}
				if(i<sents.length-1)
					tag += "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tag;
	}

	public static void main(String[] args) throws Exception{
		Options opt = new Options();

		opt.addOption("h", false, "Print help for this application");
		opt.addOption("f", false, "segment file. Default string mode.");
		opt.addOption("s", false, "segment string");
		BasicParser parser = new BasicParser();
		CommandLine cl = parser.parse(opt, args);

		if (args.length == 0 || cl.hasOption('h') ) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp("SEG:\n" +
					"java edu.fudan.nlp.tag.CWSTagger -f model_file input_file output_file;\n" +
					"java edu.fudan.nlp.tag.CWSTagger -s model_file string_to_segement", opt);
			return;
		}		
		String[] arg = cl.getArgs();
		String modelFile;
		String input;
		String output = null;
		if(cl.hasOption("f")&&arg.length==3){
			modelFile = arg[0];
			input = arg[1];
			output = arg[2];
		}else if(arg.length==2){
			modelFile = arg[0];
			input = arg[1];
		}else{
			System.err.println("paramenters format error!");
			System.err.println("Print option \"-h\" for help.");
			return;
		}
		CWSTagger seg = new CWSTagger(modelFile);
		if(cl.hasOption("f")){
			String s = seg.tagFile(input);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(output), "utf8");
			w.write(s);
			w.close();
		}else{
			String s = seg.tag(input);
			System.out.println(s);
		}
	}
	
	
	public String tagFile(String input) {
		StringBuilder res = new StringBuilder();
		try {
			InputStreamReader  read = new InputStreamReader (new FileInputStream(input),"utf-8");
			BufferedReader lbin = new BufferedReader(read);
			String str = lbin.readLine();
			while(str!=null){
				String s= tag(str);
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
