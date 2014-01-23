package cc.pp.content.library.web;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.error.ErrorResponse;
import cc.pp.sina.utils.json.JsonUtils;

public class ContentResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(ContentResource.class);

	private static SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Thread.currentThread()
			.getContextClassLoader().getResourceAsStream("mybatis-config.xml"));

	private String date;
	private String recordtype;

	@Override
	public void doInit() {
		date = (String) getRequest().getAttributes().get("date");
		recordtype = (String) getRequest().getAttributes().get("recordtype");
	}

	@Get("json")
	public Representation getContentResult() {
		logger.info("Request Url: " + getReference());
		logger.info("Params list —— recordtype: " + recordtype + ", date: " + date);
		try (SqlSession session = sqlSessionFactory.openSession();) {
			ContentDao contentDao = session.getMapper(ContentDao.class);
			HashMap<String, String> params = new HashMap<>();
			params.put("recordtype", recordtype);
			params.put("date", date);
			ContentResult result = contentDao.selectResult(params);
			return new JsonRepresentation(JsonUtils.toJson(result));
		} catch (Exception e) {
			logger.info("Exception is: " + e.getMessage());
			return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(20003,
					"your query params is illegal.").build()));
		}
	}

}
