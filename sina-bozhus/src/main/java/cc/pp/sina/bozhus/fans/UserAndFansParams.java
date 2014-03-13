package cc.pp.sina.bozhus.fans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.algorithms.top.sort.InsertSort;
import cc.pp.sina.algorithms.top.sort.PPSort;
import cc.pp.sina.bozhus.common.OutStreamUtils;
import cc.pp.sina.bozhus.common.UserPropsAnalysis;
import cc.pp.sina.bozhus.common.UserPropsArrange;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.tags.UserTagsParams;
import cc.pp.sina.domain.bozhus.UserAndFansDomain;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.json.JsonUtils;

import com.sina.weibo.model.User;
import com.sina.weibo.model.UserWapper;
import com.sina.weibo.model.WeiboException;

/**
 * 用户及其粉丝分析
 * 1、接口调用次数：1+1 = 11
 * 2、上线测评结果：  2000粉丝：4495ms
 * 5000粉丝：10544ms/8415ms/8722ms/5751ms/8363ms/7991ms
 *
 * @author wgybzb
 */
public class UserAndFansParams {

	private static Logger logger = LoggerFactory.getLogger(UserAndFansParams.class);

	private static final Random RANDOM = new Random();

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws WeiboException, SQLException {

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00D9c_yBdcZIJC542f419776_YcRsD");
		sinaTokens.add("2.00dNiIRCdcZIJC63fce1aba1M6zbLE");
		sinaTokens.add("2.00zWWk4BdcZIJC43cf08470bQcr_fB");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		// 1862087393：粉丝小于5000
		// 2775934450：粉丝大于5000
		long start = System.currentTimeMillis();
		UserAndFansDomain result = UserAndFansParams.analysis("3481175457", sinaUserInfoDao, 25);
		long end = System.currentTimeMillis();
		System.out.println(JSONArray.fromObject(result));
		System.out.println(end - start);
	}

	/**
	 * 分析函数
	 */
	public static UserAndFansDomain analysis(String uid, SinaUserInfoDao sinaUserInfoDao, int pageCount) {

		/*
		 * 获取用户基础信息
		 */
		User user = sinaUserInfoDao.getSinaUserBaseInfo(uid);
		if (user == null) {
			logger.info("User： " + uid + " baseinfo is null.");
			return null;
		}

		/*
		 * 中间变量
		 */
		int existUids = 0;
		long allFansCount = 0, allActiveFansSount = 0;
		int[] gender = new int[2];
		int[] fansClassByFans = new int[5];
		int[] quality = new int[2];
		int[] verifiedRatio = new int[2];
		int[] province = new int[101];
		String[] topNUids = new String[10];
		for (int i = 0; i < topNUids.length; i++) {
			topNUids[i] = "0=0";
		}

		/*
		 * 采集用户粉丝信息
		 */
		int cursor = 0;
		UserWapper followers = sinaUserInfoDao.getSinaUserFans(uid, cursor);
		if (followers == null) {
			logger.info("User: " + uid + " baseinfo exists, but cann't get it's fans infos.");
			return null;
		}

		/*
		 * 循环计算粉丝特性数据
		 */
		try {
			while (cursor * 200 < user.getFollowersCount() + 200) {
				if (cursor++ >= pageCount - 1) { // 采集pageCount次
					break;
				}
				for (User u : followers.getUsers()) {
					// 粉丝存在数
					if (u.getName() != null) {
						existUids++;
					} else {
						continue;
					}
					topNUids = InsertSort.toptable(topNUids, u.getId() + "=" + u.getFollowersCount());
					// 粉丝的粉丝数
					allFansCount += u.getFollowersCount();
					// 粉丝的活跃粉丝数
					allActiveFansSount += UserPropsAnalysis.analysisAllActiveFansCount(u);
					analysisUser(u, gender, fansClassByFans, quality, verifiedRatio, province);
				}
				followers = sinaUserInfoDao.getSinaUserFans(uid, cursor);
			}
		} catch (RuntimeException e) {
			logger.info("User: " + uid + " baseinfo exists, but cann't get it's fans infos.");
			throw new RuntimeException(e);
		}
		/*
		 * 验证数据
		 */
		//		resultVerifyFans1(existUids, allFansCount, allActiveFansSount, gender, fansClassByFans, quality, verifiedRatio,
		//				province);

		/**
		 * 获取粉丝标签数据
		 */
		HashMap<String, Integer> fansTags = new HashMap<>();
		List<String> tagsTemp;
		for (String str : topNUids) {
			if (str.length() > 5) {
				tagsTemp = UserTagsParams.analysis(str.split("=")[0], sinaUserInfoDao, 10);
				for (String tag : tagsTemp) {
					if (fansTags.get(tag) == null) {
						fansTags.put(tag, 1);
					} else {
						fansTags.put(tag, fansTags.get(tag) + 1);
					}
				}
			}
		}
		fansTags = PPSort.sortedToHashMapInteger(fansTags, 20);

		/**
		 * 注意：当前的cursor值是采集的页数
		 */
		return UserAndFansParams.arrangeUser(user, cursor, existUids, gender, province, verifiedRatio, quality,
				fansClassByFans, allFansCount, allActiveFansSount, fansTags);
	}

