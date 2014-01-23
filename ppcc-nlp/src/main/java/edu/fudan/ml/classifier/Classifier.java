/*
 * 文件名：Classifier.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 修改人：xpqiu
 * 修改时间：2009 Sep 6, 2009 11:09:40 AM
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 修改内容：〈修改内容〉
 */
package edu.fudan.ml.classifier;

import java.io.Serializable;

import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;

/**
 * @author xpqiu
 * @version 1.0
 * Classifier
 * package edu.fudan.ml.classifier
 */
public interface Classifier extends Serializable	{

	
	public Results predict(Instance instance);
	
	
}
