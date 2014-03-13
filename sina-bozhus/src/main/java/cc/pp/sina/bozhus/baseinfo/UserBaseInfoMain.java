package cc.pp.sina.bozhus.baseinfo;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

/**
 * 全量用户基础信息更新
 *
 * @author wgybzb
 */
public class UserBaseInfoMain {

	private static Logger logger = LoggerFactory.getLogger(UserBaseInfoMain.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		while (true) {
			UserBaseInfoMain.start();
		}
	}

	public static void start() {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		/**
		 * 获取实时token
		 */
		TokenService tokenService = new TokenService();

		/**
		 * 数据库
		 */
		logger.info("连接48数据库: ");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.48", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");

		/**
		 * 新浪数据接口
		 */
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		/**
		 * 循环计算
		 */
		logger.info("循环计算： ");
		try {
			for (int i = 0; i < 32; i++) {
				logger.info("Read at: " + UserBaseInfoRun.SINA_USER_BASEINFO + i);
				List<String> uids = null;
				try {
					uids = weiboJDBC.getSinaUsername(UserBaseInfoRun.SINA_USER_BASEINFO + i);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				if (uids == null) {
					logger.error(UserBaseInfoRun.SINA_USER_BASEINFO + i + " has none info.");
				}
				logger.info(UserBaseInfoRun.SINA_USER_BASEINFO + i + " has " + uids.size() + " records.");
				int count = 0;
				for (String uid : uids) {
					if (count++ % 1_0000 == 0) {
						logger.info("Tackle at: " + count);
						// 每隔10000次休息1分钟，为了腾出空余API供其他应用使用
						Thread.sleep(1000 * 60);
					}
					if (!pool.isShutdown()) {
						pool.execute(new UserBaseInfoRun(weiboJDBC, sinaUserInfoDao, uid));
					} else {
						logger.info("Pool is shutdown.");
					}
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
		//		Thread.sleep(100 * 1000);
		//		weiboJDBC.dbClose();
	}

}
