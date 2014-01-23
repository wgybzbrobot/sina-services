package cc.pp.sina.web.resource;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.sql.UserBaseInfo;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.net.URLCode;
import cc.pp.sina.web.application.PPCompanyApplication;
import cc.pp.sina.web.domain.ErrorResponse;

public class PPCompanyResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(BaseInfoResource.class);

	private String type = "";
	private String month = "";
	private String keywords = "";
	private int page = 0;


	@Override
	public void doInit() {
		type = (String) this.getRequest().getAttributes().get("type");
		month = (String) this.getRequest().getAttributes().get("month");
		Form form = this.getRequest().getResourceRef().getQueryAsForm();
		for (Parameter parameter : form) {
			if (parameter.getName().contains("keywords")) {
				keywords = parameter.getValue();
			} else if (parameter.getName().contains("page")) {
				if (!JavaPattern.isAllNum(parameter.getValue())) {
					logger.info("Param of verify is illegal, verify should be integer.");
				} else {
					page = Integer.parseInt(parameter.getValue());
				}
			}
		}
	}

	@Get("json")
	public Representation getCompanyInfosByKeywords() {
		if (type.length() == 0 || month.length() == 0) {
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
		PPCompanyApplication application = (PPCompanyApplication) getApplication();
		List<UserBaseInfo> result = application.getCompanyInfosByKeywords(month, type, URLCode.Utf8UrlDecode(keywords),
				page);
		logger.info("Request Url: " + getReference() + ", hits=" + result.size() + ".");
		return new JsonRepresentation(JsonUtils.toJson(result));
	}

}
