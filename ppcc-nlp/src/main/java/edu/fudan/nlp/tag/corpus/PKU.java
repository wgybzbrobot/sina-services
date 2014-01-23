package edu.fudan.nlp.tag.corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

import edu.fudan.nlp.pipe.String2Sequence;
import edu.fudan.nlp.pipe.String2SequenceWithTag;


public class PKU {

	private static boolean labeled=false;;

	
	public static void main(String[] args) throws Exception {

		String input1 ="D:/Datasets/人民日报 199801/199801.txt";
		String output1 = "D:/Datasets/人民日报 199801/key.txt";
		String2Sequence.delimer = "　+";
		
		File f = new File(input1);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				toNer(files[i].toString(),output1+files[i].getName());
			}
		}else{
			toNer(input1,output1);
		}
	

		System.out.println("Done");
	}

	public static void toNer (String input,String output) throws Exception{
		FileInputStream is = new FileInputStream(input);
//		is.skip(3); //skip BOM
		BufferedReader r = new BufferedReader(
				new InputStreamReader(is, "utf8"));
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(output), "utf8");
		while(true) {
			String sent = r.readLine();
			if(null == sent) break;
			HashSet<String> tagFilter = new HashSet<String>();
			tagFilter.add("ns");
			tagFilter.add("nr");
			tagFilter.add("nt");
			tagFilter.add("i");
			tagFilter.add("l");
			tagFilter.add("vn");
			String2SequenceWithTag.tagFilter = tagFilter;
			String2SequenceWithTag.tagDelimer = "/";
			String s = String2SequenceWithTag.genSequenceWithLabelSimple(sent);
			w.write(s);
		}
		r.close();
		w.close();
	}
	
	public static void toPos (String input,String output) throws Exception{
		FileInputStream is = new FileInputStream(input);
		
		BufferedReader r = new BufferedReader(
				new InputStreamReader(is, "utf8"));
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(output), "utf8");
		while(true) {
			String sent = r.readLine();
			if(null == sent) break;
			String2SequenceWithTag.tagDelimer = "/";
			String s = String2SequenceWithTag.genSequenceWithLabelSimple(sent);
			w.write(s);
		}
		r.close();
		w.close();
	}

}
