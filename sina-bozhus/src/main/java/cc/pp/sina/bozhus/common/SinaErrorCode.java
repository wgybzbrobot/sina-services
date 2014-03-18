package cc.pp.sina.bozhus.common;

public class SinaErrorCode {

	public static final int ERROR_CODE_IP_REQUESTS_OUT_OF_RATE_LIMIT = 10022;

	public static final int ERROR_CODE_SYSTEM_ERROR = 10001;

	public static final int ERROR_CODE_EXPIRED_TOKEN = 21327;
	public static final int ERROR_CODE_TOKEN_1 = 21314;
	public static final int ERROR_CODE_TOKEN_2 = 21315;
	public static final int ERROR_CODE_TOKEN_3 = 21316;
	public static final int ERROR_CODE_TOKEN_4 = 21317;

	// 官方说是appkey缺少，但是换个token后就可以，多次测试都是这样，所以可以认为是token失效
	public static final int ERROR_CODE_APPKEY_MISSING = 10006;

	// 远程服务错误，503错误
	public static final int ERROR_CODE_REMOTE_SERVICE = 10003;

	// 官方未说明该错误，不过应该是token无效
	public static final int ERROR_CODE_21332 = 21332;

	// 502错误
	public static final int ERROR_CODE_502 = -1;

	// 20003，用户不存在
	public static final int ERROR_CODE_20003 = 20003;

	// 微博不存在
	public static final int ERROR_CODE_20101 = 20101;

	// RPC错误
	public static final int ERROR_CODE_10011 = 10011;

	// 用户帐号被冻结
	public static final int ERROR_CODE_20145 = 20145;

	// fuid is wrong!
	public static final int ERROR_CODE_10025 = 10025;

}
