package edu.fudan.ml.feature.generator;

import edu.fudan.ml.feature.generator.Generator;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.SparseVector;

/**
 * 结构化特征生成类
 * 
 * @version Feb 16, 2009
 */
public class SFGenerator extends Generator	{

	private static final long serialVersionUID = 6404015214630864081L;

	/**
	 * 构造函数
	 * 
	 * @param alphabet
	 *            特征表
	 */
	public SFGenerator() {
	}

	@Override
	public SparseVector getVector(Instance inst, Object label) {
		int[] data = (int[]) inst.getData();
		SparseVector fv = new SparseVector();
		for(int i = 0; i < data.length; i++)	{
			int idx = data[i]+(Integer)label;
			fv.add(idx, 1.0);
		}
		return fv;
	}
}
