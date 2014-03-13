package cc.pp.sina.dao.demo;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2SingleWeibo;
import cc.pp.sina.domain.t2.T2SingleWeiboInfo;
import cc.pp.sina.utils.json.JsonUtils;

public class T2SingleWeiboDemo {

	public static void main(String[] args) {

		T2SingleWeibo singleWeibo = new T2SingleWeibo(MybatisConfig.ServerEnum.local);
		String type = "sina";
		long wid = 1234567890876L;
		String url = "http://t.qq.com/p/t/311892017991112";
		String weiboresult = "abshhd";

		singleWeibo.insertSingleWeibo(type, wid, url);
		singleWeibo.updateSingleWeibo(type, wid, url, weiboresult);
		T2SingleWeiboInfo result = singleWeibo.selectSingleWeibo(wid);
		System.out.println(JsonUtils.toJson(result));
		singleWeibo.deleteSingleWeibo(wid);

	}

}
