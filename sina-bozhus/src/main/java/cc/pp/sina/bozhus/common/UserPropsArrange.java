package cc.pp.sina.bozhus.common;

import java.util.HashMap;

import cc.pp.sina.bozhus.constant.SinaConstant;
import cc.pp.sina.domain.bozhus.FansAnalysisResult;
import cc.pp.sina.domain.bozhus.SimpleFansInfo;
import cc.pp.sina.domain.bozhus.WbfenbuByHourResult;

/**
 * 用户特性数据计算
 * @author wgybzb
 *
 */
public class UserPropsArrange {

	/**
	 * Top N 区域地区的分布比例
	 */
	public static HashMap<String, String> arrangeTopNProvinces(int[] province, int existUids,
			int topN) {

		HashMap<String, String> topNProvinces = new HashMap<String, String>();
		String[] provinceTable = UserPropsAnalysis.analysisSortedProvince(province);
		int index1 = 0;
		for (int i = 0; i < province.length; i++) {
			if (index1 > topN - 1)
				break;
			int t = Integer.parseInt(provinceTable[i].substring(0, provinceTable[i].indexOf("=")));
			if ((t < 100) && (t > 0)) {
				index1++;
				topNProvinces.put(SinaConstant.getProvince(Integer.toString(t)),
						Float.toString((float) Math.round(((float) province[t] / existUids) * 10000) / 100) + "%");
			}
		}

		return topNProvinces;
	}

	/**
	 * 用户微博创建天数
	 */
	public static int arrangeAverageWeibosByDay(long userCreateAt) {

		int userTimeDiff = 1;
		if (System.currentTimeMillis() / 1000 - userCreateAt > 86400) {
			userTimeDiff = (int) ((System.currentTimeMillis() / 1000 - userCreateAt) / 86400);
		}

		return userTimeDiff;
	}

	/**
	 * 用户的活跃度
	 */
	public static int arrangeUserActivation(int userFansSum, int userWeiboSum, int usertimediff) {

		float active = 0.00f;
		float alpha = 0.4f;
		float beta = 0.6f;
		active = 1 - alpha / (1 + (float) userFansSum / usertimediff) - beta
				/ (1 + (float) userWeiboSum / usertimediff);

		return Math.round(Math.abs(active) * 10);
	}

	/**
	 * 用户影响力
	 */
	public static int arrangeUserInfluence(int userFansSum, int[] fansClassByFans, int[] verifiedRatio, int existUids,
			int activenum, int cursor) {

		float inf = 0.00f, iflowersclass = 0.0f;
		if ((cursor * 200 >= userFansSum) && (cursor * 200 < userFansSum + 200)) {
			iflowersclass = (float) (fansClassByFans[2] + fansClassByFans[3] + fansClassByFans[4]) / userFansSum;
		} else {
			iflowersclass = (float) (fansClassByFans[2] + fansClassByFans[3] + fansClassByFans[4]) / (cursor * 200);
		}
		// 影响力计算公式
		float temp = (float) verifiedRatio[1] / existUids;
		inf = (float) (0.8 * (Math.log(activenum) / Math.log(1.5)) + 0.1
				* Math.abs(Math.log(iflowersclass) / Math.log(2)) + 0.1 * Math.abs(Math.log(temp) / Math.log(2)));
		if (inf > 100) {
			inf = 3;
		}
		int influence = Math.round(inf * 100 / 30);
		if (influence > 100)
			influence = 100;

		return influence;
	}

	/**
	 * 用户粉丝的粉丝总量，含自己的粉丝数
	 */
	public static long arrangeAllFansCountOfUserFans(long allFansCount, int userFansSum, int cursor, int existUids) {

		if ((cursor * 200 >= userFansSum) && (cursor * 200 < userFansSum + 200)) {
			return allFansCount + userFansSum;
		} else {
			return allFansCount * userFansSum / existUids + userFansSum;
		}
	}

