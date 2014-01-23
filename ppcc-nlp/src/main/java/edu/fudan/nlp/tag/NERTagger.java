package edu.fudan.nlp.tag;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import edu.fudan.ml.types.Instance;

public class NERTagger extends AbstractTagger {


	public NERTagger(String file) throws Exception{
		super(file);
	}

	public HashMap<String,String> tag(String src){
		HashMap<String,String> map = new HashMap<String,String>();
		String[] sents = src.split("\\n+");
		try {
			for(int i=0;i<sents.length;i++){
				if(sents[i].length()>0){
					Instance inst = new Instance(sents[i]);
					doProcess(inst);
					int[] pred = (int[]) cl.predict(inst).getPredAt(0);
					if(pred ==null)
						continue;
					String[] target = labels.lookupString(pred);
					analysis(inst,target, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	private HashMap<String,String> analysis(Instance inst, String[] target, HashMap<String, String> map) {
		String[][] data = (String[][]) inst.getSource();
		StringBuilder sb = new StringBuilder();
		String label=null;
		String oldtag = "";
		int len = data.length/data[0].length;
		for(int i=0; i<len; i++) {
			int idx = target[i].lastIndexOf("-");
			String tag = target[i].substring(idx+1);
			if(target[i].equals("O")||(idx!=-1&&!tag.equals(oldtag))){
				if(sb.length()>0){
					map.put(sb.toString(), oldtag);
					sb = new StringBuilder();
					label=null;
				}
			}
			oldtag = tag;
			if(target[i].equals("O"))
				continue;
			sb.append(data[i][0]);
		}
		return map;
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
			f.printHelp("Tagger:\n" +
					"java edu.fudan.nlp.tag.NERTagger -f model_file input_file output_file;\n" +
					"java edu.fudan.nlp.tag.NERTagger -s model_file string_to_segement", opt);
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
		NERTagger ner = new NERTagger(modelFile);
		if(cl.hasOption("f")){
			String s = ner.tagFile(input);
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(output), "utf8");
			w.write(s);
			w.close();
		}else{
			HashMap<String, String> s = ner.tag(input);
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
				HashMap<String, String> s= tag(str);
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
