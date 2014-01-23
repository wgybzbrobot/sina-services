package cc.pp.content.library.statistic;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.content.library.sql.ContentJDBC;
import cc.pp.sina.algorithms.utils.SortUtils;
import cc.pp.sina.domain.content.library.ContentInfo;
import cc.pp.sina.domain.content.library.ContentResultDaily;
import cc.pp.sina.domain.content.library.SendInfo;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.map.MapUtils;
import edu.fudan.nlp.keyword.Extractor;
import edu.fudan.nlp.keyword.WordExtract;
import edu.fudan.nlp.resources.StopWords;
import edu.fudan.nlp.tag.CWSTagger;

public class ContentAnalysisDaily {

	private static Logger logger = LoggerFactory.getLogger(ContentAnalysisDaily.class);

	private static final String TIMER_RECORD_TABLE = "wb_library_record_";
	private static final String CONTENT_TYPES_TABLE = "wb_library_type";
	private static final String WEIBO_LIBRARY_TABLE = "wb_library";
	private static final String CONTENT_RESULT_TABLE = "wb_content_result";

	private static final int THRESHOLD = 2;

	// 关键词提取器
	private static Extractor key;

	static {
		try {
			// 停用词
			StopWords sw = new StopWords("models/stopwords");
			// 分词器，添加词典
			CWSTagger seg = new CWSTagger("models/seg.c7.110918.gz", "models/dict.txt");
			key = new WordExtract(seg, sw);
		} catch (Exception e) {
			logger.info("The module of Keyword Extractor loaded unsuccessful.");
		}
	}

	public ContentAnalysisDaily() {
		//
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		if (args.length < 6) {
			System.err.println("Usage: ContentAnalysisDaily <-tableNum int (all record tables, eg: 80)> "
					+ "<-dayFromNow int (0-today,-1-yestady and so on)> <-recordType str (sendtime or recordtime)>");
			System.exit(-1);
		}

		int tableNum = 84;
		int day = 0; // 代表今天
		String recordType = "sendtime"; // sendtime/recordtime
		for (int i = 0; i < args.length; i++) {
			if ("-tableNum".equals(args[i])) {
				tableNum = Integer.parseInt(args[i + 1]);
				i++;
			} else if ("-dayFromNow".equals(args[i])) {
				day = Integer.parseInt(args[i + 1]);
				i++;
			} else if ("-recordType".equals(args[i])) {
				recordType = args[i + 1];
				i++;
			}
		}

		ContentAnalysisDaily cad = new ContentAnalysisDaily();
		cad.analysis(tableNum, day, recordType);
	}

