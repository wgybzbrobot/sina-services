package cc.pp.sina.dao.demo;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2UserFans;
import cc.pp.sina.utils.json.JsonUtils;

public class T2UserFansDemo {

	public static void main(String[] args) {

		T2UserFans t2User = new T2UserFans(MybatisConfig.ServerEnum.local);

		/**
		 * 新浪
		 */
		long sinaUid = 123456789L;
		String fansresult = "abccccded";
		t2User.insertT2SinaUser(sinaUid);
		System.out.println(JsonUtils.toJson(t2User.getSinaNewUids()));
		System.out.println(JsonUtils.toJson(t2User.getSinaAllUids()));
		t2User.updateT2SinaUser(sinaUid, fansresult);
		System.out.println(JsonUtils.toJson(t2User.selectT2SinaUser(sinaUid)));
		t2User.deleteT2SinaUser(sinaUid);

		/**
		 * 腾讯
		 */
		String tencentUid = "wgybzb";
		t2User.insertT2TencentUser(tencentUid);
		System.out.println(JsonUtils.toJson(t2User.getTencentNewUids()));
		System.out.println(JsonUtils.toJson(t2User.getTencentAllUids()));
		t2User.updateT2TencentUser(tencentUid, fansresult);
		System.out.println(JsonUtils.toJson(t2User.selectT2TencentUser(tencentUid)));
		t2User.deleteT2TencentUser(tencentUid);

	}

}
