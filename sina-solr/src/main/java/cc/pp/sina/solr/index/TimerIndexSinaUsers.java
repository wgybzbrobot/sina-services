package cc.pp.sina.solr.index;

import java.util.Timer;
import java.util.TimerTask;

public class TimerIndexSinaUsers {

	public static final int DAY_INTERVAL = 10;

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		Timer timer = new Timer("Timer-IndexingSinaUsers");
		timer.schedule(new IndexSinaUsers(), 0, 86400 * DAY_INTERVAL);

	}

	public static class IndexSinaUsers extends TimerTask {

		public IndexSinaUsers() {
			//
		}

		@Override
		public void run() {
			IndexSinaUsersSingleNode.main(null);
		}

	}

}
