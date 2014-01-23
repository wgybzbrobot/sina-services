package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Arrays;

import edu.fudan.ml.types.Dictionary;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;

/**
 * 将字符序列转换成特征序列 因为都是01特征，这里保存的是索引号
 * 
 * @author xpqiu
 * 
 */
public class PreTokenByDict extends Pipe implements Serializable {

	private static final long serialVersionUID = -8634966199670429510L;

	private Dictionary dict;
	private LabelAlphabet labels;

	public PreTokenByDict(Dictionary dict, LabelAlphabet labels) {
		this.dict = dict;
		this.labels = labels;
	}

	public void addThruPipe(Instance instance) throws Exception {
		String[][] data = (String[][]) instance.getData();

		int[] tempData = new int[data.length];
		Arrays.fill(tempData, -1);

		if (dict != null) {
			for (int i = 0; i < data.length; i++) {
				for (int n = dict.getMaxLen(); n >= dict.getMinLen(); n--) {
					if (i + n <= data.length) {
						String s = getNextN(data, i, n);
						if (dict.contains(s)) {
							// 下面这部分依赖{1=B,2=M,3=E,0=S}
							if (labels.size() == 4) {
								if (n == 1)
									tempData[i] = labels.lookupIndex("S");
								else {
									tempData[i] = labels.lookupIndex("B");
									for (int j = i + 1; j < i + n - 1; j++)
										tempData[j] = labels.lookupIndex("M");
									tempData[i + n - 1] = labels
											.lookupIndex("E");
								}
							} else if (labels.size() == 2) {
								tempData[i] = labels.lookupIndex("B");
								for (int j = i + 1; j < i + n; j++)
									tempData[j] = labels.lookupIndex("I");
							}

							i = i + n - 1;// 跳过n个
							break;
						}
					}
				}
			}
			// for(int i = 0; i < tempData.length; i++)
			// System.out.print(tempData[i] + " ");
			// System.out.println();
		}

		instance.setTempData(tempData);
	}

	public static String getNextN(String[][] data, int index, int N) {
		StringBuffer sb = new StringBuffer();
		for (int i = index; i < index + N; i++)
			sb.append(data[i][0]);
		// System.out.println(sb.toString());
		return sb.toString();
	}

}
