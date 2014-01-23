package cc.pp.sina.web.resource;

import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cc.pp.sina.dao.bozhus.PriceService;
import cc.pp.sina.domain.bozhus.Bozhu;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.web.bozhu.BozhuService;

/**
 * Created by chenwei on 14-1-15.
 */
public class PriceSourceDefaultResource extends ServerResource {

	private int sourceId;
	private PriceService priceService;
	private Bozhu bozhu;

	@Override
	public void doInit() {
		long uid = Long.valueOf((String) this.getRequest().getAttributes().get("uid"));
		sourceId = Integer.valueOf((String) this.getRequest().getAttributes().get("sourceId"));
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
		BozhuService bozhuService = application.getBozhuService();
		bozhu = bozhuService.get(uid);
		setExisting(bozhu != null);
	}

	@Post
	public void updateDefaultPriceSource() {
		priceService.setDefaultPriceSource(bozhu, sourceId);
		setStatus(Status.SUCCESS_NO_CONTENT);
	}

}
