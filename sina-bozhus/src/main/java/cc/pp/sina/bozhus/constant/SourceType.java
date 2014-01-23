package cc.pp.sina.bozhus.constant;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: 微博设备来源分析相关数据
 * @author wanggang
 * @version 1.1
 * @since 2013-05-27
 */
public class SourceType {

	private static Logger logger = LoggerFactory.getLogger(SourceType.class);
	private static Properties stProps;

	static {
		stProps = new Properties();
		try {
			stProps.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("sina-sourcetype.properties"));
		} catch (IOException e) {
			logger.info("Weibo source types loadd error.");
		}
	}

	/**
	 * 0——sina
	 * 1——pc
	 * 2——android
	 * 3——iphone
	 * 4——ipad
	 */
	public static int getCategory(String source) {

		int type = 5;
		boolean flag = true;
		String[] temp = null;
		if (flag) {
			temp = stProps.getProperty("sina").split(",");
			for (int i = 0; i < temp.length; i++) {
				if (source.contains(temp[i])) {
					type = 0;
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			temp = stProps.getProperty("pc").split(",");
			for (int i = 0; i < 3; i++) {
				if (source.contains(temp[i])) {
					type = 1;
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			temp = stProps.getProperty("android").split(",");
			for (int i = 0; i < 3; i++) {
				if (source.contains(temp[i])) {
					type = 2;
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			temp = stProps.getProperty("iphone").split(",");
			for (int i = 0; i < temp.length; i++) {
				if (source.contains(temp[i])) {
					type = 3;
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			temp = stProps.getProperty("ipad").split(",");
			for (int i = 0; i < temp.length; i++) {
				if (source.contains(temp[i])) {
					type = 4;
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			temp = stProps.getProperty("pc").split(",");
			for (int i = 3; i < temp.length; i++) {
				if (source.contains(temp[i])) {
					type = 1;
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			temp = stProps.getProperty("android").split(",");
			for (int i = 3; i < temp.length; i++) {
				if (source.contains(temp[i])) {
					type = 2;
					flag = false;
					break;
				}
			}
		}

		return type;
	}

}
