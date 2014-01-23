package cc.pp.sina.query.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
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
import cc.pp.sina.domain.sql.UserBaseInfo;
import cc.pp.sina.query.conf.QueryConfig;

public class PPCompanyIndex {

	private static Logger logger = LoggerFactory.getLogger(PPCompanyIndex.class);

	public static final String INDEX_DIR = QueryConfig.getProperty("index.dir");

	// 索引目录
	private final Directory dir;
	// 分析器
	private final Analyzer analyzer;
	// 写索引实例化
	private final IndexWriter writer;

	public PPCompanyIndex() throws IOException {
		dir = FSDirectory.open(new File(INDEX_DIR));
		analyzer = new IKAnalyzer(Version.LUCENE_46);
		writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_46, analyzer));
	}

	public PPCompanyIndex(String indexDir) throws IOException {
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
	public void addIndexData(UserBaseInfo user, String yearMonth, String type) throws IOException {
		writer.addDocument(getDoc(user, yearMonth, type));
	}

	/**
	 * 更新索引数据
	 */
	public void updateIndexData(UserBaseInfo user, String yearMonth, String type) {
		try {
			writer.updateDocument(new Term("id", String.valueOf(user.getId())), getDoc(user, yearMonth, type));
			// 提交更新
			writer.commit();
		} catch (IOException e) {
			logger.info("User: " + user.getId() + "'s indexed data updated error.");
		}
	}

	/**
	 * 删除索引数据
	 */
	public void deleteIndexData(UserBaseInfo user) {
		try {
			writer.deleteDocuments(new Term("id", String.valueOf(user.getId())));
			// commit提交删除
			writer.commit();
		} catch (IOException e) {
			logger.info("User: " + user.getId() + "'s indexed data deleted error.");
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
	public static Document getDoc(UserBaseInfo user, String yearMonth, String type) {

		Document doc = new Document();
		// 索引域：文本域
		doc.add(new LongField("id", user.getId(), Field.Store.YES));
		doc.add(new TextField("screenname", user.getScreen_name(), Field.Store.YES));
		doc.add(new TextField("location", user.getLocation(), Field.Store.YES));
		doc.add(new TextField("description", user.getDescription(), Field.Store.YES));
		doc.add(new TextField("verifiedreason ", user.getVerified_reason(), Field.Store.YES));
		doc.add(new StringField("yearmonth", yearMonth, Field.Store.YES));
		doc.add(new StringField("type", type, Field.Store.YES));

		return doc;
	}

}
