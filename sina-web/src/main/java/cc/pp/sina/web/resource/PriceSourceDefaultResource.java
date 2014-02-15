package cc.pp.sina.web.resource;

import cc.pp.sina.domain.bozhus.BozhuPrice;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.dao.price.BozhuService;

/**
 * Created by chenwei on 14-1-15.
 */
public class PriceSourceDefaultResource extends ServerResource {

	private int sourceId;
	private PriceService priceService;
	private BozhuPrice bozhuPrice;

	@Override
	public void doInit() {
		long uid = Long.valueOf((String) this.getRequest().getAttributes().get("uid"));
		sourceId = Integer.valueOf((String) this.getRequest().getAttributes().get("sourceId"));
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
		BozhuService bozhuService = application.getBozhuService();
		bozhuPrice = bozhuService.get(uid);
		setExisting(bozhuPrice != null);
	}

	@Post
	public void updateDefaultPriceSource() {
		priceService.setDefaultPriceSource(bozhuPrice, sourceId);
		setStatus(Status.SUCCESS_NO_CONTENT);
	}

}
