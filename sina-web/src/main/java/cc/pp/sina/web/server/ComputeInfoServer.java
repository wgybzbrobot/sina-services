package cc.pp.sina.web.server;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.web.application.ComputeInfoApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 示例：
 *      1、粉丝分析数据：http://localhost:1111/sina/compute/uid/{uid}/fans
 * @author wgybzb
 *
 */
public class ComputeInfoServer {

	private final Component component;
	private final ComputeInfoApplication application;

	public ComputeInfoServer() {
		component = new Component();
		application = new ComputeInfoApplication();
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		ComputeInfoServer server = new ComputeInfoServer();
		server.start();
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
			component.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
