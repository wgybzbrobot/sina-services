package cc.pp.sina.solr.index;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.users.UserInfo;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;

/**
 * 索引新浪用户基础数据
 * @author wgybzb
 *
 */
public class IndexSinaUsersMain {

	private static Logger logger = LoggerFactory.getLogger(IndexSinaUsersMain.class);

	private static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";
	private static final int FETCH_SIZE = 10_0000;

	private static final String ZOOKEEPER_CLOUD = "wuhu001:2181,wuhu005:2181,wuhu009:2181,wuhu013:2181,wuhu017:2181";
	private static final String COLLECTION_NAME = "sina_users";

	private CloudSolrServer cloudServer;

	public IndexSinaUsersMain() {
		try {
			cloudServer = new CloudSolrServer(ZOOKEEPER_CLOUD);
			cloudServer.setDefaultCollection(COLLECTION_NAME);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		IndexSinaUsersMain indexUsers = new IndexSinaUsersMain();

		for (int t = 0; t < 32; t++) {
			String tablename = SINA_USER_BASEINFO + t;
			logger.info("Read table: " + tablename);
			int maxBid = SinaUsers.getMaxBid(tablename);
			List<UserInfo> users = null;
			for (int i = 0; i < maxBid / FETCH_SIZE; i++) {
				users = SinaUsers.getSinaUserInfos(tablename, i * FETCH_SIZE + 1, (i + 1) * FETCH_SIZE);
				logger.info("Read table: " + tablename + ", at: " + i + ",size=" + users.size());
				if (!pool.isShutdown()) {
					pool.execute(new IndexSinaUsersRun(indexUsers.getCloudServer(), users));
				}
			}
			users = SinaUsers.getSinaUserInfos(tablename, (maxBid / FETCH_SIZE) * FETCH_SIZE + 1, maxBid);
			logger.info("Read table: " + tablename + ", at: " + maxBid / FETCH_SIZE + ",size=" + users.size());
			if (!pool.isShutdown()) {
				pool.execute(new IndexSinaUsersRun(indexUsers.getCloudServer(), users));
			}
		}

		pool.shutdown();
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
			Thread.sleep(1000 * 60 * 5);
			indexUsers.close();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public CloudSolrServer getCloudServer() {
		return cloudServer;
	}

	public void close() {
		cloudServer.shutdown();
	}

}
