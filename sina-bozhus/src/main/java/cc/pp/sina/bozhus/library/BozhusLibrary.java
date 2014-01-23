package cc.pp.sina.bozhus.library;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import cc.pp.sina.bozhus.fans.UserAndFansParams;
import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDao;
import cc.pp.sina.bozhus.info.SinaWeiboInfoDaoImpl;
import cc.pp.sina.bozhus.tags.UserTagsParams;
import cc.pp.sina.bozhus.weibos.UserAllWeibosParams;
import cc.pp.sina.bozhus.weibos.UserServalWeibosParams;
import cc.pp.sina.domain.bozhus.UserAllParamsDomain;
import cc.pp.sina.domain.bozhus.UserAllWeibosDomain;
import cc.pp.sina.domain.bozhus.UserAndFansDomain;
import cc.pp.sina.domain.bozhus.UserServalWeibosDomain;
import cc.pp.sina.tokens.service.TokenService;

import com.sina.weibo.model.WeiboException;

/**
 * 微博主参数分析
 * @author wgybzb
 *
 */
public class BozhusLibrary {

	public static final String SINA_UNUSED_UIDS = "sina_unused_uids";

	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws WeiboException, SQLException {

		TokenService tokenService = new TokenService();
		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00TaAMACdcZIJC1694b41295JLRojB");
		sinaTokens.add("2.0000onHtdcZIJC5ded8d28dc0CtU8F");
		sinaTokens.add("2.00_7nPPCdcZIJC98cf0e45900wvkMP");
		tokenService.setSinaTokens(sinaTokens);
		SinaWeiboInfoDao sinaWeiboInfoDao = new SinaWeiboInfoDaoImpl(tokenService);
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		UserAllParamsDomain bozhu = BozhusLibrary.userAllParams(sinaWeiboInfoDao, sinaUserInfoDao, "2240090994");
		System.out.println(JSONArray.fromObject(bozhu));

		//		WeiboJDBC weiboJDBC = new WeiboJDBC("127.0.0.1", "root", "root", "pp_fenxi");
		//		weiboJDBC.dbConnection();
		//		weiboJDBC.insertSinaBozhuAllParams("sina_bozhus_library", bozhu);
		//		weiboJDBC.dbClose();
	}

	/**
	 * 用户所有参数分析
	 */
	public static UserAllParamsDomain userAllParams(SinaWeiboInfoDao sinaWeiboInfoDao, SinaUserInfoDao sinaUserInfoDao,
			String uid) throws WeiboException, SQLException {

		/*
		 * 用户及其粉丝分析，10页粉丝数据，每页200个，最多25页
		 */
		UserAndFansDomain userFans = UserAndFansParams.analysis(uid, sinaUserInfoDao, 25);
		if (userFans == null) {
			return null;
		}

		/*
		 * 用户标签信息分析，10个标签
		 */
		String userTags = JSONArray.fromObject(UserTagsParams.analysis(uid, sinaUserInfoDao, 10)).toString();
		if (userTags == null) {
			userTags = "0";
		}

		/*
		 * 用户所有微博分析
		 *     第三个参数：10页微博数据，每页100个，最多20页
		 *     第四个参数：分析单条微博条数
		 */
		UserAllWeibosDomain userWeibos = UserAllWeibosParams.analysis(uid, sinaWeiboInfoDao, 5, 5);
		if (userWeibos == null) {
			userWeibos = new UserAllWeibosDomain.Builder().build();
		}

		/*
		 * 用户多条微博分析
		 *     第三个参数：每条微博分析的页数，每页200条，最多10页
		 */
		UserServalWeibosDomain weibosParams = null;
		if (userWeibos != null) {
			try {
				weibosParams = UserServalWeibosParams.analysis(userWeibos.getLastweiboids(),
						sinaWeiboInfoDao, 5);
			} catch (RuntimeException e) {
				weibosParams = null;
			}
		}
		if (weibosParams == null) {
			weibosParams = new UserServalWeibosDomain.Builder(0.0f, 0).build();
		}

		// 平均有效转评量(按周、按月)
		float validRepComByWeek = userWeibos.getAverepcombyweek() * weibosParams.getAvereposterquality();
		float validRepComByMonth = userWeibos.getAverepcombymonth() * weibosParams.getAvereposterquality();

		UserAllParamsDomain result = new UserAllParamsDomain.Builder(uid).setNickname(userFans.getNickname())
				.setDescription(userFans.getDescription()).setFanscount(userFans.getFanscount())
				.setWeibocount(userFans.getWeibocount()).setAveragewbs(userFans.getAveragewbs())
				.setInfluence(userFans.getInfluence()).setActivation(userFans.getActivation())
				.setActivecount(userFans.getActivecount()).setAddvratio(userFans.getAddvratio())
				.setActiveratio(userFans.getActiveratio()).setMaleratio(userFans.getMaleratio())
				.setFansexistedratio(userFans.getFansexistedratio()).setVerify(userFans.getVerify())
				.setAllfanscount(userFans.getAllfanscount()).setAllactivefanscount(userFans.getAllactivefanscount())
				.setTop5provinces(JSONArray.fromObject(userFans.getTop5provinces()).toString())
				.setOriratio(userWeibos.getOriratio()).setAveorirepcom(userWeibos.getAveorirepcom())
				.setAverepcom(userWeibos.getAverepcom())
				.setWbsource(JSONArray.fromObject(userWeibos.getWbsource()).toString())
				.setAverepcombyweek(userWeibos.getAverepcombyweek())
				.setAverepcombymonth(userWeibos.getAverepcombymonth())
				.setAvereposterquality(weibosParams.getAvereposterquality())
				.setAveexposionsum(weibosParams.getAveexposionsum() + userFans.getFanscount())
				.setValidrepcombyweek(validRepComByWeek).setValidrepcombymonth(validRepComByMonth)
				.setUsertags(userTags).setCreatedtime(userFans.getCreatedtime()).build();

		return result;
	}

}