	/**
	 * 分析函数
	 */
	public void analysis(int tableNum, int day, String recordType) {

		/*
		 * 获取昨天的定时数据，循环读取每个数据表
		 * 服务器：192.168.1.22：3304
		 *       library_record，w@15z!z21H3h#Yoi5f8Q
		 *       pp_library_record
		 */
		ContentJDBC recordJDBC = new ContentJDBC("192.168.1.22", "3304", "library_record", "w@15z!z21H3h#Yoi5f8Q",
				"pp_library_record");
		if (recordJDBC.dbConnection()) {
			logger.info("RecordJDBC connected successful.");
		} else {
			logger.info("RecordJDBC connected unsuccessful.");
			return;
		}
		logger.info("Getting Timer Records ...");
		// 某一天的定时纪录数据
		List<SendInfo> timerRecords = new ArrayList<>();
		try {
			for (int i = 0; i < tableNum; i++) {
				// 遍历每个定时器纪录表
				List<SendInfo> temp = recordJDBC.getLibraryRecord(TIMER_RECORD_TABLE + i, "sina", recordType,
						getSomeDayTime(day), getSomeDayTime(day + 1));
				logger.info("Table " + TIMER_RECORD_TABLE + i + " has " + temp.size() + " records.");
				for (SendInfo s : temp) {
					timerRecords.add(s);
				}
			}
		} catch (SQLException e) {
			logger.info("SQLException: " + e.getMessage());
			throw new RuntimeException(e);
		}
		logger.info("Timer Record: " + timerRecords.size());
		if (timerRecords.size() == 0) {
			return;
		}
		recordJDBC.dbClose();

		/*
		 * 读取内容库微博类别
		 * 服务器：192.168.1.33:3302
		 *       pp_library,0U@dKz#Qy4KX!Cwg@d9c
		 *       pp_library
		 */
		ContentJDBC contentTypeJDBC = new ContentJDBC("192.168.1.33", "3302", "pp_library", "0U@dKz#Qy4KX!Cwg@d9c",
				"pp_library");
		if (contentTypeJDBC.dbConnection()) {
			logger.info("ContentTypeJDBC connected successful.");
		}
		logger.info("Getting Weibo Content Types ...");
		HashMap<Integer, String> contentTypes = null;
		try {
			contentTypes = contentTypeJDBC.getWbLibraryType(CONTENT_TYPES_TABLE);
		} catch (SQLException e) {
			logger.info("Getting weibo content types unsuccessful.");
			return;
		}
		logger.info("Weibo Content Types: " + contentTypes.size());
		if (contentTypes.size() == 0) {
			return;
		}

		/**
		 * 循环计算
		 */
		// 变量
		HashMap<Integer, Integer> contentByHour = new HashMap<>(); // 记录每小时内容数
		HashMap<Integer, HashMap<Long, Integer>> userByHour = new HashMap<>(); // 记录每小时的不同用户及其使用次数
		HashMap<Integer, Integer> contentIds = new HashMap<>(); // 记录内容次数
		HashMap<Integer, Integer> columResult = new HashMap<>(); // 记录每个栏目次数
		int[] picVidMus = new int[3]; // 记录picture、video、music次数
		HashMap<Integer, Integer> columResultByPic = new HashMap<>(); // 记录使用图片的栏目次数
		HashMap<Integer, Integer> statuses = new HashMap<>(); // 记录status
		int[] sum24 = new int[24];
		int[] content24 = new int[24]; // 记录每小时来自内容库的量
		//			SimpleDateFormat fo1 = new SimpleDateFormat("HH");
		int[][] temp24h = new int[24][MapUtils.getMaxKey(contentTypes) + 1];//[小时][栏目]
		for (int i1 = 0; i1 < 24; i1++) {
			for (int i2 = 0; i2 < 200; i2++) {
				temp24h[i1][i2] = 0;
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("HH");
		int empty = 0; // 内容库错误条数
		int powerCount = 0; // 来自皮皮动力的数量

		/*
		 * 计算数据
		 */
		ContentResultDaily result = new ContentResultDaily();
		// 平台类型
		result.setType("sina");
		// 纪录类型
		result.setRecordType(recordType);
		// 0、统计日期
		result.setDate(getSomeDayDate(day));
		// 2、每天内容总数
		result.setContentCount(timerRecords.size());

		/*
		 * 循环计算
		 */
		//		int count = 0;
		ContentInfo contentInfo = null;
		for (SendInfo sendInfo : timerRecords) {
			//			logger.info("Computing at: " + count++);
			if (sendInfo.getIspower() == 1) {
				powerCount++;
			}
			/*
			 * 获取该条纪录对应的详细内容信息
			 * 服务器：
			 */
			try {
				contentInfo = contentTypeJDBC.getWbLibrary(WEIBO_LIBRARY_TABLE, sendInfo.getCid());
			} catch (SQLException e) {
				logger.info("Content: " + sendInfo.getCid() + " does not existed.");
			}
			if (contentInfo == null) {
				continue;
			}
			// 统计数据
			if (sendInfo.getUsername() == 0) {
				empty++;
				continue;
			}
			// 统计每个小时的内容使用量
			int hour = Integer.parseInt(format.format(new Date(sendInfo.getSendtime() * 1000l)));

			if (contentByHour.get(hour) == null) {
				contentByHour.put(hour, 1);
			} else {
				contentByHour.put(hour, contentByHour.get(hour) + 1);
			}
			// 统计每小时的不同用户及其使用次数
			if (userByHour.get(hour) == null) {
				HashMap<Long, Integer> users = new HashMap<>();
				users.put(sendInfo.getUsername(), 1);
				userByHour.put(hour, users);
			} else {
				if (userByHour.get(hour).get(sendInfo.getUsername()) == null) {
					userByHour.get(hour).put(sendInfo.getUsername(), 1);
				} else {
					userByHour.get(hour).put(sendInfo.getUsername(),
							userByHour.get(hour).get(sendInfo.getUsername()) + 1);
				}
			}
			// 统计每条内容使用次数
			if (contentIds.get(sendInfo.getCid()) == null) {
				contentIds.put(sendInfo.getCid(), 1);
			} else {
				contentIds.put(sendInfo.getCid(), contentIds.get(sendInfo.getCid()) + 1);
			}
			// 统计每天每个栏目使用次数
			if (columResult.get(contentInfo.getTid()) == null) {
				columResult.put(contentInfo.getTid(), 1);
			} else {
				columResult.put(contentInfo.getTid(), columResult.get(contentInfo.getTid()) + 1);
			}
			// 统计每天24个时段排名前10的栏目
			temp24h[hour][contentInfo.getTid()]++;
			// 统计每天每个小时内容来自内容库的比例，这些都来自内容库，所以最终比例为1
			sum24[hour]++;
			content24[hour]++;
			// 分别含图片、视频、音乐数
			if (contentInfo.getPicture().length() > 0) {
				picVidMus[0]++; // 1----图片
				if (columResultByPic.get(contentInfo.getTid()) == null) {
					columResultByPic.put(contentInfo.getTid(), 1);
				} else {
					columResultByPic.put(contentInfo.getTid(), columResultByPic.get(contentInfo.getTid()) + 1);
				}
			}
			if (contentInfo.getVideo().length() > 0)
				picVidMus[1]++; // 2----视频
			if (contentInfo.getMusic().length() > 0)
				picVidMus[2]++; // 3----音乐
			/** 16、每天使用的所有图片排名前10的类型或栏目 **/
			/** 记录ip使用次数 **/
			/** 18、排名前10的IP段 ***/
			/*** 19、自定义来源分布 **/
			// 20、发送状态情况
			if (statuses.get(sendInfo.getStatus()) == null) {
				statuses.put(sendInfo.getStatus(), 1);
			} else {
				statuses.put(sendInfo.getStatus(), statuses.get(sendInfo.getStatus()) + 1);
			}
			/** 21、所有关键词 **/
			/** 22、top100内容中的关键词 **/

			contentInfo = null;
		}
		// 14、每天内容库使用量前100的内容
		List<HashMap<String, Integer>> top100ContentIds = SortUtils.sortedToDoubleMap(contentIds, "content", "count",
				100);
		for (HashMap<String, Integer> temp : top100ContentIds) {
			try {
				contentInfo = contentTypeJDBC.getWbLibrary(WEIBO_LIBRARY_TABLE, temp.get("content"));
			} catch (SQLException e) {
				logger.info("Content: " + temp.get("content") + " does not existed.");
			}
			if (contentInfo != null) {
				HashMap<String, String> t = new HashMap<>();
				t.put("content", contentInfo.getContent());
				t.put("count", String.valueOf(temp.get("count")));
				result.getContentTop100().add(t);
			}
		}
		contentTypeJDBC.dbClose();
		// 来自皮皮动力的比例
		result.setPowerRatio((float) powerCount / timerRecords.size());
		int realCount = 0;
		int realContentCount = 0;
		String[] realsRatio = new String[24];
		String[] realContentRatio = new String[24];
		// 用户质量需要根据“统计每小时的不同用户数、内容使用频次”确定
		// 统计用户质量和真实内容使用量，和用户总数
		int allUserCount = 0;
		for (int k = 0; k < 24; k++) {
			if (userByHour.get(k) != null) {
				String[] rr = MapUtils.getRealRatio(userByHour.get(k), THRESHOLD);
				realsRatio[k] = rr[0];
				realContentRatio[k] = rr[1];
				int[] rc = MapUtils.getRealCount(userByHour.get(k), THRESHOLD);
				realCount += rc[0];
				realContentCount += rc[1];
				allUserCount += userByHour.get(k).size();
			}
		}
		// 5、每天使用内容库的用户数
		result.setUserCount(allUserCount);
		// 3、每天来自内容库内容的比率
		result.setFromContentRatio(1.0f);
		// 4、每天每小时内容库内容的使用比率，因为都是内容库数据，所以都为1
		float ratio = 0.0f;
		for (int k = 0; k < 24; k++) {
			ratio = (float) content24[k] / sum24[k];
			result.getFromContentRatioByHour().put(k, Float.toString((float) Math.round(ratio * 10000) / 100) + "%");
		}
		// 6、每天每小时的使用用户数
		for (int k = 0; k < 24; k++) {
			if (userByHour.get(k) != null) {
				result.getUserByHour().put(k, userByHour.get(k).size());
			} else {
				result.getUserByHour().put(k, 0);
			}
		}
		// 7、每天使用使用用户质量
		ratio = (float) realCount / allUserCount; //真实用户比例
		result.getUserQuality().put("real", Float.toString((float) (Math.round(ratio * 10000)) / 100) + "%");
		result.getUserQuality().put("mask", Float.toString((float) (Math.round((1 - ratio) * 10000)) / 100) + "%");
		// 8、每天每小时的使用用户质量
		// 10、每天每小时真实用户使用内容库的比例
		for (int k = 0; k < 24; k++) {
			result.getUserQualityByHour().put(k, realsRatio[k]);
			result.getRealContentUsedByHour().put(k, realContentRatio[k]);
		}
		// 9、每天内容库被使用的质量
		ratio = (float) realContentCount / (timerRecords.size() - empty); //真实用户使用内容比例
		result.getContentUsedQuality().put("contentreal",
				Float.toString((float) (Math.round(ratio * 10000)) / 100) + "%");
		result.getContentUsedQuality().put("contentmask",
				Float.toString((float) (Math.round((1 - ratio) * 10000)) / 100) + "%");
		// 11、每天每个栏目的使用数量
		for (Entry<Integer, Integer> temp : columResult.entrySet()) {
			result.getColumnUsedCount().put(contentTypes.get(temp.getKey()), temp.getValue());
		}
		// 12、每天各栏目的使用量排序
		transMap(result, contentTypes, SortUtils.sortedToDoubleMap(columResult, "colum", "count", 10), "colum", "count");
		// 13、每天24小时使用量排名前10的栏目
		result.setColumnTop10ByHour(SortUtils.getTop10PeerHour(temp24h, contentTypes));
		// 15、每天所有内容中分别含图片、视频、音乐的比例
		result.getPicVideoMusicRatio()
				.put("pictureratio",
						Float.toString((float) (Math.round(((float) picVidMus[0] / (timerRecords.size() - empty)) * 10000)) / 100)
								+ "%");
		result.getPicVideoMusicRatio().put(
				"videoratio",
						Float.toString((float) (Math.round(((float) picVidMus[1] / (timerRecords.size() - empty)) * 10000)) / 100)
						+ "%");
		result.getPicVideoMusicRatio().put(
				"musicratio",
						Float.toString((float) (Math.round(((float) picVidMus[2] / (timerRecords.size() - empty)) * 10000)) / 100)
						+ "%");
		// 16、每天使用的所有图片排名前10的类型或栏目
		this.transMap(result, contentTypes, SortUtils.sortedToDoubleMap(columResultByPic, "colum", "count", 10),
				"colum", "count");
		// 17、每天内容库使用量前100的IP
		// 18、排名前20的IP段
		// 19、自定义来源分布
		// 20、发送状态情况
		result.setSendStatus(MapUtils.tranToRatio(statuses));
		/** 21、所有关键词 **/
		//		result.setAllKeywords((HashMap<String, Integer>) key.extract(JsonUtils.toJson(weiboTexts), 200));
		/** 22、top100内容中的关键词 **/
		result.setKeywordsOfTopNContent((HashMap<String, Integer>) key.extract(
				JsonUtils.toJson(result.getContentTop100()), 100));
		/********************0、内容库错误记录率***************************/
		result.setErrorRatio((float) empty / timerRecords.size());

		//		System.out.println(JsonUtils.toJson(result));

		/*
		 * 存入数据库
		 * 数据库：192.168.1.6 3306
		 *       pp_fenxi, Gd3am#nB0kiSbiN!o@4KQ
		 *       pp_fenxi
		 */
		ContentJDBC resultJDBC = new ContentJDBC("192.168.1.6", "3306", "pp_fenxi", "Gd3am#nB0kiSbiN!o@4KQ", "pp_fenxi");
		if (resultJDBC.dbConnection()) {
			logger.info("ResultJDBC connected successful.");
		}
		try {
			//			resultJDBC.createContentResultTable(CONTENT_RESULT_TABLE);
			resultJDBC.insertContentResultTable(CONTENT_RESULT_TABLE, result);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("Creating content analysis table or Inserting result unsuccessful.");
		}
		resultJDBC.dbClose();
		logger.info("Result inserted successful.");
	}

	private void transMap(ContentResultDaily result, HashMap<Integer, String> contentTypes,
			List<HashMap<String, Integer>> columnUsedSort, String keyName1,
			String keyName2) {
		for (HashMap<String, Integer> temp : columnUsedSort) {
			HashMap<String, String> t = new HashMap<>();
			t.put(keyName1, contentTypes.get(temp.get(keyName1)));
			t.put(keyName2, String.valueOf(temp.get(keyName2)));
			result.getColumnUsedSort().add(t);
		}
	}

	/**
	 * 获取当天的起始时间
	 */
	public static long getSomeDayTime(int N) {
		Date now = new Date();
		long time = now.getTime() / 1000 + N * 86400;
		time = time - time % 86400;
		return time;
	}

	/**
	 * 获取某天日期
	 */
	public static String getSomeDayDate(int N) {
		Date now = new Date();
		long time = now.getTime() / 1000 + N * 86400;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(time * 1000l));
		return date.replaceAll("-", "");
	}

}
