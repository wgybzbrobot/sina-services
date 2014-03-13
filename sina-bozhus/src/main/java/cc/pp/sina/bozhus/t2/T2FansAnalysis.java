package cc.pp.sina.bozhus.t2;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.algorithms.top.sort.InsertSort;
import cc.pp.sina.algorithms.top.sort.PPSort;
import cc.pp.sina.bozhus.common.UserPropsAnalysis;
import cc.pp.sina.bozhus.common.UserPropsArrange;
import cc.pp.sina.bozhus.constant.SourceType;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2Interactions;
import cc.pp.sina.dao.t2.T2UserFans;
import cc.pp.sina.domain.bozhus.FansAnalysisResult;
import cc.pp.sina.domain.bozhus.UserTag;
import cc.pp.sina.domain.bozhus.WbfenbuByHourResult;
import cc.pp.sina.domain.t2.T2SinaInteractionsInfo;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.time.DailyUtils;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;
import com.sina.weibo.model.User;
import com.sina.weibo.model.UserWapper;

public class T2FansAnalysis {

	private static Logger logger = LoggerFactory.getLogger(T2FansAnalysis.class);

	private static final int TOP_N_FANS = 50;

	private final SinaUserInfoDao sinaUserInfoDao;
	private final SinaWeiboInfoDao sinaWeiboInfoDao;
	private final T2Interactions interactions;

	private static T2UserFans userFans = new T2UserFans(MybatisConfig.ServerEnum.fenxi);

	public T2FansAnalysis(SinaUserInfoDao sinaUserInfoDao, SinaWeiboInfoDao sinaWeiboInfoDao) {
		this.sinaUserInfoDao = sinaUserInfoDao;
		this.sinaWeiboInfoDao = sinaWeiboInfoDao;
		interactions = new T2Interactions(MybatisConfig.ServerEnum.fenxi);
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		TokenService tokenService = new TokenService();
		T2FansAnalysis fansAnalysis = new T2FansAnalysis(new SinaUserInfoDaoImpl(tokenService),
				new SinaWeiboInfoDaoImpl(tokenService));
		fansAnalysis.insertUserFansResult("all");

	}

	/**
	 * 存储粉丝结果数据
	 */
	public void insertUserFansResult(String type) {
		List<String> uids;
		if ("all".equalsIgnoreCase(type)) {
			uids = userFans.getSinaAllUids();
		} else {
			uids = userFans.getSinaNewUids();
		}
		for (String uid : uids) {
			logger.info("Tackle uid=" + uid);
			FansAnalysisResult fansAnalysisResult = fansAnalysis(uid);
			if (fansAnalysisResult != null) {
				userFans.updateT2SinaUser(Long.parseLong(uid), JsonUtils.toJsonWithoutPretty(fansAnalysisResult));
			}
		}
	}

