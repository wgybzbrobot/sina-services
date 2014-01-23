package cc.pp.sina.web.resource;

import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.CommonInfoApplication;

public class BatchBaseInfoResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(BatchBaseInfoResource.class);

	private String uids;

	@Override
	public void doInit() {
		uids = (String) getRequest().getAttributes().get("uids");
		if (uids.indexOf(",") < 0) {
			if (uids.length() < 5) {
				logger.info("The 'uids' is illegal, it should be comma-depart.");
			} else {
				uids = uids + ",";
			}
		}
	}

	@Get("json")
	public Representation getBathBaseInfos() {
		logger.info("Request Url: " + getReference() + ".");
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		List<BozhuBaseInfo> result = application.getBatchBaseInfos(uids.split(","));
		return new JsonRepresentation(JsonUtils.toJson(result));
	}

}
