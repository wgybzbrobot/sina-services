package cc.pp.sina.query.conf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigTest {

	@Test
	public void testGetProperty() {
		assertEquals("bozhus_index", QueryConfig.getProperty("index.dir"));
	}

}
