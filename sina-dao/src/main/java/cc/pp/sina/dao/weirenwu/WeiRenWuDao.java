package cc.pp.sina.dao.weirenwu;

import java.util.List;

import cc.pp.sina.domain.weirenwu.BozhuExtendInfo;
import cc.pp.sina.domain.weirenwu.SelectBozhuParams;

/**
 * 微任务（博主库）数据层
 * @author wgybzb
 *
 */
public interface WeiRenWuDao {

	/**
	 * 插入博主uid
	 */
	public void insertSinaBozhuUid(String username);

	/**
	 * 获取所有博主uid
	 */
	public List<String> selectAllSinaBozhuUids();

	/**
	 * 根据uid查找bzid
	 */
	public long selectSinaBzid(String username);

	/**
	 * 插入博主计算数据
	 */
	public void insertSinaBozhuExtendInfo(BozhuExtendInfo bozhuExtendInfo);

	/**
	 * 获取博主计算数据，根据uid
	 */
	public BozhuExtendInfo selectSinaBozhuExtendInfo(long bzid);

	/**
	 * 获取博主计算数据，根据bzid范围
	 */
	public List<BozhuExtendInfo> selectSinaBozhuExtendInfos(SelectBozhuParams selectBozhuParams);

}
