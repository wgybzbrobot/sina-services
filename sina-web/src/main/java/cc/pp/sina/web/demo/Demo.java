package cc.pp.sina.web.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import cc.pp.sina.domain.tool.TransUidData;
import cc.pp.sina.utils.json.JsonUtils;

public class Demo {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

		List<HashMap<String, String>> data = new ArrayList<>();
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("type", "domain");
		map1.put("url", "http://weibo.com/qzly5201314");
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("type", "nickname");
		map2.put("url", "重口味宅女腐女");
		HashMap<String, String> map3 = new HashMap<>();
		map3.put("type", "empty");
		map3.put("url", "");
		data.add(map1);
		data.add(map2);
		data.add(map3);
		TransUidData transUidData = new TransUidData("test", data);
		System.out.println(JsonUtils.toJsonWithoutPretty(transUidData));


	}

}
