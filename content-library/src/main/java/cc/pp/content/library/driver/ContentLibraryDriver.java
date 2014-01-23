package cc.pp.content.library.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.content.library.statistic.ContentAnalysisDaily;
import cc.pp.content.library.statistic.HeZuoFangPictures;
import cc.pp.content.library.statistic.TimerContentAnalysis;
import cc.pp.content.library.web.ContentApplication;

/**
 * 驱动类
 *
 */
public class ContentLibraryDriver {

	private static Logger logger = LoggerFactory.getLogger(ContentLibraryDriver.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage: Driver <class-name>");
			System.exit(-1);
		}
		String[] leftArgs = new String[args.length - 1];
		System.arraycopy(args, 1, leftArgs, 0, leftArgs.length);

		switch (args[0]) {
		case "contentAnalysisDaily":
			logger.info("皮皮内容库使用数据统计： ");
			try {
				ContentAnalysisDaily.main(leftArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "timerContentAnalysis":
			logger.info("定时统计皮皮内容库的使用数据： ");
			TimerContentAnalysis.main(leftArgs);
			break;
		case "contentApplication":
			logger.info("皮皮内容库数据结果接口： ");
			ContentApplication.main(leftArgs);
			break;
		case "heZuoFangPictures":
			logger.info("定时器图片数据： ");
			HeZuoFangPictures.main(leftArgs);
			break;
		default:
			return;
		}

	}

}
