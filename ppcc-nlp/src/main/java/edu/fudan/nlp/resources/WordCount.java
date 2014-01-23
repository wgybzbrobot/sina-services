/*
 * 文件名：WordCount.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 描述：程序总入口
 * 修改人：xpqiu
 * 修改时间：2009-1-5
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 跟踪单号：〈跟踪单号〉
 * 修改单号：〈修改单号〉
 * 修改内容：〈修改内容〉
 */
package edu.fudan.nlp.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Administrator
 * @version 1.0
 * @since 1.0
 */
public class WordCount {

	LinkedHashMap<String, Integer> words;

	public WordCount() {
		words = new LinkedHashMap<String, Integer>();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WordCount fm = new WordCount();
		String fileName = "\\\\10.11.7.3/f$/Corpus/人民日报 199801/199801.txt";
		fm.read(fileName);
		fm.save("../data/words.cn.dic", false);
		System.out.println("Done");

	}

	/**
	 * @param fileName
	 */
	public void read(String fileName) {
		File f = new File(fileName);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				read(files[i].toString());
			}
		} else {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(fileName), "utf-8");
				BufferedReader bin = new BufferedReader(read);
				String sent;
				while ((sent = bin.readLine()) != null) {
					calc(sent);
				}
			} catch (Exception e) {

			}
		}

	}

	/**
	 * @param filename
	 * @param bcount
	 *            是否输出词频
	 */
	public void save(String filename, boolean bcount) {

		TreeMap tm = new TreeMap<String, Integer>(new ValueComparator(words));
		tm.putAll(words);

		try {
			FileOutputStream fos = new FileOutputStream(filename);
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(
					fos, "UTF-8"));
			Iterator it = tm.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				bout.write(key.toString());
				if (bcount) {
					bout.write(" ");
					bout.write(tm.get(key).toString());
				}
				bout.write("\n");
			}
			bout.close();

		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @param sent
	 * @param b
	 * @return
	 */
	private void calc(String str) {
		// str = str.replaceAll("[\\[\\]0-9a-zA-Z/\\.<> =]+", " ").trim();
		String[] wordarray = str.split("\\s");
		for (int i = 0; i < wordarray.length; i++) {
			String w = wordarray[i].trim();
			if (w.length() == 0)
				continue;
			if (words.containsKey(w)) {
				words.put(w, words.get(w) + 1);
			} else {
				words.put(w, 1);
			}
		}
	}

	public class ValueComparator implements java.util.Comparator {
		private Map m; // the original map

		ValueComparator(Map m) {
			this.m = m;
		}

		public int compare(Object o1, Object o2) {
			// handle some exceptions here
			Object v1 = m.get(o1);
			Object v2 = m.get(o2);
			// make sure the values implement Comparable
			return -((Comparable) v1).compareTo(v2);
		}
	}

}
