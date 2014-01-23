package cc.pp.sina.dao.friends;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.friends.FriendsInfo;
import cc.pp.sina.domain.friends.FriendsInsertParams;
import cc.pp.sina.domain.friends.FriendsSelectParams;

public class SinaFriendsDis {

	private SqlSessionFactory sqlSessionFactory;

	public SinaFriendsDis(MybatisConfig.ServerEnum type) {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 插入新浪用户关注数据
	 */
	public void insertSinaFriendsInfo(String tablename, long username, String friendsuids, int friendscount,
			long lasttime) {
		FriendsInsertParams friendsInsertParams = new FriendsInsertParams(tablename, username, friendsuids,
				friendscount, lasttime);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaFriendsDao sinaFriendsDao = sqlSession.getMapper(SinaFriendsDao.class);
			sinaFriendsDao.insertSinaFriendsInfo(friendsInsertParams);
		}
	}

	/**
	 * 获取新浪用户关注数据
	 */
	public FriendsInfo getSinaFriendsInfo(String tablename, long username) {
		FriendsSelectParams friendsSelectParams = new FriendsSelectParams(tablename, username);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaFriendsDao sinaFriendsDao = sqlSession.getMapper(SinaFriendsDao.class);
			return sinaFriendsDao.getSinaFriendsInfo(friendsSelectParams);
		}
	}

	/**
	 * 更新新浪用户关注数据
	 */
	public void updateSinaFriendsInfo(String tablename, long username, String friendsuids, int friendscount,
			long lasttime) {
		FriendsInsertParams friendsInsertParams = new FriendsInsertParams(tablename, username, friendsuids,
				friendscount, lasttime);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaFriendsDao sinaFriendsDao = sqlSession.getMapper(SinaFriendsDao.class);
			sinaFriendsDao.updateSinaFriendsInfo(friendsInsertParams);
		}
	}

	/**
	 * 删除新浪用户关注数据
	 */
	public void deleteSinaFriendsInfo(String tablename, long username) {
		FriendsSelectParams friendsSelectParams = new FriendsSelectParams(tablename, username);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			SinaFriendsDao sinaFriendsDao = sqlSession.getMapper(SinaFriendsDao.class);
			sinaFriendsDao.deleteSinaFriendsInfo(friendsSelectParams);
		}
	}

}
