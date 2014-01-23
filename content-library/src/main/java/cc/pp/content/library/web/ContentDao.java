package cc.pp.content.library.web;

import java.util.HashMap;

/**
 * 使用Mapper.xml配置文件映射
 * @author wgybzb
 *
 */
public interface ContentDao {

	public ContentResult selectResult(HashMap<String, String> params);

}
