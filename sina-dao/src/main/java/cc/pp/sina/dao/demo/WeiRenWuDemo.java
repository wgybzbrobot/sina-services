package cc.pp.sina.dao.demo;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.weirenwu.WeiRenWu;
import cc.pp.sina.domain.weirenwu.BozhuExtendInfo;
import cc.pp.sina.domain.weirenwu.SelectBozhuParams;
import cc.pp.sina.utils.json.JsonUtils;

public class WeiRenWuDemo {

	public static void main(String[] args) {

		WeiRenWu weiRenWu = new WeiRenWu(MybatisConfig.ServerEnum.local);

		String username = "1234567890";

		weiRenWu.insertSinaBozhuUid(username);
		System.out.println(JsonUtils.toJson(weiRenWu.selectAllSinaBozhuUids()));
		System.out.println(weiRenWu.selectSinaBzid(username));

		BozhuExtendInfo bozhuExtendInfo = new BozhuExtendInfo();
		bozhuExtendInfo.setBzid(weiRenWu.selectSinaBzid(username));
		bozhuExtendInfo.setIdentity_type("1");
		bozhuExtendInfo.setIndustry_type("2");
		bozhuExtendInfo.setFans_age("3");
		bozhuExtendInfo.setFans_tags("4");

		weiRenWu.insertSinaBozhuExtendInfo(bozhuExtendInfo);
		System.out.println(JsonUtils.toJson(weiRenWu.selectSinaBozhuExtendInfo(username)));
		SelectBozhuParams selectBozhuParams = new SelectBozhuParams(15, 40);
		System.out.println(JsonUtils.toJson(weiRenWu.selectSinaBozhuExtendInfos(selectBozhuParams)));

	}

}
