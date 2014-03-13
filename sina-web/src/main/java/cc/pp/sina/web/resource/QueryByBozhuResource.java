package cc.pp.sina.web.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.domain.query.FloatRangeDomain;
import cc.pp.sina.domain.query.IntRangeDomain;
import cc.pp.sina.utils.java.JavaPattern;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.net.URLCode;
import cc.pp.sina.web.application.BozhuLibraryApplication;
import cc.pp.sina.web.domain.BozhusResult;

/**
 * 查询模块
 * 示例：http://localhost:8111/bozhus/query?keywords=XXXXX&influence=70-100&activecount=1-1000000&
 *                                        activation=6-10&fanscount=50000-1000000&verify=0
 *     http://60.169.74.47:8111/bozhus/query?keywords=XXXXX&influence=70-100&activation=6-10&fanscount=50000-1000000&verify=0
 * @author wgybzb
 *
 */
public class QueryByBozhuResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(QueryByBozhuResource.class);

	private String keywords = "";
	private String verify = "";
	private List<IntRangeDomain> intRangeDomains = new ArrayList<>();
	private String page = "1";

	@Override
	public void doInit() {
		// 参数：influence:1-50;  activation:1-3;  fanscount:30-300;  verify:-1;  keywords:我去年买了个表
		try {
			Form form = getRequest().getResourceRef().getQueryAsForm();
			for (Parameter parameter : form) {
				if (parameter.getName().contains("keywords")) {
					keywords = parameter.getValue();
				} else if (parameter.getName().contains("verify")) {
					verify = parameter.getValue();
					if (!JavaPattern.isAllNum(verify)) {
						logger.info("Param of verify is illegal, verify should be integer.");
					}
				} else if (parameter.getName().contains("page")) {
					page = parameter.getValue();
					if (!JavaPattern.isAllNum(page)) {
						logger.info("Param of page is illegal, page should be integer.");
					}
				} else if (parameter.getName().contains("fanscount")) {
					addParams(intRangeDomains, parameter, "fanscount");
				} else if (parameter.getName().contains("activecount")) {
					addParams(intRangeDomains, parameter, "activecount");
				} else {
					String[] values = parameter.getValue().split("-");
					if (values.length > 1) {
						intRangeDomains.add(new IntRangeDomain.Builder(parameter.getName(),
								Integer.parseInt(values[0]), Integer.parseInt(values[1])).build());
						continue;
					} else {
						logger.info("Url has other illegal params.");
					}
				}
			}
		} catch (Exception e) {
			intRangeDomains = null;
			logger.info("Query params is illegal.");
		}
	}

	@Get("json")
	public Representation getCompositeQueryResult() {
		BozhuLibraryApplication application = (BozhuLibraryApplication) getApplication();
		// 注意：utf-8编码的url需要转码成中文
		BozhusResult br = application.getCompositeQuery(intRangeDomains, new ArrayList<FloatRangeDomain>(),
				URLCode.Utf8UrlDecode(keywords), verify, Integer.parseInt(page));
		if (br != null) {
			logger.info("Request Url: " + getReference() + ", hits " + br.getTotalNumber() + ".");
			return new JsonRepresentation(JsonUtils.toJson(br));
		} else {
			logger.info("Request Url: " + getReference() + ", hits " + 0 + ".");
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
	}

	public static void addParams(List<IntRangeDomain> intRangeDomains, Parameter parameter, String fieldName) {

		int index = parameter.getValue().indexOf("-");
		if (index < 0) {
			logger.info("Param of " + fieldName + " is illegal, " + fieldName + " should be min-max form.");
		} else {
			String str = parameter.getValue();
			if (index == 0) {
				intRangeDomains.add(new IntRangeDomain.Builder(fieldName, 0, Integer.parseInt(str.substring(1)))
						.build());
			} else if (index == str.length() - 1) {
				intRangeDomains.add(new IntRangeDomain.Builder(fieldName, Integer.parseInt(str.substring(0,
						str.length() - 1)), 5000_0000).build());
			} else {
				intRangeDomains.add(new IntRangeDomain.Builder(fieldName, Integer.parseInt(str.split("-")[0]),
						Integer.parseInt(str.split("-")[1])).build());
			}
		}
	}

}
