package edu.fudan.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 实现数组排序、直方图的功能
 * 
 * @author xpqiu
 * 
 */
public class MyArrays {
	/**
	 * 记录之前的label和得分，保留前n个
	 * 
	 * @param score
	 * @param pred
	 * @return 插入位置
	 */
	public static int addBest(double[] scores, Object[] predList, double score, Object pred) {
		int n = scores.length;
		int i;
		for (i = 0; i < n; i++) {
			if (score > scores[i])
				break;
		}
		if (i >= n)
			return -1;
		for (int k = n - 2; k >= i; k--) {
			scores[k + 1] = scores[k];
			predList[k + 1] = predList[k];
		}
		scores[i] = score;
		predList[i] = pred;
		return i;
	}

	/**
	 * 
	 * @param count
	 * @param nbin
	 * @return 直方图
	 */
	public static double[][] histogram(double[] count, int nbin) {
		double maxCount = Double.NEGATIVE_INFINITY;
		double minCount = Double.MAX_VALUE;
		for (int i = 0; i < count.length; i++) {
			if (maxCount < count[i]) {
				maxCount = count[i];
			}
			if (minCount > count[i]) {
				minCount = count[i];
			}
		}
		double[][] hist = new double[2][nbin];
		double interv = (maxCount - minCount) / nbin;
		for (int i = 0; i < count.length; i++) {
			int idx = (int) Math.floor((count[i] - minCount) / interv);
			if (idx == nbin)
				idx--;
			hist[0][idx]++;
		}
		for (int i = 0; i < nbin; i++) {
			hist[1][i] = minCount + i * interv;
		}
		return hist;
	}

	/**
	 * 归一化
	 * 
	 * @param c
	 */
	public static void normalize(double[] c) {
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < c.length; i++) {
			if (min > c[i])
				min = c[i];
			if (max < c[i])
				max = c[i];
		}
		double val = max - min;
		if (val == 0)
			return;
		for (int i = 0; i < c.length; i++) {
			c[i] = (c[i] - min) / val;
		}
	}

	/**
	 * 对数组的绝对值由大到小排序，返回调整后元素对于的原始下标
	 * 
	 * @param c
	 *            待排序数组
	 * @return 原始下标
	 */
	public static int[] sort(double[] c) {

		HashMap<Integer, Double> map = new HashMap<Integer, Double>();

		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0.0) {
				map.put(i, Math.abs(c[i]));
			}
		}
		ArrayList<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(
				map.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1,
					Entry<Integer, Double> o2) {

				if (o2.getValue() > o1.getValue()) {
					return 1;
				} else if (o1.getValue() > o2.getValue()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		int[] idx = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			idx[i] = list.get(i).getKey();
		}
		return idx;
	}

	/**
	 * 得到总能量值大于thres的元素对应的下标
	 * 
	 * @param c
	 * @param thres
	 * @param r
	 *            true表示返回最大的，false表示返回剩余的
	 * @return 元素下标
	 */
	public static int[] getTop(double[] c, double thres, boolean r) {
		int[] idx = sort(c);
		int i;
		double total = 0;
		double[] cp = new double[idx.length];
		for (i = 0; i < idx.length; i++) {
			cp[i] = Math.pow(c[idx[i]], 2);
			total += cp[i];
		}

		double ratio = 0;
		for (i = 0; i < idx.length; i++) {
			ratio += cp[i] / total;
			if (ratio > thres)
				break;
		}
		int[] a;
		if (r)
			a = Arrays.copyOfRange(idx, 0, i);
		else
			a = Arrays.copyOfRange(idx, i, idx.length);
		return a;
	}

	/**
	 * 对部分下标的元素赋值
	 * 
	 * @param c
	 *            数组
	 * @param idx
	 *            赋值下标
	 * @param v
	 *            值
	 */
	public static void set(double[] c, int[] idx, double v) {
		for (int i = 0; i < idx.length; i++) {
			c[idx[i]] = v;
		}
	}

	/**
	 * 统计非零个数
	 * 
	 * @param c
	 * @return 非零元素数量
	 */
	public static int countNoneZero(double[] c) {
		int count = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0.0)
				count++;
		}
		return count;
	}

	/**
	 * 统计非零元素
	 * 
	 * @param c
	 * @return 非零元素标记
	 */
	public static boolean[] getNoneZeroIdx(double[] c) {
		boolean[] b = new boolean[c.length];
		for (int i = 0; i < c.length; i++) {
			if (c[i] != 0.0)
				b[i] = true;
		}
		return b;
	}

	public static int[] string2int(String[] c) {
		int[] d = new int[c.length];
		for (int i = 0; i < c.length; i++) {
			d[i] = Integer.parseInt(c[i]);
		}
		return d;
	}
	public static String[] int2string(int[] c) {
		String[] d = new String[c.length];
		for (int i = 0; i < c.length; i++) {
			d[i] = String.valueOf(c[i]);
		}
		return d;
	}

	public static void main(String[] args) {
		double[] w = { 1.0, 2.0, 3.0, 4.0, 0, 0, 0 };
		int[] idx = getTop(w, 0.95, false);
		set(w, idx, 0.0);
		normalize(w);
		for (int i = 0; i < w.length; i++) {
			System.out.print(w[i] + " ");
		}
	}



}
