package cc.pp.sina.web.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.web.bozhu.IndexBozhusData;
import cc.pp.sina.web.company.PPCompanyIndexMain;
import cc.pp.sina.web.server.AnalysisInfoServer;
import cc.pp.sina.web.server.BozhuLibraryServer;
import cc.pp.sina.web.server.CommonInfoServer;
import cc.pp.sina.web.server.ComputeInfoServer;
import cc.pp.sina.web.server.FuzzySearchServer;
import cc.pp.sina.web.server.PPCompanyServer;
import cc.pp.sina.web.server.ToolServer;
import cc.pp.sina.web.server.WordSegServer;

/**
 * 驱动类
 * @author wgybzb
 *
 */
public class SinaWebDriver {

	private static Logger logger = LoggerFactory.getLogger(SinaWebDriver.class);

	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage: Driver <class-name>");
			System.exit(-1);
		}
		System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");
		String[] leftArgs = new String[args.length - 1];
		System.arraycopy(args, 1, leftArgs, 0, leftArgs.length);

		switch (args[0]) {
		case "analysisInfoServer":
			logger.info("新浪博主数据服务： ");
			AnalysisInfoServer.main(leftArgs);
			break;
		case "fuzzySearchServer":
			logger.info("模糊搜索服务： ");
			FuzzySearchServer.main(leftArgs);
			break;
		case "wordSegServer":
			logger.info("分词服务： ");
			WordSegServer.main(leftArgs);
			break;
		case "commonInfoServer":
			logger.info("博主数据服务： ");
			CommonInfoServer.main(leftArgs);
			break;
		case "computeInfoServer":
			logger.info("计算数据服务： ");
			ComputeInfoServer.main(leftArgs);
			break;
		case "ppCompanyIndexMain":
			logger.info("索引皮皮企业用户的数据： ");
			PPCompanyIndexMain.main(leftArgs);
			break;
		case "ppCompanyServer":
			logger.info("搜索皮皮企业用户的数据： ");
			PPCompanyServer.main(leftArgs);
			break;
		case "toolServer":
			logger.info("工具接口： ");
			ToolServer.main(leftArgs);
			break;
		case "indexBozhusData":
			logger.info("索引博主库数据： ");
			IndexBozhusData.main(leftArgs);
			break;
		case "bozhuLibraryServer":
			logger.info("搜索博主库数据： ");
			BozhuLibraryServer.main(leftArgs);
		default:
			return;
		}

	}

}
