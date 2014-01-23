package cc.pp.sina.web.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cc.pp.sina.web.resource.AnalysisCollectedResource;
import cc.pp.sina.web.resource.AnalysisResource;
import cc.pp.sina.web.resource.PriceSourceResource;
import cc.pp.sina.web.resource.PriceSourcesResource;
import cc.pp.sina.web.resource.PriceTypesResource;

public class SinaUsersApplication extends Application {

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/users", AnalysisResource.class);
		//		router.attach("/query/bozhu", QueryByBozhuResource.class);
		//		router.attach("/query/fans", QueryByFansResource.class);
		router.attach("/users/{uid}", AnalysisResource.class);
		router.attach("/users/{uid}/basic", AnalysisResource.class);
		router.attach("/users/{uid}/extend", AnalysisResource.class);
		router.attach("/users/{uid}/analysis", AnalysisResource.class);
		router.attach("/users/{uid}/library", AnalysisResource.class);
		router.attach("/uids/{uids}/collect", AnalysisCollectedResource.class);

		router.attach("/users/{uid}/price/sources", PriceSourcesResource.class);
		router.attach("/users/{uid}/price/sources/{sourceId}", PriceSourceResource.class);
		router.attach("/users/{uid}/price/sources/{sourceId}/types", PriceTypesResource.class);

		return router;
	}

}
