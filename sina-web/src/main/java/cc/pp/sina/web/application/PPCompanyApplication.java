package cc.pp.sina.web.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.company.PPCompany;
import cc.pp.sina.domain.query.QueryResult;
import cc.pp.sina.domain.sql.UserBaseInfo;
import cc.pp.sina.query.query.PPCompanyQuery;
import cc.pp.sina.web.resource.PPCompanyResource;

public class PPCompanyApplication extends Application {

	private static Logger logger = LoggerFactory.getLogger(PPCompanyApplication.class);

	private final PPCompanyQuery ppCompanyQuery;

	public PPCompanyApplication(String indexDir) throws IOException {
		ppCompanyQuery = new PPCompanyQuery(indexDir);
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router();
		router.attach("/company/type/{type}/month/{month}/baseinfo", PPCompanyResource.class);
		return router;
	}

	/**
	 * 企业用户数据获取
	 */
	private UserBaseInfo getCompanyInfo(String month, String type, long uid) {
		if ("all".equals(type)) { // 当月所有企业用户
			return PPCompany.getCompanyInfo(month, uid);
		} else if ("add".equals(type)) { // 当月新增企业用户
			return PPCompany.getAddCompanyInfo(month, uid);
		} else if ("leave".equals(type)) { // 当月流失企业用户
			return PPCompany.getLeaveCompanyInfo(month, uid);
		} else {
			logger.info("Param 'type' is illegal.");
			return null;
		}
	}

	/**
	 * 根据关键词查询企业用户
	 */
	public List<UserBaseInfo> getCompanyInfosByKeywords(String yearmonth, String type, String keywords, int page) {

		List<UserBaseInfo> result = new ArrayList<>();
		QueryResult queryResult = null;
		try {
			queryResult = ppCompanyQuery.getQueryResultByKeywords(yearmonth, type, keywords, page);
		} catch (ParseException | IOException e) {
			logger.error("ParseException or IOException: " + e.getMessage());
			return null;
		}
		if (queryResult == null) {
			return null;
		}
		logger.info("QueryResult=" + queryResult.getTotalNumber());
		UserBaseInfo userBaseInfo = null;
		for (String id : queryResult.getData()) {
			userBaseInfo = getCompanyInfo(yearmonth, type, Long.parseLong(id));
			if (userBaseInfo != null) {
				result.add(userBaseInfo);
			}
		}

		return result;
	}

	public void close() {
		try {
			ppCompanyQuery.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
