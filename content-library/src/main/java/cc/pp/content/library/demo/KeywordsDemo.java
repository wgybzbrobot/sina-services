package cc.pp.content.library.demo;

import cc.pp.sina.utils.json.JsonUtils;
import edu.fudan.nlp.keyword.Extractor;
import edu.fudan.nlp.keyword.WordExtract;
import edu.fudan.nlp.resources.StopWords;
import edu.fudan.nlp.tag.CWSTagger;

public class KeywordsDemo {

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		// 停用词
		StopWords sw = new StopWords("models/stopwords");
		// 分词器，添加词典
		CWSTagger seg = new CWSTagger("models/seg.c7.110918.gz", "models/dict.txt");
		// 关键词提取器
		Extractor key = new WordExtract(seg, sw);
		long middle = System.currentTimeMillis();
		System.out.println(middle - start);
		System.out.println(JsonUtils.toJson(key.extract("今天在淘宝的聚划算上买了一件衣服。我在执掌时代网络科技有限公司。", 100)));
		long end = System.currentTimeMillis();
		System.out.println(end - middle);

	}

}
