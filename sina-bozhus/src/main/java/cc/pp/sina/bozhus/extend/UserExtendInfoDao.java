package cc.pp.sina.bozhus.extend;

import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.domain.params.ExtendInsertParams;
import cc.pp.sina.domain.params.ExtendSelectParams;

/**
 * 用户标签数据接口
 * @author wgybzb
 *
 */
public interface UserExtendInfoDao {

	/**
	 * 插入用户扩展数据
	 */
	public void insertExtendInfo(ExtendInsertParams extendInsertParams);

	/**
	 * 获取用户扩展数据
	 */
	public UserExtendInfo getExtendInfo(ExtendSelectParams extendSelectParams);

}
