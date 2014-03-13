package cc.pp.sina.web.resource;

import cc.pp.sina.dao.price.BozhuService;
import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.BozhuPrice;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.CommonInfoApplication;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;

/**
 * Created by chenwei on 14-1-15.
 */
public class UserPriceResource extends ServerResource {

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
	public String getDefualtPrice() throws IOException {
		PriceSource source = priceService.getDefaultPriceSource(bozhuPrice);
		return JsonUtils.getObjectMapper().writer(PriceSource.emptyFilters).writeValueAsString(source);
	}

}
