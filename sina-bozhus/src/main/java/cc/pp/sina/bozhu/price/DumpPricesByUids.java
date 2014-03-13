package cc.pp.sina.bozhu.price;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.api.Response;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;
import cc.pp.sina.domain.bozhus.price.UserPrices;
import cc.pp.sina.tokens.verify.HttpUtils;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.net.URLCode;

/**
 * 获取一批用户价格数据
 * @author wgybzb
 *
 */
public class DumpPricesByUids {

	private static Logger logger = LoggerFactory.getLogger(DumpPricesByUids.class);

	/*
	 * 用户查询接口
	 */
	private static final String USER_BASE_INFO = "http://114.112.65.13:8111/sina/users/";
	private static final String USER_UID_URL = "http://114.112.65.13:8983/solr/bozhu_library/select?";

	/*
	 * 价格查询接口
	 */
	private static final String USER_PRICE_URL = "http://60.169.74.48:8080/sina/users/";
	private static final String PRICE_BASE = "/price/sources";

	/**
	 * 主函数
	 */
	public static void main(String[] args) throws IOException {

		HashSet<Long> result = DumpPricesByUids.getAllUidsByKeywords("娱乐圈舞美师韩流电视恋爱女神");
		System.out.println(result.size());

		HashMap<Long, UserPrices> userHasPrices = new HashMap<>();
		UserPrices temp = null;
		int count = 0;
		for (long uid : result) {
			if (++count % 100 == 0) {
				logger.info("Tackle at: " + count);
			}
			temp = DumpPricesByUids.getPricesBuUid(uid);
			if (temp != null) {
				logger.info("userHasPrices's size = " + userHasPrices.size());
				userHasPrices.put(uid, temp);
			}
		}

		BozhuBaseInfo baseInfo;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("yezi_users.csv")));) {
			bw.append("url").append(",").append("nickname").append(",").append("prices");
			bw.newLine();
			for (Entry<Long, UserPrices> userPrices : userHasPrices.entrySet()) {
				baseInfo = DumpPricesByUids.getUserBaseInfo(userPrices.getKey());
				if (baseInfo == null) {
					logger.info("Not Existed : " + userPrices.getKey());
					continue;
				}
				bw.append("http://www.weibo.com/u/" + baseInfo.getId()).append(",").append(baseInfo.getScreen_name())
						.append(",").append(userPrices.getValue().getPrices().get(0).getPrice() + "");
				bw.newLine();
			}
		}

	}

	/**
	 * 获取某个用户的价格数据
	 */
	public static UserPrices getPricesBuUid(long uid) {
		String response = HttpUtils.doGet(USER_PRICE_URL + uid + PRICE_BASE, "utf-8");
		if (response.length() < 10) {
			return null;
		}
		try {
			UserPrices result = JsonUtils.getObjectMapper().readValue(response.substring(1, response.length() - 1),
					UserPrices.class);
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据关键词获取所有uid
	 */
	public static HashSet<Long> getAllUidsByKeywords(String keywords) {
		HashSet<Long> result = new HashSet<>();
		long num = getAllNumByKeywords(keywords);
		logger.info("All num is " + num);
		int rows = 100;
		List<Long> temp;
		for (int i = 0; i <= num / rows; i++) {
			logger.info("Get at: " + i * rows);
			temp = getUidsByKeywords(keywords, i * rows, rows);
			for (long uid : temp) {
				result.add(uid);
			}
		}
		return result;
	}

	public static List<Long> getUidsByKeywords(String keywords, int start, int rows) {
		List<Long> result = new ArrayList<>();
		String url = USER_UID_URL + "q=" + URLCode.Utf8UrlEncode(keywords) + "&start=" + start + "&rows=" + rows
				+ "&fl=username&wt=json";
		String response = HttpUtils.doGet(url, "utf-8");
		response = response.substring(response.lastIndexOf("response") + 10, response.length() - 1);
		try {
			Response uidInfo = JsonUtils.getObjectMapper().readValue(response, Response.class);
			for (HashMap<String, Long> user : uidInfo.getDocs()) {
				result.add(user.get("username"));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static long getAllNumByKeywords(String keywords) {
		String response = HttpUtils.doGet(USER_UID_URL + "q=" + URLCode.Utf8UrlEncode(keywords)
				+ "&fl=username&wt=json", "utf-8");
		response = response.substring(response.lastIndexOf("response") + 10, response.length() - 1);
		try {
			Response uidInfo = JsonUtils.getObjectMapper().readValue(response, Response.class);
			return uidInfo.getNumFound();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static BozhuBaseInfo getUserBaseInfo(long uid) {
		String response = HttpUtils.doGet(USER_BASE_INFO + uid + "/basic", "utf-8");
		if (response.contains("errorCode")) {
			return null;
		}
		try {
			BozhuBaseInfo result = JsonUtils.getObjectMapper().readValue(response, BozhuBaseInfo.class);
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
