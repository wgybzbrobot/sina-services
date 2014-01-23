package cc.pp.sina.dao.demo;

import java.util.List;

import cc.pp.sina.dao.timer.pictures.PPTimer;

public class PPTimerDemo {

	public static void main(String[] args) {

		List<String> result = PPTimer.getTimerPicInfos("wb_content_sina", 1389238391, 1389238400);
		System.out.println(result.size());

		PPTimer.insertTimerPicResult("pictures_result", "20140114", "12254", 125544);

	}

}
