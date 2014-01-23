package cc.pp.sina.web.resource;

import java.util.Collection;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cc.pp.sina.dao.bozhus.PriceService;
import cc.pp.sina.domain.bozhus.Bozhu;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.web.bozhu.BozhuService;

public class PriceSourcesResource extends ServerResource {

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
	public Collection<PriceSource> list() {
		return priceService.getSourcePrices(bozhu);
	}

	@Post("json")
	public PriceSource addSource(PriceSource source) {
		PriceSource result = priceService.addSource(bozhu, source);
		setStatus(Status.SUCCESS_CREATED);
		return result;
	}

}