	/**
	 * 用户粉丝的活跃粉丝总量，含自己的活跃粉丝数
	 */
	public static long arrangeAllActiveFansCountOfUserFans(long allActiveFansSount, int userFansSum, int cursor,
			int existUids, int activenum) {

		if ((cursor * 200 >= userFansSum) && (cursor * 200 < userFansSum + 200)) {
			return allActiveFansSount + activenum;
		} else {
			return allActiveFansSount * userFansSum / existUids + activenum;
		}
	}

	/**
	 * 年龄分析结果整理
	 */
	public static void arrangeAge(FansAnalysisResult result, int[] age, int existuids) {
		result.getAge().put("others",
				Float.toString((float) Math.round(((float) age[0] / existuids) * 10000) / 100) + "%");
		result.getAge()
				.put("80s", Float.toString((float) Math.round(((float) age[1] / existuids) * 10000) / 100) + "%");
		result.getAge()
				.put("90s", Float.toString((float) Math.round(((float) age[2] / existuids) * 10000) / 100) + "%");
		result.getAge()
				.put("70s", Float.toString((float) Math.round(((float) age[3] / existuids) * 10000) / 100) + "%");
		result.getAge()
				.put("60s", Float.toString((float) Math.round(((float) age[4] / existuids) * 10000) / 100) + "%");
	}

	/**
	 * 区域分析结果整理
	 */
	public static void arrangeProvince(FansAnalysisResult result, int[] province, int existuids) {
		if (province[0] != 0) {
			result.getLocation().put("400",
					Float.toString((float) Math.round(((float) province[0] / existuids) * 10000) / 100) + "%");
		}
		for (int j = 1; j < province.length; j++) {
			if (province[j] != 0) {
				result.getLocation().put(Integer.toString(j),
						Float.toString((float) Math.round(((float) province[j] / existuids) * 10000) / 100) + "%");
			}
		}
	}

	/**
	 * 性别分析结果整理
	 */
	public static void arrangeGender(FansAnalysisResult result, int[] gender, int existuids) {
		result.getGender().put("m",
				Float.toString((float) Math.round(((float) gender[0] / existuids) * 10000) / 100) + "%");
		result.getGender().put("f",
				Float.toString((float) Math.round(((float) gender[1] / existuids) * 10000) / 100) + "%");
	}

	/**
	 * Top粉丝结果整理
	 */
	public static void arrangeTopNFans(FansAnalysisResult result, String[] topNfans) {
		for (int n = 0; n < topNfans.length; n++) {
			if (topNfans[n].length() > 5) {
				result.getTopNByFans().put(Integer.toString(n), topNfans[n].substring(0, topNfans[n].indexOf("=")));
				topNfans[n] = topNfans[n].substring(0, topNfans[n].indexOf("="));
			} else {
				topNfans[n] = null;
			}
		}
	}

	/**
	 * 粉丝质量结果整理
	 */
	public static void arrangeQuality(FansAnalysisResult result, int[] quality, int existuids) {
		result.getFansQuality().put("mask",
				Float.toString((float) Math.round(((float) quality[0] / existuids) * 10000) / 100) + "%");
		result.getFansQuality().put("real",
				Float.toString((float) Math.round(((float) quality[1] / existuids) * 10000) / 100) + "%");
	}

	/**
	 * 粉丝等级结果整理
	 */
	public static void arrangeFansGrade(FansAnalysisResult result, int[] gradebyfans, int existuids) {
		result.getGradeByFans().put("<100",
				Float.toString((float) Math.round(((float) gradebyfans[0] / existuids) * 10000) / 100) + "%");
		result.getGradeByFans().put("100~1000",
				Float.toString((float) Math.round(((float) gradebyfans[1] / existuids) * 10000) / 100) + "%");
		result.getGradeByFans().put("1000~1w",
				Float.toString((float) Math.round(((float) gradebyfans[2] / existuids) * 10000) / 100) + "%");
		result.getGradeByFans().put("1w~10w",
				Float.toString((float) Math.round(((float) gradebyfans[3] / existuids) * 10000) / 100) + "%");
		result.getGradeByFans().put(">10w",
				Float.toString((float) Math.round(((float) gradebyfans[4] / existuids) * 10000) / 100) + "%");
	}

