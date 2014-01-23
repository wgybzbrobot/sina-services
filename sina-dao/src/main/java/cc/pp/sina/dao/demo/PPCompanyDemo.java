package cc.pp.sina.dao.demo;

import java.util.List;

import cc.pp.sina.dao.company.PPCompany;
import cc.pp.sina.domain.sql.UserBaseInfo;
import cc.pp.sina.utils.json.JsonUtils;

public class PPCompanyDemo {

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		UserBaseInfo userBaseInfo = null;

		/**
		 * 获取某个月企业用户基础信息
		 */
		userBaseInfo = PPCompany.getCompanyInfo("201308", 1000460271L);
		System.out.println(JsonUtils.toJson(userBaseInfo));
		userBaseInfo = PPCompany.getCompanyInfo("201312", 2878322702L);
		System.out.println(JsonUtils.toJson(userBaseInfo));

		/**
		 * 获取某个月增加的企业用户基础信息
		 */
		userBaseInfo = PPCompany.getAddCompanyInfo("201312", 3104245961L);
		System.out.println(JsonUtils.toJson(userBaseInfo));

		/**
		 * 获取某个月减少的企业用户基础信息
		 */
		userBaseInfo = PPCompany.getLeaveCompanyInfo("201312", 1002214962L);
		System.out.println(JsonUtils.toJson(userBaseInfo));

		/**
		 * 获取全部企业用户基础信息
		 */
		List<UserBaseInfo> result = PPCompany.getAllCompanyInfo("pp_sina_company_leave_201312");
		System.out.println(JsonUtils.toJson(result));
	}

}
