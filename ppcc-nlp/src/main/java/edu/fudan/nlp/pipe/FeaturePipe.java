package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;
/**
 * 将字符数据转换成特征索引
 * 数据类型：List\<String\> -\> int[]
 * @author xpqiu
 */
public class FeaturePipe extends Pipe implements Serializable {

	private static final long serialVersionUID = 358834035189351765L;
	private FeatureAlphabet features;
	private int idx;
	private LabelAlphabet label;

	public FeaturePipe(AlphabetFactory af) {
		this.features = af.DefaultFeatureAlphabet();
		this.label = af.DefaultLabelAlphabet();
		// 增加常数项
		idx = features.lookupIndex("!#@$");
	}
	
	@Override
	public void addThruPipe(Instance inst) throws Exception {
		List<String> data = (List<String>) inst.getData();
		int size = data.size();
		int[] newdata = new int[data.size()+1];
		Iterator<String> it = data.iterator();
		
		for(int i=0;i<size;i++){
			String token = it.next();
			int id = features.lookupIndex(token,label.size());
			if(id==-1)
				continue;
			newdata[i] = id;
		}
		newdata[size]=idx;
		inst.setData(newdata);
	}

}
