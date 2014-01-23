package cc.pp.sina.bozhus.tags;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.tokens.service.TokenService;

/**
 * 批量获取用户标签数据
 *
 * @author wgybzb
 */
public class BatchTagsMain {

	private static Logger logger = LoggerFactory.getLogger(BatchTagsMain.class);

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
		 * 新浪数据接口
		 */
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		/**
		 * 循环计算
		 */
		logger.info("循环计算： ");
		String uids = "";
		int count = 0;
		for (Long uid : tokenService.getSinaUids()) {
			if ((count > 0) && (count % 20 == 0)) {
				uids = uids.substring(0, uids.length() - 1);
				if (!pool.isShutdown()) {
					pool.execute(new BatchTagsRun(sinaUserInfoDao, uids));
				} else {
					logger.info("Pool is shutdown.");
				}
				uids = "";
			}
			uids = uids + uid + ",";
			count++;
		}
		uids = uids.substring(0, uids.length() - 1);
		if (!pool.isShutdown()) {
			pool.execute(new BatchTagsRun(sinaUserInfoDao, uids));
		}

		pool.shutdown();
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
