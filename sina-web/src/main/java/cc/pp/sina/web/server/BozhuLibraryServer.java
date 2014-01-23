package cc.pp.sina.web.server;

import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.query.query.BozhuQuery;
import cc.pp.sina.web.application.BozhuLibraryApplication;
import cc.pp.sina.web.bozhu.AnalysisInfo;
import cc.pp.sina.web.conf.Config;

/**
 *  博主库数据服务
 * @author wgybzb
 * 示例：
 *    1、http://114.112.65.13:8111/sina/users/1000051161,1000305284,1000150803/info
 *    2、http://114.112.65.13:8111/sina/users/1000051161,1000305284,1000150803/collect
 *    3、http://114.112.65.13:8111/sina/users/bozhu?keywords=商业娱乐美食&influence=70-100&activation=6-10&fanscount=50000-1000000&verify=0&page=1
 *    4、http://114.112.65.13:8111/sina/users/fans?fansage=20-35&gender=0&provinces=北京上海&fanstags=娱乐美食&page=1
 */
public class BozhuLibraryServer {

	private final Component component;
	private final BozhuLibraryApplication application;

	public BozhuLibraryServer(AnalysisInfo bozhusInfoByUid, BozhuQuery bozhuQuery) {
		component = new Component();
		application = new BozhuLibraryApplication(bozhusInfoByUid, bozhuQuery);
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		AnalysisInfo analysisInfo = new AnalysisInfo();
		try {
			BozhuQuery bozhuQuery = new BozhuQuery();
			BozhuLibraryServer server = new BozhuLibraryServer(analysisInfo, bozhuQuery);
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
			application.closed();
			component.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
