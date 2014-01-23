package cc.pp.sina.dao.bozhus;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.domain.params.BaseInfoParams;
import cc.pp.sina.domain.params.ExtendSelectParams;

/**
 * 用户数据
 * @author wgybzb
 *
 */
public interface CommonInfoDao {

	/**
	 * 获取用户基础数据
	 */
	public BozhuBaseInfo getUserBaseInfo(BaseInfoParams params);

	/**
	 * 获取用户粉丝Id列表
	 */
	public SimpleFansInfo getFansByUid(long uid);

	/**
	 * 获取用户扩展数据
	 */
	public UserExtendInfo getExtendInfo(ExtendSelectParams extendSelectParams);

}
