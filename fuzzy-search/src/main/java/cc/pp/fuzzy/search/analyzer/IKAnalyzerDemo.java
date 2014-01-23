package cc.pp.fuzzy.search.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import cc.pp.analyzer.ik.lucene.IKAnalyzer;

public class IKAnalyzerDemo {

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws IOException {

		String text = "今天天气";
		IKAnalyzer analyzer = new IKAnalyzer(Version.LUCENE_46, true);
		TokenStream tokenStream = analyzer.tokenStream("content", text);
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
