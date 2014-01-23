package cc.pp.sina.solr.query;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 查询数据
 * @author wgybzb
 *
 */
public class QuerySinaUsers {

	private static Logger logger = LoggerFactory.getLogger(QuerySinaUsers.class);

	private static final String ZOOKEEPER_CLOUD = "wuhu001:2181,wuhu005:2181,wuhu009:2181,wuhu013:2181,wuhu017:2181";
	private static final String COLLECTION_NAME = "sina_users";

	private CloudSolrServer cloudServer;

	public QuerySinaUsers() {
		try {
			cloudServer = new CloudSolrServer(ZOOKEEPER_CLOUD);
			cloudServer.setDefaultCollection(COLLECTION_NAME);
		} catch (MalformedURLException e) {
			logger.info("MalformedURLException: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		QuerySinaUsers querySinaUsers = new QuerySinaUsers();

		try {
			querySinaUsers.queryPPCompany();
		} catch (SolrServerException e) {
			throw new RuntimeException(e);
		}

		querySinaUsers.close();
	}

	/**
	 * 企业数据查询
	 */
	public void queryPPCompany() throws SolrServerException {

		SolrQuery query = new SolrQuery();
		query.setFilterQueries("remark:1");
		query.setFilterQueries("verified:2");
		query.setQuery("location:安徽");
		QueryResponse rsp = cloudServer.query(query);
		SolrDocumentList docs = rsp.getResults();
		System.out.println(docs.size());
	}

	public void close() {
		cloudServer.shutdown();
	}

}
