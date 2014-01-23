package cc.pp.sina.bozhus.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.tokens.service.TokenService;

public class BozhusLeftGetMain {

	//	private static Logger logger = LoggerFactory.getLogger(BozhuLibraryMain.class);

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

		String info = "";
		try (BufferedReader br = new BufferedReader(new FileReader(new File("output.txt")));) {
			while ((info = br.readLine()) != null) {
				if (!pool.isShutdown()) {
					pool.execute(new BozhusLeftGetRun(info.split(",")[1], sinaUserInfoDao, sinaWeiboInfoDao));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		pool.shutdown();
		try {
			pool.awaitTermination(300, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
