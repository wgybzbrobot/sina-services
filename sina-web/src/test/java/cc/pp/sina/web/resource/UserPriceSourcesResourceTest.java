package cc.pp.sina.web.resource;

import cc.pp.sina.domain.bozhus.price.PriceSource;
import cc.pp.sina.web.server.CommonInfoServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import static org.junit.Assert.assertEquals;

/**
 * Created by chenwei on 14-1-15.
 */
public class UserPriceSourcesResourceTest {

	CommonInfoServer server;

	@Test
	@Ignore
	public void testAddSource() throws Exception {
		ClientResource sources = new ClientResource("http://localhost:8088/sina/bozhu/uid/1/prices/sources");
		Representation resp = sources.post(new JacksonRepresentation<>(new PriceSource(1, "测试渠道", "qq", "phone")));
		assertEquals("http://localhost:8088/sina/bozhu/uid/1/prices/sources/1", resp.getLocationRef().toString());
		sources.release();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	@Before
	public void setUp() throws Exception {
		server = new CommonInfoServer();
		server.start();
	}
}
