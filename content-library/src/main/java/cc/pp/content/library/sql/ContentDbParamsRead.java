package cc.pp.content.library.sql;

import java.io.IOException;
import java.util.Properties;

public class ContentDbParamsRead {

	public static Properties getDbParams() throws IOException {

		Properties prop = new Properties();
		prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("content_db.properties"));

		return prop;
	}

}
