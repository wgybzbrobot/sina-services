package cc.pp.sina.dao.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.pp.sina.domain.tool.TransUidData;
import cc.pp.sina.utils.json.JsonUtils;

public class TransUidDemo {

	public static void main(String[] args) {

		//		TransUid.insertTransUidInfo("trans_uid_result", "shsh", "http://www", "123655451", "wgybzb1");
		//		TransUid.insertTransUidInfo("trans_uid_result", "shsh", "http://wbo.com2", "123655452", "wgybzb2");
		//		TransUid.insertTransUidInfo("trans_uid_result", "shsh", "http://wo.com3", "123655453", "wgybzb3");
		//
		//		List<TransUidInfo> result = TransUid.getTransUidInfos("trans_uid_result", "shshfjf");
		//		System.out.println(result.size());

		List<HashMap<String, String>> data = new ArrayList<>();
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("type", "domain");
		map1.put("url", "http://weibo.com/uc3115307");
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("type", "nickname");
		map2.put("url", "IT观察员张莹");
		data.add(map1);
		data.add(map2);
		TransUidData transUidData = new TransUidData("test", data);
		System.out.println(JsonUtils.toJsonWithoutPretty(transUidData));

	}

}
