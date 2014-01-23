package cc.pp.sina.dao.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.demo.SinaFriendsDemo;

public class SinaDaoDriver {

	private static Logger logger = LoggerFactory.getLogger(SinaDaoDriver.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage: Driver <class-name>");
			System.exit(-1);
		}

		String[] restArgs = new String[args.length - 1];
		System.arraycopy(args, 1, restArgs, 0, restArgs.length);

		switch (args[0]) {
		case "sinaFriendsDemo":
			logger.info("新浪用户关注数据CURD: ");
			SinaFriendsDemo.main(restArgs);
			break;
		default:
			break;
		}

	}

}
