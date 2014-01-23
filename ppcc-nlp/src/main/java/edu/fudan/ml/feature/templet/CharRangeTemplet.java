package edu.fudan.ml.feature.templet;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.nlp.chinese.Chars;

/**
 * 当前位置固定窗口下，字符串的组合类型
 * 
 * @see edu.fudan.nlp.chinese.Chars#getType(String)
 * @author xpqiu
 * 
 */
public class CharRangeTemplet implements Serializable, Templet {

	private static final long serialVersionUID = 3572735523891704313L;
	private int id;
	private int[] position;

	public CharRangeTemplet(int id, int[] pos) {
		this.id = id;
		Arrays.sort(pos);
		this.position = pos;
	}

	@Override
	public int generateAt(Instance instance, FeatureAlphabet features, int pos,
			int... numLabels) {
		List data = (List) instance.getData();
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(':');
		for (int i = 0; i < position.length; i++) {
			if (pos + position[i] < 0) {
				sb.append("//B:");
				sb.append(position[i]);
			} else if (pos + position[i] >= data.size()) {
				sb.append("//E:");
				sb.append(position[i]);
			} else {
				String firstColumn;
				if (data.get(pos + position[i]) instanceof String) {
					firstColumn = (String) data.get(pos + position[i]);
				} else {// 有多列特征
					firstColumn = ((String[]) data.get(pos + position[i]))[0];
				}
				// 得到字符串类型
				String type = Chars.getType(firstColumn);
				sb.append("//" + type + ":");
				sb.append(position[i]);
			}
		}

		int index = features.lookupIndex(sb.toString(),
				(int) Math.pow(numLabels[0], 1));
		return index;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	public int[] getVars() {
		return new int[] { 0 };
	}

	public int offset(int... curs) {
		return 0;
	}

}
