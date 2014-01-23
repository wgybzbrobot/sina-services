package cc.pp.sina.bozhus.tags;

import cc.pp.sina.bozhus.info.SinaUserInfoDao;
import cc.pp.sina.bozhus.info.SinaUserInfoDaoImpl;
import cc.pp.sina.domain.bozhus.UserTag;
import cc.pp.sina.tokens.service.TokenService;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class UserTagsParams {

	//	private static Logger logger = LoggerFactory.getLogger(UserTagsParams.class);

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		List<String> sinaTokens = new ArrayList<String>();
		sinaTokens.add("2.00D9c_yBdcZIJC542f419776_YcRsD");
		sinaTokens.add("2.00dNiIRCdcZIJC63fce1aba1M6zbLE");
		sinaTokens.add("2.00zWWk4BdcZIJC43cf08470bQcr_fB");
		TokenService tokenService = new TokenService(sinaTokens);
		SinaUserInfoDao sinaUserInfoDao = new SinaUserInfoDaoImpl(tokenService);

		// 1862087393：粉丝小于5000
		// 2775934450：粉丝大于5000
		List<String> result = UserTagsParams.analysis("1862087393", sinaUserInfoDao, 10);
		System.out.println(JSONArray.fromObject(result));
	}

	/**
	 * 限制标签个数
	 */
	public static List<String> analysis(String uid, SinaUserInfoDao sinaUserInfoDao, int tagsNum) {

		List<UserTag> tagList = sinaUserInfoDao.getSinaUserTags(uid, tagsNum);
		if (tagList != null) {
			List<String> result = new ArrayList<String>();
			for (UserTag temp : tagList) {
				result.add(temp.getValue());
			}
			return result;
		} else {
			return null;
		}
	}

}
