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
public class Sequence2FeatureSequence extends Pipe implements Serializable {

	private static final long serialVersionUID = -6481304918657094682L;
	List<Templet> templets;
	public FeatureAlphabet features;
	LabelAlphabet labels;

	public Sequence2FeatureSequence(List<Templet> templets,
			FeatureAlphabet features, LabelAlphabet labels) {
		this.templets = templets;
		this.features = features;
		this.labels = labels;
	}

	public void addThruPipe(Instance instance) throws Exception {	
		String[][] data = (String[][]) instance.getData();
			
//		Dictionary dict = (Dictionary)instance.getTempData();
//		int[] tempData = new int[data.length];
//		Arrays.fill(tempData, -1);
//		
//		if(dict != null) {		
//			for(int i = 0; i < data.length; i++) {
//				for(int n = dict.getMaxLen(); n >= dict.getMinLen(); n--) {
//					if(i + n <= data.length) {
//						String s = getNextN(data, i , n);
//						if(dict.contains(s)) {
//							//下面这部分依赖{1=B,2=M,3=E,0=S}
//							if(n == 1)
//								tempData[i] = 0;
//							else {
//								tempData[i] = 1;
//								for(int j = i + 1; j < i + n - 1; j++)
//									tempData[j] = 2;
//								tempData[i + n - 1] = 3;
//							}
//							
//							i = i + n - 1;//跳过n个
//							break;
//						}
//					}
//				}
//			}
//	//		for(int i = 0; i < tempData.length; i++)
//	//			System.out.print(tempData[i] + " ");
//	//		System.out.println();	
//		}
//		
//		instance.setTempData(tempData);
		
		int[][] newData = new int[data.length][templets.size()];
		for (int i = 0; i < data.length; i++) {
			Arrays.fill(newData[i], -1);
			for (int j = 0; j < templets.size(); j++) {
				//如果该位已经在dict中标过且模版的阶为0，跳过抽取特征
//				if(tempData[i] != -1 && templets.get(j).getOrder() == 0)
//					continue;
				
				newData[i][j] = templets.get(j).generateAt(instance,
						this.features, i, labels.size());
			}
		}
		instance.setData(newData);
	}
	
//	public static String getNextN(String[][] data, int index, int N) {
//		StringBuffer sb = new StringBuffer();	
//		for(int i = index; i < index + N; i++)
//			sb.append(data[i][0]);
//		//System.out.println(sb.toString());
//		return sb.toString();
//	}

}
