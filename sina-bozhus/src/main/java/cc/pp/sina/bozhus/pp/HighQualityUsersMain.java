package cc.pp.sina.bozhus.pp;

import java.sql.SQLException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

public class HighQualityUsersMain {

	private static Logger logger = LoggerFactory.getLogger(HighQualityUsersMain.class);

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
		 * 获取皮皮用户以及实时token
		 */
		TokenService tokenService = new TokenService();

		/**
		 * 数据库
		 */
		logger.info("连接48数据库: ");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connected error.");
		}
		try {
			weiboJDBC.createSinaBaseInfoTable(HighQualityUsersRun.PP_HIGH_QUALITY_USERS);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		/**
		 * 新浪数据接口
		 */
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		/**
		 * 循环计算
		 */
		logger.info("循环计算： ");
		try {
			for (Long uid : tokenService.getSinaUids()) {
				if (!pool.isShutdown()) {
					pool.execute(new HighQualityUsersRun(weiboJDBC, sinaUserInfoDao, uid.toString()));
				} else {
					logger.info("Pool is shutdown.");
				}
			}
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
		}

		pool.shutdown();
		try {
			pool.awaitTermination(3 * 60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//		Thread.sleep(10 * 1000);
		//		weiboJDBC.dbClose();
	}

}
