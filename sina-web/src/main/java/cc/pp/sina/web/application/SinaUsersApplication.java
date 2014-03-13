package cc.pp.sina.web.application;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.dao.bozhus.CommonInfo;
import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.web.resource.BaseInfoGetResource;
import cc.pp.sina.web.resource.BaseInfoResource;

public class SinaUsersApplication extends Application {

	private final CommonInfo commonInfo;
	private final SinaUserInfoDao sinaUserInfoDao;
	private final SinaUsers sinaUsers;

	public SinaUsersApplication() {
		commonInfo = new CommonInfo();
		sinaUserInfoDao = new SinaUserInfoDaoImpl(new TokenService());
		//		sinaUserInfoDao = new SinaUserInfoDaoImpl(null);
		sinaUsers = new SinaUsers(MybatisConfig.ServerEnum.fenxi);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		//		router.attach("/users", AnalysisResource.class);
		//		router.attach("/query/bozhu", QueryByBozhuResource.class);
		//		router.attach("/query/fans", QueryByFansResource.class);
		//		router.attach("/users/{uid}", AnalysisResource.class);

		// 新浪用户基础信息
		router.attach("/users/{uid}/basic", BaseInfoResource.class);
		router.attach("/users/{uid}/fromapi", BaseInfoGetResource.class);

		//		router.attach("/users/{uid}/extend", AnalysisResource.class);
		//		router.attach("/users/{uid}/analysis", AnalysisResource.class);
		//		router.attach("/users/{uid}/library", AnalysisResource.class);
		//		router.attach("/uids/{uids}/collect", AnalysisCollectedResource.class);

		//		router.attach("/users/{uid}/price/sources", UserPriceSourcesResource.class);
		//		router.attach("/users/{uid}/price/sources/{sourceId}", UserPriceSourceResource.class);
		//		router.attach("/users/{uid}/price/sources/{sourceId}/types", UserPriceTypesResource.class);

		return router;
	}

	/**
	 * 获取基础信息
	 */
	public BozhuBaseInfo getBaseInfo(long uid) {
		return commonInfo.getBozhuBaseInfo(uid);
	}

	/**
	 * 批量获取基础信息
	 */
	public List<BozhuBaseInfo> getBatchBaseInfos(String[] uids) {

		List<BozhuBaseInfo> result = new ArrayList<>();
		for (String uid : uids) {
			BozhuBaseInfo temp = getBaseInfo(Long.parseLong(uid));
			if (temp != null) {
				result.add(temp);
			}
		}

		return result;
	}

	/**
	 * 获取扩展数据
	 */
	public UserExtendInfo getBozhuExtendInfo(long uid) {
		return commonInfo.getExtendInfo(uid);
	}

	public SinaUserInfoDao getSinaUserInfoDao() {
		return sinaUserInfoDao;
	}

	public SinaUsers getSinaUsers() {
		return sinaUsers;
	}

}
