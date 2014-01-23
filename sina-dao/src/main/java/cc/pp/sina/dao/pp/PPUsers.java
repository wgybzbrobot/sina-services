package cc.pp.sina.dao.pp;

import java.util.HashSet;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;

public class PPUsers {

	private static SqlSessionFactory sqlSessionFactory;

	static {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.pp);
	}

	/**
	 * 获取皮皮用户Uid
	 */
	public static HashSet<Long> getPPUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			PPUsersDao ppUsersDao = sqlSession.getMapper(PPUsersDao.class);
			List<Long> uids = ppUsersDao.getPPUids();
			HashSet<Long> result = new HashSet<>();
			for (long uid : uids) {
				result.add(uid);
			}
			return result;
		}
	}

	/**
	 * 获取当前皮皮用户Uid
	 */
	public static HashSet<Long> getPPUidsNow(long lasttime) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			PPUsersDao ppUsersDao = sqlSession.getMapper(PPUsersDao.class);
			List<Long> uids = ppUsersDao.getPPUidsNow(lasttime);
			HashSet<Long> result = new HashSet<>();
			for (long uid : uids) {
				result.add(uid);
			}
			return result;
		}
	}

}
