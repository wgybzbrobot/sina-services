package cc.pp.sina.dao.bozhus;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;

/**
 * 博主库数据层
 * @author wgybzb
 *
 */
public class BozhuLibrary {

	private static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			sqlSessionFactory = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.beijing);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取博主库中所有博主Uid
	 */
	public static List<Long> getBozhuUids() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			BozhuLibraryDao bozhuLibraryDao = sqlSession.getMapper(BozhuLibraryDao.class);
			return bozhuLibraryDao.getBozhuUids();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
