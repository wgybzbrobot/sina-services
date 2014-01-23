package cc.pp.sina.query.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.analyzer.ik.lucene.IKAnalyzer;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.query.conf.QueryConfig;

public class BozhuIndex {

	private static Logger logger = LoggerFactory.getLogger(BozhuIndex.class);

	public static final String INDEX_DIR = QueryConfig.getProperty("index.dir");

	// 索引目录
	private final Directory dir;
	// 分析器
	private final Analyzer analyzer;
	// 写索引实例化
	private final IndexWriter writer;

	public BozhuIndex(String indexDir) throws IOException {
		dir = FSDirectory.open(new File(indexDir));
		analyzer = new IKAnalyzer(Version.LUCENE_46);
		writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_46, analyzer));
	}

	/**
	 * 关闭索引
	 */
	public void close() {
		try {
			try {
				writer.close();
			} finally {
				if (IndexWriter.isLocked(dir)) {
					IndexWriter.unlock(dir);
				}
			}
			dir.close();
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage());
		}
	}

	/**
	 * 添加索引数据
	 */
	public void addIndexData(UserAllParamsDomain bozhu) throws IOException {
		writer.addDocument(getDoc(bozhu));
	}

	public void addIndexDatas(List<UserAllParamsDomain> bozhus) throws IOException {
		List<Document> docs = new ArrayList<>();
		for (UserAllParamsDomain bozhu : bozhus) {
			docs.add(getDoc(bozhu));
		}
		writer.addDocuments(docs);
	}

	/**
	 * 更新索引数据
	 */
	public void updateIndexData(UserAllParamsDomain bozhu) {
		try {
			writer.updateDocument(new Term("username", bozhu.getUsername()), getDoc(bozhu));
			// 提交更新
			writer.commit();
		} catch (IOException e) {
			logger.info("User: " + bozhu.getUsername() + "'s indexed data updated error.");
		}
	}

	/**
	 * 删除索引数据
	 */
	public void deleteIndexData(UserAllParamsDomain bozhu) {
		try {
			writer.deleteDocuments(new Term("username", bozhu.getUsername()));
			// commit提交删除
			writer.commit();
		} catch (IOException e) {
			logger.info("User: " + bozhu.getUsername() + "'s indexed data deleted error.");
		}
	}

	/**
	 * 获取Writer类
	 */
	public IndexWriter getWriter() {
		return writer;
	}

	/**
	 * 组装Document
	 */
	public static Document getDoc(UserAllParamsDomain bozhu) {

		Document doc = new Document();
		// 索引域：文本域
		doc.add(new TextField("nickname", bozhu.getNickname(), Field.Store.YES));
		doc.add(new TextField("description", bozhu.getDescription(), Field.Store.YES));
		doc.add(new TextField("usertags", bozhu.getUsertags(), Field.Store.YES));
		doc.add(new TextField("top5provinces", bozhu.getTop5provinces(), Field.Store.YES));
		doc.add(new TextField("identitytype", bozhu.getIdentitytype(), Field.Store.YES));
		doc.add(new TextField("industrytype", bozhu.getIndustrytype(), Field.Store.YES));
		//		doc.add(new Field("fansage", bozhu.getFansage(), TextField.TYPE_STORED));
		//		doc.add(new Field("fanstags", bozhu.getFanstags(), TextField.TYPE_STORED));
		// 索引域：数值域
		doc.add(new IntField("createdtime", bozhu.getCreatedtime(), Field.Store.YES));
		doc.add(new IntField("fanscount", bozhu.getFanscount(), Field.Store.YES));
		doc.add(new IntField("weibocount", bozhu.getWeibocount(), Field.Store.YES));
		doc.add(new FloatField("averagewbs", bozhu.getAveragewbs(), Field.Store.YES));
		doc.add(new IntField("influence", bozhu.getInfluence(), Field.Store.YES));
		doc.add(new IntField("activation", bozhu.getActivation(), Field.Store.YES));
		doc.add(new IntField("activecount", bozhu.getActivecount(), Field.Store.YES));
		doc.add(new FloatField("addvratio", bozhu.getAddvratio(), Field.Store.YES));
		doc.add(new FloatField("activeratio", bozhu.getActiveratio(), Field.Store.YES));
		doc.add(new FloatField("maleratio", bozhu.getMaleratio(), Field.Store.YES));
		doc.add(new FloatField("fansexistedratio", bozhu.getFansexistedratio(), Field.Store.YES));
		doc.add(new LongField("allfanscount", bozhu.getAllfanscount(), Field.Store.YES));
		doc.add(new LongField("allactivefanscount", bozhu.getAllactivefanscount(), Field.Store.YES));
		doc.add(new FloatField("oriratio", bozhu.getOriratio(), Field.Store.YES));
		doc.add(new FloatField("aveorirepcom", bozhu.getAveorirepcom(), Field.Store.YES));
		doc.add(new FloatField("averepcom", bozhu.getAverepcom(), Field.Store.YES));
		doc.add(new FloatField("averepsbyweek", bozhu.getAverepcombyweek(), Field.Store.YES));
		doc.add(new FloatField("averepsbymonth", bozhu.getAverepcombymonth(), Field.Store.YES));
		doc.add(new FloatField("validrepcombyweek", bozhu.getValidrepcombyweek(), Field.Store.YES));
		doc.add(new FloatField("validrepcombymonth", bozhu.getValidrepcombymonth(), Field.Store.YES));
		doc.add(new FloatField("avereposterquality", bozhu.getAvereposterquality(), Field.Store.YES));
		doc.add(new LongField("aveexposionsum", bozhu.getAveexposionsum(), Field.Store.YES));
		// 索引域：单查询
		doc.add(new Field("username", bozhu.getUsername(), StringField.TYPE_STORED));
		doc.add(new Field("verify", String.valueOf(bozhu.getVerify()), StringField.TYPE_STORED));
		// 存储域
		doc.add(new StoredField("wbsource", bozhu.getWbsource()));

		return doc;
	}

}
