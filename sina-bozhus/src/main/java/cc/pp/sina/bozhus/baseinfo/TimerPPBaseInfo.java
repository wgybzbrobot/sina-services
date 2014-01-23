package cc.pp.sina.bozhus.baseinfo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时采集皮皮用户基础信息，一周更新一次
 * @author wgybzb
 *
 */
public class TimerPPBaseInfo {

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		Timer timer = new Timer();
		/*
		 *  第一个参数：所执行的任务
		 *  第二个参数：开始执行的时间（毫秒）
		 *  第三个参数：定时执行的时间（毫秒）
		 */
		timer.schedule(new TimerPPUB(), 0, 1000 * 86400 * 7);

	}

	/**
	 * 定时任务
	 */
	public static class TimerPPUB extends TimerTask {

		public TimerPPUB() {
			//
		}

		@Override
		public void run() {
			PPUserBaseInfoMain.main(null);
		}

	}

}
