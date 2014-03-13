package cc.pp.sina.web.resource;

import cc.pp.sina.dao.price.BozhuService;
import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.BozhuPrice;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.CommonInfoApplication;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.Collection;

public class UserPriceSourcesResource extends ServerResource {

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
	public String list() throws IOException {
		Collection<PriceSource> result = priceService.getSources(bozhuPrice);
		return JsonUtils.getObjectMapper().writer(PriceSource.emptyFilters).writeValueAsString(result);
	}

	@Post("json")
	public String addSource(PriceSource source) throws IOException {
		PriceSource result = priceService.addSource(bozhuPrice, source);
		setStatus(Status.SUCCESS_CREATED);
		return JsonUtils.getObjectMapper().writer(PriceSource.emptyFilters).writeValueAsString(result);
	}

}





