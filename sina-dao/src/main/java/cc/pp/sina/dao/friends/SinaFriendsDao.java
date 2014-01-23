package cc.pp.sina.dao.friends;

import cc.pp.sina.domain.friends.FriendsInfo;
import cc.pp.sina.domain.friends.FriendsInsertParams;
import cc.pp.sina.domain.friends.FriendsSelectParams;

/**
 * 新浪用户关注数据Dao
 * @author wgybzb
 *
 */
public interface SinaFriendsDao {

	/**
	 * 插入新浪用户关注数据
	 */
	public void insertSinaFriendsInfo(FriendsInsertParams friendsInsertParams);

	/**
	 * 获取新浪用户关注数据
	 */
	public FriendsInfo getSinaFriendsInfo(FriendsSelectParams friendsSelectParams);

	/**
	 * 更新新浪用户关注数据
	 */
	public void updateSinaFriendsInfo(FriendsInsertParams friendsInsertParams);

	/**
	 * 删除新浪用户关注数据
	 */
	public void deleteSinaFriendsInfo(FriendsSelectParams friendsSelectParams);

}
