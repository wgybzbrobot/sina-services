package cc.pp.sina.web.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.web.application.BozhuLibraryApplication;

/**
 * 用户信息查询模块
 * 示例：http://localhost:8111/bozhus/info/{uids}
 *     uids是逗号分开的用户名，如14335555,145666454,6643334
 * @author wgybzb
 *
 */
public class AnalysisResource extends ServerResource {

	private List<String> uidsList;

	@Override
	public void doInit() {
		String uids = (String) getRequest().getAttributes().get("uids");
		uidsList = new ArrayList<>();
		for (String uid : uids.split(",")) {
			uidsList.add(uid);
		}
	}

	@Get("json")
	public Representation getBozhuAllParams() {
		BozhuLibraryApplication application = (BozhuLibraryApplication) getApplication();
		List<UserAllParamsDomain> bozhu = application.getBozhusInfo(uidsList);
		return new JsonRepresentation(JsonUtils.toJson(bozhu));
	}

}
