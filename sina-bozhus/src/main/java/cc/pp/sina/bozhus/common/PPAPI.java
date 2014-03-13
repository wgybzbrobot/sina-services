package cc.pp.sina.bozhus.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.tokens.verify.HttpUtils;
import cc.pp.sina.utils.json.JsonUtils;

public class PPAPI {

	private static Logger logger = LoggerFactory.getLogger(PPAPI.class);

	// 用户基础信息接口
	private static final String USER_BASEINFO_BASE_URL = "http://114.112.65.13:8111/sina/users/";
	//	private static final String USER_BASEINFO_BASE_URL = "http://60.169.74.26:8111/sina/users/";
	private static final String TYPE = "/basic";

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {

		System.out.println(JsonUtils.toJson(PPAPI.getSianUserBaseInfo(1644646784L)));

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
			logger.error("baseInfo=" + baseInfo + ",url=" + USER_BASEINFO_BASE_URL + uid + TYPE);
			throw new RuntimeException(e);
		}
		return bozhu;
	}

}
