package cc.pp.sina.dao.t2;

import java.util.List;

import cc.pp.sina.domain.t2.T2SingleWeiboInfo;

/**
 * T2用户微博分析
 * @author wgybzb
 *
 */
public interface T2SingleWeiboDao {

	/**
	 * 获取待分析微博id
	 */
	public List<String> getNewWids(String type);

	/**
	 * 插入单条微博信息
	 */
	public void insertSingleWeibo(T2SingleWeiboInfo singleWeibo);

	/**
	 * 更新单条微博分析结果
	 */
	public void updateSingleWeibo(T2SingleWeiboInfo singleWeibo);

	/**
	 * 获取单条微博分析结果
	 */
	public T2SingleWeiboInfo selectSingleWeibo(long wid);

	/**
	 * 删除单条微博
	 */
	public void deleteSingleWeibo(long wid);

}