	/**
	 * 认证分布结果整理
	 */
	public static void arrangeVerifiedType(FansAnalysisResult result, int[] verifiedtype, int existuids) {
		if (verifiedtype[13] != 0) {
			result.getVerifiedType().put("other",
					Float.toString((float) Math.round(((float) verifiedtype[13] / existuids) * 10000) / 100) + "%");
		}
		if (verifiedtype[12] != 0) {
			result.getVerifiedType().put("-1",
					Float.toString((float) Math.round(((float) verifiedtype[12] / existuids) * 10000) / 100) + "%");
		}
		if (verifiedtype[11] != 0) {
			result.getVerifiedType().put("400",
					Float.toString((float) Math.round(((float) verifiedtype[11] / existuids) * 10000) / 100) + "%");
		}
		if (verifiedtype[10] != 0) {
			result.getVerifiedType().put("220",
					Float.toString((float) Math.round(((float) verifiedtype[10] / existuids) * 10000) / 100) + "%");
		}
		if (verifiedtype[9] != 0) {
			result.getVerifiedType().put("200",
					Float.toString((float) Math.round(((float) verifiedtype[9] / existuids) * 10000) / 100) + "%");
		}
		for (int n7 = 0; n7 < 9; n7++) {
			if (verifiedtype[n7] != 0) {
				result.getVerifiedType().put(Integer.toString(n7),
						Float.toString((float) Math.round(((float) verifiedtype[n7] / existuids) * 10000) / 100) + "%");
			}
		}
	}

	/**
	 * 计算活跃粉丝数
	 */
	public static void arrangeActiveCount(FansAnalysisResult result, int[] quality, SimpleFansInfo simpleFansInfo,
			int fansIdsSum, int existuids) {
		float r = (float) existuids / fansIdsSum;
		int activefanssum = Math.round(((float) quality[1] / existuids) * simpleFansInfo.getFanscount() * r);
		result.setActiveFansSum(activefanssum);
	}

	/**
	 * 微博来源整理
	 */
	public static void arrangeWbSource(FansAnalysisResult result, WbfenbuByHourResult wbfenbuByHourResult) {
		result.getSource().put(
				"sina",
				Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbsource()[0] / wbfenbuByHourResult
						.getSum()) * 10000) / 100) + "%");
		result.getSource().put("pc",
				Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbsource()[1] / wbfenbuByHourResult
						.getSum()) * 10000) / 100) + "%");
		result.getSource().put("android",
				Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbsource()[2] / wbfenbuByHourResult
						.getSum()) * 10000) / 100) + "%");
		result.getSource().put("iphone",
				Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbsource()[3] / wbfenbuByHourResult
						.getSum()) * 10000) / 100) + "%");
		result.getSource().put("ipad",
				Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbsource()[4] / wbfenbuByHourResult
						.getSum()) * 10000) / 100) + "%");
		result.getSource().put("others",
				Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbsource()[5] / wbfenbuByHourResult
						.getSum()) * 10000) / 100) + "%");
	}

	/**
	 * 粉丝的24小时的活跃时间线
	 */
	public static void arrangeFansActiveTimeline(FansAnalysisResult result, WbfenbuByHourResult wbfenbuByHourResult) {
		for (int i = 0; i < wbfenbuByHourResult.getWbfenbubyhour().length; i++) {
			result.getActiveTimeline().put(Integer.toString(i),
							Float.toString((float) Math.round(((float) wbfenbuByHourResult.getWbfenbubyhour()[i] / wbfenbuByHourResult
									.getSum()) * 10000) / 100)
									+ "%");
		}
	}

}
