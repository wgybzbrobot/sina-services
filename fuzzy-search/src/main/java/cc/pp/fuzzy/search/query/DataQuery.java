package cc.pp.fuzzy.search.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.analyzer.ik.lucene.IKAnalyzer;
import cc.pp.sina.utils.lucene.LuceneUtils;

public class DataQuery {

	private static Logger logger = LoggerFactory.getLogger(DataQuery.class);

	/**
	 * 读索引类
	 */
	private final DirectoryReader reader;

	/**
	 * 搜索类
	 */
	private final IndexSearcher searcher;

	/**
	 * 分词器
	 */
	private final IKAnalyzer analyzer;

	/**
	 * 最大返回结果数
	 */
	private static final int MAX_HITS = 100;

	/**
	 * 初始化
	 */
	public DataQuery(Directory indexDir) throws IOException {
		reader = DirectoryReader.open(indexDir);
		searcher = new IndexSearcher(reader);
		analyzer = new IKAnalyzer(LuceneUtils.CURRENT_VERSION, true);
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException, ParseException {

		Directory indexDir = FSDirectory.open(new File("weixin_index_data"));
		DataQuery dataQuery = new DataQuery(indexDir);
		List<String> result = dataQuery.simpleQuery("今天的天气很好啊");
		for (String str : result) {
			System.out.println(str);
		}
	}

	/**
	 * 简单查询
	 */
	public List<String> simpleQuery(String keywords) throws ParseException, IOException {

		QueryParser parser = new QueryParser(LuceneUtils.CURRENT_VERSION, "content", analyzer);
		Query query = parser.parse(keywords);
		logger.info("Keywords=" + keywords + ", query=" + query.toString());

		TopDocs topDocs = searcher.search(query, null, MAX_HITS);
		logger.info("All hits=" + topDocs.totalHits);
		List<String> result = new ArrayList<>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			//			System.out.println(scoreDoc.score);
			result.add(searcher.doc(scoreDoc.doc).get("content"));
		}

		return result;
	}

	/**
	 * Term查询
	 */
	public List<String> singleTermQuery(String word) throws IOException {

		List<String> result = new ArrayList<>();
		TopDocs topDocs = searcher.search(new TermQuery(new Term("content", word)), MAX_HITS);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			result.add(searcher.doc(scoreDoc.doc).get("content"));
		}

		return result;
	}

	/**
	 * 关闭资源
	 */
	public void close() {
		try {
			reader.close();
			analyzer.close();
		} catch (IOException e) {
			logger.info("DataQuery's resources closed error.");
			throw new RuntimeException(e);
		}
	}

}
