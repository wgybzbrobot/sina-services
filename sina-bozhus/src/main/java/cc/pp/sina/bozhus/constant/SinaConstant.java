package cc.pp.sina.bozhus.constant;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinaConstant {

	private static Logger logger = LoggerFactory.getLogger(SinaConstant.class);

	// 省份
	private static Properties propsProvince;
	// 认证类型
	private static Properties propsVerify;
	private static Properties propsDetailVerify;

	static {
		propsProvince = new Properties();
		propsVerify = new Properties();
		propsDetailVerify = new Properties();
		try {
			propsProvince.load(Thread.currentThread().getContextClassLoader(). //
					getResourceAsStream("sina-province.properties"));
			propsVerify.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("sina-verifytype.properties"));
			propsDetailVerify.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("sina-verifytype-detail.properties"));
		} catch (IOException e) {
			logger.info("City properties load error.");
		}
	}

	/**
	 * 省份编码
	 */
	public static String getProvince(String key) {
		return propsProvince.getProperty(key);
	}

	/**
	 * 认证类型：蓝V、黄V、微女郎、达人、已故V、普通用户
	 */
	public static String getVerify(String key) {
		return propsVerify.getProperty(key);
	}

	/**
	 * 详细认证类型：普通用户、名人、政府、企业、媒体、校园、网站、应用、团体/机构、
	 *               待审企业、初级达人、中高级达人、已故V用户、微博女郎
	 */
	public static String getDetailVerify(String key) {
		return propsDetailVerify.getProperty(key);
	}

	public static void main(String[] args) {
		System.out.println(SinaConstant.getVerify("0"));
	}

}
