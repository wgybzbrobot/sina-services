package cc.pp.sina.web.application;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.fuzzy.search.query.DataQuery;
import cc.pp.sina.web.resource.FuzzyQueryResource;

public class FuzzySearchApplication extends Application {

	private static Logger logger = LoggerFactory.getLogger(FuzzySearchApplication.class);

	@SuppressWarnings("unused")
	private final Directory indexDir;
	private final DataQuery dataQuery;

	public FuzzySearchApplication(Directory indexDir) throws IOException {
		this.indexDir = indexDir;
		dataQuery = new DataQuery(indexDir);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/query/text/{text}", FuzzyQueryResource.class);
//		router.attach("/index/id/{id}/text/{text}", FuzzyIndexResource.class);
		return router;
	}

//	/**
//	 * 添加索引数据
//	 */
//	public void insertIndexData(long id, String text) {
//		DataIndex dataIndex;
//		try {
//			dataIndex = new DataIndex(indexDir);
//			dataIndex.addIndexData(id, text);
//			dataIndex.close();
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * 获取简单的查询结果
	 */
	public List<String> getQueryResultByText(String text) {
		List<String> result = null;
		try {
			result = dataQuery.simpleQuery(text);
		} catch (ParseException | IOException e) {
			logger.info("Querying occurs error.");
			throw new RuntimeException(e);
		}
		return result;
	}

	public void close() {
		dataQuery.close();
	}

}
