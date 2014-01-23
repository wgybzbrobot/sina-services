package cc.pp.fuzzy.search.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.analyzer.ik.lucene.IKAnalyzer;
import cc.pp.sina.utils.lucene.LuceneUtils;

public class DataIndex {

	private static Logger logger = LoggerFactory.getLogger(DataIndex.class);

	/**
	 * 写索引
	 */
	private final IndexWriter writer;

	public IndexWriter getWriter() {
		return writer;
	}

	/**
	 * 初始化
	 */
	public DataIndex(Directory indexDir) throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.CURRENT_VERSION, new IKAnalyzer(
				LuceneUtils.CURRENT_VERSION));
		writer = new IndexWriter(indexDir, config);
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws IOException {

		Directory indexDir = FSDirectory.open(new File("weixin_index_data"));
		DataIndex dataIndex = new DataIndex(indexDir);
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/dataset")));) {
			String str = null;
			String[] strs = null;
			while ((str = br.readLine()) != null) {
				strs = str.split("\t");
				logger.info("Add index: " + strs[0]);
				dataIndex.addIndexData(Long.parseLong(strs[0]), strs[1]);
			}
		}
		dataIndex.mergeIndexData();
		dataIndex.close();
	}

	/**
	 * 添加索引
	 */
	public void addIndexData(long id, String text) {
		try {
			writer.addDocument(getDoc(id, text));
		} catch (IOException e) {
			logger.error("Adding index data: " + text + " occurs error.");
		}
	}

	/**
	 * 更新索引
	 */
	public void updateIndexData(String text, long id) {
		try {
			writer.updateDocument(new Term("id", String.valueOf(id)), getDoc(id, text));
		} catch (IOException e) {
			logger.info("Updating index data: " + id + " occurs error.");
		}
	}

	/**
	 * 删除索引
	 */
	public void deleteIndexData(long id) {
		try {
			writer.deleteDocuments(new Term("id", String.valueOf(id)));
		} catch (IOException e) {
			logger.info("Deleting index data: " + id + " occurs error.");
		}
	}

	/**
	 * 合并索引段
	 */
	public void mergeIndexData() {
		try {
			writer.forceMerge(1);
		} catch (IOException e) {
			logger.info("Merging index datas occurs error.");
		}
	}

	/**
	 * 关闭资源
	 */
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取组装好的文档
	 */
	public static Document getDoc(long id, String text) {
		Document doc = new Document();
		doc.add(new NumericDocValuesField("id", id));
		doc.add(new StoredField("id", id));
		doc.add(new TextField("content", text, Field.Store.YES));
		return doc;
	}

}
