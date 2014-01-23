package cc.pp.sina.bozhus.weibos;

import cc.pp.sina.bozhus.common.MidToWid;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.domain.bozhus.UserServalWeibosDomain;
import cc.pp.sina.domain.bozhus.UserSingleWeiboDomain;
import cc.pp.sina.tokens.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 多条微博分析
 * 调用接口次数：10*2 = 20
 *
 * @author Administrator
 */
public class UserServalWeibosParams {

	private static Logger logger = LoggerFactory.getLogger(UserServalWeibosParams.class);

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		List<String> lastweiboids = new ArrayList<String>();
		lastweiboids.add(MidToWid.mid2wid("http://weibo.com/2300716454/AmbvcstAK"));
		lastweiboids.add(MidToWid.mid2wid("http://weibo.com/1192329374/AkEKk3yYN"));
		lastweiboids.add(MidToWid.mid2wid("http://weibo.com/2356423042/AlU9DmlBO"));

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00D9c_yBdcZIJC542f419776_YcRsD");
		sinaTokens.add("2.00dNiIRCdcZIJC63fce1aba1M6zbLE");
		sinaTokens.add("2.00zWWk4BdcZIJC43cf08470bQcr_fB");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);

		UserServalWeibosDomain result = UserServalWeibosParams.analysis(lastweiboids, sinaWeiboInfoDao,
				5); // 5、10、20
		System.out.println(result.getAvereposterquality());
		System.out.println(result.getAveexposionsum());
	}

	/**
	 * 多条微博分析函数
	 */
	public static UserServalWeibosDomain analysis(List<String> lastweiboids,
												  SinaWeiboInfoDao sinaWeiboInfoDao,
												  int pageCountForSingleWeibo) {

		int[] fansquality = new int[2];
		long allexposion = 0;
		for (String wid : lastweiboids) {
			UserSingleWeiboDomain temp = UserSingleWeiboParams.analysis(wid, sinaWeiboInfoDao,
					pageCountForSingleWeibo);
			if (temp != null) {
				fansquality[0] += temp.getReposterquality()[0];
				fansquality[1] += temp.getReposterquality()[1];
				allexposion += temp.getExposionsum();
			}
		}
		if (fansquality[0] + fansquality[1] == 0) {
			logger.info("Several weibos all has no reposters or has api error.");
			return null;
		} else {
			return new UserServalWeibosDomain.Builder((float) fansquality[1] / (fansquality[0] + fansquality[1]),
					allexposion).build();
		}
	}

}
