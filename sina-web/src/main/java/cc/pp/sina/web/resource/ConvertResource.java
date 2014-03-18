package cc.pp.sina.web.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.domain.tool.ConvetedUserInfo;
import cc.pp.sina.web.application.ToolApplication;

import com.sina.weibo.model.User;

public class ConvertResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(ConvertResource.class);

	private String url;
	private ToolApplication application;

	@Override
	public void doInit() {
		url = (String) getRequest().getAttributes().get("url");
		application = (ToolApplication) getApplication();
	}

	@Get("json")
	public Object getTransUidInfos() {

		if (url == null || url.length() == 0) {
			return new ErrorResponse.Builder(20003, "your query params is illegal.").build();
		}
		logger.info("Request Url: " + getReference() + ".");
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage());
			throw new RuntimeException(e);
		}
		logger.info("url=" + url);
		User user = application.getDidToUser(url);
		if (user == null) {
			return new ErrorResponse.Builder(20003, "user does not existed.").build();
		} else {
			return new ConvetedUserInfo("http://weibo.com/u/" + user.getId(), user.getId(), user.getScreenName());
		}
	}

}
