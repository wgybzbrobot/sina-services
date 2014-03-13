package cc.pp.sina.bozhus.t2;

import java.util.Timer;
import java.util.TimerTask;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.tokens.service.TokenService;

public class T2InteractionsTimerTask {

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.schedule(new T2IAnalysisTimerTask(), 0, 86400 * 1000);
	}

	public static class T2IAnalysisTimerTask extends TimerTask {

		private static T2InteractionsAnalysis interactionsAnalysis;
		private static T2FansAnalysis fansAnalysis;

		public T2IAnalysisTimerTask() {
			TokenService tokenService = new TokenService();
			SinaUserInfoDao sinaUserInfoDao= new SinaUserInfoDaoImpl(tokenService);
			SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);
			interactionsAnalysis = new T2InteractionsAnalysis(sinaUserInfoDao, sinaWeiboInfoDao);
			fansAnalysis = new T2FansAnalysis(sinaUserInfoDao, sinaWeiboInfoDao);
		}

		@Override
		public void run() {
			try {
				interactionsAnalysis.insertUserInteractionResult();
				fansAnalysis.insertUserFansResult("new");
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
