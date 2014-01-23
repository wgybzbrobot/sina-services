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

import cc.pp.sina.dao.weibos.SinaWeibos;
import cc.pp.sina.domain.weibos.WeiboInfo;
import cc.pp.sina.domain.weibos.WeiboInfoOld;

public class IndexSinaWeibosThread {

	private static Logger logger = LoggerFactory.getLogger(IndexSinaWeibosThread.class);

	//	private static final String SINA_USER_WEIBOS = "sina_user_weibos_";
	private static final String TABLE_NAMES_TABLE = "sina_user_weibos_tablenames";
	private static final int FETCH_SIZE = 1_0000;

	private static final String ZOOKEEPER_CLOUD = "wuhu001:2181,wuhu005:2181,wuhu009:2181,wuhu013:2181,wuhu017:2181";
	private static final String COLLECTION_NAME = "sina_weibos";

	private CloudSolrServer cloudServer;

	public IndexSinaWeibosThread() {
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

		if (args.length < 1) {
			System.err.println("Usage: <type> [<tabletablename>]");
			System.exit(-1);
		}
		String type = args[0];
		String tablenames_table = TABLE_NAMES_TABLE;
		if (args.length > 1) {
			tablenames_table = args[1];
		}

		IndexSinaWeibosThread indexWeibos = new IndexSinaWeibosThread();
		List<String> tablenames = SinaWeibos.getTablenames(tablenames_table);
		logger.info("Tablenames's size = " + tablenames.size());
		if ("old".equalsIgnoreCase(type)) {
			for (String tablename : tablenames) {
				int maxId = SinaWeibos.getMaxBid(tablename);
				// 旧数据表
				List<WeiboInfoOld> weibos = null;
				for (int i = 0; i < maxId / FETCH_SIZE; i++) {
					weibos = SinaWeibos.getWeibosInfoOld(tablename, i * FETCH_SIZE + 1, (i + 1) * FETCH_SIZE);
					logger.info("Read table: " + tablename + ", at: " + i + ",size=" + weibos.size());
					indexWeibos.addDocsToSolrOld(weibos);
				}
				weibos = SinaWeibos.getWeibosInfoOld(tablename, (maxId / FETCH_SIZE) * FETCH_SIZE + 1, maxId);
				logger.info("Read table: " + tablename + ", at: " + maxId / FETCH_SIZE + ",size=" + weibos.size());
				indexWeibos.addDocsToSolrOld(weibos);
			}
		} else if ("new".equalsIgnoreCase(type)) {
			for (String tablename : tablenames) {
				int maxId = SinaWeibos.getMaxBid(tablename);
				// 新数据表
				List<WeiboInfo> weibos = null;
				for (int i = 0; i < maxId / FETCH_SIZE; i++) {
					weibos = SinaWeibos.getWeibosInfo(tablename, i * FETCH_SIZE + 1, (i + 1) * FETCH_SIZE);
					logger.info("Read table: " + tablename + ", at: " + i + ",size=" + weibos.size());
					indexWeibos.addDocsToSolr(weibos);
				}
				weibos = SinaWeibos.getWeibosInfo(tablename, (maxId / FETCH_SIZE) * FETCH_SIZE + 1, maxId);
				logger.info("Read table: " + tablename + ", at: " + maxId / FETCH_SIZE + ",size=" + weibos.size());
				indexWeibos.addDocsToSolr(weibos);
			}
		}

		indexWeibos.close();
	}

	/**
	 * 索引数据
	 */
	public void addDocsToSolr(List<WeiboInfo> weibos) {

		Collection<SolrInputDocument> docs = new ArrayList<>();
		for (WeiboInfo weibo : weibos) {
			docs.add(getDoc(weibo));
		}
		try {
			cloudServer.add(docs);
			cloudServer.commit();
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addDocsToSolrOld(List<WeiboInfoOld> weibos) {

		Collection<SolrInputDocument> docs = new ArrayList<>();
		for (WeiboInfoOld weibo : weibos) {
			docs.add(getDocOld(weibo));
		}
		try {
			cloudServer.add(docs);
			cloudServer.commit();
		} catch (SolrServerException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加文档
	 */
	private SolrInputDocument getDoc(WeiboInfo weibo) {

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", weibo.getWid());
		doc.addField("wid", weibo.getWid());
		doc.addField("username", weibo.getUsername());
		doc.addField("repostscount", weibo.getRepostscount());
		doc.addField("commentscount", weibo.getCommentscount());
		doc.addField("attitudescount", weibo.getAttitudescount());
		doc.addField("text", weibo.getText());
		doc.addField("createat", weibo.getCreateat());
		doc.addField("owid", weibo.getOwid());
		doc.addField("ousername", weibo.getOusername());
		doc.addField("favorited", weibo.isFavorited());
		doc.addField("geo", weibo.getGeo());
		doc.addField("latitude", weibo.getLatitude());
		doc.addField("longitude", weibo.getLongitude());
		doc.addField("originalpic", weibo.getOriginalpic());
		doc.addField("source", weibo.getSource());
		doc.addField("visible", weibo.getVisible());
		doc.addField("mlevel", weibo.getMlevel());
		doc.addField("lastime", weibo.getLasttime());

		return doc;
	}

	private SolrInputDocument getDocOld(WeiboInfoOld weibo) {

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", weibo.getWid());
		doc.addField("wid", weibo.getWid());
		doc.addField("username", weibo.getUsername());
		doc.addField("repostscount", weibo.getRepostscount());
		doc.addField("commentscount", weibo.getCommentscount());
		doc.addField("attitudescount", 0);
		doc.addField("text", weibo.getText());
		doc.addField("createat", weibo.getCreateat());
		doc.addField("owid", weibo.getOwid());
		doc.addField("ousername", weibo.getOusername());
		doc.addField("favorited", weibo.isFavorited());
		doc.addField("geo", weibo.getGeo());
		doc.addField("latitude", weibo.getLatitude());
		doc.addField("longitude", weibo.getLongitude());
		doc.addField("originalpic", weibo.getOriginalpic());
		doc.addField("source", weibo.getSource());
		doc.addField("visible", weibo.getVisible());
		doc.addField("mlevel", 0);
		doc.addField("lastime", weibo.getLasttime());

		return doc;
	}

	public void close() {
		cloudServer.shutdown();
	}
}
