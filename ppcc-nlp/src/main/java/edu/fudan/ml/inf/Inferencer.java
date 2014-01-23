package edu.fudan.ml.inf;

import java.io.Serializable;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;

/**
 * 推理类
 * @author xpqiu
 *
 */
public abstract class Inferencer implements Serializable	{

	private static final long serialVersionUID = -7254946709189008567L;
	
	protected double[] weights;
	
	protected boolean isUseTarget;
		
	/**
	 * 得到前n个最可能的预测值
	 * @param inst 
	 * @param n 返回个数
	 * @return
	 * Sep 9, 2009
	 */
	public abstract Results getBest(Instance inst);
	
	public abstract Results getBest(Instance inst, int n);
	
	public double[] getWeights()	{
		return weights;
	}
	
	public void setWeights(double[] weights)	{
		this.weights = weights;
	}

	public void isUseTarget(boolean b) {
		isUseTarget = b;
	}
}
