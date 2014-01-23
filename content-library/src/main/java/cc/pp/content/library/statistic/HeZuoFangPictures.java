package cc.pp.content.library.statistic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.dao.timer.pictures.PPTimer;
import cc.pp.sina.domain.timer.library.TimerPicturesOne;
import cc.pp.sina.domain.timer.library.TimerPicturesTwo;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.time.TimeUtils;

/**
 * 合作方图片统计
 * @author wgybzb
 *
 */
public class HeZuoFangPictures {

	private static Logger logger = LoggerFactory.getLogger(HeZuoFangPictures.class);

	private static final String READ_TABLE = "wb_content_sina";
	private static final String WRITE_TABLE = "pictures_result";

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.schedule(new TimerPicTasker(), 0, 1000 * 86400);

	}

	/**
	 * 定时类
	 */
	public static class TimerPicTasker extends TimerTask {
		@Override
		public void run() {
			HeZuoFangPictures.analysis();
		}
	}

	/**
	 * 分析函数
	 */
	public static void analysis() {

		long todayStart = TimeUtils.getSomeDayTime(-1);
		String date = TimeUtils.getTodayDaily(-1);
		List<String> picInfos = PPTimer.getTimerPicInfos(READ_TABLE, todayStart, todayStart + 86400);
		logger.info("Date: " + date + "'s pic records is " + picInfos.size());
		HashMap<String, Integer> result = new HashMap<>();
		for (String picInfo : picInfos) {
			String name = getFactoryName(picInfo);
			if (!"0".equals(name)) {
				if (result.get(name) == null) {
					result.put(name, 1);
				} else {
					result.put(name, result.get(name) + 1);
				}
			}
		}
		for (Entry<String, Integer> temp : result.entrySet()) {
			PPTimer.insertTimerPicResult(WRITE_TABLE, date, temp.getKey(), temp.getValue());
		}
	}

	public static String getFactoryName(String str) {
		if (str.contains("name") && str.contains("type") && str.contains("path") && str.length() > 10) {
			String picName = null;
			if (str.contains("from")) {
				picName = getPicNameTwo(str);
			} else {
				picName = getPicNameOne(str);
			}
			int index = picName.indexOf("_");
			if (index > 0) {
				return picName.substring(0, index);
			} else {
				return "0";
			}
		} else {
			return "0";
		}
	}

	private static String getPicNameOne(String str) {
		try {
			TimerPicturesOne timerPic = JsonUtils.getObjectMapper().readValue(str.substring(1,
					str.length() - 1), TimerPicturesOne.class);
			return timerPic.getName();
		} catch (IOException e) {
			logger.error("Pictures is " + str + ", IOException: " + e.getMessage());
			return null;
		}
	}

	private static String getPicNameTwo(String str) {
		try {
			TimerPicturesTwo timerPic = JsonUtils.getObjectMapper().readValue(str.substring(1, str.length() - 1),
					TimerPicturesTwo.class);
			return timerPic.getName();
		} catch (IOException e) {
			logger.error("Pictures is " + str + ", IOException: " + e.getMessage());
			return null;
		}
	}

}
