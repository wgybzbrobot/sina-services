package cc.pp.sina.dao.tool;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.tool.TransUidInfo;
import cc.pp.sina.domain.tool.TransUidInsertParams;
import cc.pp.sina.domain.tool.TransUidSelectParams;

public class TransUid {

	private static SqlSessionFactory sqlSessionFactory;

	static {
		sqlSessionFactory = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.fenxi);
	}

	/**
	 * 插入转换数据
	 */
	public static void insertTransUidInfo(String tablename, String identify, String url, String username,
			String nickname) {
		TransUidInsertParams transUidInsertParams = new TransUidInsertParams(tablename, identify, url, username,
				nickname);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			TransUidDao transUidDao = sqlSession.getMapper(TransUidDao.class);
			transUidDao.insertTransUidInfo(transUidInsertParams);
		}
	}

	/**
	 * 读取转换数据
	 */
	public static List<TransUidInfo> getTransUidInfos(String tablename, String identify) {
		TransUidSelectParams transUidSelectParams = new TransUidSelectParams(tablename, identify);
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			TransUidDao transUidDao = sqlSession.getMapper(TransUidDao.class);
			return transUidDao.getTransUidInfos(transUidSelectParams);
		}
	}

}
