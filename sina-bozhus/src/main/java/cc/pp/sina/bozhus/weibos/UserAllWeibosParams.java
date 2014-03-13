package cc.pp.sina.bozhus.weibos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.common.WeiboPropsAnalysis;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.domain.bozhus.UserAllWeibosDomain;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;
import com.sina.weibo.model.WeiboException;

/**
 * 用户微博粉丝
 *
 * @author wgybzb
 */
public class UserAllWeibosParams {

	private static Logger logger = LoggerFactory.getLogger(UserAllWeibosParams.class);

	private static Random random = new Random();

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws WeiboException, SQLException {

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		sinaTokens.add("2.00vyKmPBdcZIJCab7e4cdb505dUSLC");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);
		UserAllWeibosDomain result = UserAllWeibosParams.analysis("1649155730", sinaWeiboInfoDao, 20, 2);
		System.out.println(JSONArray.fromObject(result));
	}

	/**
	 * 分析函数
	 *
	 * @param pageCount：微博页数
	 * @param singleNum：分析单条微博数
	 */
	public static UserAllWeibosDomain analysis(String uid, SinaWeiboInfoDao sinaWeiboInfoDao, int pageCount,
			int singleNum) {
		/*
		 * 变量初始化
		 */
		// 2、原创微博数
		int original = 0;
		// 3、原创总转评数
		int allorirepcomcount = 0;
		// 4、总转评量
		int allrepcomcount = 0;
		// 5、微博来源分布
		HashMap<String, Integer> wbsources = new HashMap<>();
		// 6、最近一周内的平均转评量：0---微博条数，1---总转评量
		int[] lastweek = new int[2];
		// 7、最近一月内的平均转评量：0---微博条数，1---总转评量
		int[] lastmonth = new int[2];
		// 8、最近100条微博id
		List<String> lastweiboids = new ArrayList<String>();

		/*
		 * 采集用户微博信息
		 */
		int cursor = 1;
		StatusWapper status = sinaWeiboInfoDao.getSinaUserWeibos(uid, cursor);
		if (status == null) {
			logger.info("User: " + uid + " has no weibos.");
			return null;
		}

		long nowtime = System.currentTimeMillis() / 1000;
		long createdAt = 0; // 微博创建时间
		int index = 0, existWb = 0;
		long weiboSum = status.getTotalNumber();

		while (cursor * 100 < weiboSum + 100) {
			if (status != null) {
				for (Status s : status.getStatuses()) {
					// 存在微博数
					if (s.getId() == null) {
						continue;
					} else {
						existWb++;
					}
					createdAt = s.getCreatedAt().getTime() / 1000;
					// 原创微博数
					if (s.getRetweetedStatus() == null) {
						original++;
					}
					// 原创中的转评数
					if (s.getThumbnailPic().length() != 0) {
						allorirepcomcount += s.getRepostsCount() + s.getCommentsCount();
					}
					// 总转评量
					allrepcomcount += s.getRepostsCount() + s.getCommentsCount();
					// 微博来源分布
					if (wbsources.get(s.getSource().getName()) == null) {
						wbsources.put(s.getSource().getName(), 1);
					} else {
						wbsources.put(s.getSource().getName(), wbsources.get(s.getSource().getName()) + 1);
					}
					// 最近一周内的平均转评量
					if (createdAt + 7 * 86400 > nowtime) {
						lastweek[0]++;
						lastweek[1] += s.getRepostsCount() + s.getCommentsCount();
					}
					// 最近一月内的平均转评量
					if (createdAt + 30 * 86400 > nowtime) {
						lastmonth[0]++;
						lastmonth[1] += s.getRepostsCount() + s.getCommentsCount();
					}
					// 最近100条微博ids
					if (index < 100) {
						lastweiboids.add(s.getId());
					}
				}
			}

			if (cursor > Math.min(pageCount, 20)) { // 限制pageCount次，最大20次
				break;
			}

			try {
				status = sinaWeiboInfoDao.getSinaUserWeibos(uid, ++cursor);
			} catch (RuntimeException e) {
				logger.info("User: " + uid + "'s weibos dump error.");
				status = null;
				continue;
			}
		}
		if (existWb == 0) {
			logger.info("User: " + uid + "'s has no weibos.");
			return null;
		}
		/*
		 * 没有全部采集
		 */
		if (cursor * 100 < weiboSum) {
			original = (int) (original * weiboSum / existWb);
			allorirepcomcount = (int) (allorirepcomcount * weiboSum / existWb);
		}

		/*
		 * 验证结果
		 */
		//		UserAllWeibosParams.weibosResultVerfiy1(existWb, original, allrepcomcount, allorirepcomcount, wbsources,
		//				lastweek, lastmonth, lastweiboids);

		/*
		 * 数据结果整理
		 */
		// 原创微博比例
		float oriratio = 0.0f;
		if (weiboSum != 0) {
			oriratio = (float) original / weiboSum;
		}
		// 原创中的平均转评量
		float aveorirepcom = 0.0f;
		if (original != 0) {
			aveorirepcom = (float) allorirepcomcount / original;
		}
		// 平均转评量
		float averepcom = 0.0f;
		if (weiboSum != 0) {
			averepcom = (float) allrepcomcount / weiboSum;
		}
		// 微博来源分布
		HashMap<String, Integer> wbsource = WeiboPropsAnalysis.analysisTopNWbSources(wbsources, 5);
		// 最近一周内的平均转评量
		float averepcombyweek = 0.0f;
		if (lastweek[0] != 0) {
			averepcombyweek = (float) lastweek[1] / lastweek[0];
		}
		// 最近一月内的平均转评量
		float averepcombymonth = 0.0f;
		if (lastmonth[0] != 0) {
			averepcombymonth = (float) lastmonth[1] / lastmonth[0];
		}

		return new UserAllWeibosDomain.Builder().setWeibosum(weiboSum).setOriratio(oriratio)
				.setAveorirepcom(aveorirepcom).setAverepcom(averepcom).setWbsource(wbsource)
				.setAverepcombyweek(averepcombyweek).setAverepcombymonth(averepcombymonth)
				.setLastweiboids(randWeibos(lastweiboids, singleNum)).build();
	}

	public static void weibosResultVerfiy1(int existWb, int original, int allrepcomcount, int allorirepcomcount,
										   HashMap<String, Integer> wbsources, int[] lastweek, int[] lastmonth, List<String> lastweiboids) {

		System.out.println("existWb: " + existWb);
		System.out.println("original: " + original);
		System.out.println("allrepcomcount: " + allrepcomcount);
		System.out.println("allorirepcomcount: " + allorirepcomcount);
		System.out.println("wbsources: ");
		for (Entry<String, Integer> temp : wbsources.entrySet()) {
			System.out.println(temp.getKey() + "=" + temp.getValue());
		}
		System.out.println("lastweek: " + lastweek[0] + "," + lastweek[1]);
		System.out.println("lastmonth: " + lastmonth[0] + "," + lastmonth[1]);
		System.out.println("lastweiboids: " + JSONArray.fromObject(lastweiboids));
	}

	/**
	 * 从一堆微博中随机选出num条
	 */
	public static List<String> randWeibos(List<String> lastWeiboIds, int num) {

		if (lastWeiboIds.size() <= num) {
			return lastWeiboIds;
		} else {
			while (lastWeiboIds.size() != num) {
				lastWeiboIds.remove(random.nextInt(lastWeiboIds.size()));
			}
			return lastWeiboIds;
		}
	}

}