	/**
	 * 粉丝单个用户数据
	 */
	public static void analysisUser(User user, int[] gender, int[] fansClassByFans, int[] quality, int[] verifiedRatio,
			int[] province) {

		// 性别分析
		gender[UserPropsAnalysis.analysisGender(user)]++;
		// 用户等级分析（按粉丝量）
		fansClassByFans[UserPropsAnalysis.analysisFansClassByFans(user)]++;
		// 粉丝质量
		quality[UserPropsAnalysis.analysisQuality(user)]++;
		// 认证信息（认证与否）
		verifiedRatio[UserPropsAnalysis.analysisVerified(user)]++;
		// 区域分析
		province[UserPropsAnalysis.analysisProvince(user)]++;
	}

	/**
	 * 整理用户及其粉丝分析的最后结果类
	 */
	public static UserAndFansDomain arrangeUser(User user, int cursor, int existUids, int[] gender, int[] province,
			int[] verifiedRatio, int[] quality, int[] fansClassByFans, long allFansCount, long allActiveFansSount,
			HashMap<String, Integer> fansTags) {

		/*
		 * 获取粉丝数、微博数、创建时间
		 */
		int userFansSum = user.getFollowersCount();
		int userWeiboSum = user.getStatusesCount();
		long userCreateAt = 0;
		try {
			userCreateAt = user.getCreatedAt().getTime() / 1000;
		} catch (RuntimeException e) {
			logger.info("User: " + user.getId() + " baseinfo exists, but it's createtime is null.");
			return null;
		}

		/*
		 * 不存在粉丝
		 */
		if (existUids == 0) {
			logger.info("User: " + user.getId() + " baseinfo exists, but it's fans has none exists.");
			return null;
		}
		// 粉丝存在比例
		float fansExistedRatio = (float) existUids / (cursor * 200);
		// 认证比例，这里非认证的包括：不存在的、未认证的
		float addvratio = (float) verifiedRatio[1] / (cursor * 200);
		if ((cursor * 200 >= user.getFollowersCount()) && (cursor * 200 < user.getFollowersCount() + 200)) {
			fansExistedRatio = (float) existUids / user.getFollowersCount();
			addvratio = (float) verifiedRatio[1] / user.getFollowersCount();
		}
		if (addvratio < 0.000001) {
			addvratio = (float) RANDOM.nextInt(100) / 1000000;
		}
		// 男性比例
		float maleratio = (float) gender[0] / existUids;
		// 区域分析
		HashMap<String, String> top5provinces = UserPropsArrange.arrangeTopNProvinces(province, existUids, 5);
		// 用户微博创建天数
		int usertimediff = UserPropsArrange.arrangeAverageWeibosByDay(userCreateAt);
		// 用户的平均每天发博量
		float averagewbs = (float) Math.round(((float) userWeiboSum / usertimediff) * 100) / 100;
		// 用户的活跃度
		int activation = UserPropsArrange.arrangeUserActivation(userFansSum, userWeiboSum, usertimediff);
		// 粉丝活跃比例
		float activeratio = ((float) quality[1] / existUids) * fansExistedRatio;
		// 粉丝活跃数
		int activenum = Math.round(activeratio * userFansSum);
		// 用户的影响力
		int influence = UserPropsArrange.arrangeUserInfluence(userFansSum, fansClassByFans, verifiedRatio, existUids,
				activenum, cursor);
		// 用户粉丝的粉丝总量，含自己的粉丝数
		allFansCount = UserPropsArrange.arrangeAllFansCountOfUserFans(allFansCount, userFansSum, cursor, existUids);
		// 用户粉丝的活跃粉丝总量，含自己的活跃粉丝数
		allActiveFansSount = UserPropsArrange.arrangeAllActiveFansCountOfUserFans(allActiveFansSount, userFansSum, cursor,
				existUids, activenum);

		/*******************数据转换与存储*****************/
		UserAndFansDomain result = new UserAndFansDomain.Builder(user.getId()).setNickname(user.getScreenName())
				.setDescription(user.getDescription()).setFanscount(userFansSum).setWeibocount(userWeiboSum)
				.setVerify(user.getVerifiedType()).setAveragewbs(averagewbs).setInfluence(influence)
				.setActivecount(activenum).setActivation(activation).setAddvratio(addvratio)
				.setActiveratio(activeratio).setMaleratio(maleratio).setCreatedtime(usertimediff)
				.setFansexistedratio(fansExistedRatio).setAllfanscount(allFansCount)
				.setAllactivefanscount(allActiveFansSount).setTop5provinces(top5provinces)
				.setFanstags(JsonUtils.toJsonWithoutPretty(fansTags)).build();

		return result;
	}

	public static void resultVerifyFans1(int existUids, long allFansCount, long allActiveFansSount, int[] gender,
			int[] fansClassByFans, int[] quality, int[] verifiedRatio, int[] province) {

		System.out.println("existUids: " + existUids);
		System.out.println("allFansCount: " + allFansCount);
		System.out.println("allActiveFansSount: " + allActiveFansSount);
		System.out.println("gender: " + OutStreamUtils.sumIntArr(gender));
		OutStreamUtils.outIntArr(gender);
		System.out.println("fansClassByFans:" + OutStreamUtils.sumIntArr(fansClassByFans));
		OutStreamUtils.outIntArr(fansClassByFans);
		System.out.println("quality: " + OutStreamUtils.sumIntArr(quality));
		OutStreamUtils.outIntArr(quality);
		System.out.println("verifiedRatio: " + OutStreamUtils.sumIntArr(verifiedRatio));
		OutStreamUtils.outIntArr(verifiedRatio);
		System.out.println("province: " + OutStreamUtils.sumIntArr(province));
		OutStreamUtils.outIntArr(province);
	}

}

