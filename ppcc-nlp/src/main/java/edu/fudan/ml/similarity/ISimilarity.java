package edu.fudan.ml.similarity;

/**
 * @author xpqiu
 * @version 1.0
 * @since 1.0
 * ISimilarity
 */
public interface ISimilarity {
	
	public <E> float calc(E item1,E item2) throws Exception;

}
