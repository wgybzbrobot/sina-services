package cc.pp.sina.query.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

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
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.query.FloatRangeDomain;
import cc.pp.sina.domain.query.IntRangeDomain;
import cc.pp.sina.domain.query.QueryResult;
import cc.pp.sina.query.index.BozhuIndex;

public class BozhuQuery {

	private static Logger logger = LoggerFactory.getLogger(BozhuQuery.class);

	/**
	 * 关键词查询域
	 */
	private static final String[] KEYWORDS_FIELD_NAMES = { "nickname", "description", "usertags" };
	private static final String[] SORTED_FIELD_NAMES = { "activecount", "fanscount", "influence" };
	private static final float[] BOOST = { 3, 2, 1 };
	// 分页操作的每页返回个数
	private static final int COUNT_PER_PAGE = 15;
	// 返回最大查询结果
	private static final int MAX_QUERY_RESULT = 5_000;

	private final Directory dir;
	private final DirectoryReader reader;
	private final IndexSearcher searcher;
	private final Analyzer analyzer;

	public BozhuQuery() throws IOException {
		dir = FSDirectory.open(new File(BozhuIndex.INDEX_DIR));
		reader = DirectoryReader.open(dir);
		searcher = new IndexSearcher(reader);
		analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);
	}

	public void close() throws IOException {
		reader.close();
		dir.close();
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException, ParseException {

		BozhuQuery bozhuQuery = new BozhuQuery();

		// 1、关键词查询类型
		//		List<String> result = bozhuQuery.getKeywordsQueryResult("activecount", "fanscount", "influence", "电梯");
		// 2、数值范围查询类型
		List<IntRangeDomain> intRangeDomains = new ArrayList<IntRangeDomain>();
		intRangeDomains.add(new IntRangeDomain.Builder("influence", 70, 100).build());
		intRangeDomains.add(new IntRangeDomain.Builder("activation", 6, 10).build());
		intRangeDomains.add(new IntRangeDomain.Builder("fanscount", 5_0000, 100_0000).build());
		List<FloatRangeDomain> floatRangeDomains = new ArrayList<FloatRangeDomain>();
		floatRangeDomains.add(new FloatRangeDomain.Builder("averagewbs", 1.0f, 3.0f).build());
		//		List<String> sortedNames = new ArrayList<String>();
		//		sortedNames.add("activecount");
		//		sortedNames.add("fanscount");
		//		sortedNames.add("influence");
		//		List<String> result = bozhuQuery.getRangeQueryResult(intRangeDomains, floatRangeDomains, sortedNames);
		//		QueryResult qr = bozhuQuery.getCompositeQueryResult(intRangeDomains, floatRangeDomains, "商业娱乐美食", "0", 1);
		//		QueryResult qr = bozhuQuery.getCompositeQueryResult(intRangeDomains, floatRangeDomains, "", "0", 1);
		//		QueryResult qr = bozhuQuery.getCompositeQueryResult(intRangeDomains, new ArrayList<FloatRangeDomain>(), "",
		//				"0", 1);
		//		QueryResult qr = bozhuQuery.getCompositeQueryResult(new ArrayList<IntRangeDomain>(),
		//				new ArrayList<FloatRangeDomain>(), "", "0", 1);
		//		QueryResult qr = bozhuQuery.getCompositeQueryResult(new ArrayList<IntRangeDomain>(),
		//				new ArrayList<FloatRangeDomain>(), "", "", 1);
		QueryResult qr = bozhuQuery.getCompositeQueryByFans(new ArrayList<IntRangeDomain>(), 1, "北京上海", "娱乐美食", "0", 1);
		System.err.println(qr.getTotalNumber());
		System.out.println("\n");
		for (String temp : qr.getData()) {
			System.out.println(temp);
		}
		bozhuQuery.close();
	}

	/**
	 * 联合查询，根据博主特性数据查找博主
	 * @param intRangeDomains：整数域
	 * @param floatRangeDomains：浮点域
	 * @param keywords：关键词
	 * @param verify：认证类型
	 * @param page：页码
	 */
	public QueryResult getCompositeQueryResult(List<IntRangeDomain> intRangeDomains,
			List<FloatRangeDomain> floatRangeDomains, String keywords, String verify, int page) throws ParseException,
			IOException {

		logger.info("Query keywords is \"" + keywords + "\", verify is \"" + verify
				+ "\", intRangeDomains is \"" + JSONArray.fromObject(intRangeDomains) + "\"");

		BooleanQuery booleanQuery = new BooleanQuery();
		QueryParser parser;
		Query query;

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
		if (intRangeDomains.size() != 0) {
			for (IntRangeDomain intRangeDomain : intRangeDomains) {
				booleanQuery.add(NumericRangeQuery.newIntRange(intRangeDomain.getFieldName(), intRangeDomain.getLow(),
						intRangeDomain.getHigh(), true, true), BooleanClause.Occur.MUST);
			}
		}
		if (floatRangeDomains.size() != 0) {
			for (FloatRangeDomain floatRangeDomain : floatRangeDomains) {
				booleanQuery.add(NumericRangeQuery.newFloatRange(floatRangeDomain.getFieldName(),
						floatRangeDomain.getLow(), floatRangeDomain.getHigh(), true, true), BooleanClause.Occur.MUST);
			}
		}

		if (booleanQuery1 != null) {
			booleanQuery.add(booleanQuery1, BooleanClause.Occur.MUST);
		}
		if (verify.length() > 0) {
			booleanQuery.add(new TermQuery(new Term("verify", verify)), BooleanClause.Occur.MUST);
		}
		if (!booleanQuery.iterator().hasNext()) {
			//			booleanQuery.add(NumericRangeQuery.newIntRange("influence", 70, 100, true, true), BooleanClause.Occur.MUST);
			booleanQuery.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
		}

		TopDocs topDocs = searcher.search(booleanQuery, MAX_QUERY_RESULT, new Sort(getSortedFields()));

		if (topDocs.totalHits < (page - 1) * COUNT_PER_PAGE) {
			return new QueryResult.Builder().build();
		}

		List<String> result = new ArrayList<String>();
		int count = 1;
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			if ((count > (page - 1) * COUNT_PER_PAGE) && (count <= page * COUNT_PER_PAGE)) {
				Document doc = searcher.doc(scoreDoc.doc);
				result.add(doc.get("username"));
			}
			count++;
		}

		return new QueryResult.Builder().setData(result).setTotalNumber(topDocs.totalHits).build();
	}

	/**
	 * 联合查询，根据博主特性数据查找博主
	 * @param intRangeDomains：整数fansage
	 * @param gender：浮点域，主要是maleratio，0——maleratio>=0.5; 1——maleratio<=0.5
	 * @param keywords：关键词，主要是top5provinces和fanstags
	 * @param page：页码
	 */
	public QueryResult getCompositeQueryByFans(List<IntRangeDomain> intRangeDomains, int gender, String provinces,
			String fanstags, String verify, int page) throws ParseException, IOException {

		logger.info("Query provinces is \"" + provinces + "\", gender is \"" + gender + "\", fanstags is \"" + fanstags
				+ "\", intRangeDomains is \"" + JSONArray.fromObject(intRangeDomains) + "\"");

		BooleanQuery booleanQuery = new BooleanQuery();
		// fansage
		//		if (intRangeDomains.size() != 0) {
		//			for (IntRangeDomain intRangeDomain : intRangeDomains) {
		//				booleanQuery.add(NumericRangeQuery.newIntRange(intRangeDomain.getFieldName(), intRangeDomain.getLow(),
		//						intRangeDomain.getHigh(), true, true), BooleanClause.Occur.MUST);
		//			}
		//		}
		// gender,-1表示其他
		if (gender == 1) {
			booleanQuery.add(NumericRangeQuery.newFloatRange("maleratio", 0.0f, 0.5f, true, true),
					BooleanClause.Occur.MUST);
		} else if (gender == 0) {
			booleanQuery.add(NumericRangeQuery.newFloatRange("maleratio", 0.5f, 1.0f, true, true),
					BooleanClause.Occur.MUST);
		}
		// top5provinces和fanstags
		if (provinces.length() != 0) {
			Query query = new QueryParser(Version.LUCENE_46, "top5provinces", analyzer).parse(provinces);
			logger.info("Query: " + query.toString());
			booleanQuery.add(new QueryParser(Version.LUCENE_46, "top5provinces", analyzer).parse(provinces),
					BooleanClause.Occur.MUST);
		}
		//		if (fanstags.length() != 0) {
		//			parser = new QueryParser(Version.LUCENE_46, "fanstags", analyzer);
		//			query = parser.parse(fanstags);
		//			booleanQuery.add(query, BooleanClause.Occur.MUST);
		//		}
		// verify
		if (verify.length() > 0) {
			booleanQuery.add(new TermQuery(new Term("verify", verify)), BooleanClause.Occur.MUST);
		}
		if (!booleanQuery.iterator().hasNext()) {
			booleanQuery.add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST);
		}

		TopDocs topDocs = searcher.search(booleanQuery, MAX_QUERY_RESULT, new Sort(getSortedFields()));

		if (topDocs.totalHits < (page - 1) * COUNT_PER_PAGE) {
			return new QueryResult.Builder().build();
		}

		List<String> result = new ArrayList<String>();
		int count = 1;
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			if ((count > (page - 1) * COUNT_PER_PAGE) && (count <= page * COUNT_PER_PAGE)) {
				Document doc = searcher.doc(scoreDoc.doc);
				result.add(doc.get("username"));
			}
			count++;
		}


		return new QueryResult.Builder().setData(result).setTotalNumber(topDocs.totalHits).build();
	}

	/**
	 * 根据博主特性查找博主
	 * 关键词查询类型：nickname、description、usertags
	 * @param：sortedField1,sortedField2,...,sortedField3,keywords
	 * 注意：排序域必须为整数
	 */
	public List<String> getKeywordsQueryResult(String... strs) throws IOException, ParseException {

		if (strs.length < 2) {
			logger.info("getKeywordsQueryResult's params should be： sortedField1,sortedField2,...,sortedField3,keywords");
			return null;
		}

		String keyWords = strs[strs.length - 1];
		QueryParser parser;
		Query query;
		BooleanQuery booleanQuery = new BooleanQuery();
		for (int i = 0; i < KEYWORDS_FIELD_NAMES.length; i++) {
			parser = new QueryParser(Version.LUCENE_46, KEYWORDS_FIELD_NAMES[i], analyzer);
			query = parser.parse(keyWords);
			// 添加权重
			query.setBoost(BOOST[i]);
			booleanQuery.add(query, BooleanClause.Occur.SHOULD);
		}

		SortField[] sortFields = new SortField[strs.length];
		for (int i = 0; i < strs.length - 1; i++) {
			// 添加排序域类型为Int型，从大到小排序
			sortFields[i] = new SortField(strs[i], SortField.Type.INT, true);
		}
		sortFields[strs.length - 1] = SortField.FIELD_SCORE;
		TopDocs topDocs = searcher.search(booleanQuery, 1000, new Sort(sortFields));

		List<String> result = new ArrayList<String>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			result.add(doc.get("username"));
		}

		return result;
	}

	/**
	 * 根据博主特性查找博主
	 * 数值范围查询类型：createdtime、fanscount、weibocount、influence、activation、activecount、
	 * allfanscount、allactivefanscount、aveexposionsum、averagewbs、addvratio、activeratio、
	 * maleratio、fansexistedratio、oriratio、aveorirepcom、averepcom、averepsbyweek、averepsbymonth、
	 * validrepcombyweek、validrepcombymonth、avereposterquality、
	 * verify
	 * @param：
	 * 注意：排序域必须为整数
	 */
	public List<String> getRangeQueryResult(List<IntRangeDomain> intRangeDomains,
			List<FloatRangeDomain> floatRangeDomains, List<String> sortedNames) throws IOException {

		if (intRangeDomains.size() == 0 && floatRangeDomains.size() == 0) {
			logger.info("getRangeQueryResult params should be： List<IntRangeDomain>,"
					+ "List<FloatRangeDomain>,List<String>(sortedNames)");
			return null;
		}

		BooleanQuery booleanQuery = new BooleanQuery();
		if (intRangeDomains.size() != 0) {
			for (IntRangeDomain intRangeDomain : intRangeDomains) {
				booleanQuery.add(NumericRangeQuery.newIntRange(intRangeDomain.getFieldName(), intRangeDomain.getLow(),
						intRangeDomain.getHigh(), true, true), BooleanClause.Occur.MUST);
			}
		}
		if (floatRangeDomains.size() != 0) {
			for (FloatRangeDomain floatRangeDomain : floatRangeDomains) {
				booleanQuery.add(NumericRangeQuery.newFloatRange(floatRangeDomain.getFieldName(),
						floatRangeDomain.getLow(), floatRangeDomain.getHigh(), true, true), BooleanClause.Occur.MUST);
			}
		}

		SortField[] sortFields = new SortField[sortedNames.size() + 1];
		for (int i = 0; i < sortedNames.size(); i++) {
			sortFields[i] = new SortField(sortedNames.get(i), SortField.Type.INT, true);
		}
		sortFields[sortedNames.size()] = SortField.FIELD_SCORE;
		TopDocs topDocs = searcher.search(booleanQuery, 1000, new Sort(sortFields));

		List<String> result = new ArrayList<String>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			result.add(doc.get("username"));
		}

		return result;
	}

	/**
	 * 单查询：username，适合走数据库查询
	 */

	/**
	 * 排序域
	 */
	private SortField[] getSortedFields() {

		SortField[] sortFields = new SortField[SORTED_FIELD_NAMES.length + 1];
		for (int i = 0; i < SORTED_FIELD_NAMES.length; i++) {
			sortFields[i] = new SortField(SORTED_FIELD_NAMES[i], SortField.Type.INT, true);
		}
		sortFields[SORTED_FIELD_NAMES.length] = SortField.FIELD_SCORE;
		return sortFields;
	}

}
