package cc.pp.content.library.web;

import java.util.HashMap;
import java.util.List;

/**
 * 使用Mapper.xml配置文件映射
 * @author wgybzb
 *
 */
public interface ContentDao {

	/**
	 * 获取内容库数据表表名
	 */
	public List<String> showTables();

	/**
	 * 获取内容库统计结果
	 */
	public ContentResult selectResult(HashMap<String, String> params);

}
