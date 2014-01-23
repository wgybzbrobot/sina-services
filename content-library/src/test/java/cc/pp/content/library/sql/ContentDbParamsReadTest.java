package cc.pp.content.library.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class ContentDbParamsReadTest {

	@Test
	public void testContentDbParamsRead() throws IOException {

		Properties prop = ContentDbParamsRead.getDbParams();
		assertEquals("com.mysql.jdbc.Driver", prop.get("db.driver"));
		assertTrue(prop.get("db.user").toString().length() > 2);
		assertTrue(prop.get("db.password").toString().length() > 2);
		assertTrue(prop.get("db.name").toString().length() > 2);
		assertTrue(prop.get("db.ip").toString().length() > 5);
		assertTrue(prop.get("db.port").toString().length() == 4);
	}


}
