package cc.pp.sina.solr.index;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.pp.PPUsers;
import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.users.UserInfo;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;

public class IndexPPUsersMain {

	private static Logger logger = LoggerFactory.getLogger(IndexPPUsersMain.class);

	private static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";
	private static final int FETCH_SIZE = 1_000;

	private static final String ZOOKEEPER_CLOUD = "wuhu001:2181,wuhu005:2181,wuhu009:2181,wuhu013:2181,wuhu017:2181";
	private static final String COLLECTION_NAME = "sina_users";

	private CloudSolrServer cloudServer;

	public IndexPPUsersMain() {
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

		IndexPPUsersMain indexUsers = new IndexPPUsersMain();

		HashSet<Long> uids = PPUsers.getPPUidsNow(System.currentTimeMillis() / 1000);
		logger.info("PP Users' size=" + uids.size());

		int count = 0;
		for (long uid : uids) {
			count++;
			if (count % FETCH_SIZE == 0) {
				logger.info("Read at: " + count);
				indexUsers.commit();
			}
			if (!pool.isShutdown()) {
				pool.execute(new IndexPPUsersRun(indexUsers, uid));
			} else {
				logger.error("Pool is shutdown!");
			}
		}
		indexUsers.commit();

		pool.shutdown();
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
			Thread.sleep(1000 * 60 * 5);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		indexUsers.close();
	}

	/**
	 * 索引皮皮用户数据
	 * @author wgybzb
	 *
	 */
	public static class IndexPPUsersRun implements Runnable {

		private final IndexPPUsersMain indexPPUsers;
		private final long uid;

		public IndexPPUsersRun(IndexPPUsersMain indexPPUsers, long uid) {
			this.indexPPUsers = indexPPUsers;
			this.uid = uid;
		}

		@Override
		public void run() {
			UserInfo userInfo = SinaUsers.getSinaUserInfo(SINA_USER_BASEINFO + uid % 32, uid);
			if (userInfo != null) {
				userInfo.setRemark("1");
				indexPPUsers.addDocToSolr(userInfo);
			}
		}

	}

	public void addDocToSolr(UserInfo user) {
		try {
			cloudServer.add(getDoc(user));
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void commit() {
		try {
			cloudServer.commit();
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private SolrInputDocument getDoc(UserInfo user) {

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", user.getId());
		doc.addField("username", user.getId());
		doc.addField("screenname", user.getScreen_name(), 3.0f);
		doc.addField("name", user.getName());
		doc.addField("province", user.getProvince());
		doc.addField("city", user.getCity());
		doc.addField("location", user.getLocation());
		doc.addField("description", user.getDescription(), 2.0f);
		doc.addField("url", user.getUrl());
		doc.addField("profileimageurl", user.getProfile_image_url());
		doc.addField("domain", user.getDomain());
		doc.addField("gender", user.getGender());
		doc.addField("followerscount", user.getFollowers_count());
		doc.addField("friendscount", user.getFriends_count());
		doc.addField("statusescount", user.getStatuses_count());
		doc.addField("favouritescount", user.getFavourites_count());
		doc.addField("createdat", user.getCreated_at());
		doc.addField("verified", user.isVerified());
		doc.addField("verifiedtype", user.getVerified_type());
		doc.addField("avatarlarge", user.getAvatar_large());
		doc.addField("bifollowerscount", user.getBi_followers_count());
		doc.addField("remark", user.getRemark());
		doc.addField("verifiedreason", user.getVerified_reason());
		doc.addField("weihao", user.getWeihao());
		doc.addField("lastime", System.currentTimeMillis() / 1000);

		/**
		 * 注意：friends字段数据要和上面所有字段一起添加
		 */
		//		doc.addField("friends", "");

		return doc;
	}

	public void close() {
		cloudServer.shutdown();
	}

}
