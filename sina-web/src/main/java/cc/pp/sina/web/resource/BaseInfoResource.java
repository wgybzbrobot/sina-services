package cc.pp.sina.web.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.web.application.SinaUsersApplication;
import cc.pp.sina.web.common.HttpUtils;

public class BaseInfoResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(BaseInfoResource.class);

	private long uid;

	@Override
	public void doInit() {
		String uidStr = (String) this.getRequest().getAttributes().get("uid");
		if (!JavaPattern.isAllNum(uidStr)) {
			logger.info("The 'uid' is illegal, it should be long.");
		} else {
			uid = Long.parseLong(uidStr);
		}
	}

	//	@Get("json")
	//	public Representation getFansUids() {
	//		if (uid == 0) {
	//			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
	//					"your query params is illegal.").build()));
	//		}
	//		logger.info("Request Url: " + getReference() + ".");
	//		CommonInfoApplication application = (CommonInfoApplication) getApplication(); // 旧接口使用的应用
	//		return new JsonRepresentation(JsonUtils.toJson(application.getBaseInfo(uid)));
	//	}

	@Get("json")
	public Object getBaseInfo() {
		if (uid == 0) {
			return new ErrorResponse.Builder(20003, "your query params is illegal.").build();
		}
		logger.info("Request Url: " + getReference() + ".");
		SinaUsersApplication application = (SinaUsersApplication) getApplication();
		BozhuBaseInfo userInfo = application.getBaseInfo(uid);
		if (userInfo == null) {
			return HttpUtils.doGet("http://60.169.74.26:8111/sina/users/" + uid + "/fromapi", "utf-8");
		}
		return userInfo;
	}

}
