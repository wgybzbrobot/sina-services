package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.fudan.ml.types.Instance;

/**
 * 将字符串转为ngram记号序列
 * 数据类型由String转换为ArrayList\<String\>
 * @author xpqiu
 *
 */
public class String2NgramToken extends Pipe implements Serializable {

	private static final long serialVersionUID = -3622451640920544061L;
	int[] gramSizes = null;

	public String2NgramToken(int[] sizes) {
		this.gramSizes = sizes;
	}

	public void addThruPipe(Instance instance) {
		String data = (String) instance.getData();
		instance.setSource(data);
		ArrayList<String> list = ngram(data,gramSizes);
		instance.setData(list);
	}
	/**
	 * 提取ngram
	 * @param data
	 * @param gramSizes
	 * @return
	 */
	public static ArrayList<String> ngram(String data,int[] gramSizes) {
		// 提取ngram
		ArrayList<String> list = new ArrayList<String>();
		ngram(data, gramSizes, list);
		return list;
	}
	
	/**
	 * 提取ngram
	 * @param data
	 * @param gramSizes
	 * @return
	 */
	public static Set<String> ngramSet(String data,int[] gramSizes) {
		// 提取ngram
		Set<String> list = new HashSet<String>();
		ngram(data, gramSizes, list);
		return list;
	}

	private static void ngram(String data, int[] gramSizes, Collection<String> list) {
		for (int j = 0; j < gramSizes.length; j++) {
			int len = gramSizes[j];
			if (len <= 0 || len > data.length())
				continue;
			for (int i = 0; i < data.length() - len; i++) {
				list.add(data.substring(i, i + len));
			}
		}
	}

}