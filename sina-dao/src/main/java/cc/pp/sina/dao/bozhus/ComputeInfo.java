package cc.pp.sina.dao.bozhus;

import org.apache.ibatis.session.SqlSession;

import cc.pp.sina.dao.common.MybatisConfig;

public class ComputeInfo {

	/**
	 * 新浪用户粉丝分析结果数据
	 */
	public String getFansAnalysisResult(long uid) {
		try (SqlSession sqlSession = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.fenxi).openSession();) {
			ComputeInfoDao computeResultDao = sqlSession.getMapper(ComputeInfoDao.class);
			return computeResultDao.getFansAnalysisResult(uid);
		}
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {
		ComputeInfo computeInfo = new ComputeInfo();
		System.out.println(computeInfo.getFansAnalysisResult(3283013665L));
	}

}
