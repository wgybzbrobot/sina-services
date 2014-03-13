package cc.pp.sina.dao.t2;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.t2.SinaSelectParams;
import cc.pp.sina.domain.t2.T2SinaInteractionsInfo;
import cc.pp.sina.domain.t2.T2TencentInteractionsInfo;
import cc.pp.sina.domain.t2.TencentSelectParams;

public class T2Interactions {

	private static SqlSessionFactory sqlSessionFactory;

	public T2Interactions(MybatisConfig.ServerEnum server) {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(server);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 新浪插入互动信息
	 */
	public void insertT2SinaInteractions(long username, int date, int fanscount, int allcount, String emotionratio) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			interactionsDao.insertT2SinaInteractions(new T2SinaInteractionsInfo(username, date, fanscount, allcount,
					emotionratio));
		}
	}

	/**
	 * 新浪获取分析结果
	 */
	public T2SinaInteractionsInfo selectT2SinaInteractions(long username, int date) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			return interactionsDao.selectT2SinaInteractions(new T2SinaInteractionsInfo(username, date, 0, 0, ""));
		}
	}

	public List<T2SinaInteractionsInfo> selectT2SinaInteractionsList(long username, int maxnum) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			return interactionsDao.selectT2SinaInteractionsList(new SinaSelectParams(username, maxnum));
		}
	}

	/**
	 * 新浪删除互动信息
	 */
	public void deleteT2SinaInteractions(long username, int date) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			interactionsDao.deleteT2SinaInteractions(new T2SinaInteractionsInfo(username, date, 0, 0, ""));
		}
	}

	/**
	 * 腾讯插入互动信息
	 */
	public void insertT2TencentInteractions(String username, int date, int fanscount, int allcount, String emotionratio) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			interactionsDao.insertT2TencentInteractions(new T2TencentInteractionsInfo(username, date, fanscount,
					allcount, emotionratio));
		}
	}

	/**
	 * 腾讯获取互动信息
	 */
	public T2TencentInteractionsInfo selectT2TencentInteractions(String username, int date) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			return interactionsDao.selectT2TencentInteractions(new T2TencentInteractionsInfo(username, date, 0, 0, ""));
		}
	}

	public List<T2TencentInteractionsInfo> selectT2TencentInteractionsList(String username, int maxnum) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			return interactionsDao.selectT2TencentInteractionsList(new TencentSelectParams(username, maxnum));
		}
	}

	/**
	 * 腾讯删除互动信息
	 */
	public void deleteT2TencentInteractions(String username, int date) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2InteractionsDao interactionsDao = sqlSession.getMapper(T2InteractionsDao.class);
			interactionsDao.deleteT2TencentInteractions(new T2TencentInteractionsInfo(username, date, 0, 0, ""));
		}
	}

}
