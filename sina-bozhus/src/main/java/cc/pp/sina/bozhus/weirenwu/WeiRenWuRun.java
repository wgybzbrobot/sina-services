package cc.pp.sina.bozhus.weirenwu;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.library.BozhusLibrary;
import cc.pp.sina.dao.weirenwu.WeiRenWu;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.weirenwu.BozhuExtendInfo;
import cc.pp.sina.utils.json.JsonUtils;

public class WeiRenWuRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(WeiRenWuRun.class);

	private final WeiRenWu weiRenWu;
	private final String uid;
	private final SinaUserInfoDao sinaUserInfoDao;
	private final SinaWeiboInfoDao sinaWeiboInfoDao;

	public WeiRenWuRun(WeiRenWu weiRenWu, SinaUserInfoDao sinaUserInfoDao, SinaWeiboInfoDao sinaWeiboInfoDao, String uid) {
		this.weiRenWu = weiRenWu;
		this.uid = uid;
		this.sinaUserInfoDao = sinaUserInfoDao;
		this.sinaWeiboInfoDao = sinaWeiboInfoDao;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		/**
		 * 博主参数信息
		 */
		UserAllParamsDomain bozhu = null;
		try {
			bozhu = BozhusLibrary.userAllParams(sinaWeiboInfoDao, sinaUserInfoDao, uid);
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
			throw new RuntimeException(e);
		}
		/**
		 * 插入数据库
		 */
		if (bozhu == null) {
			logger.info("User: " + uid + " has no params.");
		} else {
			try {
				weiRenWu.insertSinaBozhuExtendInfo(transBozhuInfo(bozhu));
			} catch (Exception e) {
				logger.info(JsonUtils.toJson(transBozhuInfo(bozhu)));
				throw new RuntimeException(e);
			}
		}

	}

	public BozhuExtendInfo transBozhuInfo(UserAllParamsDomain bozhu) {

		BozhuExtendInfo result = new BozhuExtendInfo();
		result.setBzid(weiRenWu.selectSinaBzid(bozhu.getUsername()));
		result.setInfluence(bozhu.getInfluence());
		result.setActive(bozhu.getActivation());
		result.setWbnum(bozhu.getWeibocount());
		result.setFannum(bozhu.getFanscount());
		result.setMalerate(bozhu.getMaleratio());
		result.setVrate(bozhu.getAddvratio());
		result.setExsit_fan_rate(bozhu.getFansexistedratio());
		result.setAct_fan(bozhu.getActivecount());
		result.setAct_fan_rate(bozhu.getActiveratio());
		result.setFan_fans(bozhu.getAllfanscount());
		result.setAct_fan_fans(bozhu.getAllactivefanscount());
		result.setWb_avg_daily(bozhu.getAveragewbs());
		result.setWb_avg_repost_lastweek(bozhu.getAverepcombyweek());
		result.setWb_avg_repost_lastmonth(bozhu.getAverepcombymonth());
		result.setWb_avg_repost(bozhu.getAverepcom());
		result.setOrig_wb_rate(bozhu.getOriratio());
		result.setOrig_wb_avg_repost(bozhu.getAveorirepcom());
		result.setWb_avg_valid_repost_lastweek(bozhu.getValidrepcombyweek());
		result.setWb_avg_valid_repost_lastmonth(bozhu.getValidrepcombymonth());
		result.setRt_user_avg_quality(bozhu.getAvereposterquality());
		result.setAvg_valid_fan_cover_last100(bozhu.getAveexposionsum());
		result.setIdentity_type(bozhu.getIdentitytype());
		result.setIndustry_type(bozhu.getIndustrytype());
		result.setFans_age(bozhu.getFansage());
		result.setFans_tags(bozhu.getFanstags());
		result.setTop5provinces(bozhu.getTop5provinces());
		result.setWbsource(bozhu.getWbsource());
		result.setUsertags(bozhu.getUsertags());

		return result;
	}

}
