package cc.pp.sina.web.server;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.web.application.T2Application;
import cc.pp.sina.web.common.ServerUtils;
import cc.pp.sina.web.conf.Config;

/**
 * T2相关数据服务
 *
 * 示例：
 *         1、http://localhost:4321/t2/ type/{type}/uids/post
 *              ["1835127861","1097201945","1904746890"]
 *              ["herky828","zsuzjl328","shifang"]
 *         2、http://localhost:4321/t2/type/{type}/uid/{uid}/get
 *         3、http://localhost:4321/t2/type/{type}/urls/post
 *              ["http://weibo.com/1890124614/AAhIcy70n?ref=home","http://weibo.com/2803301701/AAfjzkRzu","http://weibo.com/1642634100/AAi9mxQRn"]
 *              ["http://t.qq.com/p/t/378906098439934?apiType=14","http://t.qq.com/p/t/239649013587968?apiType=14","http://t.qq.com/p/t/387378005294018?apiType=14"]
 *         4、http://localhost:4321/t2/type/{type}/url/{url}/get
 *
 * @author wgybzb
 *
 */
public class T2Server {

	private final Component component;
	private final T2Application application;

	public T2Server() {
		component = new Component();
		application = new T2Application();
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		T2Server server = new T2Server();
		server.start();
	}

	public void start() {
		component.getServers().add(Protocol.HTTP, Integer.parseInt(Config.getProperty("api.port")));
		try {
			component.getDefaultHost().attach("/t2", application);
			ServerUtils.configureJacksonConverter();
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
