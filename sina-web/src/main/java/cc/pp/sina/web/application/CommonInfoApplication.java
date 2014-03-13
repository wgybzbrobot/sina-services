package cc.pp.sina.web.application;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cc.pp.sina.dao.bozhus.CommonInfo;
import cc.pp.sina.dao.price.BozhuService;
import cc.pp.sina.dao.price.BozhuServiceDb;
import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.web.resource.BaseInfoResource;
import cc.pp.sina.web.resource.BatchBaseInfoResource;
import cc.pp.sina.web.resource.ExtendResource;
import cc.pp.sina.web.resource.FansIdsResource;
import cc.pp.sina.web.resource.FansInfosResource;
import cc.pp.sina.web.resource.PriceSourcesResource;
import cc.pp.sina.web.resource.PriceTypesResource;
import cc.pp.sina.web.resource.UserPriceResource;
import cc.pp.sina.web.resource.UserPriceSourceDefaultResource;
import cc.pp.sina.web.resource.UserPriceSourceResource;
import cc.pp.sina.web.resource.UserPriceSourcesResource;
import cc.pp.sina.web.resource.UserPriceTypesResource;
import cc.pp.sina.web.resource.WeiboResource;
import cc.pp.sina.web.resource.WeibosResource;

/**
 * 待废弃
 *
 * @author wgybzb
 */
public class CommonInfoApplication extends Application {

//	private static Logger logger = LoggerFactory.getLogger(BozhuFansApplication.class);

	private final CommonInfo commonInfo;
	private final PriceService priceService = new PriceService();
	private final BozhuService bozhuService = new BozhuServiceDb();

	public CommonInfoApplication() {
		commonInfo = new CommonInfo();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/bozhu/uid/{uid}/baseinfo", BaseInfoResource.class);
		router.attach("/bozhu/uids/{uids}/baseinfos", BatchBaseInfoResource.class);
		router.attach("/bozhu/uid/{uid}/fansids", FansIdsResource.class);
		router.attach("/bozhu/uid/{uid}/fansinfos", FansInfosResource.class);
		router.attach("/bozhu/uid/{uid}/extend", ExtendResource.class);

		router.attach("/price/types", PriceTypesResource.class);
		router.attach("/price/sources", PriceSourcesResource.class);
		router.attach("/users/{uid}/price", UserPriceResource.class);
		router.attach("/users/{uid}/price/sources", UserPriceSourcesResource.class);
		router.attach("/users/{uid}/price/sources/{sourceId}", UserPriceSourceResource.class);
		router.attach("/users/{uid}/price/sources/{sourceId}/default", UserPriceSourceDefaultResource.class);
		router.attach("/users/{uid}/price/sources/{sourceId}/types", UserPriceTypesResource.class);
		router.attach("/weibos", WeibosResource.class);
		router.attach("/weibos/{wid}", WeiboResource.class);
		return router;
	}

	/**
	 * 获取基础信息
	 */
	public BozhuBaseInfo getBaseInfo(long uid) {
		return commonInfo.getBozhuBaseInfo(uid);
	}

	/**
	 * 批量获取基础信息
	 */
	public List<BozhuBaseInfo> getBatchBaseInfos(String[] uids) {

		List<BozhuBaseInfo> result = new ArrayList<>();
		for (String uid : uids) {
			BozhuBaseInfo temp = getBaseInfo(Long.parseLong(uid));
			if (temp != null) {
				result.add(temp);
			}
		}

		return result;
	}

	/**
	 * 获取扩展数据
	 */
	public UserExtendInfo getBozhuExtendInfo(long uid) {
		return commonInfo.getExtendInfo(uid);
	}


	public BozhuService getBozhuService() {
		return bozhuService;
	}

	/**
	 * 获取粉丝基础信息
	 */
	public List<BozhuBaseInfo> getFansBaseInfos(long uid) {

		List<BozhuBaseInfo> result = new ArrayList<>();
		SimpleFansInfo fans = commonInfo.getFansUids(uid);
		if (fans.getFansuids().length() > 5) {
			int index = fans.getFansuids().indexOf(",");
			if (index > 0) {
				for (String id : fans.getFansuids().split(",")) {
					BozhuBaseInfo baseinfo = commonInfo.getBozhuBaseInfo(Long.parseLong(id));
					if (baseinfo != null) {
						result.add(baseinfo);
					}
				}
			} else {
				BozhuBaseInfo baseinfo = commonInfo.getBozhuBaseInfo(Long.parseLong(fans.getFansuids()));
				if (baseinfo != null) {
					result.add(baseinfo);
				}
			}
		}

		return result;
	}

	/**
	 * 获取粉丝uid列表
	 */
	public SimpleFansInfo getFansUids(long uid) {
		return commonInfo.getFansUids(uid);
	}

	public PriceService getPriceService() {
		return priceService;
	}


}
