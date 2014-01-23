package cc.pp.sina.bozhus.fans;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.FansAddDailyInfo;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.domain.params.BaseInfoParams;
import cc.pp.sina.domain.params.ExtendSelectParams;
import cc.pp.sina.domain.params.FansAddSelectParams;
import cc.pp.sina.domain.params.FansAnalysisInsertParams;


public interface FansAnalysisDao {

	/**
	 * 粉丝Id列表
	 */
	public SimpleFansInfo getFansByUid(long uid);

	/**
	 * 用户基础信息
	 */
	public BozhuBaseInfo getUserBaseInfo(BaseInfoParams baseInfoParams);

	/**
	 * 用户扩展信息
	 */
	public UserExtendInfo getExtendInfo(ExtendSelectParams extendSelectParams);

	/**
	 * 用户粉丝增减信息
	 */
	public FansAddDailyInfo getFansAddDailyInfo(FansAddSelectParams fansAddSelectParams);

	/**
	 * 插入粉丝分析结果数据
	 */
	public void insertFansAnalysisResult(FansAnalysisInsertParams faip);

}
