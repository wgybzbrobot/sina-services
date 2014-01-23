package edu.fudan.ml.classifier;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.nlp.pipe.Sequence2FeatureSequence;
import gnu.trove.TDoubleArrayList;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntIterator;

/**
 * 优化模型文件，去掉无用的特征
 * 
 * @author xpqiu
 * 
 */
public class ModelAnalysis {

	private Linear cl;
	private FeatureAlphabet feature;
	private double thresh = 0;
	private double[] weights;
	private Sequence2FeatureSequence p;

	public ModelAnalysis(Linear cl) {
		this.cl = cl;
		this.feature = cl.getAlphabetFactory().buildFeatureAlphabet("features");
		this.weights = cl.getWeights();
	}

	/**
	 * 统计信息，计算删除非0特征后，权重的长度
	 * 
	 * @throws IOException
	 */
	public void removeZero() {
		boolean freeze = false;
		if (feature.isStopIncrement()) {
			feature.setStopIncrement(false);
			freeze = true;
		}

		TIntObjectHashMap<String> index = new TIntObjectHashMap<String>();
		TObjectIntIterator<String> it = feature.iterator();
		while (it.hasNext()) {
			it.advance();
			String value = it.key();
			int key = it.value();
			index.put(key, value);
		}
		int[] idx = index.keys();
		Arrays.sort(idx);
		int length = this.weights.length;
		FeatureAlphabet newfeat = cl.getAlphabetFactory().rebuildFeatureAlphabet("features");
		TDoubleArrayList ww = new TDoubleArrayList();
		for (int i = 0; i < idx.length; i++) {
			int base = idx[i];
			int end = length;
			if (i < idx.length - 1)
				end = idx[i + 1];
			boolean del = true;
			for (int j = base; j < end; j++) {
				if (this.weights[j] != 0) {
					del = false;
					break;
				}
			}
			int interv = end - base;
			if (!del) {
				String str = index.get(base);
				int id = newfeat.lookupIndex(str, interv);
				for (int j = 0; j < interv; j++) {
					ww.insert(id + j, weights[base + j]);
				}
			}
		}
		
		newfeat.setStopIncrement(freeze);
		cl.setWeights(ww.toNativeArray());
		
		index.clear();
		ww.clear();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1)
			return;
		String file = args[0];
		Linear cl = loadFrom(file);

		// double[][] hist = MyArrays.histogram(weis, 100);
		ModelAnalysis ma = new ModelAnalysis(cl);

		ma.removeZero();
		// cl.saveTo(file);
		System.out.print("Done");
	}

	protected static Linear loadFrom(String modelfile) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
				new GZIPInputStream(new FileInputStream(modelfile))));
		TempletGroup templets = (TempletGroup) in.readObject();
		Linear cl = (Linear) in.readObject();
		in.close();
		return cl;
	}

}
