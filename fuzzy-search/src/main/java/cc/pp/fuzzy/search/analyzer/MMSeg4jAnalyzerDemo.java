package cc.pp.fuzzy.search.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import cc.pp.analyzer.mmseg4j.analyzer.SimpleAnalyzer;

public class MMSeg4jAnalyzerDemo {

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws IOException {

		String text = "你大爷的，我去年买了个表！";
		//		ComplexAnalyzer analyzer = new ComplexAnalyzer();
		//		MaxWordAnalyzer analyzer = new MaxWordAnalyzer();
		SimpleAnalyzer analyzer = new SimpleAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		try {
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				System.out.println(charTermAttribute.toString());
			}
			tokenStream.end();
		} finally {
			tokenStream.close();
			analyzer.close();
		}

	}

}
