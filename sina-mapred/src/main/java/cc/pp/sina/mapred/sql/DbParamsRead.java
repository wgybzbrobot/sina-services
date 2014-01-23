package cc.pp.sina.mapred.sql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DbParamsRead {

	public static Properties getDbParams() throws FileNotFoundException, IOException {

		Properties props = new Properties();
		props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));

		return props;
	}

}
