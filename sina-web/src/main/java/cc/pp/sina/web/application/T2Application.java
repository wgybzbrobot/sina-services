package cc.pp.sina.web.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2Interactions;
import cc.pp.sina.dao.t2.T2SingleWeibo;
import cc.pp.sina.dao.t2.T2UserFans;
import cc.pp.sina.web.resource.t2.T2SingleWeiboResource;
import cc.pp.sina.web.resource.t2.T2UsersResource;

public class T2Application extends Application {

	private final T2UserFans userFans;
	private final T2SingleWeibo singleWeibo;
	private final T2Interactions interactions;

	public T2Application() {
		this.userFans = new T2UserFans(MybatisConfig.ServerEnum.fenxi);
		this.singleWeibo = new T2SingleWeibo(MybatisConfig.ServerEnum.fenxi);
		this.interactions = new T2Interactions(MybatisConfig.ServerEnum.fenxi);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/type/{type}/uids/post", T2UsersResource.class);
		router.attach("/type/{type}/uid/{uid}/get", T2UsersResource.class);
		router.attach("/type/{type}/urls/post", T2SingleWeiboResource.class);
		router.attach("/type/{type}/url/{url}/get", T2SingleWeiboResource.class);
		return router;
	}

	public T2UserFans getUserFans() {
		return userFans;
	}

	public T2SingleWeibo getSingleWeibo() {
		return singleWeibo;
	}

	public T2Interactions getInteractions() {
		return interactions;
	}

}
