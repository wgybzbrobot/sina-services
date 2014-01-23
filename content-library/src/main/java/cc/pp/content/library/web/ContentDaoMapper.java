package cc.pp.content.library.web;

import org.apache.ibatis.annotations.Select;

/**
 * 使用Mapper.java类映射
 * @author wgybzb
 *
 */
public interface ContentDaoMapper {

	@Select("SELECT * FROM wb_content_result WHERE recordType = #{recordType} AND date = #{date}")
	public ContentResult selectResult(String recordType, String date);

}
