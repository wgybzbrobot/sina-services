package cc.pp.sina.web.server;

import java.io.IOException;

import org.restlet.Component;
import org.restlet.data.Protocol;

import cc.pp.sina.query.query.BozhuQuery;
import cc.pp.sina.web.application.AnalysisInfoApplication;
import cc.pp.sina.web.bozhu.AnalysisInfo;
import cc.pp.sina.web.conf.Config;

/**
 * 示例：
 *    1、http://localhost:8111/bozhus/info/1000051161,1000305284,1000150803
 *    2、http://localhost:8111/bozhus/collect/1000051161,1000305284,1000150803
 *    3、http://localhost:8111/bozhus/query/bozhu?keywords=商业娱乐美食&influence=70-100&activation=6-10&fanscount=50000-1000000&verify=0&page=1
 *    4、http://localhost:8111/bozhus/query/fans?fansage=20-35&gender=0&provinces=北京上海&fanstags=娱乐美食&page=1
 */
public class AnalysisInfoServer {

	private final Component component;
	private final AnalysisInfoApplication application;

	public AnalysisInfoServer(AnalysisInfo bozhusInfoByUid, BozhuQuery bozhuQuery) {
		component = new Component();
		application = new AnalysisInfoApplication(bozhusInfoByUid, bozhuQuery);
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		AnalysisInfo analysisInfo = new AnalysisInfo();
		try {
			BozhuQuery bozhuQuery = new BozhuQuery();
			AnalysisInfoServer server = new AnalysisInfoServer(analysisInfo, bozhuQuery);
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
