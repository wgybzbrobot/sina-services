package cc.pp.sina.dao.tool;

import java.util.List;

import cc.pp.sina.domain.tool.TransUidInfo;
import cc.pp.sina.domain.tool.TransUidInsertParams;
import cc.pp.sina.domain.tool.TransUidSelectParams;

/**
 *  根据Domain、Nickname转换Uid
 * @author wgybzb
 *
 */
public interface TransUidDao {

	/**
	 * 插入转换数据
	 */
	public void insertTransUidInfo(TransUidInsertParams transUidInsertParams);

	/**
	 * 读取转换数据
	 */
	public List<TransUidInfo> getTransUidInfos(TransUidSelectParams transUidSelectParams);

}
