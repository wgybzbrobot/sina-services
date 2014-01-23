package cc.pp.sina.bozhus.fans;

import java.sql.SQLException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

public class FansAnalysisMain {

	private static Logger logger = LoggerFactory.getLogger(FansAnalysisMain.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		/**
		 * 从150机器上获取皮皮用户uid和token
		 */
		logger.info("从150机器上获取皮皮用户uid: ");
		TokenService tokenService = new TokenService();

		/**
		 * 连接48数据库
		 */
		logger.info("连接48数据库: ");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connected error.");
		}
		// 创建粉丝分析结果数据表
		try {
			weiboJDBC.createFansAnalysisTable(FansAnalysisInfoUtils.PP_SINA_FANS_ANALYSIS);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		weiboJDBC.dbClose();

		/**
		 * 循环计算
		 */
		logger.info("循环计算: ");
		for (Long uid : tokenService.getSinaUids()) {
			if (!pool.isShutdown()) {
				pool.execute(new FansAnalysisRun(uid));
			}
		}

		pool.shutdown();
		try {
			pool.awaitTermination(100, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
