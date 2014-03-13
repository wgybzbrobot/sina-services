package cc.pp.sina.bozhus.t2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2Interactions;
import cc.pp.sina.dao.t2.T2UserFans;
import cc.pp.sina.domain.t2.T2SinaInteractionsInfo;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.time.TimeUtils;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;
import com.sina.weibo.model.User;

/**
 * 用户与粉丝的交互数据分析
 * @author wgybzb
 *
 */
public class T2InteractionsAnalysis {

	private static Logger logger = LoggerFactory.getLogger(T2InteractionsAnalysis.class);

	private final SinaUserInfoDao sinaUserInfoDao;
	private final SinaWeiboInfoDao sinaWeiboInfoDao;
	private final AtInfoAnalysis atInfoAnalysis;

	private static T2Interactions interactions = new T2Interactions(MybatisConfig.ServerEnum.fenxi);
	private static T2UserFans userFans = new T2UserFans(MybatisConfig.ServerEnum.fenxi);

	public T2InteractionsAnalysis(SinaUserInfoDao sinaUserInfoDao, SinaWeiboInfoDao sinaWeiboInfoDao) {
		this.sinaUserInfoDao = sinaUserInfoDao;
		this.sinaWeiboInfoDao = sinaWeiboInfoDao;
		atInfoAnalysis = new AtInfoAnalysis(sinaUserInfoDao);
	}

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		TokenService tokenService = new TokenService();
		T2InteractionsAnalysis interactionsAnalysis = new T2InteractionsAnalysis(new SinaUserInfoDaoImpl(tokenService),
				new SinaWeiboInfoDaoImpl(tokenService));
		interactionsAnalysis.insertUserInteractionResult();
		//		Timer timer = new Timer();
		//		timer.schedule(new IATimerTask(), 0, 86400 * 1000);
	}

	/**
	 * 存储交互数据
	 */
	public void insertUserInteractionResult() {
		List<String> uids = userFans.getSinaAllUids();
		for (String uid : uids) {
			logger.info("Tackle uid=" + uid);
			T2SinaInteractionsInfo interactionsInfo = userInteractionAnalysis(uid);
			if (interactionsInfo != null) {
				interactions.insertT2SinaInteractions(interactionsInfo.getUsername(), interactionsInfo.getDate(),
						interactionsInfo.getFanscount(), interactionsInfo.getAllcount(),
						interactionsInfo.getEmotionratio());
			} else {
				interactions.insertT2SinaInteractions(Long.parseLong(uid), Integer.parseInt(TimeUtils.getTodayDaily()),
						0, 0, "");
			}
		}
	}

	/**
	 * 分析用户与粉丝交互数据
	 */
	public T2SinaInteractionsInfo userInteractionAnalysis(String uid) {

		// 获取用户信息
		User user = sinaUserInfoDao.getSinaUserBaseInfo(uid);
		if (user == null) {
			return null;
		}
		// 分析用户的@数据
		int[] emotions = atInfoAnalysis.userAtInfoAnalysis(uid);
		// 分析最近一天的微博数据
		int allCount = emotions[3];
		StatusWapper weibos = sinaWeiboInfoDao.getSinaUserWeibos(uid, 1);
		if (weibos == null) {
			return null;
		}
		List<String> wids = new ArrayList<>();
		long time = System.currentTimeMillis() / 1000;
		for (Status weibo : weibos.getStatuses()) {
			if (weibo.getCreatedAt().getTime() / 1000 > time - 86400) {
				if (weibo.getRepostsCount() > 0) {
					wids.add(weibo.getId());
				}
				allCount += weibo.getCommentsCount() + weibo.getRepostsCount() + weibo.getAttitudesCount();
			}
		}
		if (wids.size() > 0) {
			StatusWapper reposts;
			for (String wid : wids) {
				reposts = sinaWeiboInfoDao.getSinaSingleWeiboResposts(wid, 1);
				for (Status repost : reposts.getStatuses()) {
					emotions[T2Utils.getEmotions(repost.getText())]++;
				}
			}
		}

		int sum = emotions[0] + emotions[1] + emotions[2];
		if (sum == 0) {
			emotions[1] = 1;
			sum = 1;
		}
		int[] remotions = T2Utils.transData(emotions);
		HashMap<String, String> emotionratio = new HashMap<>();
		emotionratio.put("negative", Float.toString((float)Math.round(((float)remotions[0]/sum)*10000)/100) + "%");
		emotionratio.put("positive", Float.toString((float)Math.round(((float)remotions[1]/sum)*10000)/100) + "%");
		emotionratio.put("neutral", Float.toString((float)Math.round(((float)remotions[2]/sum)*10000)/100) + "%");

		return new T2SinaInteractionsInfo(Long.parseLong(uid), Integer.parseInt(TimeUtils.getTodayDaily()),
				user.getFollowersCount(), allCount, JsonUtils.toJsonWithoutPretty(emotionratio));
	}

}
