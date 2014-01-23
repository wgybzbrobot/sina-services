package cc.pp.sina.web.server;

import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.web.application.ToolApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 示例：
 *     1、http://localhost:9999/bozhu/uid/trans/{identify}
 * @author wgybzb
 *
 */
public class ToolServer {

	private final Component component;
	private final ToolApplication application;

	public ToolServer() throws IOException {
		component = new Component();
		application = new ToolApplication();
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		try {
			ToolServer server = new ToolServer();
			server.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void start() {
		component.getServers().add(Protocol.HTTP, Integer.parseInt(Config.getProperty("api.port")));
		try {
			component.getDefaultHost().attach("/bozhu/uid", application);
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
