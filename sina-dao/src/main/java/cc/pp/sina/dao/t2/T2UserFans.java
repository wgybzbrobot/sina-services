package cc.pp.sina.dao.t2;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.t2.T2SinaUserInfo;
import cc.pp.sina.domain.t2.T2TencentUserInfo;

public class T2UserFans {

	private static SqlSessionFactory sqlSessionFactory;

	public T2UserFans(MybatisConfig.ServerEnum server) {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(server);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 新浪获取待分析uid
	 */
	public List<String> getSinaNewUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			return userDao.getSinaNewUids();
		}
	}

	public List<String> getSinaAllUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			return userDao.getSinaAllUids();
		}
	}

	/**
	 * 新浪插入用户信息
	 */
	public void insertT2SinaUser(long username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			if (userDao.selectT2SinaUser(username) == null) {
				userDao.insertT2SinaUser(username);
			} else {
				//				userDao.updateT2SinaUser(new T2SinaUserInfo(username, ""));
			}
		}
	}

	/**
	 * 新浪更新分析结果
	 */
	public void updateT2SinaUser(long username, String fansresult) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			userDao.updateT2SinaUser(new T2SinaUserInfo(username, fansresult));
		}
	}

	/**
	 * 新浪获取分析结果
	 */
	public T2SinaUserInfo selectT2SinaUser(long username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			return userDao.selectT2SinaUser(username);
		}
	}

	/**
	 * 新浪删除用户
	 */
	public void deleteT2SinaUser(long username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			userDao.deleteT2SinaUser(username);
		}
	}

	/**
	 * 腾讯插入用户信息
	 */
	public void insertT2TencentUser(String username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			if (userDao.selectT2TencentUser(username) == null) {
				userDao.insertT2TencentUser(username);
			} else {
				//				userDao.updateT2TencentUser(new T2TencentUserInfo(username, ""));
			}
		}
	}

	/**
	 * 腾讯获取待分析uid
	 */
	public List<String> getTencentNewUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			return userDao.getTencentNewUids();
		}
	}

	public List<String> getTencentAllUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			return userDao.getTencentAllUids();
		}
	}

	/**
	 * 腾讯更新分析结果
	 */
	public void updateT2TencentUser(String username, String fansresult) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			userDao.updateT2TencentUser(new T2TencentUserInfo(username, fansresult));
		}
	}

	/**
	 * 腾讯获取分析结果
	 */
	public T2TencentUserInfo selectT2TencentUser(String username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			return userDao.selectT2TencentUser(username);
		}
	}

	/**
	 * 腾讯删除用户
	 */
	public void deleteT2TencentUser(String username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2UserFansDao userDao = sqlSession.getMapper(T2UserFansDao.class);
			userDao.deleteT2TencentUser(username);
		}
	}

}
