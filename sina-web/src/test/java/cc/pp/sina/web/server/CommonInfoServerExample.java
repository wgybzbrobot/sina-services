package cc.pp.sina.web.server;

import java.io.IOException;

/**
 * Created by chenwei@pp.cc on 14-1-12.
 */
public class CommonInfoServerExample {

	public static void main(String[] args) throws IOException {
		System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");
		CommonInfoServer server = new CommonInfoServer();
		server.start();
		System.in.read();
		server.stop();
	}

}
