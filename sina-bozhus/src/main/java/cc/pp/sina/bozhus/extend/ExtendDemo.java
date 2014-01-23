package cc.pp.sina.bozhus.extend;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.domain.bozhus.UserTagWapper;
import cc.pp.sina.domain.params.ExtendInsertParams;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.json.JsonUtils;

public class ExtendDemo {

	public static void main(String[] args) {

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("mybatis-bozhus-config.xml"));

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00LGdiHCdcZIJC4c3f92c6cfYPjA8D");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);
		//		List<UserTag> tags = sinaUserInfoDao.getSinaUserTags(new Tags(), "1862087393", 20);
		List<UserTagWapper> tags = sinaUserInfoDao.getSinaUsersBatchTags("1862087393,1223", 20);
		System.err.println(JsonUtils.toJson(tags));

		ExtendInsertParams extendInsertParams = new ExtendInsertParams();
		extendInsertParams.setTablename("sina_user_extend_info_" + 1862087393 % 10);
		extendInsertParams.setUsername(1862087393L);
		extendInsertParams.setTags(JsonUtils.toJsonWithoutPretty(tags));
		extendInsertParams.setIsppuser(false);

		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
			UserExtendInfoDao userExtendInfoDao = sqlSession.getMapper(UserExtendInfoDao.class);
			userExtendInfoDao.insertExtendInfo(extendInsertParams);
		}

		//		ExtendSelectParams extendSelectParams = new ExtendSelectParams();
		//		extendSelectParams.setTablename("sina_user_extend_info_" + 1862087393 % 10);
		//		extendSelectParams.setUsername(1862087393L);
		//		try (SqlSession sqlSession = sqlSessionFactory.openSession();) {
		//			UserExtendInfoDao userExtendInfoDao = sqlSession.getMapper(UserExtendInfoDao.class);
		//			UserExtendInfo userExtendInfo = userExtendInfoDao.selectExtendInfo(extendSelectParams);
		//			System.out.println(userExtendInfo.getTags());
		//		}

	}

}
