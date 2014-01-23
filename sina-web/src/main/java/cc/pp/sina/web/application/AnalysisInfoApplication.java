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

import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.query.FloatRangeDomain;
import cc.pp.sina.domain.query.IntRangeDomain;
import cc.pp.sina.domain.query.QueryResult;
import cc.pp.sina.query.query.BozhuQuery;
import cc.pp.sina.web.bozhu.AnalysisInfo;
import cc.pp.sina.web.domain.BozhusResult;
import cc.pp.sina.web.resource.AnalysisCollectedResource;
import cc.pp.sina.web.resource.AnalysisResource;
import cc.pp.sina.web.resource.QueryByBozhuResource;
import cc.pp.sina.web.resource.QueryByFansResource;

/**
 * 后期删除，通过BozhuLibraryApplication替代
 * @author wgybzb
 *
 */
public class AnalysisInfoApplication extends Application {

	private static Logger logger = LoggerFactory.getLogger(AnalysisInfoApplication.class);

	private final AnalysisInfo bozhusInfoByUid;
	private final BozhuQuery bozhuQuery;

	public AnalysisInfoApplication(AnalysisInfo bozhusInfoByUid, BozhuQuery bozhuQuery) {
		this.bozhusInfoByUid = bozhusInfoByUid;
		this.bozhuQuery = bozhuQuery;
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/uids/{uids}/info", AnalysisResource.class);
		router.attach("/uids/{uids}/collect", AnalysisCollectedResource.class);
		router.attach("/query/bozhu", QueryByBozhuResource.class);
		router.attach("/query/fans", QueryByFansResource.class);
		return router;
	}

	/**
	 * 根据uid列表批量查询用户信息
	 */
	public List<UserAllParamsDomain> getBozhusInfo(List<String> uids) {
		if (uids == null) {
			return null;
		}
		List<UserAllParamsDomain> result = new ArrayList<UserAllParamsDomain>();
		UserAllParamsDomain temp;
		for (String uid : uids) {
			temp = bozhusInfoByUid.getBozhusInfoByUid(uid);
			if (temp != null) {
				result.add(temp);
			}
		}
		return result;
	}

	/**
	 * 关键词查询
	 */
	public List<UserAllParamsDomain> getKeywordsQuery(String... strs) {
		try {
			return getBozhusInfo(bozhuQuery.getKeywordsQueryResult(strs));
		} catch (IOException | ParseException e) {
			logger.info("getKeywordsQuery is error.");
			throw new RuntimeException(e);
		}
	}

	/**
	 * 综合查询，根据博主特性数据查询博主
	 */
	public BozhusResult getCompositeQuery(List<IntRangeDomain> intRangeDomains,
			List<FloatRangeDomain> floatRangeDomains, String keywords, String verify, int page) {
		try {
			QueryResult qr = bozhuQuery.getCompositeQueryResult(intRangeDomains, floatRangeDomains, keywords, verify,
					page);
			return new BozhusResult.Builder().setData(getBozhusInfo(qr.getData())).setTotalNumber(qr.getTotalNumber())
					.build();
		} catch (ParseException | IOException e) {
			logger.info("getCompositeQuery is error.");
			return null;
		}
	}

	/**
	 * 综合查询，根据粉丝特性数据查询博主
	 */
	public BozhusResult getCompositeQueryByFans(List<IntRangeDomain> intRangeDomains, int gender, String provinces,
			String fanstags, String verify, int page) {
		try {
			QueryResult qr = bozhuQuery.getCompositeQueryByFans(intRangeDomains, gender, provinces, fanstags, verify,
					page);
			return new BozhusResult.Builder().setData(getBozhusInfo(qr.getData())).setTotalNumber(qr.getTotalNumber())
					.build();
		} catch (ParseException | IOException e) {
			logger.info("getCompositeQueryByFans is error.");
			return null;
		}
	}

	public void closed() {
		try {
			bozhuQuery.close();
		} catch (IOException e) {
			logger.info("Bozhu query closed error.");
			throw new RuntimeException(e);
		}
	}

}
