package cc.pp.sina.dao.t2;

import java.util.List;

import cc.pp.sina.domain.t2.SinaSelectParams;
import cc.pp.sina.domain.t2.T2SinaInteractionsInfo;
import cc.pp.sina.domain.t2.T2TencentInteractionsInfo;
import cc.pp.sina.domain.t2.TencentSelectParams;

/**
 * T2用户粉丝互动数据
 * @author wgybzb
 *
 */
public interface T2InteractionsDao {

	/**
	 * 新浪插入互动信息
	 */
	public void insertT2SinaInteractions(T2SinaInteractionsInfo sinaInteractionsInfo);

	/**
	 * 新浪获取分析结果
	 */
	public T2SinaInteractionsInfo selectT2SinaInteractions(T2SinaInteractionsInfo sinaInteractionsInfo);

	public List<T2SinaInteractionsInfo> selectT2SinaInteractionsList(SinaSelectParams sinaSelectParams);

	/**
	 * 新浪删除互动信息
	 */
	public void deleteT2SinaInteractions(T2SinaInteractionsInfo sinaInteractionsInfo);

	/**
	 * 腾讯插入互动信息
	 */
	public void insertT2TencentInteractions(T2TencentInteractionsInfo tencentInteractionsInfo);

	/**
	 * 腾讯获取互动信息
	 */
	public T2TencentInteractionsInfo selectT2TencentInteractions(T2TencentInteractionsInfo tencentInteractionsInfo);

	public List<T2TencentInteractionsInfo> selectT2TencentInteractionsList(TencentSelectParams tencentSelectParams);

	/**
	 * 腾讯删除互动信息
	 */
	public void deleteT2TencentInteractions(T2TencentInteractionsInfo tencentInteractionsInfo);

}
