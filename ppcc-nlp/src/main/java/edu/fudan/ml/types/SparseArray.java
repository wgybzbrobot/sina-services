package edu.fudan.ml.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
/**
 * 稀疏数组
 * @author xpqiu
 *
 * @param <T>
 */
public class SparseArray<T> implements Serializable {
	
	private static final long serialVersionUID = -1541973985451807285L;
	protected Object[] data = new Object[0];
	protected int[] index = new int[0];
	protected int length;
	private int increSize = 8;
	
	public SparseArray()	{
		length = 0;
		data = new Object[0];
		index = new int[0];
	}
	
	public SparseArray(int init)	{
		length = 0;
		data = new Object[init];
		index = new int[init];
		Arrays.fill(index, Integer.MAX_VALUE);
	}
	
	public T get(int idx) {
		int cur = Arrays.binarySearch(index, idx);
		if (cur >= 0)
			return (T)data[cur];
		
		return null;
	}

	public void put(int idx, T value) {
		int cur = Arrays.binarySearch(index, idx);
		
		if (cur < 0)	{
			if (length == data.length)
				grow();
			
			int p = -cur-1;
			System.arraycopy(data, p, data, p+1, length-p);
			System.arraycopy(index, p, index, p+1, length-p);
			data[p] = value;
			index[p] = idx;
			length++;
		}else	{
			data[cur] = value;
		}
	}
	
	public T remove(int idx)	{
		T ret = null;
		int p = Arrays.binarySearch(index, idx);
		if (p >= 0)	{
			System.arraycopy(data, p+1, data, p, length-p);
			System.arraycopy(index, p+1, index, p, length-p);
			length--;
		}
		return ret;
	}

	protected void grow() {
		int nSize = data.length+increSize;
		Object[] nData = new Object[nSize];
		Arrays.fill(nData, null);
		System.arraycopy(data, 0, nData, 0, length);
		
		int[] nIndex = new int[nSize];
		Arrays.fill(nIndex, Integer.MAX_VALUE);
		System.arraycopy(index, 0, nIndex, 0, length);
		
		data = null; index = null;
		data = nData;
		index = nIndex;
	}
	
	public int capacity()	{
		return data.length;
	}
	
	public void compact()	{
		Object[] nData = new Object[length];
		System.arraycopy(data, 0, nData, 0, length);
		
		int[] nIndex = new int[length];
		System.arraycopy(index, 0, nIndex, 0, length);
		
		data = null; index = null;
		data = nData;
		index = nIndex;
	}
	
	public int size()	{
		return length;
	}
	
	public boolean containsKey(int idx)	{
		int cur = Arrays.binarySearch(index, idx);
		if (cur < 0)
			return false;
		else
			return true;
	}
	
	public Iterator<Integer> iterator()	{
		return new IndexIterator();
	}
	
	protected class IndexIterator implements Iterator<Integer>	{
		int cur = 0;

		public boolean hasNext() {
			return (cur < length);
		}

		public Integer next() {
			return index[cur++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException	{
		out.writeObject(data);
		out.writeObject(index);
		out.writeInt(length);
		out.writeInt(increSize);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException	{
		data = (Object[]) in.readObject();
		index = (int[])in.readObject();
		length = in.readInt();
		increSize = in.readInt();
	}
	
	public static void main(String[] args)	{
		SparseArray<String> sa = new SparseArray<String>(20);
		sa.put(0, "hello");
		sa.put(1, ",");
		sa.put(2, "the");
		sa.put(3, "world");
		sa.put(4, "!");
		for(int i = 0; i < sa.size(); i++)	{
			String v = sa.get(sa.index[i]);
			System.out.println(v);
		}
		System.out.println();
		
		sa.remove(3);
		for(int i = 0; i < sa.size(); i++)	{
			String v = sa.get(sa.index[i]);
			System.out.println(v);
		}
		System.out.println();
		
		sa.put(2, "that");
		for(int i = 0; i < sa.size(); i++)	{
			String v = sa.get(sa.index[i]);
			System.out.println(v);
		}
		System.out.println();
	}
}
