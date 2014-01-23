package edu.fudan.ml.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;

/**
 * 稀疏数组，实现数组对应的各种运算
 * 
 * @author xpqiu
 * 
 */
public class SparseVector extends SparseArray<Double> implements Serializable {

	private static final long serialVersionUID = 1467092492463327579L;

	public SparseVector() {
	}

	/**
	 * 将一般数组转换成稀疏表示
	 * 
	 * @param w
	 */
	public SparseVector(double[] w) {
		for (int i = 0; i < w.length; i++) {
			if (w[i] != 0) {
				put(i, w[i]);
			}
		}
	}

	public SparseVector(SparseVector sv) {
		index = Arrays.copyOf(sv.index, sv.length);
		data = Arrays.copyOf(sv.data, sv.length);
		length = sv.length;
	}

	/**
	 * 向量减法: x-y
	 * 
	 * @param sv
	 */
	public void minus(SparseVector sv) {
		for (int r = 0; r < sv.length; r++) {
			int p = Arrays.binarySearch(index, sv.index[r]);
			if (p >= 0) {
				data[p] = (Double) data[p] - (Double) sv.data[r];
			} else {
				put(sv.index[r], -(Double) sv.data[r]);
			}
		}
	}

	/**
	 * 对应位置加上值: x[i] = x[i]+c
	 * 
	 * @param id
	 * @param c
	 */
	public void add(int id, double c) {
		int p = Arrays.binarySearch(index, id);
		if (p >= 0) {
			data[p] = ((Double) data[p]) + c;
		} else {
			put(id, c);
		}
	}

	/**
	 * 向量加法：x+y
	 * 
	 * @param sv
	 */
	public void plus(SparseVector sv) {
		plus(sv, 1);
	}

	/**
	 * 计算x+y*w
	 * 
	 * @param sv
	 * @param w
	 */
	public void plus(SparseVector sv, double w) {
		if (sv == null)
			return;
		for (int i = 0; i < sv.length; i++) {
			double val = (Double) sv.data[i] * w;
			add(sv.index[i], val);
		}
	}

	public double elementAt(int id) {
		double ret = 0;
		int p = Arrays.binarySearch(index, id);
		if (p >= 0)
			ret = (Double) data[p];
		return ret;
	}

	public int[] indices() {
		return Arrays.copyOfRange(index, 0, length);
	}

	/**
	 * 向量点积: x*y
	 * 
	 * @param sv
	 * @return
	 */
	public double dotProduct(SparseVector sv) {
		return dotProduct(sv, 0);
	}

	/**
	 * 向量点积: x*(y+c)
	 * 
	 * @param sv
	 * @return
	 */
	public double dotProduct(SparseVector sv, double c) {
		double product = 0;

		for (int i = 0; i < sv.length; i++) {
			int p = Arrays.binarySearch(index, sv.index[i]);
			if (p >= 0) {
				double val = (Double) sv.data[i] + c;
				val *= (Double) data[p];
				product += val;
			}
		}

		return product;
	}

	// A*(B+c)
	public double dotProduct(SparseVector sv, int li, int n) {
		double product = 0;
		int z = n * li;
		for (int i = 0; i < length; i++) {
			int p = Arrays.binarySearch(sv.index, index[i] + z);
			if (p >= 0) {
				product += (Double) data[i] + (Double) sv.data[p];
			}
		}
		return product;
	}

	public void scaleMultiply(double c) {
		if (c == 0)
			clear();
		for (int i = 0; i < length; i++) {
			data[i] = (Double) data[i] * c;
		}
	}

	public void scaleDivide(double c) {
		if (c == 0)
			throw new ArithmeticException();
		for (int i = 0; i < length; i++) {
			data[i] = (Double) data[i] / c;
		}
	}

	public double l1Norm() {
		double norm = 0;
		for (int i = 0; i < length; i++) {
			norm += Math.abs((Double) data[i]);
		}
		return norm;
	}

