package edu.fudan.nlp.pipe;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;

/**
 * 数据类型转换，通过一系列的组合将数据从原始方式转为需要的数据类型
 * Pipe只能每次连续流水处理一个样本，不能按阶段多遍执行
 * 要分阶段多遍执行参见 {@link edu.fudan.ml.types.InstanceSet#loadThruStagePipes(edu.fudan.ml.data.SequenceReader)}
 * @author xpqiu
 *
 */
public abstract class Pipe{
	
	/**
	 * 用来判断是否使用类别，以便在无类别使用时删掉
	 */
	boolean useTarget = false;
	
	public abstract void addThruPipe(Instance inst) throws Exception;
	
	/**
	 * 通过pipe直接处理实例
	 * @param instList
	 * @throws Exception
	 */
	public void process(InstanceSet instList) throws Exception {
		for(int i=0;i<instList.size();i++){
			Instance inst = instList.getInstance(i);
			addThruPipe(inst);
		}
	}
	
}

