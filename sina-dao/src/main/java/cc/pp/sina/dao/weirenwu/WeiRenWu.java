package cc.pp.sina.dao.weirenwu;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.weirenwu.BozhuExtendInfo;
import cc.pp.sina.domain.weirenwu.SelectBozhuParams;

public class WeiRenWu {

	private static SqlSessionFactory sqlSessionFactory;

	public WeiRenWu(MybatisConfig.ServerEnum server) {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(server);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 插入博主uid
	 */
	public void insertSinaBozhuUid(String username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			WeiRenWuDao weiRenWuDao = sqlSession.getMapper(WeiRenWuDao.class);
			weiRenWuDao.insertSinaBozhuUid(username);
			sqlSession.commit();
		}
	}

	/**
	 * 获取所有博主uid
	 */
	public List<String> selectAllSinaBozhuUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			WeiRenWuDao weiRenWuDao = sqlSession.getMapper(WeiRenWuDao.class);
			return weiRenWuDao.selectAllSinaBozhuUids();
		}
	}

	/**
	 * 根据uid查找bzid
	 */
	public long selectSinaBzid(String username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			WeiRenWuDao weiRenWuDao = sqlSession.getMapper(WeiRenWuDao.class);
			return weiRenWuDao.selectSinaBzid(username);
		}
	}

	/**
	 * 插入博主计算数据
	 */
	public void insertSinaBozhuExtendInfo(BozhuExtendInfo bozhuExtendInfo) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			WeiRenWuDao weiRenWuDao = sqlSession.getMapper(WeiRenWuDao.class);
			weiRenWuDao.insertSinaBozhuExtendInfo(bozhuExtendInfo);
			sqlSession.commit();
		}
	}

	/**
	 * 获取博主计算数据，根据uid
	 */
	public BozhuExtendInfo selectSinaBozhuExtendInfo(String username) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			WeiRenWuDao weiRenWuDao = sqlSession.getMapper(WeiRenWuDao.class);
			return weiRenWuDao.selectSinaBozhuExtendInfo(weiRenWuDao.selectSinaBzid(username));
		}
	}

	/**
	 * 获取博主计算数据，根据bzid范围
	 */
	public List<BozhuExtendInfo> selectSinaBozhuExtendInfos(SelectBozhuParams selectBozhuParams) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			WeiRenWuDao weiRenWuDao = sqlSession.getMapper(WeiRenWuDao.class);
			return weiRenWuDao.selectSinaBozhuExtendInfos(selectBozhuParams);
		}
	}

}
