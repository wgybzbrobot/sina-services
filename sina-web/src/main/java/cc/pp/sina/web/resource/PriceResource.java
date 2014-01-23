package cc.pp.sina.web.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import cc.pp.sina.dao.bozhus.PriceService;
import cc.pp.sina.domain.bozhus.Bozhu;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.web.bozhu.BozhuService;

/**
 * Created by chenwei on 14-1-15.
 */
public class PriceResource extends ServerResource {

	private PriceService priceService;
	private Bozhu bozhu;

	@Override
	public void doInit() {
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
		BozhuService bozhuService = application.getBozhuService();
		long uid = Long.valueOf((String) this.getRequest().getAttributes().get("uid"));
		bozhu = bozhuService.get(uid);
		setExisting(bozhu != null);
	}

	@Get("json")
	public PriceSource getDefualtPrice() {
		return priceService.getDefaultPriceSource(bozhu);
	}

}
