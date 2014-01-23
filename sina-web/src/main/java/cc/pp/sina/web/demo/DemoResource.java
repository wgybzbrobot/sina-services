package cc.pp.sina.web.demo;

import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.tool.TransUidData;

public class DemoResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(DemoResource.class);

	@Get("txt")
	public String getTest() {
		return "test ok";
	}

	// {"identify":"test","data":[{"type":"domain","url":"http://www.baidu.com"},{"type":"nickname","url":"http://www.google.com"}]}
	//  curl -X POST --data '{"identify":"test","data":[{"type":"domain","url":"http://www.baidu.com"},{"type":"nickname","url":"http://www.google.com"}]}' --header 'Content-Type: application/json' http://localhost:2222/bozhu/uid/trans
	@Post("json")
	public Representation acceptData(TransUidData data) {

		logger.info("identify=" + data.getIdentify());
		Representation result = new EmptyRepresentation();
		logger.info(getRequest().getResourceRef().getIdentifier() + "/" + data.getIdentify());
		result.setLocationRef(getRequest().getResourceRef().getIdentifier() + "/" + data.getIdentify());

		return result;
	}

}
