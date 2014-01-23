package cc.pp.sina.web.demo;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * 1、http://localhost:2222/bozhu/uid/trans
 * @author wgybzb
 *
 */
public class DemoApplication extends Application {

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws Exception {

		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 2222);
		component.getDefaultHost().attach("/bozhu", new DemoApplication());
		component.start();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/uid/trans", DemoResource.class);
		//		router.attach("/type/{type}/url/{url}", TransUidResource.class);
		return router;
	}

}
