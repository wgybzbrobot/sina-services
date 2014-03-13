package cc.pp.sina.dao.users;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.users.UserInfo;
import cc.pp.sina.domain.users.UserInsertParams;
import cc.pp.sina.domain.users.UserSelectParams;
import cc.pp.sina.domain.users.UsersSelectParams;

import com.sina.weibo.model.User;

public class SinaUsers {

	private static SqlSessionFactory sqlSessionFactory;

	public SinaUsers(MybatisConfig.ServerEnum server) {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(server);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取数据表中的最大bid
	 */
	public int getMaxBid(String tablename) {
		UsersSelectParams usersSelectParams = new UsersSelectParams(tablename, 0, 0);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			return sinaUsersDao.getMaxBid(usersSelectParams);
		}
	}

	/**
	 * 获取新浪用户基础信息，批量查询
	 */
	public List<UserInfo> getSinaUserInfos(String tablename, int low, int high) {
		UsersSelectParams usersSelectParams = new UsersSelectParams(tablename, low, high);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			return sinaUsersDao.getSinaUserInfos(usersSelectParams);
		}
	}

	/**
	 * 获取新浪用户基础信息，单个查询
	 */
	public UserInfo getSinaUserInfo(String tablename, long username) {
		UserSelectParams userSelectParams = new UserSelectParams(tablename, username);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			return sinaUsersDao.getSinaUserInfo(userSelectParams);
		}
	}

	/**
	 * 判断某个用户存在与否
	 */
	public boolean isSinaUserExisted(String tablename, long username) {
		UserSelectParams userSelectParams = new UserSelectParams(tablename, username);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			return (sinaUsersDao.getSinaUserInfo(userSelectParams) == null) ? false : true;
		}
	}

	/**
	 * 插入新浪用户基础信息
	 */
	public void insertSinaUserInfo(String tablename, User user) {
		if (user.getRemark() == null) {
			user.setRemark("");
		}
		UserInsertParams userInsertParams = new UserInsertParams.Builder(tablename, Long.parseLong(user.getId()))
				.setScreen_name(user.getScreenName()).setName(user.getName()).setProvince(user.getProvince())
				.setCity(user.getCity()).setLocation(user.getLocation()).setDescription(user.getDescription())
				.setUrl(user.getUrl()).setProfile_image_url(user.getProfileImageUrl()).setDomain(user.getUserDomain())
				.setGender(user.getGender()).setFollowers_count(user.getFollowersCount())
				.setFriends_count(user.getFriendsCount()).setStatuses_count(user.getStatusesCount())
				.setFavourites_count(user.getFavouritesCount()).setCreated_at(user.getCreatedAt().getTime() / 1000)
				.setVerified(user.isVerified()).setVerified_type(user.getVerifiedType())
				.setAvatar_large(user.getAvatarLarge()).setBi_followers_count(user.getBiFollowersCount())
				.setRemark(user.getRemark()).setVerified_reason(user.getVerifiedReason()).setWeihao(user.getWeihao())
				.setLasttime(System.currentTimeMillis() / 1000).build();
		System.out.println(userInsertParams.getRemark());
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			sinaUsersDao.insertSinaUserInfo(userInsertParams);
		}
	}

	/**
	 * 删除新浪用户基础信息
	 */
	public void deleteSinaUserInfo(String tablename, long username) {
		UserSelectParams userSelectParams = new UserSelectParams(tablename, username);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			sinaUsersDao.deleteSinaUserInfo(userSelectParams);
		}
	}

	/**
	 * 更新新浪用户基础信息，更新不存在用户
	 */
	public void updateSinaUserInfo(String tablename, long username) {
		UserSelectParams userSelectParams = new UserSelectParams(tablename, username);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaUsersDao sinaUsersDao = sqlSession.getMapper(SinaUsersDao.class);
			sinaUsersDao.updateSinaUserInfo(userSelectParams);
		}
	}

}
