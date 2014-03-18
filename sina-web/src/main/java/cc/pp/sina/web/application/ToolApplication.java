package cc.pp.sina.web.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.tool.TransUid;
import cc.pp.sina.domain.tool.TransUidInfo;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;
import cc.pp.sina.web.resource.ConvertResource;
import cc.pp.sina.web.resource.ConvertToUid;

import com.sina.weibo.model.User;

public class ToolApplication extends Application {

	private static Logger logger = LoggerFactory.getLogger(AnalysisInfoApplication.class);

	public static final String TRANS_UID_PATH = "transuidpath/";
	public static final String TRANS_UID_DATA = "trans_uid_data";
	public static final String TRANS_UID_RESULT = "trans_uid_result";
	private final ConvertToUid convertToUid;

	public ToolApplication() {
		new File(TRANS_UID_PATH).mkdir();
		convertToUid = new ConvertToUid();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		//		router.attach("/trans", TransUidResource.class);
		//		router.attach("/trans/{identify}", TransUidResource.class);
		router.attach("/trans/{url}", ConvertResource.class);
		return router;
	}

	/**
	 * 获取转换数据
	 */
	public List<TransUidInfo> getTransInfosByIdentify(String identify) {
		return TransUid.getTransUidInfos(TRANS_UID_RESULT, identify);
	}

	/**
	 * 批量转换，多线程
	 */
	public void batchTrans(String identify, List<HashMap<String, String>> data) {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		/**
		 * 写到本地缓存
		 */
		try (FileWriter writer = new FileWriter(TRANS_UID_PATH + TRANS_UID_DATA + "_" + identify, true);) {
			for (HashMap<String, String> temp : data) {
				writer.append(temp.get("url")).append("\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (HashMap<String, String> temp : data) {
			if (!pool.isShutdown()) {
				logger.info("identify: type=" + temp.get("type") + ",url=" + temp.get("url"));
				pool.execute(new BatchTransRun(identify, temp.get("type"), temp.get("url")));
			} else {
				logger.error("Pool is shutdown.");
			}
		}

		pool.shutdown();
		try {
			pool.awaitTermination(60, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批量计算http://weibo.com/u/
	 */
	public class BatchTransRun implements Runnable {

		private final String type;
		private final String url;
		private final String identify;

		public BatchTransRun(String identify, String type, String url) {
			this.identify = identify;
			this.type = type;
			this.url = url;
		}

		@Override
		public void run() {
			try {
				dumpLocalFile(identify, type, url);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void dumpLocalFile(String identify, String type, String url) throws IOException {

		if ("domain".equalsIgnoreCase(type)) {
			User user = getDidToUser(url);
			if (user != null) {
				try (FileWriter writer = new FileWriter(TRANS_UID_PATH + TRANS_UID_RESULT + "_" + identify, true)) {
					writer.append(url + "," + user.getId() + "," + user.getScreenName()).append("\n");
				}
			} else {
				try (FileWriter writer = new FileWriter(TRANS_UID_PATH + TRANS_UID_RESULT + "_" + identify, true)) {
					writer.append(url + ",0,0").append("\n");
				}
			}
		} else if ("nickname".equalsIgnoreCase(type)) {
			User user = getNidToUser(url);
			if (user != null) {
				try (FileWriter writer = new FileWriter(TRANS_UID_PATH + TRANS_UID_RESULT + "_" + identify, true)) {
					writer.append(url + "," + user.getId() + "," + user.getScreenName()).append("\n");
				}
			} else {
				try (FileWriter writer = new FileWriter(TRANS_UID_PATH + TRANS_UID_RESULT + "_" + identify, true)) {
					writer.append(url + ",0,0").append("\n");
				}
			}
		} else {
			//			try (FileWriter writer = new FileWriter(TRANS_UID_PATH + TRANS_UID_RESULT + "_" + identify, true)) {
			//				writer.append("empty").append("\n");
			//			}
		}

	}

	/**
	 * 新浪用户域名转换成User
	 */
	public User getDidToUser(String url) {
		return convertToUid.convertDomainToUser(url);
	}

	/**
	 * 新浪用户昵称转换成User
	 */
	public User getNidToUser(String url) {
		return convertToUid.convertNickToUser(url);
	}

}
