package cc.pp.sina.solr.index;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.friends.SinaFriendsDis;
import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.friends.FriendsInfo;
import cc.pp.sina.domain.users.UserInfo;

/**
 * 通过HTTP方式索引数据到CoudSolr上。
 * @author wgybzb
 *
 */
public class IndexSinaUsersThread {

	private static Logger logger = LoggerFactory.getLogger(IndexSinaUsersThread.class);

	/*
	 * 新浪用户关注数据获取
	 */
	private static final String SINA_USER_FRIENDS = "sina_user_friends_";
	private static SinaFriendsDis sinaFriends1 = new SinaFriendsDis(MybatisConfig.ServerEnum.friend1);
	private static SinaFriendsDis sinaFriends2 = new SinaFriendsDis(MybatisConfig.ServerEnum.friend2);

	private static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";
	private static final int FETCH_SIZE = 10_0000;

	//	private static final String ZOOKEEPER_CLOUD = "wuhu001:2181,wuhu005:2181,wuhu009:2181,wuhu013:2181,wuhu017:2181";
	// 这里使用集群中的一个结点即可
	private static final String ZOOKEEPER_CLOUD = "wuhu017:2181";
	private static final String COLLECTION_NAME = "sina_users";

	private CloudSolrServer cloudServer;

	public IndexSinaUsersThread() {
		try {
			cloudServer = new CloudSolrServer(ZOOKEEPER_CLOUD);
			cloudServer.setDefaultCollection(COLLECTION_NAME);
			cloudServer.setParallelUpdates(true);
			cloudServer.setZkConnectTimeout(5_000);
			cloudServer.setZkClientTimeout(5_000);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		IndexSinaUsersThread indexUsers = new IndexSinaUsersThread();

		SinaUsers sinaUsers = new SinaUsers(MybatisConfig.ServerEnum.fenxi);

		for (int t = 0; t < 32; t++) {
			String tablename = SINA_USER_BASEINFO + t;
			logger.info("Read table: " + tablename);
			int maxBid = sinaUsers.getMaxBid(tablename);
			List<UserInfo> users = null;
			for (int i = 0; i < maxBid / FETCH_SIZE; i++) {
				users = sinaUsers.getSinaUserInfos(tablename, i * FETCH_SIZE + 1, (i + 1) * FETCH_SIZE);
				logger.info("Read table: " + tablename + ", at: " + i + ",size=" + users.size());
				indexUsers.addDocsToSolr(users);
			}
			users = sinaUsers.getSinaUserInfos(tablename, (maxBid / FETCH_SIZE) * FETCH_SIZE + 1, maxBid);
			logger.info("Read table: " + tablename + ", at: " + maxBid / FETCH_SIZE + ",size=" + users.size());
			indexUsers.addDocsToSolr(users);
		}

		indexUsers.close();
	}

	/**
	 * 索引数据
	 */
	public void addDocsToSolr(List<UserInfo> users) {
		if (users.size() == 0) {
			return;
		}
		Collection<SolrInputDocument> docs = new ArrayList<>();
		for (UserInfo user : users) {
			docs.add(getDoc(user));
		}
		try {
			cloudServer.add(docs);
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
		long username = user.getId();
		FriendsInfo friendsInfo = null;
		if (username % 64 < 32) {
			friendsInfo = sinaFriends1.getSinaFriendsInfo(SINA_USER_FRIENDS + username % 64, username);
		} else {
			friendsInfo = sinaFriends2.getSinaFriendsInfo(SINA_USER_FRIENDS + username % 64, username);
		}
		if (friendsInfo != null) {
			doc.addField("friends", friendsInfo.getFriendsuids());
		} else {
			doc.addField("friends", "");
		}

		return doc;
	}

	public void close() {
		cloudServer.shutdown();
	}

}