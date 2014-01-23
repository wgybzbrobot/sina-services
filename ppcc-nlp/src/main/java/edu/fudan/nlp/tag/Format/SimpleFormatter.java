package edu.fudan.nlp.tag.Format;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;

public class SimpleFormatter {
	public static String format(InstanceSet testSet, String[][] labelsSet) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < testSet.size(); i++) {
			Instance inst = testSet.getInstance(i);
			String[] labels = labelsSet[i];
			sb.append(format(inst, labels));
			sb.append("\n");
		}
		return sb.toString();
	}

	public static String format(Instance inst, String[] labels) {

		StringBuilder sb = new StringBuilder();
		String[][] data = (String[][]) inst.getSource();

		for (int j = 0; j < data.length; j++) {
			for (int i = 0; i < data[j].length; i++) {
				sb.append(data[j][i]);
				sb.append(" ");
			}
			sb.append(labels[j]);
			sb.append("\n");
		}
		return sb.toString();
	}
}
