package cc.pp.sina.bozhus.library;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.json.JSONArray;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.fans.UserAndFansParams;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.domain.bozhus.BozhuInfo;
import cc.pp.sina.domain.bozhus.UserAndFansDomain;

import com.sina.weibo.model.WeiboException;

public class BozhusLeftGetRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(BozhusLeftGetRun.class);

	private final String uid;
	private final SinaUserInfoDao sinaUserInfoDao;
	@SuppressWarnings("unused")
	private final SinaWeiboInfoDao sinaWeiboInfoDao;

	public BozhusLeftGetRun(String uid, SinaUserInfoDao sinaUserInfoDao, SinaWeiboInfoDao sinaWeiboInfoDao) {
		this.uid = uid;
		this.sinaUserInfoDao = sinaUserInfoDao;
		this.sinaWeiboInfoDao = sinaWeiboInfoDao;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			/**
			 * 博主参数信息
			 */
			//			UserAllParamsDomain bozhu = BozhusLibrary.userAllParams(new Users(), new Friendships(), new Tags(),
			//					new Timeline(), sinaWeiboInfoDao, sinaUserInfoDao, uid);
			UserAndFansDomain bozhu = UserAndFansParams.analysis("3481175457", sinaUserInfoDao, 25);
			/**
			 * 插入数据库
			 */
			if (bozhu == null) {
				logger.info("User: " + uid + " has no params.");
			} else {
				/**
				 * 更新统计信息
				 */
				//				BozhuInfo bozhuInfo = new BozhuInfo.Builder().setInfluence(bozhu.getInfluence())
				//						.setActive(bozhu.getActivation()).setWbnum(bozhu.getWeibocount())
				//						.setFannum(bozhu.getFanscount()).setMalerate(bozhu.getMaleratio())
				//						.setVrate(bozhu.getAddvratio()).setAct_fan(bozhu.getActivecount())
				//						.setFan_fans(bozhu.getAllfanscount()).setAct_fan_fans(bozhu.getAllactivefanscount())
				//						.setWb_avg_daily(bozhu.getAveragewbs()).setWb_avg_repost_lastweek(bozhu.getAverepcombyweek())
				//						.setWb_avg_repost_lastmonth(bozhu.getAverepcombymonth()).setWb_avg_repost(bozhu.getAverepcom())
				//						.setOrig_wb_rate(bozhu.getOriratio()).setOrig_wb_avg_repost(bozhu.getAveorirepcom())
				//						.setWb_avg_valid_repost_lastweek(bozhu.getValidrepcombyweek())
				//						.setWb_avg_valid_repost_lastmonth(bozhu.getValidrepcombymonth())
				//						.setRt_user_avg_quality(bozhu.getAvereposterquality())
				//						.setAvg_valid_fan_cover_last100(bozhu.getAveexposionsum()).build();
				BozhuInfo bozhuInfo = new BozhuInfo.Builder().setInfluence(bozhu.getInfluence())
						.setActive(bozhu.getActivation()).setWbnum(bozhu.getWeibocount())
						.setFannum(bozhu.getFanscount()).setMalerate(bozhu.getMaleratio())
						.setVrate(bozhu.getAddvratio()).setAct_fan(bozhu.getActivecount())
						.setFan_fans(bozhu.getAllfanscount()).setAct_fan_fans(bozhu.getAllactivefanscount())
						.setWb_avg_daily(bozhu.getAveragewbs()).build();
				BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(uid, "sina"),
						JSONArray.fromObject(bozhuInfo).get(0).toString(), "put");
			}
		} catch (WeiboException e) {
			logger.info("WeiboException is occurs in User: " + uid);
			e.printStackTrace();
		} catch (HttpException e) {
			logger.info("HttpException is occurs in User: " + uid);
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException is occurs in User: " + uid);
			e.printStackTrace();
		}
	}

}
