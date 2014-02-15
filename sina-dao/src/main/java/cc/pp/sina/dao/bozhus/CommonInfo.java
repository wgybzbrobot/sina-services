package cc.pp.sina.dao.bozhus;

import org.apache.ibatis.session.SqlSession;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.domain.params.BaseInfoParams;
import cc.pp.sina.domain.params.ExtendSelectParams;

public class CommonInfo {

//	private static Logger logger = LoggerFactory.getLogger(BozhuFansByUid.class);

	/**
	 * 新浪基础信息表
	 */
	private static final String SINA_USER_BASEINFO_TABLE = "sinauserbaseinfo_";

	/**
	 * 新浪扩展信息表
	 */
	private static final String SINA_USER_EXTEND_INFO = "sina_user_extend_info_";

	/**
	 * 获取用户基础信息
	 */
	public BozhuBaseInfo getBozhuBaseInfo(long uid) {
		BaseInfoParams params = new BaseInfoParams(SINA_USER_BASEINFO_TABLE + uid % 32, uid);
		try (SqlSession session = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.fenxi).openSession();) {
			CommonInfoDao bozhuFansDao = session.getMapper(CommonInfoDao.class);
			return bozhuFansDao.getUserBaseInfo(params);
		}
	}

	/**
	 * 获取用户的粉丝Uid列表
	 */
	public SimpleFansInfo getFansUids(long uid) {
		try (SqlSession session = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.fenxi).openSession();) {
			CommonInfoDao bozhuFansDao = session.getMapper(CommonInfoDao.class);
			return bozhuFansDao.getFansByUid(uid);
		}
	}

	/**
	 * 获取用户扩展数据
	 */
	public UserExtendInfo getExtendInfo(long uid) {
		ExtendSelectParams extendSelectParams = new ExtendSelectParams(SINA_USER_EXTEND_INFO + uid % 10, uid);
		try (SqlSession sqlSession = MybatisConfig.getSqlSessionFactory(MybatisConfig.ServerEnum.fenxi).openSession();) {
			CommonInfoDao commonInfoDao = sqlSession.getMapper(CommonInfoDao.class);
			return commonInfoDao.getExtendInfo(extendSelectParams);
		}
	}

}
