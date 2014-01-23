package cc.pp.fuzzy.search.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import cc.pp.analyzer.ik.lucene.IKAnalyzer;
import cc.pp.sina.utils.lucene.LuceneUtils;

public class WordsSegsment {

	private static IKAnalyzer analyzer = new IKAnalyzer(LuceneUtils.CURRENT_VERSION, true);

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException {

		System.out.println(WordsSegsment.wordSeg("你大爷的，我是做数据挖掘的，去年买了个表！"));

	}

	/**
	 * 获取分词结果
	 */
	public static List<String> wordSeg(String text) throws IOException {

		List<String> result = new ArrayList<>();
		TokenStream tokenStream = analyzer.tokenStream("content", text);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		try {
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				result.add(charTermAttribute.toString());
			}
			tokenStream.end();
		} finally {
			tokenStream.close();
		}

		return result;
	}

}
