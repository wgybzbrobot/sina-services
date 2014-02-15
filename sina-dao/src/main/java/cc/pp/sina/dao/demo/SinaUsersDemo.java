package cc.pp.sina.dao.demo;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.users.SinaUsers;

import com.sina.weibo.model.WeiboException;

public class SinaUsersDemo {

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws WeiboException {

		//		int max = SinaUsers.getMaxBid("sinauserbaseinfo_test");
		//		System.out.println(max);
		//		List<UserInfo> users = SinaUsers.getSinaUserInfos("sinauserbaseinfo_test", 1, 100);
		//		System.out.println(users.size());
		//		UserInfo user = SinaUsers.getSinaUserInfo("sinauserbaseinfo_test", 1001318521L);
		//		System.out.println(JsonUtils.toJson(user));
		//		Users users = new Users();
		//		User user = users.showUserById("1001318521", "2.00lb_yKCdcZIJC4bd5178db43QmLQE");
		//		SinaUsers.insertSinaUserInfo("sinauserbaseinfo_test1", user);
		//		SinaUsers.updateSinaUserInfo("sinauserbaseinfo_test1", 1001318521L);
		SinaUsers sinaUsers = new SinaUsers(MybatisConfig.ServerEnum.local);
		sinaUsers.deleteSinaUserInfo("sinauserbaseinfo_test1", 1001318521L);

	}

}
