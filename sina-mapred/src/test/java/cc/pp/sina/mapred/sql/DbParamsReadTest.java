package cc.pp.sina.mapred.sql;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import cc.pp.sina.mapred.sql.DbParamsRead;

public class DbParamsReadTest {

	@Test
	public void testGetDbParams() throws FileNotFoundException, IOException {

		Properties props = DbParamsRead.getDbParams();
		assertThat(props.getProperty("db.driver"), is("com.mysql.jdbc.Driver"));
		assertThat(props.getProperty("db.user"), is("pp_fenxi"));
		assertThat(props.getProperty("db.password"), is("q#tmuYzC@sqB6!ok@sHd"));
	}

}
