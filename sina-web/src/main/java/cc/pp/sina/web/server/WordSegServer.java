package cc.pp.sina.web.server;

import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.web.application.WordSegApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 示例：http://localhost:2222/wordseg/text/{text}
 * @author wgybzb
 *
 */
public class WordSegServer {

	private final Component component;
	private final WordSegApplication application;

	public WordSegServer() throws IOException {
		component = new Component();
		application = new WordSegApplication();
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		try {
			WordSegServer server = new WordSegServer();
			server.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		component.getServers().add(Protocol.HTTP, Integer.parseInt(Config.getProperty("api.port")));
		try {
			component.getDefaultHost().attach("/wordseg", application);
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
