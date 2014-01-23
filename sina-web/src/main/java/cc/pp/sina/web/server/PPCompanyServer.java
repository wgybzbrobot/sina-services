package cc.pp.sina.web.server;

import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.web.application.PPCompanyApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 皮皮企业用户查询
 * 示例：
 *    1、http://localhost:999/sina/company/type/{type}/month/{month}/baseinfo?keywords=XXX&page=XXX
 */
public class PPCompanyServer {

	private final Component component;
	private final PPCompanyApplication application;

	public PPCompanyServer(String indexDir) throws IOException {
		component = new Component();
		application = new PPCompanyApplication(indexDir);
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		try {
			PPCompanyServer server = new PPCompanyServer(args[0]);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		component.getServers().add(Protocol.HTTP, Integer.parseInt(Config.getProperty("api.port")));
		try {
			component.getDefaultHost().attach("/sina", application);
			component.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		try {
			application.close();
			component.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
