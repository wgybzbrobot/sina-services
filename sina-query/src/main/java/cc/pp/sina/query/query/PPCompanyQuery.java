package cc.pp.sina.query.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.query.QueryResult;

public class PPCompanyQuery {

	private static Logger logger = LoggerFactory.getLogger(BozhuQuery.class);

	/**
	 * 关键词查询域
	 */
	private static final String[] KEYWORDS_FIELD_NAMES = { "screenname", "description", "verifiedreason", "location" };

	private static final float[] BOOST = { 3, 2, 1, 3 };
	// 分页操作的每页返回个数
	private static final int COUNT_PER_PAGE = 100;
	// 返回最大查询结果
	private static final int MAX_QUERY_RESULT = 5_000;

	private final Directory dir;
	private final DirectoryReader reader;
	private final IndexSearcher searcher;
	private final Analyzer analyzer;

	public PPCompanyQuery(String indexDir) throws IOException {
		dir = FSDirectory.open(new File(indexDir));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
		analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);
	}

	/**
	 * 关闭搜索
	 */
	public void close() throws IOException {
		reader.close();
		dir.close();
	}

	/**
	 * 根据关键词查询uid
	 * @param yearmonth：年月
	 * @param type：企业用户数据类型，all/add/leaves
	 * @param keywords：关键词
	 * @param page：页码
	 */
	public QueryResult getQueryResultByKeywords(String yearmonth, String type, String keywords, int page)
			throws ParseException, IOException {

		logger.info("Query yearmonth=\"" + yearmonth + "\",type=\"" + type + "\", keywords=\"" + keywords
				+ "\",page=\"" + page + "\".");

		BooleanQuery booleanQuery = new BooleanQuery();
		QueryParser parser;
		Query query;

		if (yearmonth.length() == 0 || type.length() == 0) {
			logger.error("Params of 'yearmonth' and 'type' should haven.");
			return null;
		}

		// yearmonth
		booleanQuery.add(new TermQuery(new Term("yearmonth", yearmonth)), BooleanClause.Occur.MUST);
		// type
		booleanQuery.add(new TermQuery(new Term("type", type)), BooleanClause.Occur.MUST);

		// keywords
		BooleanQuery booleanQuery1 = null;
		if (keywords.length() != 0) {
			booleanQuery1 = new BooleanQuery();
			for (int i = 0; i < KEYWORDS_FIELD_NAMES.length; i++) {
				parser = new QueryParser(Version.LUCENE_46, KEYWORDS_FIELD_NAMES[i], analyzer);
				query = parser.parse(keywords);
				// 添加权重
				query.setBoost(BOOST[i]);
				booleanQuery1.add(query, BooleanClause.Occur.SHOULD);
			}
		}
		if (booleanQuery1 != null) {
			booleanQuery.add(booleanQuery1, BooleanClause.Occur.MUST);
		}

		TopDocs topDocs = searcher.search(booleanQuery, MAX_QUERY_RESULT);

		if (topDocs.totalHits < (page - 1) * COUNT_PER_PAGE) {
			return null;
		}

		List<String> result = new ArrayList<>();
		int count = 1;
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			if ((count > (page - 1) * COUNT_PER_PAGE) && (count <= page * COUNT_PER_PAGE)) {
				Document doc = searcher.doc(scoreDoc.doc);
				result.add(doc.get("id"));
			}
			count++;
		}

		return new QueryResult.Builder().setData(result).setTotalNumber(topDocs.totalHits).build();
	}

}
