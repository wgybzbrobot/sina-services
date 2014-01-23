package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import edu.fudan.ml.feature.templet.Templet;
import edu.fudan.ml.types.Dictionary;
import edu.fudan.ml.types.FeatureAlphabet;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;

/**
 * 将字符序列转换成特征序列 因为都是01特征，这里保存的是索引号
 * 
 * @author xpqiu
 * 
 */
public class Sequence2FeatureSequence_Dict extends Pipe implements Serializable {

	private static final long serialVersionUID = -8407401788900554542L;
	
	List<Templet> templets;
	public FeatureAlphabet features;
	LabelAlphabet labels;

	public Sequence2FeatureSequence_Dict(List<Templet> templets,
			FeatureAlphabet features, LabelAlphabet labels) {
		this.templets = templets;
		this.features = features;
		this.labels = labels;
	}

	public void addThruPipe(Instance instance) throws Exception {
		String[][] data = (String[][]) instance.getData();
			
		int[] tempData = (int[]) instance.getTempData();
		
		int[][] newData = new int[data.length][templets.size()];
		for (int i = 0; i < data.length; i++) {
			Arrays.fill(newData[i], -1);
			for (int j = 0; j < templets.size(); j++) {
				//如果该位已经在dict中标过且模版的阶为0，跳过抽取特征
				if(tempData[i] != -1 && templets.get(j).getOrder() == 0)
					continue;
				
				newData[i][j] = templets.get(j).generateAt(instance,
						this.features, i, labels.size());
			}
		}
		instance.setData(newData);
	}
}
