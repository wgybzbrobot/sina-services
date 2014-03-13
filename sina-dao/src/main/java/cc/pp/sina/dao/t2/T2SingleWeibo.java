package cc.pp.sina.dao.t2;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.t2.T2SingleWeiboInfo;

public class T2SingleWeibo {

	public static enum TYPE {
		sina, tencent
	};

	private static SqlSessionFactory sqlSessionFactory;

	public T2SingleWeibo(MybatisConfig.ServerEnum server) {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(server);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取待分析微博id
	 */
	public List<String> getNewWids(String type) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2SingleWeiboDao singleWeiboDao = sqlSession.getMapper(T2SingleWeiboDao.class);
			return singleWeiboDao.getNewWids(type);
		}
	}

	/**
	 * 插入单条微博信息
	 */
	public void insertSingleWeibo(String type, long wid, String url) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2SingleWeiboDao singleWeiboDao = sqlSession.getMapper(T2SingleWeiboDao.class);
			if (singleWeiboDao.selectSingleWeibo(wid) == null) {
				singleWeiboDao.insertSingleWeibo(new T2SingleWeiboInfo(type, wid, url, ""));
			} else {
				singleWeiboDao.updateSingleWeibo(new T2SingleWeiboInfo(type, wid, url, ""));
			}
		}
	}

	/**
	 * 更新单条微博分析结果
	 */
	public void updateSingleWeibo(String type, long wid, String url, String weiboresult) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2SingleWeiboDao singleWeiboDao = sqlSession.getMapper(T2SingleWeiboDao.class);
			singleWeiboDao.updateSingleWeibo(new T2SingleWeiboInfo(type, wid, url, weiboresult));
		}
	}

	/**
	 * 获取单条微博分析结果
	 */
	public T2SingleWeiboInfo selectSingleWeibo(long wid) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2SingleWeiboDao singleWeiboDao = sqlSession.getMapper(T2SingleWeiboDao.class);
			return singleWeiboDao.selectSingleWeibo(wid);
		}
	}

	/**
	 * 删除单条微博
	 */
	public void deleteSingleWeibo(long wid) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			T2SingleWeiboDao singleWeiboDao = sqlSession.getMapper(T2SingleWeiboDao.class);
			singleWeiboDao.deleteSingleWeibo(wid);
		}
	}

}
