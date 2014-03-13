package cc.pp.sina.web.resource.t2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.MidToWid;
import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.domain.t2.T2SingleWeiboInfo;
import cc.pp.sina.web.application.T2Application;

public class T2SingleWeiboResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(T2SingleWeiboResource.class);

	private static T2Application application;

	private String type;

	@Override
	public void doInit() {
		application = (T2Application) getApplication();
		type = (String) this.getRequest().getAttributes().get("type");
	}

	@Post("json")
	public ErrorResponse acceptData(List<String> urls) {
		logger.info("Request Url: " + getReference() + ".");
		if (type == null || type.length() == 0) {
			return new ErrorResponse.Builder(20003, "your query params is illegal.").build();
		}
		for (String url : urls) {
			if ("sina".equalsIgnoreCase(type)) {
				application.getSingleWeibo().insertSingleWeibo(type,
						Long.parseLong(MidToWid.mid2wid(retrivalWid(url))), url);
			} else {
				application.getSingleWeibo().insertSingleWeibo(type, Long.parseLong(retrivalWid(url)), url);
			}
		}
		return new ErrorResponse.Builder(0, "ok").build();
	}

	@Get("json")
	public Object getSingleWeiboResult() {
		String url = (String) this.getRequest().getAttributes().get("url");
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		if ("sina".equalsIgnoreCase(type)) {
			T2SingleWeiboInfo result = application.getSingleWeibo().selectSingleWeibo(
					Long.parseLong(MidToWid.mid2wid(retrivalWid(url))));
			if (result == null) {
				return new ErrorResponse.Builder(20003, "weibo does existed.").build();
			} else {
				return result;
			}
		} else {
			T2SingleWeiboInfo result = application.getSingleWeibo().selectSingleWeibo(Long.parseLong(retrivalWid(url)));
			if (result == null) {
				return new ErrorResponse.Builder(20003, "weibo does existed.").build();
			} else {
				return result;
			}
		}
	}

	private String retrivalWid(String url) {
		int index = url.indexOf("?");
		if (index > 0) {
			return url.substring(url.lastIndexOf("/") + 1, index);
		} else {
			return url.substring(url.lastIndexOf("/") + 1);
		}
	}

}