	/**
	 * 主分析函数
	 */
	public FansAnalysisResult fansAnalysis(String uid) {

		FansAnalysisResult result = new FansAnalysisResult();
		/**
		 * 用户粉丝总信息
		 */
		int cursor = 0;
		UserWapper fansInfos = sinaUserInfoDao.getSinaUserFans(uid, cursor++);
		if (fansInfos == null) {
			logger.error("User:" + uid + " has no fansinfo.");
			return null;
		}
		// 1、粉丝总数
		result.setFansSum((int) fansInfos.getTotalNumber());

		/**
		 * 分析粉丝信息
		 */
		// 获取粉丝列表
		if (fansInfos.getUsers().size() == 0) {
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
		int all = 0;
		while (fansInfos != null) {
			for (User user : fansInfos.getUsers()) {
				all++;
				if (user == null) {
					continue;
				}
				// 粉丝存在数
				existuids++;
				// 4、认证分布
				verifiedtype[UserPropsAnalysis.analysisVerifiedType(user)]++;
				// 6、粉丝等级分布（按粉丝量）
				gradebyfans[UserPropsAnalysis.analysisFansClassByFans(user)]++;
				// 7、年龄分析
				age[UserPropsAnalysis.analysisAge(user)]++;
				// 8、性别分析
				gender[UserPropsAnalysis.analysisGender(user)]++;
				// 9、区域分析
				province[UserPropsAnalysis.analysisProvince(user)]++;
				// 10、水军分析
				quality[UserPropsAnalysis.analysisQuality(user)]++;
				// 11、粉丝Top分析（按粉丝量）
				topNFans = InsertSort.toptable(topNFans, user.getId() + "=" + user.getFollowersCount());
				// 12、认证比例
				verifiedratio[UserPropsAnalysis.analysisVerified(user)]++;
				// 存放待分析的用户名
				top10uids = InsertSort.toptable(top10uids, user.getId() + "=" + user.getStatusesCount());
			}
			if (cursor * 200 >= Math.min(fansInfos.getTotalNumber(), 5000)) {
				break;
			}
			fansInfos = sinaUserInfoDao.getSinaUserFans(uid, cursor++);
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
		UserPropsArrange.arrangeActiveCount(result, quality, result.getFansSum(), all, existuids);
		// 用户标签信息
		result.setHotTags(getFansTags(topNFans));
		// 最近一周的粉丝增减曲线
		result.setFansAddTimeline(getFansCountTimeline(Long.parseLong(uid), 7));
		// 终端设备分布
		String[] uids = InsertSort.trans(top10uids);
		WbfenbuByHourResult wbfenbuByHourResult = analysis(uids);
		UserPropsArrange.arrangeWbSource(result, wbfenbuByHourResult);
		// 粉丝微博时间线
		UserPropsArrange.arrangeFansActiveTimeline(result, wbfenbuByHourResult);

		return result;
	}

	/**
	 * 获取用户标签信息
	 */
	public List<HashMap<String, String>> getFansTags(String[] topNFans) {

		HashMap<String, Long> tagsAndSum = new HashMap<>();
		List<UserTag> tags = null;
		for (String uid : topNFans) {
			if (uid != null) {
				tags = sinaUserInfoDao.getSinaUserTags(uid, 10);
				if (tags == null) {
					continue;
				}
				for (UserTag userTags : tags) {
					if (tagsAndSum.get(userTags.getValue()) == null) {
						tagsAndSum.put(userTags.getValue(), userTags.getWeight());
					} else {
						tagsAndSum.put(userTags.getValue(), tagsAndSum.get(userTags.getValue()) + userTags.getWeight());
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
	private HashMap<String, Integer> getFansCountTimeline(long uid, int NDaysFromNow) {
		HashMap<String, Integer> result = new HashMap<>();
		List<T2SinaInteractionsInfo> fansLists = interactions.selectT2SinaInteractionsList(uid, NDaysFromNow);
		for (T2SinaInteractionsInfo fan : fansLists) {
			result.put(fan.getDate() + "", fan.getFanscount());
		}
		return result;
	}

	private WbfenbuByHourResult analysis(String[] uids) {
		/*
		 *  变量初始化
		 */
		int[] wbfenbubyhour = new int[24];
		int[] wbsource = new int[6];
		int sum = 0;

		for (int i = 0; i < uids.length; i++) {
			if (uids[i] == null) {
				break;
			}
			StatusWapper status = sinaWeiboInfoDao.getSinaUserWeibos(uids[i], 1);
			if (status == null) {
				continue;
			}
			for (Status s : status.getStatuses()) {
				sum++;
				// 24小时内的微博发布分布
				wbfenbubyhour[DailyUtils.getHour(s.getCreatedAt().getTime() / 1000)]++;
				// 微博来源分布
				wbsource[SourceType.getCategory(s.getSource().getName())]++;
			}
		}
		WbfenbuByHourResult result = new WbfenbuByHourResult();
		result.setSum(sum);
		result.setWbfenbubyhour(wbfenbubyhour);
		result.setWbsource(wbsource);

		return result;
	}

}
