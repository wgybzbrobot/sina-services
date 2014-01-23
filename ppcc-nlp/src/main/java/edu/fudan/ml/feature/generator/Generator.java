/*
 * 文件名：Generator.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 修改人：xpqiu
 * 修改时间：2009 Sep 8, 2009 5:17:13 PM
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 修改内容：〈修改内容〉
 */
package edu.fudan.ml.feature.generator;

import java.io.Serializable;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.SparseVector;

/**
 * 生成特征向量，包含类别信息
 * 
 * @author xpqiu
 * @version 1.0 Generator package edu.fudan.ml.feature.generator
 */
public abstract class Generator implements Serializable {

	private static final long serialVersionUID = 8640098825477722199L;
	
	public Generator()	{
	}
	
	public SparseVector getVector(Instance inst) {
		return getVector(inst, inst.getTarget());
	}

	public abstract SparseVector getVector(Instance inst, Object object);

}
