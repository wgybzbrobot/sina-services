package cc.pp.sina.query.indexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.query.BozhuParamsInfo;
import cc.pp.sina.query.common.IndexUtils;
import cc.pp.sina.query.index.BozhuIndex;
import cc.pp.sina.utils.files.PathUtils;
import cc.pp.sina.utils.json.JsonUtils;

public class BozhuIndexTest {

	private static final String INDEX_DIR = "test_index/";
	private BozhuIndex bozhuIndex;
	private UserAllParamsDomain bozhu1;
	private UserAllParamsDomain bozhu2;

	@Before
	public void prepared() {
		try {
			// 索引
			bozhuIndex = new BozhuIndex(INDEX_DIR);
			// 1000218641/1000109393
			bozhu1 = IndexUtils.bozhuTrans(JsonUtils.getObjectMapper().readValue(
					getClass().getResourceAsStream("bozhu1.json"), BozhuParamsInfo.class));
			bozhu2 = IndexUtils.bozhuTrans(JsonUtils.getObjectMapper().readValue(
					getClass().getResourceAsStream("bozhu2.json"), BozhuParamsInfo.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void closed() {
		PathUtils.deletePath(INDEX_DIR);
	}

	@Test
	public void testGetDoc_测试文档载入() throws JsonParseException, JsonMappingException, IOException {

		Document doc1 = BozhuIndex.getDoc(bozhu1);
		Document doc2 = BozhuIndex.getDoc(bozhu2);
		assertEquals("1000218641", doc1.get("username"));
		assertEquals("1000109393", doc2.get("username"));
		bozhuIndex.close();
	}

	@Test
	public void testAddIndexData_添加索引数据() throws IOException, ParseException {

		bozhuIndex.addIndexData(bozhu1);
		bozhuIndex.addIndexData(bozhu2);
		assertEquals(2, bozhuIndex.getWriter().maxDoc());
		assertEquals(2, bozhuIndex.getWriter().numDocs());
		bozhuIndex.close();
		assertEquals("张鹏_500", searcherDocs(bozhu1.getUsername()));
		assertEquals("zh-YJ", searcherDocs(bozhu2.getUsername()));
	}

	@Test
	public void testUpdateIndexData1_更新同一个数据_数据都一样() throws IOException, ParseException {

		bozhuIndex.addIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.updateIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.close();
		assertEquals("张鹏_500", searcherDocs(bozhu1.getUsername()));
	}

	@Test
	public void testUpdateIndexData2_更新同一个数据_但是昵称不一样() throws IOException, ParseException {

		bozhuIndex.addIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		BozhuParamsInfo bozhu = IndexUtils.bozhuTrans(bozhu1, "test");
		bozhuIndex.updateIndexData(IndexUtils.bozhuTrans(bozhu));
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.close();
		assertEquals("test", searcherDocs(bozhu1.getUsername()));
	}

	@Test
	public void testUpdateIndexData3_更新新数据_相当于添加数据() throws IOException, ParseException {

		bozhuIndex.addIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.updateIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.updateIndexData(bozhu2);
		assertEquals(2, bozhuIndex.getWriter().numDocs());
		bozhuIndex.close();
		assertEquals("张鹏_500", searcherDocs(bozhu1.getUsername()));
		assertEquals("zh-YJ", searcherDocs(bozhu2.getUsername()));
	}

	@Test
	public void testDeleteIndexData1_删除一条数据() throws IOException, ParseException {

		bozhuIndex.addIndexData(bozhu1);
		bozhuIndex.addIndexData(bozhu2);
		assertEquals(2, bozhuIndex.getWriter().numDocs());
		bozhuIndex.deleteIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.close();
		assertNull(searcherDocs(bozhu1.getUsername()));
		assertEquals("zh-YJ", searcherDocs(bozhu2.getUsername()));
	}

	@Test
	public void testDeleteIndexData2_删除两条数据() throws IOException, ParseException {

		bozhuIndex.addIndexData(bozhu1);
		bozhuIndex.addIndexData(bozhu2);
		assertEquals(2, bozhuIndex.getWriter().numDocs());
		bozhuIndex.deleteIndexData(bozhu1);
		assertEquals(1, bozhuIndex.getWriter().numDocs());
		bozhuIndex.deleteIndexData(bozhu2);
		assertEquals(0, bozhuIndex.getWriter().numDocs());
		bozhuIndex.close();
		assertNull(searcherDocs(bozhu1.getUsername()));
		assertNull(searcherDocs(bozhu2.getUsername()));
	}

	private String searcherDocs(String uid) throws IOException, ParseException {

		String result = null;
		Directory dir = FSDirectory.open(new File(INDEX_DIR));
		DirectoryReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);
		QueryParser parser = new QueryParser(Version.LUCENE_46, "username", analyzer);
		Query query = parser.parse(uid);
		ScoreDoc[] scoreDoc = searcher.search(query, null, 10).scoreDocs;
		if (scoreDoc.length > 0) {
			Document doc = searcher.doc(scoreDoc[0].doc);
			result = doc.get("nickname").toString();
		}
		reader.close();
		dir.close();
		return result;
	}


}
