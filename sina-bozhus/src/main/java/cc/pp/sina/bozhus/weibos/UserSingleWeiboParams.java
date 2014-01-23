package cc.pp.sina.bozhus.weibos;

import cc.pp.sina.bozhus.common.MidToWid;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.domain.bozhus.UserSingleWeiboDomain;
import cc.pp.sina.tokens.service.TokenService;
import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserSingleWeiboParams {

	private static Logger logger = LoggerFactory.getLogger(UserSingleWeiboParams.class);

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		// http://weibo.com/2300716454/AmbvcstAK：324条转发
		// http://weibo.com/2302787264/AlVJazU5o：超多2000条转发

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);

		System.out.println(JSONArray.fromObject(UserSingleWeiboParams.analysis(
				MidToWid.mid2wid("http://weibo.com/2300716454/AmbvcstAK"), sinaWeiboInfoDao, 4)));
		//		System.out.println(JSONArray.fromObject(UserSingleWeiboParams.analysis(new Timeline(),
		//				MidToWid.mid2wid("http://weibo.com/2300716454/AmbvcstAK"), sinaWeiboInfoDao, 8))); // 超过转发数
		//		System.out.println(JSONArray.fromObject(UserSingleWeiboParams.analysis(new Timeline(),
		//				MidToWid.mid2wid("http://weibo.com/2302787264/AlVJazU5o"), sinaWeiboInfoDao, 5)));
		//		System.out.println(JSONArray.fromObject(UserSingleWeiboParams.analysis(new Timeline(),
		//				MidToWid.mid2wid("http://weibo.com/2302787264/AlVJazU5o"), sinaWeiboInfoDao, 10)));
		//		System.out.println(JSONArray.fromObject(UserSingleWeiboParams.analysis(new Timeline(),
		//				MidToWid.mid2wid("http://weibo.com/2302787264/AlVJazU5o"), sinaWeiboInfoDao, 20))); // 超过限制
	}

	/**
	 * 单条微博分析函数
	 */
	public static UserSingleWeiboDomain analysis(String wid, SinaWeiboInfoDao sinaWeiboInfoDao, int pageCount) {

		// 变量初始化
		int[] reposterquality = new int[2];
		long exposionsum = 0;
		int existwb = 0, cursor = 1;

		// 采集转发信息
		StatusWapper status = sinaWeiboInfoDao.getSinaSingleWeiboResposts(wid, cursor);
		if (status == null) {
			logger.info("Wid: " + wid + " has no reposter.");
			return null;
		}

		HashMap<String, Integer> texts = new HashMap<>();
		long reposterSum = status.getTotalNumber();

		// 开始循环计算
		while (cursor * 200 < reposterSum + 200) {

			if (status != null) {
				for (Status reposter : status.getStatuses()) {
					if (reposter.getId() == null) {
						continue;
					} else {
						existwb++;
					}
					// 水军比例
					//				reposterquality[WeiboPropsAnalysis.analysisQuality(reposter)]++;
					//				reposterquality[UserPropsAnalysis.analysisQuality(reposter.getUser())]++;
					if (texts.get(reposter.getText().trim()) == null) {
						texts.put(reposter.getText().trim(), 0);
					}
					// 总曝光量
					exposionsum += reposter.getUser().getFollowersCount();

				}
			}
			if (cursor++ >= Math.min(pageCount, 10)) { // 限制pageCount次
				break;
			}

			try {
				status = sinaWeiboInfoDao.getSinaSingleWeiboResposts(wid, cursor);
			} catch (RuntimeException e) {
				logger.info("Wid: " + wid + "'s reposters dump error.");
				status = null;
				continue;
			}
		}
		if (existwb == 0) {
			logger.info("Wid: " + wid + " has no reposter or has invalid reposters.");
			return null;
		}
		if ((cursor * 200 >= status.getTotalNumber()) && (cursor * 200 < status.getTotalNumber() + 200)) {
			// 转发质量
			reposterquality[1] = texts.size();
			reposterquality[0] = (int) status.getTotalNumber() - texts.size();
		} else {
			reposterquality[1] = texts.size() * (int) status.getTotalNumber() / existwb;
			reposterquality[0] = (int) status.getTotalNumber() - reposterquality[1];
			// 总曝光量
			exposionsum = exposionsum * status.getTotalNumber() / existwb;
		}

		return new UserSingleWeiboDomain.Builder(reposterquality, exposionsum).build();
	}

}
