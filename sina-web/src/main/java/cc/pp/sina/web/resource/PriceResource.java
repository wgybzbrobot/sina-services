package cc.pp.sina.web.resource;

import cc.pp.sina.domain.bozhus.BozhuPrice;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.dao.price.BozhuService;

/**
 * Created by chenwei on 14-1-15.
 */
public class PriceResource extends ServerResource {

	private PriceService priceService;
	private BozhuPrice bozhuPrice;

	@Override
	public void doInit() {
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
		BozhuService bozhuService = application.getBozhuService();
		long uid = Long.valueOf((String) this.getRequest().getAttributes().get("uid"));
		bozhuPrice = bozhuService.get(uid);
		setExisting(bozhuPrice != null);
	}

	@Get("json")
	public PriceSource getDefualtPrice() {
		return priceService.getDefaultPriceSource(bozhuPrice);
	}

}
