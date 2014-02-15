package cc.pp.sina.web.bozhu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.datacenter.bozhudao.BozhuDao;
import cc.pp.datacenter.bozhudao.BozhuDbConnection;
import cc.pp.datacenter.bozhudao.BozhuDetail;
import cc.pp.datacenter.bozhudao.BozhuNotFoundException;
import cc.pp.sina.dao.bozhus.BozhuLibrary;
import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.users.SinaUsers;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.users.UserInfo;
import cc.pp.sina.query.index.BozhuIndex;

/**
 * 索引300w博主数据
 * @author wgybzb
 *
 */
public class IndexBozhusData {

	private static Logger logger = LoggerFactory.getLogger(IndexBozhusData.class);

	public static final String DB_URL = "jdbc:mysql://192.168.1.5:3306/pp_fenxi";
	public static final String DB_USERNAME = "pp_fenxi";
	public static final String DB_PASSWORD = "q#tmuYzC@sqB6!ok@sHd";

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		logger.info("读取300多万博主Uid：");
		List<Long> uids = BozhuLibrary.getBozhuUids();
		logger.info("Uids' size=" + uids.size());

		SinaUsers sinaUsers = new SinaUsers(MybatisConfig.ServerEnum.fenxi);

		logger.info("循环索引每一个博主数据：");
		try {
			BozhuIndex bozhuIndex = new BozhuIndex(BozhuIndex.INDEX_DIR);
			BozhuDbConnection.connectDb(DB_URL, DB_USERNAME, DB_PASSWORD);
			BozhuDao dao = new BozhuDao();
			int count = 0;
			List<UserAllParamsDomain> bozhus = new ArrayList<>();
			for (long uid : uids) {
				if (++count % 1_0000 == 0) {
					logger.info("Index at: " + count);
					bozhuIndex.addIndexDatas(bozhus);
					bozhus = new ArrayList<>();
				}
				UserInfo baseInfo = sinaUsers.getSinaUserInfo("sinauserbaseinfo_" + uid % 32, uid);
				BozhuDetail detail = null;
				try {
					detail = dao.getBozhuDetail(uid);
				} catch (BozhuNotFoundException e) {
					continue;
				} catch (Exception e) {
					continue;
				}
				if (baseInfo == null || detail == null) {
					continue;
				}
				UserAllParamsDomain userAllParamsDomain = new UserAllParamsDomain.Builder(String.valueOf(uid))
						.setNickname(baseInfo.getScreen_name()).setDescription(baseInfo.getDescription())
						.setFanscount(baseInfo.getFollowers_count()).setWeibocount(baseInfo.getStatuses_count())
						.setAveragewbs(detail.getWb_avg_daily()).setInfluence(detail.getInfluence())
						.setActivation(detail.getActive()).setActivecount(detail.getAct_fan())
						.setAddvratio(detail.getVrate()).setActiveratio(detail.getAct_fan_rate())
						.setMaleratio(detail.getMalerate()).setFansexistedratio(detail.getExsit_fan_rate())
						.setVerify(baseInfo.getVerified_type()).setAllfanscount(detail.getFan_fans())
						.setAllactivefanscount(detail.getAct_fan_fans()).setTop5provinces("0")
						.setOriratio(detail.getOrig_wb_rate()).setAveorirepcom(detail.getOrig_wb_avg_repost())
						.setAverepcom(detail.getWb_avg_repost()).setWbsource("0")
						.setAverepcombyweek(detail.getWb_avg_repost_lastweek())
						.setAverepcombymonth(detail.getWb_avg_repost_lastmonth())
						.setAvereposterquality(detail.getRt_user_avg_quality())
						.setAveexposionsum(detail.getAvg_valid_fan_cover_last100())
						.setValidrepcombyweek(detail.getWb_avg_valid_repost_lastweek())
						.setValidrepcombymonth(detail.getWb_avg_valid_repost_lastmonth()).setUsertags("0")
						.setIdentitytype("0").setIndustrytype("0").setFanstags("0").setFansage("0").build();
				//				.setCreatedtime(0)  // 暂时去掉
				bozhus.add(userAllParamsDomain);
			}
			bozhuIndex.addIndexDatas(bozhus);
			bozhuIndex.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
