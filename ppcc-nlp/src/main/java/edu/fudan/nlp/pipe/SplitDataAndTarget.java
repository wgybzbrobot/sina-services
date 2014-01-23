package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import edu.fudan.ml.types.Instance;

/**
 * 将数据和标签分开
 * 
 * @author xpqiu
 * 
 */
public class SplitDataAndTarget extends Pipe implements Serializable {

	private static final long serialVersionUID = 331639154658696010L;

	public void addThruPipe(Instance instance) {
		List<String[]> seq = (List<String[]>) instance.getData();
		String[][] data = new String[seq.size()][];
		String[] target = new String[seq.size()];
		for (int i = 0; i < seq.size(); i++) {
			String[] arr = seq.get(i);
			if (arr.length < 2) {
				System.err
						.println("The number of column must be 2 at least. skip");
				System.err.println(arr[0]);
				continue;
			}
			data[i] = Arrays.copyOfRange(arr, 0, arr.length - 1);
			target[i] = arr[arr.length - 1];
		}
		instance.setData(data);
		instance.setTarget(target);
	}
}
