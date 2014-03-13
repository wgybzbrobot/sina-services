package cc.pp.sina.dao.users;

import java.util.List;

import cc.pp.sina.domain.users.UserInfo;
import cc.pp.sina.domain.users.UserInsertParams;
import cc.pp.sina.domain.users.UserSelectParams;
import cc.pp.sina.domain.users.UsersSelectParams;

/**
 * 新浪用户基础信息Dao
 * @author wgybzb
 *
 */
public interface SinaUsersDao {

	/**
	 * 获取数据表中的最大bid
	 */
	public int getMaxBid(UsersSelectParams usersSelectParams);

	/**
	 * 获取新浪用户基础信息，批量查询
	 */
	public List<UserInfo> getSinaUserInfos(UsersSelectParams usersSelectParams);

	/**
	 * 获取新浪用户基础信息，单个查询
	 */
	public UserInfo getSinaUserInfo(UserSelectParams userSelectParams);

	/**
	 * 判断某个用户存在与否
	 */
	public int isSinaUserExisted(UserSelectParams userSelectParams);

	/**
	 * 插入新浪用户基础信息
	 */
	public void insertSinaUserInfo(UserInsertParams userInsertParams);

	/**
	 * 删除新浪用户基础信息
	 */
	public void deleteSinaUserInfo(UserSelectParams userSelectParams);

	/**
	 * 更新新浪用户基础信息，更新不存在用户
	 */
	public void updateSinaUserInfo(UserSelectParams userSelectParams);

}
