package cc.pp.sina.bozhus.fans;

import java.io.IOException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.FansAddDailyInfo;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.domain.params.BaseInfoParams;
import cc.pp.sina.domain.params.ExtendSelectParams;
import cc.pp.sina.domain.params.FansAddSelectParams;
import cc.pp.sina.domain.params.FansAnalysisInsertParams;
import cc.pp.sina.utils.time.DailyUtils;

public class FansAnalysisInfoUtils {

	/**
	 * 新浪基础信息表
	 */
	private static final String SINA_USER_BASEINFO_TABLE = "sinauserbaseinfo_";

	/**
	 * 新浪扩展信息表
	 */
	private static final String SINA_USER_EXTEND_INFO = "sina_user_extend_info_";

	/**
	 * 皮皮粉丝日增减数据表
	 */
	private static final String PP_SINA_FANS_DAILY = "pp_sina_fans_daily_";

	/**
	 * 皮皮粉丝分析结果数据表
	 */
	public static final String PP_SINA_FANS_ANALYSIS = "pp_sina_fans_analysis_result";


	private static SqlSessionFactory sqlSessionFactory;

	static {
		try {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources
					.getResourceAsReader("mybatis--bozhus-config.xml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *  粉丝Id列表
	 */
	public static SimpleFansInfo getPPFansByUid(long uid) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FansAnalysisDao fansAnalysisDao = sqlSession.getMapper(FansAnalysisDao.class);
			return fansAnalysisDao.getFansByUid(uid);
		}
	}

	/**
	 *  用户基础信息
	 */
	public static BozhuBaseInfo getUserBaseInfo(long uid) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FansAnalysisDao fansAnalysisDao = sqlSession.getMapper(FansAnalysisDao.class);
			BaseInfoParams baseInfoParams = new BaseInfoParams(SINA_USER_BASEINFO_TABLE + uid % 32, uid);
			return fansAnalysisDao.getUserBaseInfo(baseInfoParams);
		}
	}

	/**
	 *  用户扩展信息
	 */
	public static UserExtendInfo getExtendInfo(long uid) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FansAnalysisDao fansAnalysisDao = sqlSession.getMapper(FansAnalysisDao.class);
			ExtendSelectParams extendSelectParams = new ExtendSelectParams(SINA_USER_EXTEND_INFO + uid % 10, uid);
			return fansAnalysisDao.getExtendInfo(extendSelectParams);
		}
	}

	/**
	 * 用户粉丝增减信息
	 */
	public static FansAddDailyInfo getFansAddDailyInfo(long uid, int NDays) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FansAnalysisDao fansAnalysisDao = sqlSession.getMapper(FansAnalysisDao.class);
			FansAddSelectParams fansAddSelectParams = new FansAddSelectParams(PP_SINA_FANS_DAILY
					+ DailyUtils.getSomeDayDate(NDays), uid);
			return fansAnalysisDao.getFansAddDailyInfo(fansAddSelectParams);
		}
	}

	/**
	 * 插入粉丝分析结果数据
	 */
	public static void intsertFansAnalysisResult(long uid, String result) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FansAnalysisDao fansAnalysisDao = sqlSession.getMapper(FansAnalysisDao.class);
			FansAnalysisInsertParams faip = new FansAnalysisInsertParams(PP_SINA_FANS_ANALYSIS, uid, result,
					System.currentTimeMillis() / 1000);
			fansAnalysisDao.insertFansAnalysisResult(faip);
		}
	}

}
