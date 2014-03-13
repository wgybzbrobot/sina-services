package cc.pp.content.library.statistic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.content.library.common.HttpUtils;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.content.library.ColumnUsedDetailed;
import cc.pp.sina.domain.content.library.LocationAndCovering;
import cc.pp.sina.utils.json.JsonUtils;

/**
 * 统计使用内容库用户的粉丝覆盖量、区域等信息。
 * @author wgybzb
 *
 */
public class UsersInfoStatistic {

	private static Logger logger = LoggerFactory.getLogger(UsersInfoStatistic.class);

	// 用户基础信息接口
	private static final String USER_BASEINFO_BASE_URL = "http://114.112.65.13:8111/sina/users/";
	//	private static final String USER_BASEINFO_BASE_URL = "http://60.169.74.26:8111/sina/users/";
	private static final String TYPE = "/basic";

	private static final int AVE_FANS = 28;
	private static final Random RANDOM = new Random();

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		long[] uids = { 2640541444L, 2845189190L, 3928491956L, 3202629800L, 2647560614L, 2625486467L, 3816110776L,
				5036373902L, 3170543797L, 1745483307L, 2296671270L, 2721922494L, 2707096431L, 5040994217L, 1869618977L,
				1822686584L, 5014738528L, 3505685761L, 5042704392L, 3516233860L, 5046180243L, 1972307677L, 2587766224L,
				5044872459L, 2215090110L, 2089590493L, 3059236520L, 3205153037L, 3907398531L, 1840184081L, 5036673578L,
				1856786000L, 3963627259L, 1029781252L, 3992640535L, 3190366542L, 3504936657L, 3981796541L, 3647081015L,
				3867998824L, 3656560310L, 3228903010L, 1649183483L, 3161929661L, 2232230061L, 3668065735L, 3501264070L,
				3225662691L, 3546863191L, 2334113131L };
		for (long uid : uids) {
			if (UsersInfoStatistic.getSianUserBaseInfo(uid) != null) {
				System.out.println(uid + "," + UsersInfoStatistic.getSianUserBaseInfo(uid).getScreen_name());
			}
		}

	}

	/**
	 * 统计多个用户的区域分布、粉丝覆盖量
	 */
	@Deprecated
	public static LocationAndCovering getSinaUsersStatistic(HashMap<Long, Integer> users, int type) {

		LocationAndCovering result = new LocationAndCovering();
		BozhuBaseInfo bozhu;
		int count = 0;
		for (Entry<Long, Integer> temp : users.entrySet()) {
			if (count++ > 1_000) {
				break;
			}
			bozhu = getSianUserBaseInfo(temp.getKey());
			if (bozhu == null) {
				continue;
			}
			if (type == 1) {
				//  区域分析
				if (bozhu.getProvince() == 400) {
					result.getProvince()[0] += temp.getValue();
				} else {
					result.getProvince()[bozhu.getProvince()] += temp.getValue();
				}
			} else if (type == 2) {
				// 粉丝覆盖量
				if (result.getCoverings().get(temp.getKey()) == null) {
					result.getCoverings().put(temp.getKey(),
							(long) (bozhu.getFollowers_count() + AVE_FANS + RANDOM.nextInt(30)));
				}
			}
		}

		return result;
	}

	public static void coveringAndTopNUsers(HashMap<Long, Integer> uids, int allCount,
			ColumnUsedDetailed columnUsedDetailed) {

		BozhuBaseInfo bozhu;
		long covering = 0;
		for (Entry<Long, Integer> uid : uids.entrySet()) {
			bozhu = getSianUserBaseInfo(uid.getKey());
			if (bozhu == null) {
				continue;
			}
			bozhu.setBid(uid.getValue());
			columnUsedDetailed.getTopNUserBaseInfos().add(bozhu);
			covering += bozhu.getFollowers_count();
		}
		covering = covering / uids.size();
		columnUsedDetailed.setAllCoveringNum(covering * allCount);
	}

	/**
	 * 获取单个用户信息
	 */
	public static BozhuBaseInfo getSianUserBaseInfo(long uid) {

		String baseInfo = HttpUtils.doGet(USER_BASEINFO_BASE_URL + uid + TYPE, "utf-8");
		if (baseInfo.contains("errorCode")) {
			return null;
		}
		BozhuBaseInfo bozhu = null;
		try {
			bozhu = JsonUtils.getObjectMapper().readValue(baseInfo, BozhuBaseInfo.class);
		} catch (IOException e) {
			//			logger.error("BozhuBaseInfo's mapping is error," + e.getMessage());
			logger.error("baseInfo=" + baseInfo + ",url=" + USER_BASEINFO_BASE_URL + uid + TYPE);
			throw new RuntimeException(e);
		}
		return bozhu;
	}

}
