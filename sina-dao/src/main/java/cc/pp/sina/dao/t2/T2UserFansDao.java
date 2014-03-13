package cc.pp.sina.dao.t2;

import java.util.List;

import cc.pp.sina.domain.t2.T2SinaUserInfo;
import cc.pp.sina.domain.t2.T2TencentUserInfo;

/**
 * T2用户及其粉丝分析
 * @author wgybzb
 *
 */
public interface T2UserFansDao {

	/**
	 * 新浪获取待分析uid
	 */
	public List<String> getSinaNewUids();

	public List<String> getSinaAllUids();

	/**
	 * 新浪插入用户信息
	 */
	public void insertT2SinaUser(long username);

	/**
	 * 新浪更新分析结果
	 */
	public void updateT2SinaUser(T2SinaUserInfo sinaUserInfo);

	/**
	 * 新浪获取分析结果
	 */
	public T2SinaUserInfo selectT2SinaUser(long username);

	/**
	 * 新浪删除用户
	 */
	public void deleteT2SinaUser(long username);

	/**
	 * 腾讯获取待分析uid
	 */
	public List<String> getTencentNewUids();

	public List<String> getTencentAllUids();

	/**
	 * 腾讯插入用户信息
	 */
	public void insertT2TencentUser(String username);

	/**
	 * 腾讯更新分析结果
	 */
	public void updateT2TencentUser(T2TencentUserInfo tencentUserInfo);

	/**
	 * 腾讯获取分析结果
	 */
	public T2TencentUserInfo selectT2TencentUser(String username);

	/**
	 * 腾讯删除用户
	 */
	public void deleteT2TencentUser(String username);

}
