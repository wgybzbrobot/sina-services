package cc.pp.sina.web.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.FuzzySearchApplication;

public class FuzzyIndexResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(FuzzyIndexResource.class);

	private long id = -1;
	private String text = "";

	@Override
	public void doInit() {
		String idStr = (String) this.getRequest().getAttributes().get("id");
		if (!JavaPattern.isAllNum(idStr)) {
			logger.info("The 'idStr' is illegal, it should be long.");
		} else {
			id = Long.parseLong(idStr);
		}
		text = (String) this.getRequest().getAttributes().get("text");
		try {
			text = URLDecoder.decode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("'Text' decoded error.");
		}
		if (text == null || text.length() == 0) {
			logger.info("The 'text' is illegal, it should be long.");
			text = "";
		}
	}

	@Get("json")
	public Representation addIndexData() {
		@SuppressWarnings("unused")
		FuzzySearchApplication application = (FuzzySearchApplication) getApplication();
		if (id == -1 || text.length() == 0) {
			logger.error("Query params 'id' or 'text' is illegal.");
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
//		application.insertIndexData(id, text);;
		logger.info("Request Url=" + getReference() + ".");
		return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(200, "ok").build()));
	}

}
