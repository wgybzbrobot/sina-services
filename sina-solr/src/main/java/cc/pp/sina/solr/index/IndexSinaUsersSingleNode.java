package cc.pp.sina.solr.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.users.UserInfo;

/**
 * 通过HTTP方式索引数据到单台机器上。
 * @author wgybzb
 *
 */
public class IndexSinaUsersSingleNode {

	private static Logger logger = LoggerFactory.getLogger(IndexSinaUsersSingleNode.class);

	/*
	 * 数据库信息
	 */
	private static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";
	private static final int FETCH_SIZE = 10_0000;

	/*
	 * Solr信息
	 */
	private static final String BASE_URL = "http://localhost:8983/solr/";
	private static final String COLLECTION_NAME = "sina_users";

	private final HttpSolrServer solrServer;

	public IndexSinaUsersSingleNode() {
		try {
			solrServer = new HttpSolrServer(BASE_URL + COLLECTION_NAME);
			// 最大尝试次数，默认是0，不推荐大于1的值
			solrServer.setMaxRetries(1);
			// 建立TCP链接的最大链接时间，一般设置5秒
			solrServer.setConnectionTimeout(5000);
			// 当跨版本兼容的时候需要设置XML响应解析器，默认的是Binary Parser
			solrServer.setParser(new XMLResponseParser());
			// Socket读超时，这个一般不做设置
			solrServer.setSoTimeout(1000);
			solrServer.setDefaultMaxConnectionsPerHost(500);
			solrServer.setMaxTotalConnections(100);
			// 跟踪重定向，默认是false
			solrServer.setFollowRedirects(false);
			// 支持压缩，服务器端必须支持gzip或者default压缩算法来满足此设置，默认为false
			solrServer.setAllowCompression(true);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		IndexSinaUsersSingleNode indexUsers = new IndexSinaUsersSingleNode();

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
		Collection<SolrInputDocument> docs = new ArrayList<>();
		for (UserInfo user : users) {
			docs.add(getDoc(user));
		}
		try {
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException | IOException e) {
			logger.error("SolrServerException | IOException: " + e.getMessage());
			//			throw new RuntimeException(e);
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

		return doc;
	}

	public void close() {
		solrServer.shutdown();
	}

}
