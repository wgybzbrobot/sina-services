package edu.fudan.nlp.tag.Format;

import java.util.List;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;

public class Seq2String {

	public static String format(InstanceSet testSet, String[][] labelsSet) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < testSet.size(); i++) {
			Instance inst = testSet.getInstance(i);
			String[] labels = labelsSet[i];
			sb.append(format(inst, labels));
		}
		return sb.toString();
	}

	public static String format(Instance inst, String[] labels) {
		String[][] data = (String[][]) inst.getSource();

		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < data.length; j++) {
			String label = labels[j];
			String w = data[j][0];
			sb.append(w);
			if (label.equals("E") || label.equals("S")) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

}
