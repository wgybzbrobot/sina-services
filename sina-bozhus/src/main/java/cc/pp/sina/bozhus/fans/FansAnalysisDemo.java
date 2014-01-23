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
import cc.pp.sina.utils.json.JsonUtils;

public class FansAnalysisDemo {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: <uid>");
			System.exit(-1);
		}

		long uid = Long.parseLong(args[0]);

		SqlSessionFactory sqlSessionFactory = null;
		try {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources
					.getResourceAsReader("mybatis-config.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sqlSessionFactory == null) {
			throw new RuntimeException();
		}

		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			FansAnalysisDao fansAnalysisDao = sqlSession.getMapper(FansAnalysisDao.class);
			// 粉丝Id列表
			System.err.println("粉丝Id列表: ");
			SimpleFansInfo simpleFansInfo = fansAnalysisDao.getFansByUid(uid);
			System.out.println(JsonUtils.toJson(simpleFansInfo));
			// 用户基础信息
			System.err.println("用户基础信息: ");
			BaseInfoParams baseInfoParams = new BaseInfoParams("sinauserbaseinfo_" + uid % 32, uid);
			BozhuBaseInfo bozhuBaseInfo = fansAnalysisDao.getUserBaseInfo(baseInfoParams);
			System.out.println(JsonUtils.toJson(bozhuBaseInfo));
			// 用户扩展信息
			System.err.println("用户扩展信息: ");
			ExtendSelectParams extendSelectParams = new ExtendSelectParams("sina_user_extend_info_" + uid % 10, uid);
			UserExtendInfo userExtendInfo = fansAnalysisDao.getExtendInfo(extendSelectParams);
			System.out.println(JsonUtils.toJson(userExtendInfo));
			// 用户粉丝增减信息
			System.err.println("用户粉丝增减信息: ");
			FansAddSelectParams fansAddSelectParams = new FansAddSelectParams("pp_sina_fans_daily_20140104", uid);
			FansAddDailyInfo fansAddDailyInfo = fansAnalysisDao.getFansAddDailyInfo(fansAddSelectParams);
			System.out.println(JsonUtils.toJson(fansAddDailyInfo));
		}

	}

}
