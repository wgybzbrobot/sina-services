package cc.pp.content.library.statistic;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每天定时分析昨天内容库的使用数据
 *
 */
public class TimerContentAnalysis {

	private static Logger logger = LoggerFactory.getLogger(TimerContentAnalysis.class);

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: <tableNum>");
		}

		int tableNum = Integer.parseInt(args[0]);

		Timer timer = new Timer();
		/*
		 *  第一个参数：所执行的任务
		 *  第二个参数：开始执行的时间（毫秒）
		 *  第三个参数：定时执行的时间（毫秒）
		 */
		timer.schedule(new TimerCA(tableNum), 0, 1000 * 86400);
	}

	/**
	 * 定时任务
	 */
	public static class TimerCA extends TimerTask {

		private final int tableNum;

		public TimerCA(int tableNum) {
			this.tableNum = tableNum;
		}

		@Override
		public void run() {
			ContentAnalysisDaily cad = new ContentAnalysisDaily();
			logger.info("Start analyzer 'sendtime' data ...");
			cad.analysis(tableNum, -1, "sendtime");
			logger.info("Start analyzer 'recordtime' data ...");
			cad.analysis(tableNum, -1, "recordtime");
		}

	}

}
