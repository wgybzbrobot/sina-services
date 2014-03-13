package cc.pp.content.library.web;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用示例：http://localhost:2222/content/statistic/recordtype/sendtime/date/20131219
 * @author wgybzb
 *
 */
public class ContentApplication extends Application {

	private static Logger logger = LoggerFactory.getLogger(ContentApplication.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: ContentApplication <port>");
			System.exit(-1);
		}

		Component component = new Component();
		component.getServers().add(Protocol.HTTP, Integer.parseInt(args[0]));
		try {
			component.getDefaultHost().attach("/content", new ContentApplication());
			component.start();
		} catch (Exception e) {
			logger.info("Component start unsucessful.");
			e.printStackTrace();
		}
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/statistic/recordtype/{recordtype}/date/{date}", ContentResource.class);
		return router;
	}

}
