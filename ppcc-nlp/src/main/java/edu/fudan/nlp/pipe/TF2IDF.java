package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Iterator;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.ml.types.SparseVector;
import gnu.trove.TIntDoubleIterator;

/**
 * 由TF计算IDF
 * 
 * @author xpqiu
 * 
 */
public class TF2IDF extends Pipe implements Serializable {

	private static final long serialVersionUID = 5563900451276233502L;
	public int[] idf;

	public TF2IDF(InstanceSet train, InstanceSet test) {

		int numFeatures = 0;
		// 得到最大的特征维数
		for (int i = 0; i < train.size(); i++) {
			int len = ((SparseVector) train.getInstance(i).getData()).size();
			if (len > numFeatures)
				numFeatures = len;
		}
		for (int i = 0; i < test.size(); i++) {
			int len = ((SparseVector) test.getInstance(i).getData()).size();
			if (len > numFeatures)
				numFeatures = len;
		}
		idf = new int[numFeatures + 1];

	}

	@Override
	public void addThruPipe(Instance inst) {
		SparseVector data = (SparseVector) inst.getData();
		Iterator<Integer> it = data.iterator();
		while (it.hasNext()) {
			int id = it.next();
			idf[id]++;
		}
	}

}