	public double squared() {
		double norm = 0;
		for (int i = 0; i < length; i++) {
			double val = (Double) data[i];
			norm += val * val;
		}
		return norm;
	}

	public double l2Norm() {
		double norm = 0;
		for (int i = 0; i < length; i++) {
			double val = (Double) data[i];
			norm += val * val;
		}
		return Math.sqrt(norm);
	}

	public double infinityNorm() {
		double norm = 0;
		for (int i = 0; i < length; i++) {
			double val = Math.abs((Double) data[i]);
			if (val > norm)
				norm = val;
		}
		return norm;
	}

	public SparseVector replicate(ArrayList<Integer> list, int dim) {
		SparseVector sv = new SparseVector();
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < list.size(); j++) {
				sv.put(index[i] + dim * list.get(j), (Double) data[i]);
			}
		}
		return sv;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(index[i]);
			sb.append(':');
			sb.append(data[i]);
			sb.append(' ');
		}
		return sb.toString();
	}

	/**
	 * 计算两个向量距离
	 * 
	 * @param sv
	 * @return 距离值
	 */
	public double euclideanDistance(SparseVector sv) {
		double dist = 0.0;
		int r = 0;
		for (int i = 0; i < sv.length; i++) {
			if (sv.index[i] == index[r]) {
				double cur = (Double) data[r] - (Double) sv.data[i];
				dist += cur * cur;
				r++;
			} else {
				double cur = (Double) sv.data[i];
				dist += cur * cur;
			}
		}
		for (; r < length; r++) {
			double cur = (Double) data[r];
			dist += cur * cur;
		}
		return dist;
	}

	public void clear() {
		length = 0;
	}

	public void normalize() {
		double norm = l2Norm();
		if (norm > 0)
			scaleMultiply(1 / norm);
	}

	public void normalize2() {
		double sum = 0;
		for (int i = 0; i < length; i++) {
			double value = Math.exp((Double) data[i]);
			data[i] = value;
			sum += value;
		}
		scaleDivide(sum);
	}

	public static int dotProduct(TIntIntHashMap v1, TIntIntHashMap v2) {

		TIntIntHashMap sv1;
		TIntIntHashMap sv2;
		if (v1.size() < v2.size()) {
			sv1 = v1;
			sv2 = v2;
		} else {
			sv1 = v2;
			sv2 = v1;
		}
		double product = 0;
		TIntIntIterator it = sv1.iterator();
		for (int i = sv1.size(); i-- > 0;) {
			it.advance();
			product += it.value() * sv2.get(it.key());
		}
		return (int) product;
	}

	public double dotProduct(double[] weights) {
		if (index[length - 1] >= weights.length)
			throw new IllegalArgumentException();

		double product = 0;
		for (int i = 0; i < length; i++) {
			product += (Double) data[i] * weights[index[i]];
		}
		return product;
	}

	public static void main(String[] args) {
		SparseVector sv = new SparseVector();
		for (int i = 0; i < 10; i++) {
			sv.put(i, i + 0.0);
		}
		SparseVector vec = new SparseVector(sv);
		for (int i = 0; i < vec.length; i++) {
			vec.add(i, 1);
		}
		int[] index = sv.indices();
		System.out.println(sv);
		System.out.println(vec);

		System.out.println(sv.l1Norm());
		System.out.println(sv.squared());
		System.out.println(vec.l1Norm());
		System.out.println(vec.squared());

		vec.plus(sv);
		System.out.println(sv);
		System.out.println(vec);

		sv.minus(vec);
		System.out.println(sv);
		System.out.println(vec);
		
		System.out.println(sv.dotProduct(vec));
		System.out.println(vec.dotProduct(sv));
		System.out.println(sv.squared());
		System.out.println(sv.dotProduct(sv));
		
		sv.minus(sv);
		System.out.println(sv.size());
	}
}
