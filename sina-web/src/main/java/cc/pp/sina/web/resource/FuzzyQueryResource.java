package cc.pp.sina.web.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.FuzzySearchApplication;

public class FuzzyQueryResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(FuzzyQueryResource.class);

	private String text;

	@Override
	public void doInit() {
		text = (String) this.getRequest().getAttributes().get("text");
		try {
			text = URLDecoder.decode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("'Text' decoded error.");
		}
	}

	@Get("json")
	public Representation getQueryResult() {
		FuzzySearchApplication application = (FuzzySearchApplication) getApplication();
		if (text == null || text.length() == 0) {
			logger.error("Query param `text` is illegal.");
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
		List<String> result = application.getQueryResultByText(text);
		logger.info("Request Url=" + getReference() + ", hits=" + result.size() + ".");
		return new JsonRepresentation(JsonUtils.toJson(result));
	}

}
