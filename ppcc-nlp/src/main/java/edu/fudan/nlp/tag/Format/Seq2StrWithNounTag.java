package edu.fudan.nlp.tag.Format;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;

public class Seq2StrWithNounTag {

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
		// StringBuilder sbt = new StringBuilder();

		for (int j = 0; j < data.length; j++) {
			String label = labels[j];
			// String nextlabel = j + 1 == data.size() ? "O" : labels[j + 1];
			String w = data[j][0];

			if (label.equals("O") == false) {
				sb.append(w);
				if (label.equals("E") || label.equals("S")) {
					sb.append(" ");
				}
			}
			/*
			 * if(label.equals("O") == false) { sbt.append(w);
			 * 
			 * if(label.equals("I") && nextlabel.equals("I") == false) {
			 * sb.append(sbt + " "); sbt = new StringBuilder(); } }
			 */

			/*
			 * sb.append(w); if(label.equals("O")) { sb.append("/O "); }
			 * if(label.equals("B")) { sb.append("/B "); } if(label.equals("E"))
			 * { sb.append("/E "); } if(label.equals("S")) { sb.append("/S "); }
			 * if(label.equals("M")) { sb.append("/M "); }
			 */
		}
		return sb.toString();
	}

}
