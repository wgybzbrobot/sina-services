package cc.pp.sina.web.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.WordSegApplication;

public class WordSegResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(WordSegResource.class);

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

	@Get("string")
	public Representation getQueryResult() {
		WordSegApplication application = (WordSegApplication) getApplication();
		if (text == null || text.length() == 0) {
			logger.error("Query param `text` is illegal.");
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
		String result = application.getWords(text);
		logger.info("Request Url=" + getReference() + ".");
		logger.info("'Text' is " + text);
		return new StringRepresentation(result);
	}

}
