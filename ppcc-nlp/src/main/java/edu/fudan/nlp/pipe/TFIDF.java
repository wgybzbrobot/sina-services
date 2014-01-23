package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Iterator;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.SparseVector;
import gnu.trove.TIntDoubleIterator;

/**
 * 计算IFIDF
 * 
 * @author xpqiu
 * 
 */
public class TFIDF extends Pipe implements Serializable {

	private static final long serialVersionUID = 2937341538282834618L;
	int[] idf;
	private int docNum;

	public TFIDF(int[] idf, int docNum) {
		this.idf = idf;
		this.docNum = docNum;
	}

	@Override
	public void addThruPipe(Instance inst) {
		SparseVector data = (SparseVector) inst.getData();
		Iterator<Integer> it = data.iterator();
		while (it.hasNext()) {
			int id = it.next();
			if (idf[id] > 0) {
				double value = data.get(id)*Math.log(docNum / idf[id]);
				data.put(id, value);
			}
		}

	}

}
