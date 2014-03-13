package cc.pp.sina.bozhus.fans;

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
import cc.pp.sina.utils.time.TimeUtils;

public class PPUserFansUpdateMain {

	private static Logger logger = LoggerFactory.getLogger(PPUserFansUpdateMain.class);

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
		logger.info("从150机器上获取皮皮用户uid和token: ");
		TokenService tokenService = new TokenService();

		/**
		 * 获取不存在的皮皮用户，通过日志抓取
		 */

		/**
		 * 连接48数据库
		 */
		logger.info("连接28数据库: ");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.28", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");

		// 创建皮皮新浪用户粉丝更新表
		try {
			weiboJDBC.createPPUserFansDaily(PPUserFansUpdateRun.PP_SINA_FANS_DAILY + TimeUtils.getTodayDaily());
			logger.info("Create table " + PPUserFansUpdateRun.PP_SINA_FANS_DAILY + " successed.");
		} catch (SQLException e) {
			logger.info("Create table " + PPUserFansUpdateRun.PP_SINA_FANS_DAILY + " unsuccessed.");
			throw new RuntimeException(e);
		}

		/**
		 * 新浪数据接口
		 */
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		/**
		 * 循环计算
		 */
		logger.info("循环计算: ");
		for (Long uid : tokenService.getSinaUids()) {
			if (!pool.isShutdown()) {
				pool.execute(new PPUserFansUpdateRun(weiboJDBC, uid.toString(), sinaUserInfoDao));
			}
		}

		pool.shutdown();
		try {
			pool.awaitTermination(100, TimeUnit.SECONDS);
			Thread.sleep(50 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		weiboJDBC.dbClose();
	}

}
