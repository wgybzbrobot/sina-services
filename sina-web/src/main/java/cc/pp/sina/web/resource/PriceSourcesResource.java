package cc.pp.sina.web.resource;

import cc.pp.sina.dao.price.PriceService;
import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.CommonInfoApplication;
import cc.pp.sina.web.domain.PageInfo;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by chenwei on 14-2-17.
 */
public class PriceSourcesResource extends ServerResource {

	private PriceService priceService;

	@Override
	public void doInit() {
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		priceService = application.getPriceService();
	}

	@Get("json")
	public String list() throws IOException {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String qq = form.getFirstValue("qq");
		if (!StringUtils.isEmpty(qq)) {
			PriceSource source = priceService.getSourceByQq(qq);
			return JsonUtils.getObjectMapper().writer(PriceSource.basicInfoFilters).writeValueAsString(source);
		}
		String telephone = form.getFirstValue("telephone");
		if (!StringUtils.isEmpty(telephone)) {
			PriceSource source = priceService.getSourceByTelephone(telephone);
			return JsonUtils.getObjectMapper().writer(PriceSource.basicInfoFilters).writeValueAsString(source);
		}
		String q = form.getFirstValue("q", "");
		int page = Integer.valueOf(form.getFirstValue("page", "1"));
		int perPage = Integer.valueOf(form.getFirstValue("perPage", "20"));
		Collection<PriceSource> sources;
		int sourceCount;
		if ("".equals(q)) {
			sources = priceService.getSources((page - 1) * perPage, perPage);
			sourceCount = priceService.getSourceCount();
		} else {
			sources = priceService.getSources(q, (page - 1) * perPage, perPage);
			sourceCount = priceService.getSourceCount(q);
		}

		return JsonUtils.getObjectMapper().writer(PriceSource.basicInfoFilters).writeValueAsString(new PageInfo(sourceCount, page, perPage, sources));
	}

}
