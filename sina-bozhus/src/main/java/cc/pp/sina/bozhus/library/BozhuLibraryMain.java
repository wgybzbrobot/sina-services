package cc.pp.sina.bozhus.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.tokens.service.TokenService;

/**
 * 多线程采集用户微博数据
 *
 * @author wgybzb
 */
public class BozhuLibraryMain {

	private static Logger logger = LoggerFactory.getLogger(BozhuLibraryMain.class);
	private static final String SINA_BOZHUS_LIBRARY = "sina_bozhus_library";

	public static void main(String[] args) {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		/**
		 * 获取token数据
		 */
		//		TokenService tokenService = new TokenService("127.0.0.1", "root", "root", "pp_fenxi");
		TokenService tokenService = new TokenService("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.1.47:3306/pp_fenxi", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd");

		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);

		/**
		 * 获取用户uid，并采集微博数据
		 */
		//		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		WeiboJDBC weiboJDBC = new WeiboJDBC("192.168.1.47", "pp_fenxi", "q#tmuYzC@sqB6!ok@sHd", "pp_fenxi");
		if (!weiboJDBC.dbConnection()) {
			logger.info("Db connected error.");
			return;
		}
		// 创建博主数据表
		try {
			weiboJDBC.createSinaBozhuAllParamsTable(SINA_BOZHUS_LIBRARY);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(new File("sina_bozhus_uids")));) {
			String uid = "";
			while ((uid = br.readLine()) != null) {
				if (!pool.isShutdown()) {
					pool.execute(new BozhusLibraryRun(weiboJDBC, SINA_BOZHUS_LIBRARY, uid, sinaUserInfoDao,
							sinaWeiboInfoDao));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//		weiboJDBC.dbClose();
		pool.shutdown();
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
