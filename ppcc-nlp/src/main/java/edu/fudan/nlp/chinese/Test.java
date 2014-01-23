/*
 * 文件名：Test.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 描述：程序总入口
 * 修改人：xpqiu
 * 修改时间：2008-12-22
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 跟踪单号：〈跟踪单号〉
 * 修改单号：〈修改单号〉
 * 修改内容：〈修改内容〉
 */
package edu.fudan.nlp.chinese;

import java.util.HashSet;

/**
 * @author Administrator
 * @version 1.0
 * @since 1.0
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		int res = EditDistance.calc("最的我生  日","生日们");
//		String string = "'&";
//		string = ChineseTrans.toDBC(string);
//		string = ChineseTrans.toSimp(string);
//		string = string.replaceAll("(\\(.*\\)|(（.*）)|(【.*】))", "");
//		string = string.replaceAll(CharSets.allRegexPunc, "");
//		System.out.println(string);
		HashSet<String> s =new HashSet<String>();
		s.add("1");
		s.add("2");
		s.add("1");
		System.out.println(s.toArray());
		System.out.println(s.toArray().toString());
		System.out.println(s.toString());
	}

}
	