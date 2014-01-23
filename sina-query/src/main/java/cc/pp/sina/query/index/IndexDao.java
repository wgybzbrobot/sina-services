package cc.pp.sina.query.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

public interface IndexDao {

	/**
	 * 添加索引数据
	 */
	public void addIndexData(Object object);

	/**
	 * 更新索引数据
	 */
	public void updateIndexData(Object object);

	/**
	 * 删除索引数据
	 */
	public void deleteIndexData(Object object);

	/**
	 * 组装Document
	 */
	public Document getDoc(Object object);

	/**
	 * 获取索引
	 */
	public IndexWriter getWriter();

	/**
	 * 关闭索引
	 */
	public void close();

}
