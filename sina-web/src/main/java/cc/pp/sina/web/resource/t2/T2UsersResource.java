package cc.pp.sina.web.resource.t2;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.domain.t2.T2SinaUserInfo;
import cc.pp.sina.domain.t2.T2TencentUserInfo;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.web.application.T2Application;

public class T2UsersResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(T2UsersResource.class);

	private static T2Application application;

	private String type;

	@Override
	public void doInit() {
		application = (T2Application) getApplication();
		type = (String) this.getRequest().getAttributes().get("type");
	}

	@Post("json")
	public ErrorResponse acceptData(List<String> uids) {
		logger.info("Request Url: " + getReference() + ".");
		if (type == null || type.length() == 0) {
			return new ErrorResponse.Builder(20003, "your query params is illegal.").build();
		}

		for (String uid : uids) {
			if ("sina".equalsIgnoreCase(type)) {
				application.getUserFans().insertT2SinaUser(Long.parseLong(uid));
			} else {
				application.getUserFans().insertT2TencentUser(uid);
			}
		}
		return new ErrorResponse.Builder(0, "ok").build();
	}

	@Get("json")
	public Object getUserFansResult() {
		String uid = (String) this.getRequest().getAttributes().get("uid");
		if ("sina".equalsIgnoreCase(type)) {
			if (JavaPattern.isAllNum(uid)) {
				T2SinaUserInfo result = application.getUserFans().selectT2SinaUser(Long.parseLong(uid));
				if (result == null) {
					return new ErrorResponse.Builder(20003, "user does existed.").build();
				} else {
					return result;
				}
			} else {
				return new ErrorResponse.Builder(20003, "your query params is illegal.").build();
			}
		} else {
			T2TencentUserInfo result = application.getUserFans().selectT2TencentUser(uid);
			if (result == null) {
				return new ErrorResponse.Builder(20003, "user does existed.").build();
			} else {
				return result;
			}
		}
	}

}
