package cc.pp.sina.web.resource;

import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.price.PriceType;
import cc.pp.sina.web.application.CommonInfoApplication;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.Collection;

/**
 * Created by chenwei on 14-2-18.
 */
public class PriceTypesResource extends ServerResource {

	private PriceService priceService;

	@Override
	public void doInit() {
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
	}

	@Get("json")
	public Collection<PriceType> getTypes() {
		return priceService.getTypes();
	}

}
