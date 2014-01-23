package edu.fudan.ml.feature.templet;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
/**
 * 通过字典生成特征
 * @author xpqiu
 *
 */
public class DictionaryTemplet implements Templet, Serializable {

	private static final long serialVersionUID = -4516243129442692024L;
	private Dictionary d;
	private int[] args;
	private int id;
	private String text;

	public DictionaryTemplet(Dictionary d, int id, int ... args) {
		this.d = d;
		this.id = id;
		this.args = args;
		Arrays.sort(args);
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(":dict");
		for(int i=0; i<args.length; i++) {
			sb.append(':');
			sb.append(args[i]);
		}
		sb.append(':');
		this.text = new String(sb);
	}

	@Override
	public int generateAt(Instance instance, FeatureAlphabet features, int pos, int ... numLabels) {
		assert(numLabels.length == 1);
		List<String[]> data = (List<String[]> )instance.getData();
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<args.length; i++) {
			int idx = pos+args[i];
			if(idx>=0&&idx<data.size())
				sb.append((data.get(idx)[0]));

		}
		
		List<String> l = d.test(sb.toString());
		int index = -1;
		if(l.size()>0) {
			String label = text+l.get(0);
			index = features.lookupIndex(label, numLabels[0]);;
		}
		return index;
	}

	public int getOrder() { return 0; }

	public int[] getVars() { return new int[]{0}; }

	public int offset(int... curs) {
		return 0;
	}



}
