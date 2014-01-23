package cc.pp.sina.web.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.query.IntRangeDomain;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.net.URLCode;
import cc.pp.sina.web.application.BozhuLibraryApplication;
import cc.pp.sina.web.domain.BozhusResult;
import cc.pp.sina.web.domain.ErrorResponse;

public class QueryByFansResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(QueryByFansResource.class);

	private String provinces = "";
	private String fanstags = "";
	private String gender = "-1";
	private String verify = "";
	private List<IntRangeDomain> intRangeDomains = new ArrayList<>();
	private String page = "1";

	@Override
	public void doInit() {
		// 关键词
		provinces = "";
		fanstags = "";
		// fansage=20-35&gender=0&provinces=北京上海&fanstags=娱乐美食&page=1
		try {
			Form form = getRequest().getResourceRef().getQueryAsForm();
			for (Parameter parameter : form) {
				if (parameter.getName().contains("provinces")) {
					provinces = parameter.getValue();
				} else if (parameter.getName().contains("fanstags")) {
					fanstags = parameter.getValue();
				} else if (parameter.getName().contains("gender")) {
					gender = parameter.getValue();
					if (!JavaPattern.isAllNum(gender)) {
						logger.info("Param of gender is illegal, it should be integer.");
					}
				} else if (parameter.getName().contains("page")) {
					page = parameter.getValue();
					if (!JavaPattern.isAllNum(page)) {
						logger.info("Param of page is illegal, it should be integer.");
					}
				} else if (parameter.getName().contains("verify")) {
					verify = parameter.getValue();
					if (!JavaPattern.isAllNum(verify)) {
						logger.info("Param of verify is illegal, it should be integer.");
					}
				} else if (parameter.getName().contains("fansage")) {
					String[] values = parameter.getValue().split("-");
					if (values.length > 1) {
						intRangeDomains.add(new IntRangeDomain.Builder(parameter.getName(),
								Integer.parseInt(values[0]), Integer.parseInt(values[1])).build());
						continue;
					} else {
						logger.info("Params of fansage is illegal.");
					}
				} else {
					logger.info("Url has other illegal params.");
				}
			}
		} catch (Exception e) {
			intRangeDomains = null;
			logger.info("Query params is illegal.");
		}
	}

	@Get("json")
	public Representation getCompositeQueryByFans() {
		BozhuLibraryApplication application = (BozhuLibraryApplication) getApplication();
		BozhusResult br = application.getCompositeQueryByFans(intRangeDomains, Integer.parseInt(gender),
				URLCode.Utf8UrlDecode(provinces), URLCode.Utf8UrlDecode(fanstags), verify, Integer.parseInt(page));
		if (br != null) {
			logger.info("Request Url: " + getReference() + ", hits " + br.getTotalNumber() + ".");
			return new JsonRepresentation(JsonUtils.toJson(br));
		} else {
			logger.info("Request Url: " + getReference() + ", hits " + 0 + ".");
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
	}

}
