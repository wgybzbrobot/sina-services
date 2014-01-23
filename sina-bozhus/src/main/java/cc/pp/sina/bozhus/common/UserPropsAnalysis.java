package cc.pp.sina.bozhus.common;

import cc.pp.sina.algorithms.top.sort.InsertSort;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;

import com.sina.weibo.model.User;

/**
 * 用户分析：用户、微博等属性中间值数据
 * @author wgybzb
 *
 */
public class UserPropsAnalysis {

	/**
	 * 初始化top排序数组
	 */
	public static void initTopArray(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = "0=0";
		}
	}

	/**
	 * 判断年龄
	 */
	public static int analysisAge(BozhuBaseInfo bozhuBaseInfo) {
		if (bozhuBaseInfo.getStatuses_count() < 100) {
			return 0; //0----"others"
		} else if (bozhuBaseInfo.getStatuses_count() < 1000) {
			return 1; //1----"80s"
		} else if (bozhuBaseInfo.getStatuses_count() < 5000) {
			return 2; //2----"90s"
		} else if (bozhuBaseInfo.getStatuses_count() < 10000) {
			return 3; //3----"70s"
		} else {
			return 4; //4----"60s"
		}
	}

	/**
	 * 性别分析
	 */
	public static int analysisGender(User user) {
		if (user.getGender().equals("m")) {
			return 0; //0----m
		} else {
			return 1; //1----f
		}
	}

	public static int analysisGender(BozhuBaseInfo bozhuBaseInfo) {
		if (bozhuBaseInfo.getGender().equals("m")) {
			return 0; //0----m
		} else {
			return 1; //1----f
		}
	}

	/**
	 * 用户等级分析（按粉丝量）
	 */
	public static int analysisFansClassByFans(User user) {
		if (user.getFollowersCount() < 100) {
			return 0; //0-----"X<100"
		} else if (user.getFollowersCount() < 1000) {
			return 1; //1-----"100<X<1000"
		} else if (user.getFollowersCount() < 10000) {
			return 2; //2-----"1000<X<1w"
		} else if (user.getFollowersCount() < 100000) {
			return 3; //3-----"1w<X<10w"
		} else {
			return 4; //4-----"X>10w"
		}
	}

	public static int analysisFansClassByFans(BozhuBaseInfo bozhuBaseInfo) {
		if (bozhuBaseInfo.getFollowers_count() < 100) {
			return 0; //0-----"X<100"
		} else if (bozhuBaseInfo.getFollowers_count() < 1000) {
			return 1; //1-----"100<X<1000"
		} else if (bozhuBaseInfo.getFollowers_count() < 10000) {
			return 2; //2-----"1000<X<1w"
		} else if (bozhuBaseInfo.getFollowers_count() < 100000) {
			return 3; //3-----"1w<X<10w"
		} else {
			return 4; //4-----"X>10w"
		}
	}

	/**
	 * 用户质量
	 */
	public static int analysisQuality(User user) {
		if (user.isVerified() == false) {
			if (user.getFollowersCount() < 50 || user.getStatusesCount() < 20) {
				return 0;
			} else if ((user.getFollowersCount() * 3 < user.getStatusesCount()) && (user.getFollowersCount() < 150)) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

	public static int analysisQuality(BozhuBaseInfo bozhuBaseInfo) {
		if (bozhuBaseInfo.isVerified() == false) {
			if (bozhuBaseInfo.getFollowers_count() < 50 || bozhuBaseInfo.getStatuses_count() < 20) {
				return 0;
			} else if ((bozhuBaseInfo.getFollowers_count() * 3 < bozhuBaseInfo.getStatuses_count())
					&& (bozhuBaseInfo.getFollowers_count() < 150)) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

	/**
	 * 认证与否
	 */
	public static int analysisVerified(User user) {
		if (user.isVerified() == false) {
			return 0;
		} else {
			return 1;
		}
	}

	public static int analysisVerified(BozhuBaseInfo bozhuBaseInfo) {
		if (bozhuBaseInfo.isVerified() == false) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 判断认证类型
	 */
	public static int analysisVerifiedType(BozhuBaseInfo bozhuBaseInfo) {
		if ((bozhuBaseInfo.getVerified_type() < 9) && (bozhuBaseInfo.getVerified_type() >= 0)) {
			return bozhuBaseInfo.getVerified_type();
		} else if (bozhuBaseInfo.getVerified_type() == 200) {
			return 9;
		} else if (bozhuBaseInfo.getVerified_type() == 220) {
			return 10;
		} else if (bozhuBaseInfo.getVerified_type() == 400) {
			return 11;
		} else if (bozhuBaseInfo.getVerified_type() == -1) {
			return 12;
		} else {
			return 13;
		}
	}

	/**
	 * 粉丝的活跃粉丝数
	 */
	public static int analysisAllActiveFansCount(User user) {
		if (user.isVerified() == false) {
			if (user.getFollowersCount() < 50 || user.getStatusesCount() < 20) {
				return 0;
			} else if ((user.getFollowersCount() * 3 < user.getStatusesCount()) && (user.getFollowersCount() < 150)) {
				return 0;
			} else {
				return user.getFollowersCount();
			}
		} else {
			return user.getFollowersCount();
		}
	}

	/**
	 * 用户区域分析
	 */
	public static int analysisProvince(User user) {
		if (user.getProvince() == 400) {
			return 0;
		} else {
			return user.getProvince();
		}
	}

	public static int analysisProvince(BozhuBaseInfo bozhuBaseInfo) {
		if (bozhuBaseInfo.getProvince() == 400) {
			return 0;
		} else {
			return bozhuBaseInfo.getProvince();
		}
	}

	/**
	 * 用户区域分布排序
	 */
	public static String[] analysisSortedProvince(int[] province) {
		String[] provinceTable = new String[province.length];
		for (int i = 0; i < province.length; i++) {
			provinceTable[i] = "0=0";
		}
		for (int i = 0; i < province.length; i++) {
			provinceTable = InsertSort.toptable(provinceTable, i + "=" + province[i]);
		}

		return provinceTable;
	}

}
