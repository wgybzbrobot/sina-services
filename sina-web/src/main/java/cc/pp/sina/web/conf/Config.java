package cc.pp.sina.web.conf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	private static Properties props = getProps("conf.properties");

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static Properties getProps(String confFileName) {
		try {
			Properties result = new Properties();
			Enumeration<URL> resources = Config.class.getClassLoader().getResources(confFileName);
			URL resource = resources.nextElement();
			logger.info("Loading sina-web resource: " + resource);
			while (resources.hasMoreElements()) {
				logger.warn("Multi resource: " + resources.nextElement());
			}
			try (InputStream in = resource.openStream();) {
				result.load(in);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
