package cc.pp.sina.bozhus.fans;

import cc.pp.sina.bozhus.constant.SourceType;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.domain.bozhus.WbfenbuByHourResult;
import cc.pp.sina.tokens.service.TokenService;
import cc.pp.sina.utils.time.DailyUtils;
import com.sina.weibo.model.Status;
import com.sina.weibo.model.StatusWapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WbfenbuByHour {

	private static Logger logger = LoggerFactory.getLogger(WbfenbuByHour.class);

	private static TokenService tokenService;
	private static SinaWeiboInfoDao sinaUserInfoDao;

	static {
		// 从150机器上获取皮皮用户uid和token
		logger.info("从150机器上获取皮皮用户token: ");
		tokenService = new TokenService();
		// 新浪数据接口
		sinaUserInfoDao = new SinaWeiboInfoDaoImpl(tokenService);
	}

	/**
	 * 分析函数
	 */
	public static WbfenbuByHourResult analysis(String[] uids) {

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
			StatusWapper status = sinaUserInfoDao.getSinaUserWeibos(uids[i], 1);
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
