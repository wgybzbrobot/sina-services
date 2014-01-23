package edu.fudan.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.fudan.ml.types.SparseVector;
import gnu.trove.TIntDoubleIterator;
import gnu.trove.TIntHashSet;

public class MySparseArrays {

	/**
	 * 对数组的绝对值由大到小排序，返回调整后元素对于的原始下标
	 * 
	 * @param sv
	 *            待排序数组
	 * @return 原始下标
	 */
	public static int[] sort(SparseVector sv) {
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();

		Iterator<Integer> it = sv.iterator();
		while (it.hasNext()) {
			int id = it.next();
			double val = sv.get(id);
			map.put(id, Math.abs(val));
		}
		it = null;

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
	public static int[] getTop(SparseVector sv, double thres, boolean r) {
		int[] idx = sort(sv);
		int i;
		double total = 0;
		double[] cp = new double[idx.length];
		for (i = idx.length; i-- > 0;) {
			cp[i] = Math.pow(sv.get(idx[i]), 2);
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
	 * 对部分下标的元素赋零
	 * 
	 * @param sv
	 *            数组
	 * @param idx
	 *            赋值下标
	 */
	public static void setZero(SparseVector sv, int[] idx) {
		for(int i = 0; i < idx.length; i++)	{
			if (sv.containsKey(idx[i]))	{
				sv.remove(idx[i]);
			}
		}
	}

	public static void main(String[] args) {
		double[] w = { 0.3, 1.2, 1.09, -0.45, -1.2, 0, 0, 0, 0 };
		SparseVector sv = new SparseVector(w);
		int[] idx = getTop(sv, 0.99, false);
		setZero(sv, idx);
		System.out.println(sv);
	}
}
