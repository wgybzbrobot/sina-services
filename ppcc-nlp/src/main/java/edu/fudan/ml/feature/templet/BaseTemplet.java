package edu.fudan.ml.feature.templet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;

/**
 * 类CRF模板 格式为： %x[-1,0]%x[1,0]%y[0] %y[-1]%y[0]
 * 
 */
public class BaseTemplet implements Templet, Serializable {

	private static final long serialVersionUID = 7543856094273600355L;
	Pattern parser = Pattern.compile("(?:%(x|y)\\[(-?\\d+)(?:,(\\d+))?\\])");
	String templet;

	int order;
	int id;
	int[][] dims;
	int[] vars;

	/**
	 * 构造函数
	 * 
	 * @param templet
	 *            模板字符串 格式如：%x[0,0]%y[0] %y[-1]%y[0]
	 */
	public BaseTemplet(int id, String templet) {
		this.id = id;
		this.templet = templet;
		Matcher matcher = parser.matcher(this.templet);
		/**
		 * 解析y的位置
		 */
		List<String> l = new ArrayList<String>();
		List<String> x = new ArrayList<String>();
		while (matcher.find()) {
			if (matcher.group(1).equalsIgnoreCase("y")) {
				l.add(matcher.group(2));
			} else if (matcher.group(1).equalsIgnoreCase("x")) {
				x.add(matcher.group(2));
				x.add(matcher.group(3));
			}
		}
		if(l.size()==0){//兼容CRF++模板
			vars = new int[]{0};
		}else{
			vars = new int[l.size()];
			for (int j = 0; j < l.size(); j++) {
				vars[j] = Integer.parseInt(l.get(j));
			}
		}
		order = vars.length - 1;
		l = null;

		dims = new int[x.size() / 2][2];
		for (int i = 0; i < x.size(); i += 2) {
			dims[i / 2][0] = Integer.parseInt(x.get(i));
			dims[i / 2][1] = Integer.parseInt(x.get(i + 1));
		}
		x = null;
	}

	/**
	 * @see edu.fudan.ml.feature.templet.Templet#getVars()
	 */
	public int[] getVars() {
		return this.vars;
	}

	/**
	 * @see edu.fudan.ml.feature.templet.Templet#getOrder()
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * @see edu.fudan.ml.feature.templet.Templet#generateAt(Instance,
	 *      int, int[], Alphabet, int)
	 */
	public int generateAt(Instance instance, FeatureAlphabet features, int pos,
			int... numLabels) throws Exception {
		assert (numLabels.length == 1);

		String[][] data = (String[][]) instance.getData();

		for(int i = 0; i < vars.length; i++)	{
			int j = vars[i];
			if (pos+j < 0 || pos+j >= data.length)
				return -1;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(':');
		for (int i = 0; i < dims.length; i++) {
			String rp = "";
			int j = dims[i][0];
			int k = dims[i][1];
			if (pos + j < 0 || pos + j >= data.length) {
				if (pos + j < 0)
					rp = "B_" + String.valueOf(-(pos + j) - 1);
				if (pos + j >= data.length)
					rp = "E_" + String.valueOf(pos + j - data.length);
			} else {
				rp = data[pos + j][k];
			}
			if (-1 != rp.indexOf('$'))
				rp = rp.replaceAll("\\$", "\\\\\\$");
			sb.append(rp);
			sb.append("//");
		}
		int index = features.lookupIndex(sb.toString(),
				(int) Math.pow(numLabels[0], order + 1));
		return index;
	}

	public String toString() {
		return this.templet;
	}

	public int offset(int... curs) {
		return 0;
	}
}
