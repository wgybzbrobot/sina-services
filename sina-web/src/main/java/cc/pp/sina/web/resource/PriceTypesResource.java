package cc.pp.sina.web.resource;

import cc.pp.sina.domain.bozhus.BozhuPrice;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.price.Price;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.dao.price.BozhuService;

/**
 * Created by chenwei on 14-1-15.
 */
public class PriceTypesResource extends ServerResource {

	private PriceService priceService;
	private int sourceId;
	private BozhuPrice bozhuPrice;

	@Post("json")
	public Price addPrice(Price price) {
		Price result = priceService.addPrice(bozhuPrice, sourceId, price);
		setStatus(Status.SUCCESS_CREATED);
		return result;
	}

	@Override
	public void doInit() {
		sourceId = Integer.valueOf((String) this.getRequest().getAttributes().get("sourceId"));
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
		BozhuService bozhuService = application.getBozhuService();
		long uid = Long.valueOf((String) this.getRequest().getAttributes().get("uid"));
		bozhuPrice = bozhuService.get(uid);
		setExisting(bozhuPrice != null);
	}

}
