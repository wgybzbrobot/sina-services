package cc.pp.sina.bozhus.sql;

import java.io.IOException;
import java.util.Properties;

public class WbDbParamsRead {

	public static Properties getDbParams() throws IOException {

		Properties prop = new Properties();
		prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("weibo_db.properties"));

		return prop;
	}

}
