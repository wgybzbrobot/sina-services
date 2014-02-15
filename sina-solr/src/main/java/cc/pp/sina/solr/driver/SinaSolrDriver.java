package cc.pp.sina.solr.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.solr.demo.Demo;
import cc.pp.sina.solr.index.IndexBozhuLibraryMain;
import cc.pp.sina.solr.index.IndexPPUsersMain;
import cc.pp.sina.solr.index.IndexSinaUsersMain;
import cc.pp.sina.solr.index.IndexSinaUsersStandalone;
import cc.pp.sina.solr.index.IndexSinaUsersThread;
import cc.pp.sina.solr.index.IndexSinaWeibosThread;

/**
 * 驱动类
 * @author wgybzb
 *
 */
public class SinaSolrDriver {

	private static Logger logger = LoggerFactory.getLogger(SinaSolrDriver.class);

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
		case "indexSinaUsersMain":
			logger.info("多线程索引新浪用户基础数据： ");
			IndexSinaUsersMain.main(leftArgs);
			break;
		case "indexSinaUsersThread":
			logger.info("单线程索引新浪用户基础数据： ");
			IndexSinaUsersThread.main(leftArgs);
			break;
		case "indexPPUsersMain":
			logger.info("多线程索引皮皮用户基础数据： ");
			IndexPPUsersMain.main(leftArgs);
			break;
		case "indexSinaWeibosThread":
			logger.info("单线程索引新浪用户微博数据： ");
			IndexSinaWeibosThread.main(leftArgs);
			break;
		case "indexBozhuLibraryMain":
			logger.info("单线程索引博主库博主数据： ");
			IndexBozhuLibraryMain.main(leftArgs);
			break;
		case "indexSinaUsersStandalone":
			logger.info("单台机器建立数据索引：");
			IndexSinaUsersStandalone.main(leftArgs);
			break;
		case "demo":
			logger.info("测试： ");
			Demo.main(leftArgs);
			break;
		default:
			return;
		}
	}

}
