package cc.pp.sina.solr.index;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;

/**
 * 用一台机器提供Solr服务，位于北京114.112.65.13服务器下。
 * @author wgybzb
 *
 */
public class IndexSinaUsersStandalone {

	private final EmbeddedSolrServer server;

	public IndexSinaUsersStandalone() {
		CoreContainer container = new CoreContainer("/home/wanggang/develop/solr/cloudsolr/ppcc/solr/");
		container.load();
		server = new EmbeddedSolrServer(container, "sina_users");
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		IndexSinaUsersStandalone indexData = new IndexSinaUsersStandalone();
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "12345");
		doc.addField("name", "wanggang45");
		try {
			indexData.getServer().add(doc);
			indexData.getServer().commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}

		indexData.close();
	}

	public SolrServer getServer() {
		return server;
	}

	public void close() {
		server.shutdown();
	}

}
