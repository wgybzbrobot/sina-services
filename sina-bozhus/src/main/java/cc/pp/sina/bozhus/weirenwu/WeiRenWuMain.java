package cc.pp.sina.bozhus.weirenwu;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.ApplyThreadPool;
import cc.pp.sina.bozhus.common.PPAPI;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.weirenwu.WeiRenWu;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.tokens.service.TokenService;

public class WeiRenWuMain {

	private static Logger logger = LoggerFactory.getLogger(WeiRenWuMain.class);

	private final WeiRenWu weiRenWu = new WeiRenWu(MybatisConfig.ServerEnum.fenxi);

	private final SinaUserInfoDao sinaUserInfoDao;
	private final SinaWeiboInfoDao sinaWeiboInfoDao;

	public WeiRenWuMain() {
		TokenService tokenService = new TokenService();
		sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);
		sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		WeiRenWuMain weiRenWuMain = new WeiRenWuMain();
		weiRenWuMain.analysis();
	}

	/**
	 * 分析函数
	 */
	public void analysis() {

		final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		/**
		 * 获取用户uid列表
		 */
		List<String> uids = weiRenWu.selectAllSinaBozhuUids();

		BozhuBaseInfo baseInfo;
		for (String uid : uids) {
			/**
			 * 获取用户基础信息，判断出粉丝量大于10000的用户，并且只入库这些用户
			 */
			// 获取基本信息
			baseInfo = PPAPI.getSianUserBaseInfo(Long.parseLong(uid));
			if (baseInfo == null) {
				continue;
			}
			if ("unexisted".equalsIgnoreCase(baseInfo.getName())) {
				continue;
			}
			if (baseInfo.getFollowers_count() < 10_000) {
				continue;
			}
			if (!pool.isShutdown()) {
				pool.execute(new WeiRenWuRun(weiRenWu, sinaUserInfoDao, sinaWeiboInfoDao, uid));
			} else {
				logger.error("pool is shutdown.");
				throw new RuntimeException();
			}
		}

		pool.shutdown();
		try {
			pool.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

}
