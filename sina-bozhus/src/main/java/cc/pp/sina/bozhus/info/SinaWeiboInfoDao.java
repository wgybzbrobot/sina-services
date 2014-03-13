package cc.pp.sina.bozhus.info;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;

public interface SinaWeiboInfoDao {

	/**
	 * 获取单条微博详细数据
	 */
	public Status getSingleWeiboDetail(String wid);

	/**
	 * 单条微博转发数据
	 */
	public StatusWapper getSinaSingleWeiboResposts(String wid, int cursor);

	/**
	 * 用户微博数据
	 */
	public StatusWapper getSinaUserWeibos(String uid, int cursor);

	/**
	 * 获取公共微博数据
	 */
	public StatusWapper getPublicWeibos();

}
