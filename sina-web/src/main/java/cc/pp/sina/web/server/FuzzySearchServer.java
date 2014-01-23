package cc.pp.sina.web.server;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.FSDirectory;
import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.web.application.FuzzySearchApplication;
import cc.pp.sina.web.conf.Config;

/**
 * 写数据：http://localhost:8111/fuzzy/index/id/1/text/测试数据
 * 查数据：http://localhost:8111/fuzzy/query/text/测试
 * @author wgybzb
 *
 */
public class FuzzySearchServer {

	private final Component component;
	private final FuzzySearchApplication application;

	private static final String INDEX_DIR = "weixin_index_data";

	public FuzzySearchServer() throws IOException {
		component = new Component();
		application = new FuzzySearchApplication(FSDirectory.open(new File(INDEX_DIR)));
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		FuzzySearchServer server;
		try {
			server = new FuzzySearchServer();
			server.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		component.getServers().add(Protocol.HTTP, Integer.parseInt(Config.getProperty("api.port")));
		try {
			component.getDefaultHost().attach("/fuzzy", application);
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
