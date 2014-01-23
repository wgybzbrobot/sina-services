package cc.pp.sina.bozhus.weibos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

/**
 * 多线程采集用户微博数据
 *
 * @author wgybzb
 */
public class HistoryWeibosDumpMain {

	private static Logger logger = LoggerFactory.getLogger(HistoryWeibosDumpMain.class);

	private static final String SINA_USER_WEIBOS_TABLENAMES = "sina_user_weibos_tablenames";
	private static final String SINA_USER_WEIBOS = "sina_user_weibos_";

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
		 * 从150上获取token数据
		 */
		TokenService tokenService = new TokenService();

		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);

		/**
		 * 链接数据库
		 */
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.38", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connected error.");
			return;
		}

		/**
		 * 创建存放表名数据表
		 */
		// 获取表名
		String tablename = SINA_USER_WEIBOS + System.currentTimeMillis() / 1000;
		try {
			weiboJDBC.createTablenamesTable(SINA_USER_WEIBOS_TABLENAMES);
			// 创建微博数据表
			weiboJDBC.createSinaUserWeibosTable(tablename);
			// 插入表名
			weiboJDBC.insertTablename(SINA_USER_WEIBOS_TABLENAMES, tablename);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}


		int count = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(new File("existed_uids")));) {
			String uid = "";
			while ((uid = br.readLine()) != null) {
				if (count > 1_6965_8000) {
					logger.info("Read at: " + count);
					if (uid.length() < 5) {
						logger.info("Uid: " + uid + " is invalid.");
						continue;
					}
					/**
					 * 检查是否更换微博数据表
					 */
					if (count % 1000 == 0) {
						// 查看微博数据表最大值
						int max = weiboJDBC.getMaxId(tablename);
						if (max > 1000_0000) {
							// 获取表名
							tablename = SINA_USER_WEIBOS + System.currentTimeMillis() / 1000;
							// 创建微博数据表
							weiboJDBC.createSinaUserWeibosTable(tablename);
							// 插入表名
							weiboJDBC.insertTablename(SINA_USER_WEIBOS_TABLENAMES, tablename);
						}
					}
					/**
					 * 多线程采集
					 */
					if (!pool.isShutdown()) {
						pool.execute(new HistoryWeibosDumpRun(weiboJDBC, tablename, uid, sinaWeiboInfoDao));
					} else {
						logger.info("Pool is shutdown.");
					}
				}
				count++;
			}
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}

		pool.shutdown();
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//		Thread.sleep(50 * 1000);
		//		weiboJDBC.dbClose();
	}

}
