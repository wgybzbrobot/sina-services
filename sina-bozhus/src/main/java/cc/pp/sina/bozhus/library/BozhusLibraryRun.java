package cc.pp.sina.bozhus.library;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.sql.WeiboJDBC;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;

import com.sina.weibo.model.WeiboException;

public class BozhusLibraryRun implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(BozhusLibraryRun.class);

	private final WeiboJDBC weiboJDBC;
	private final String tablename;
	private final String uid;
	private final SinaUserInfoDao sinaUserInfoDao;
	private final SinaWeiboInfoDao sinaWeiboInfoDao;

	public BozhusLibraryRun(WeiboJDBC weiboJDBC, String tablename, String uid, SinaUserInfoDao sinaUserInfoDao,
			SinaWeiboInfoDao sinaWeiboInfoDao) {
		this.weiboJDBC = weiboJDBC;
		this.tablename = tablename;
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
			UserAllParamsDomain bozhu = BozhusLibrary.userAllParams(sinaWeiboInfoDao, sinaUserInfoDao, uid);
			/**
			 * 插入数据库
			 */
			if (bozhu == null) {
				logger.info("User: " + uid + " has no params.");
			} else {
				try {
					weiboJDBC.insertSinaBozhuAllParams(tablename, bozhu);
				} catch (SQLException e) {
					// 处理'\xF0\x9F\x94\xAB\xE2\x80...'字符异常
					UserAllParamsDomain temp = new UserAllParamsDomain.Builder(uid).setNickname(bozhu.getNickname())
							.setDescription("").setFanscount(bozhu.getFanscount()).setWeibocount(bozhu.getWeibocount())
							.setAveragewbs(bozhu.getAveragewbs()).setInfluence(bozhu.getInfluence())
							.setActivation(bozhu.getActivation()).setActivecount(bozhu.getActivecount())
							.setAddvratio(bozhu.getAddvratio()).setActiveratio(bozhu.getActiveratio())
							.setMaleratio(bozhu.getMaleratio()).setFansexistedratio(bozhu.getFansexistedratio())
							.setVerify(bozhu.getVerify()).setAllfanscount(bozhu.getAllfanscount())
							.setAllactivefanscount(bozhu.getAllactivefanscount())
							.setTop5provinces(bozhu.getTop5provinces()).setOriratio(bozhu.getOriratio())
							.setAveorirepcom(bozhu.getAveorirepcom()).setAverepcom(bozhu.getAverepcom())
							.setWbsource(bozhu.getWbsource()).setAverepcombyweek(bozhu.getAverepcombyweek())
							.setAverepcombymonth(bozhu.getAverepcombymonth())
							.setAvereposterquality(bozhu.getAvereposterquality())
							.setAveexposionsum(bozhu.getAveexposionsum())
							.setValidrepcombyweek(bozhu.getValidrepcombyweek())
							.setValidrepcombymonth(bozhu.getValidrepcombymonth()).setUsertags(bozhu.getUsertags())
							.setCreatedtime(bozhu.getCreatedtime()).build();
					weiboJDBC.insertSinaBozhuAllParams(tablename, temp);
				}
			}
			//			/**
			//			 * 更新统计信息
			//			 */
			//			BozhuInfo bozhuInfo = new BozhuInfo.Builder().setInfluence(bozhu.getInfluence())
			//					.setActive(bozhu.getActivation()).setWbnum(bozhu.getWeibocount()).setFannum(bozhu.getFanscount())
			//					.setMalerate(bozhu.getMaleratio()).setVrate(bozhu.getAddvratio())
			//					.setAct_fan(bozhu.getActivecount()).setFan_fans(bozhu.getAllfanscount())
			//					.setAct_fan_fans(bozhu.getAllactivefanscount()).setWb_avg_daily(bozhu.getAveragewbs())
			//					.setWb_avg_repost_lastweek(bozhu.getAverepcombyweek())
			//					.setWb_avg_repost_lastmonth(bozhu.getAverepcombymonth()).setWb_avg_repost(bozhu.getAverepcom())
			//					.setOrig_wb_rate(bozhu.getOriratio()).setOrig_wb_avg_repost(bozhu.getAveorirepcom())
			//					.setWb_avg_valid_repost_lastweek(bozhu.getValidrepcombyweek())
			//					.setWb_avg_valid_repost_lastmonth(bozhu.getValidrepcombymonth())
			//					.setRt_user_avg_quality(bozhu.getAvereposterquality())
			//					.setAvg_valid_fan_cover_last100(bozhu.getAveexposionsum()).build();
			//			BozhukuHttpUtils.doBozhuInfoConnectionKeepAlive(BozhukuHttpUtils.getBozhuInfoUrl(uid, "sina"), JSONArray
			//					.fromObject(bozhuInfo).get(0).toString(), "put");
		} catch (WeiboException e) {
			logger.info("WeiboException is occurs in User: " + uid);
			e.printStackTrace();
		} catch (SQLException e) {
			logger.info("SQLException is occurs in User: " + uid);
			e.printStackTrace();
		}
	}

}
