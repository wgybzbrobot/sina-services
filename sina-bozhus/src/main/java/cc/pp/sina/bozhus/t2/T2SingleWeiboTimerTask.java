package cc.pp.sina.bozhus.t2;

import java.util.Timer;
import java.util.TimerTask;

import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.tokens.service.TokenService;

public class T2SingleWeiboTimerTask {

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.schedule(new T2SWTimeTask(), 0, 3 * 3600 * 1000);

	}

	public static class T2SWTimeTask extends TimerTask {

		private static T2SingleWeiboAnalysis simgleWeiboAnalysis;

		public T2SWTimeTask() {
			SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(new TokenService());
			simgleWeiboAnalysis = new T2SingleWeiboAnalysis(sinaWeiboInfoDao);
		}

		@Override
		public void run() {
			simgleWeiboAnalysis.insertSingleWeiboResult();
		}
	}

}
