package cc.pp.sina.bozhus.t2;

import java.util.Timer;
import java.util.TimerTask;

import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.tokens.service.TokenService;

public class T2FansTimerTask {

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.schedule(new T2FAnalysisTimerTask(), 0, 86400 * 1000 * 7);
	}

	public static class T2FAnalysisTimerTask extends TimerTask {

		private static T2FansAnalysis fansAnalysis;

		public T2FAnalysisTimerTask() {
			TokenService tokenService = new TokenService();
			fansAnalysis = new T2FansAnalysis(new SinaUserInfoDaoImpl(tokenService), new SinaWeiboInfoDaoImpl(
					tokenService));
		}

		@Override
		public void run() {
			try {
				fansAnalysis.insertUserFansResult("all");
			} catch (RuntimeException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
