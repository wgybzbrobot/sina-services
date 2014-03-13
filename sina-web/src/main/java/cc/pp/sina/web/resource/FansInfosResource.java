package cc.pp.sina.web.resource;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.CommonInfoApplication;

public class FansInfosResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(FansIdsResource.class);

	private long uid;

	@Override
	public void doInit() {
		String uidStr = (String) this.getRequest().getAttributes().get("uid");
		if (!JavaPattern.isAllNum(uidStr)) {
			logger.info("The 'uidStr' is illegal, it should be long.");
		} else {
			uid = Long.parseLong(uidStr);
		}
	}

	@Get("json")
	public Representation getFansUids() {
		if (uid == 0) {
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
		logger.info("Request Url: " + getReference() + ".");
		CommonInfoApplication application = (CommonInfoApplication) getApplication();
		return new JsonRepresentation(JsonUtils.toJson(application.getFansBaseInfos(uid)));
	}

}
