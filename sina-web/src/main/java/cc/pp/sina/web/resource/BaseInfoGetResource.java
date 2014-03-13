package cc.pp.sina.web.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.web.application.SinaUsersApplication;

import com.sina.weibo.model.User;

public class BaseInfoGetResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(BaseInfoGetResource.class);

	private long uid;
	public static final String SINA_USER_BASEINFO = "sinauserbaseinfo_";

	private static SinaUsersApplication application;

	@Override
	public void doInit() {
		String uidStr = (String) this.getRequest().getAttributes().get("uid");
		if (!JavaPattern.isAllNum(uidStr)) {
			logger.info("The 'uid' is illegal, it should be long.");
		} else {
			uid = Long.parseLong(uidStr);
		}
		application = (SinaUsersApplication) getApplication();
	}

	@Get("json")
	public Object getBaseInfoFromAPI() {
		if (uid == 0) {
			return new ErrorResponse.Builder(20003, "your query params is illegal.").build();
		}
		logger.info("Request Url: " + getReference() + ".");
		User user = application.getSinaUserInfoDao().getSinaUserBaseInfo(String.valueOf(uid));
		if (user != null) {
			if (!application.getSinaUsers().isSinaUserExisted(SINA_USER_BASEINFO + uid % 32, uid)) {
				try {
					application.getSinaUsers().insertSinaUserInfo(SINA_USER_BASEINFO + uid % 32, user);
				} catch (Exception e) {
					user.setDescription("");
					application.getSinaUsers().insertSinaUserInfo(SINA_USER_BASEINFO + uid % 32, user);
				}
			}
			return application.getBaseInfo(uid);
		} else {
			return new ErrorResponse.Builder(20003, "user is not existed.").build();
		}
	}

}
