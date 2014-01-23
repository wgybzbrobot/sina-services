package edu.fudan.ml.types;

import java.util.Arrays;

/**
 * pred为预测 oracle为真实
 * 
 * @author xpqiu
 * 
 */
public class Results<T> {
	/**
	 * 记录前n个结果
	 */
	int n;
	/**
	 * 预测值得分
	 */
	public double[] predScores;
	/**
	 * 预测值
	 */
	public T[] predList;
	/**
	 * 真实值得分
	 */
	public double[] oracleScores;
	/**
	 * 真实值
	 */
	public T[] oracleList;

	public Object other;

	public Results(int n) {
		assert(n > 0);
		this.n = n;
		predScores = new double[n];
		predList = (T[]) new Object[n];
		Arrays.fill(predScores, Double.NEGATIVE_INFINITY);
	}

	/**
	 * 记录正确标注对应的得分
	 */
	public void buildOracle() {
		oracleScores = new double[n];
		oracleList = (T[]) new Object[n];
		Arrays.fill(oracleScores, Double.NEGATIVE_INFINITY);
	}

	/**
	 * 返回插入的位置
	 * 
	 * @param score
	 * @param cur
	 * @return 插入位置
	 */
	public int addPred(double score, T pred) {
		return adjust(predScores, predList, score, pred);
	}

	/**
	 * 返回插入的位置
	 * 
	 * @param score
	 * @param cur
	 * @return 插入位置
	 */
	public int addOracle(double score, T pred) {
		return adjust(oracleScores, oracleList, score, pred);
	}

	private int adjust(double[] scores, T[] preds, double score, T pred) {
		int i;
		for (i = 0; i < n; i++) {
			if (score > scores[i])
				break;
		}
		if (i >= n)
			return -1;
		for (int k = n - 2; k >= i; k--) {
			scores[k + 1] = scores[k];
			preds[k + 1] = preds[k];
		}
		scores[i] = score;
		preds[i] = pred;
		return i;
	}
	
	public T getPredAt(int i)	{
		if (i < 0 || i >= n)
			return null;
		return predList[i];
	}
	
	public double getScoreAt(int i)	{
		if (i < 0 || i >= n)
			return Double.NEGATIVE_INFINITY;
		return predScores[i];
	}

	public int size() {
		return n;
	}
}
