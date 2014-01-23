package cc.pp.fuzzy.search.analyzer;

import java.io.IOException;

import cc.pp.analyzer.mmseg4j.examples.Complex;
import cc.pp.analyzer.mmseg4j.examples.MaxWord;
import cc.pp.analyzer.mmseg4j.examples.Simple;

public class MMSeg4jWordSegDemo {

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws IOException {

		Complex complex = new Complex();
		String words = complex.segWords("今天在淘宝上买了件衣服", ",");
		System.out.println(words);

		MaxWord maxword = new MaxWord();
		words = maxword.segWords("我去年买了个表", " ");
		System.out.println(words);

		Simple simple = new Simple();
		words = simple.segWords("我去年买了个表", " ");
		System.out.println(words);

	}

}
