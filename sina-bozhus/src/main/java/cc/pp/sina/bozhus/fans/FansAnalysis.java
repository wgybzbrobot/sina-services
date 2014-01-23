package cc.pp.sina.bozhus.fans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.algorithms.top.sort.InsertSort;
import cc.pp.sina.algorithms.top.sort.PPSort;
import cc.pp.sina.bozhus.common.UserPropsAnalysis;
import cc.pp.sina.bozhus.common.UserPropsArrange;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.FansAddDailyInfo;
import cc.pp.sina.domain.bozhus.FansAnalysisResult;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.UserExtendInfo;
import cc.pp.sina.domain.bozhus.UserTagInfo;
import cc.pp.sina.domain.bozhus.WbfenbuByHourResult;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.time.DailyUtils;

public class FansAnalysis {

	private static Logger logger = LoggerFactory.getLogger(FansAnalysis.class);

	private static final int TOP_N_FANS = 50;

	private static final int START_DATE = 20140104;

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: <uid>");
			System.exit(-1);
		}
		long uid = Long.parseLong(args[0]);

		FansAnalysisResult result = FansAnalysis.analysis(uid);
		System.out.println(JsonUtils.toJson(result));
	}

	/**
	 * 主分析函数
	 */
	public static FansAnalysisResult analysis(long uid) {

		FansAnalysisResult result = new FansAnalysisResult();
		/**
		 * 用户粉丝总信息
		 */
		SimpleFansInfo simpleFansInfo = FansAnalysisInfoUtils.getPPFansByUid(uid);
		if (simpleFansInfo == null) {
			logger.error("User:" + uid + " has no fansinfo.");
			return null;
		}
		// 1、粉丝总数
		result.setFansSum(simpleFansInfo.getFanscount());

		/**
		 * 分析粉丝信息
		 */
		// 获取粉丝列表
		String[] fansIds = simpleFansInfo.getFansuids().split(",");
		if (fansIds.length == 0) {
			logger.error("User:" + uid + " has no fansinfo.");
			return null;
		}

		/**
		 * 初始化变量
		 */
		int existuids = 0;
		int[] verifiedtype = new int[14]; // 4、认证分布
		int[] gradebyfans = new int[5]; // 6、粉丝等级分布（按粉丝量）
		int[] age = new int[5]; // 7、年龄分析
		int[] gender = new int[2]; // 8、性别分析
		int[] province = new int[101]; // 9、区域分析
		int[] quality = new int[2]; // 10、水军分析
		String[] topNFans = new String[TOP_N_FANS]; // 11、粉丝Top分析（按粉丝量）
		UserPropsAnalysis.initTopArray(topNFans);
		int[] verifiedratio = new int[2]; // 12、认证比例
		String[] top10uids = new String[10]; // 存放待分析的用户名，用户分析这些用户的发博时间线
		UserPropsAnalysis.initTopArray(top10uids);

		// 循环计算
		BozhuBaseInfo bozhuBaseInfo = null;
		for (String fansId : fansIds) {
			if (fansId.length() < 5) {
				continue;
			}
			bozhuBaseInfo = FansAnalysisInfoUtils.getUserBaseInfo(Long.parseLong(fansId));
			if (bozhuBaseInfo == null) {
				continue;
			}
			// 粉丝存在数
			existuids++;
			// 4、认证分布
			verifiedtype[UserPropsAnalysis.analysisVerifiedType(bozhuBaseInfo)]++;
			// 6、粉丝等级分布（按粉丝量）
			gradebyfans[UserPropsAnalysis.analysisFansClassByFans(bozhuBaseInfo)]++;
			// 7、年龄分析
			age[UserPropsAnalysis.analysisAge(bozhuBaseInfo)]++;
			// 8、性别分析
			gender[UserPropsAnalysis.analysisGender(bozhuBaseInfo)]++;
			// 9、区域分析
			province[UserPropsAnalysis.analysisProvince(bozhuBaseInfo)]++;
			// 10、水军分析
			quality[UserPropsAnalysis.analysisQuality(bozhuBaseInfo)]++;
			// 11、粉丝Top分析（按粉丝量）
			topNFans = InsertSort.toptable(topNFans, bozhuBaseInfo.getId() + "=" + bozhuBaseInfo.getFollowers_count());
			// 12、认证比例
			verifiedratio[UserPropsAnalysis.analysisVerified(bozhuBaseInfo)]++;
			// 存放待分析的用户名
			top10uids = InsertSort.toptable(top10uids, bozhuBaseInfo.getId() + "=" + bozhuBaseInfo.getStatuses_count());
		}
		/**
		 * 数据整理
		 */
		// 年龄分析（按微博量）
		UserPropsArrange.arrangeAge(result, age, existuids);
		// 区域分析
		UserPropsArrange.arrangeProvince(result, province, existuids);
		// 性别分析
		UserPropsArrange.arrangeGender(result, gender, existuids);
		// Top粉丝UID（按粉丝量）
		UserPropsArrange.arrangeTopNFans(result, topNFans);
		// 水军分析
		UserPropsArrange.arrangeQuality(result, quality, existuids);
		// 认证比例
		result.setAddVRatio(Float.toString((float) Math.round(((float) verifiedratio[1] / existuids) * 10000) / 100)
				+ "%");
		// 用户等级分析（按粉丝量）
		UserPropsArrange.arrangeFansGrade(result, gradebyfans, existuids);
		// 认证分布
		UserPropsArrange.arrangeVerifiedType(result, verifiedtype, existuids);
		// 活跃粉丝数
		UserPropsArrange.arrangeActiveCount(result, quality, simpleFansInfo, fansIds.length, existuids);
		// 用户标签信息
		result.setHotTags(getFansTags(topNFans));
		// 最近一周的粉丝增减曲线
		result.setFansAddTimeline(getFansCountTimeline(uid, 7));
		// 终端设备分布
		String[] uids = InsertSort.trans(top10uids);
		WbfenbuByHourResult wbfenbuByHourResult = WbfenbuByHour.analysis(uids);
		UserPropsArrange.arrangeWbSource(result, wbfenbuByHourResult);
		// 粉丝微博时间线
		UserPropsArrange.arrangeFansActiveTimeline(result, wbfenbuByHourResult);

		return result;
	}

	/**
	 * 获取用户标签信息
	 */
	public static List<HashMap<String, String>> getFansTags(String[] topNFans) {

		HashMap<String, Long> tagsAndSum = new HashMap<>();
		UserExtendInfo extendInfo = null;
		for (String uid : topNFans) {
			if (uid != null) {
				extendInfo = FansAnalysisInfoUtils.getExtendInfo(Long.parseLong(uid));
				if (extendInfo != null) {
					List<?> list = JSONArray.toList(JSONArray.fromObject(extendInfo.getTags()), UserTagInfo.class);
					Iterator<?> iterator = list.iterator();
					while (iterator.hasNext()) {
						UserTagInfo userTag = (UserTagInfo) iterator.next();
						if (tagsAndSum.get(userTag.getValue()) == null) {
							tagsAndSum.put(userTag.getValue(), userTag.getWeight());
						} else {
							tagsAndSum
									.put(userTag.getValue(), tagsAndSum.get(userTag.getValue()) + userTag.getWeight());
						}
					}
				}
			} else {
				break;
			}
		}
		List<HashMap<String, String>> result = PPSort.sortedToDoubleMap(tagsAndSum, "tag", "weight", 20);

		return result;
	}

	/**
	 * 获取最近N天的粉丝量数据
	 */
	public static HashMap<String, Integer> getFansCountTimeline(long uid, int NDaysFromNow) {

		String today = DailyUtils.getSomeDayDate(0);
		int min = Math.min(Integer.parseInt(today) - START_DATE, NDaysFromNow);
		HashMap<String, Integer> result = new HashMap<>();
		FansAddDailyInfo dailyInfo = null;
		for (int i = 1; i <= min; i++) {
			dailyInfo = FansAnalysisInfoUtils.getFansAddDailyInfo(uid, -i);
			if (dailyInfo != null) {
				result.put(DailyUtils.getSomeDayDate(-i), dailyInfo.getAllfanscount());
			}
		}

		return result;
	}

}
