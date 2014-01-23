package edu.fudan.nlp.tag.Format;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import edu.fudan.ml.data.SequenceReader;
import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.SeriesPipes;
import edu.fudan.nlp.pipe.SplitDataAndTarget;
import edu.fudan.nlp.pipe.StringTokenizer;

public class Conll2String {
	public static void main(String[] args) throws Exception {
		convert("D:/Datasets/sighan2005/output/as.conll","D:/Datasets/sighan2005/output/as-pa");
	}
	
	public static void convert(String input, String output) throws Exception{

		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(output), "utf8");
		LabelAlphabet labels = AlphabetFactory.buildFactory().buildLabelAlphabet("labels");

		Pipe prePipe = new SeriesPipes(new Pipe[] {
				new StringTokenizer(),
				new SplitDataAndTarget()});
		InstanceSet set = new InstanceSet(prePipe);

		// 训练集
		set.loadThruStagePipes(new SequenceReader(input, "utf8"));
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<set.size(); i++) {
			Instance inst = set.getInstance(i);
			List t = (List) inst.getTarget();
			String[] lab = (String[]) t.toArray(new String[t.size()]);
			List<String> data = (List<String>) inst.getSource();
			sb.append(Seq2String.format(inst, lab));
			sb.append("\n");
		}
		w.write(sb.toString());
		System.out.print(sb);
		
	}
		
}
