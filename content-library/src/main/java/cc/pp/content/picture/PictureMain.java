package cc.pp.content.picture;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.content.library.statistic.UsersInfoStatistic;
import cc.pp.sina.domain.bozhus.BozhuBaseInfo;

public class PictureMain {

	private static Logger logger = LoggerFactory.getLogger(PictureMain.class);

	public static final String[] DATES = {"201303","201304","201305","201306","201307",
		"201308","201309","201310","201311","201312","201401","201402","201403"};

	private static final int[] KEYS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 200, 220, 400, -1 };
	private static final String[] VALUES = { "名人", "政府", "企业", "媒体", "校园", "网站", "应用", "团体机构", "待审企业", "初级达人",
			"通过审核的达人", "已故V用户", "普通用户" };
	private static HashMap<Integer, String> VERIFIED_TYPE = new HashMap<>();

	static {
		for (int i = 0; i < KEYS.length; i++) {
			VERIFIED_TYPE.put(KEYS[i], VALUES[i]);
		}
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

		PSJDBC jdbc = new PSJDBC("root", "root", "pp_weibo");

		for (String d : PictureMain.DATES) {
			HashMap<String, List<UsePicInfo>> result = jdbc.getPictureInfo("wb_picture_record_" + d);
			for (Entry<String, List<UsePicInfo>> temp : result.entrySet()) {
				System.out.println("wb_picture_record_" + d + ":" + temp.getKey() + "\t" + temp.getValue().size());
				UsePicResult usePicResult = getActiveResult(temp.getValue());
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("picture_statistic_result_" + d
						+ "_" + temp.getKey() + ".csv")))) {
					bw.append("AllActiveNum");
					bw.newLine();
					bw.append(usePicResult.getActiveNum() + "");
					bw.newLine();
					bw.newLine();
					bw.append("AddVRatio");
					bw.newLine();
					bw.append(usePicResult.getAddVRatio() + "");
					bw.newLine();
					bw.newLine();
					for (Entry<String,Integer> t : usePicResult.getVerifiedInfos().entrySet()) {
						bw.append(t.getKey()).append(",");
					}
					bw.newLine();
					for (Entry<String,Integer> t : usePicResult.getVerifiedInfos().entrySet()) {
						bw.append(t.getValue() + "").append(",");
					}
					bw.newLine();
					bw.newLine();
					bw.append("UserUrl").append(",").append("UsedCount").append(",").append("IsAddV").append(",")
							.append("VerifiedType");
					bw.newLine();
					for (Entry<ActiveUserInfo, Integer> t : usePicResult.getActiveUsers().entrySet()) {
						if (t.getKey().getUrl().contains("weibo.com")) {
							bw.append(t.getKey().getUrl()).append(",").append(t.getValue() + "").append(",")
									.append(t.getKey().isAddV() + "").append(",")
									.append(VERIFIED_TYPE.get(t.getKey().getVerifiedType()));
						} else {
							bw.append(t.getKey().getUrl()).append(",").append(t.getValue() + "");
						}
						bw.newLine();
					}
					bw.newLine();
					bw.append("PicUrl").append(",").append("UsedCount");
					bw.newLine();
					for (Entry<String, Integer> t : usePicResult.getTopNPictures().entrySet()) {
						bw.append(t.getKey()).append(",").append(t.getValue() + "");
						bw.newLine();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		jdbc.close();
	}

	/**
	 *  1、长期使用veer正版图片的活跃微博用户数量 （长期活跃用户定义： 每周发veer图片配图3次以上，连续使用2个月以上）
	 *     也就是一个月使用次数大于12，使用月数大于2
	 *  2、长期活跃用户名单（ 微博账号，微博名称，微博url, 是否加V, 蓝V, 还是黄V?）
	 * 3、veer图片累计使用量达到102万次， 那么可否根据使用频次高低进行分析？
	 *     可否导出使用频次最高的图片列表。比如: top1000 ?
	 */
	public static UsePicResult getActiveResult(List<UsePicInfo> pics) {

		UsePicResult result = new UsePicResult();
		HashMap<String, Integer> userCount = new HashMap<>();
		HashMap<String, Integer> userCounts = new HashMap<>();
		HashMap<String, Integer> picCount = new HashMap<>();
		for (UsePicInfo usePicInfo : pics) {
			if (userCount.get(getUserUrl(usePicInfo)) == null) {
				userCount.put(getUserUrl(usePicInfo), 1);
			} else {
				userCount.put(getUserUrl(usePicInfo), userCount.get(getUserUrl(usePicInfo)) + 1);
			}
			if (picCount.get(usePicInfo.getUrl()) == null) {
				picCount.put(usePicInfo.getUrl(), 1);
			} else {
				picCount.put(usePicInfo.getUrl(), picCount.get(usePicInfo.getUrl()) + 1);
			}
		}
		for (Entry<String, Integer> t : userCount.entrySet()) {
			if (t.getValue() > 12) {
				userCounts.put(t.getKey(), t.getValue());
			}
		}
		result.setActiveNum(userCounts.size());
		HashMap<ActiveUserInfo, Integer> activeUsers = new HashMap<>();
		int sinaAll = 0, sinaAddV = 0;
		HashMap<String, Integer> verifiedInfos = new HashMap<>();
		for (int i = 0; i < VALUES.length; i++) {
			verifiedInfos.put(VALUES[i], 0);
		}
		verifiedInfos.put("others", 0);
		for (Entry<String, Integer> t : userCounts.entrySet()) {
			ActiveUserInfo activeUserInfo = new ActiveUserInfo();
			activeUserInfo.setUsername(t.getKey().substring(t.getKey().lastIndexOf("/") + 1));
			activeUserInfo.setUrl(t.getKey());
			// 通过接口获取用户信息
			if (t.getKey().contains("weibo.com")) {
				sinaAll++;
				BozhuBaseInfo baseInfo = UsersInfoStatistic.getSianUserBaseInfo(Long.parseLong(activeUserInfo
						.getUsername()));
				if (baseInfo != null) {
					logger.info("SinaAdd=" + sinaAddV);
					if (baseInfo.isVerified()) {
						sinaAddV++;
					}
					if (VERIFIED_TYPE.get(baseInfo.getVerified_type()) == null) {
						verifiedInfos.put("others", verifiedInfos.get("others") + 1);
					} else {
						verifiedInfos.put(VERIFIED_TYPE.get(baseInfo.getVerified_type()),
								verifiedInfos.get(VERIFIED_TYPE.get(baseInfo.getVerified_type())) + 1);
					}
					activeUserInfo.setAddV(baseInfo.isVerified());
					activeUserInfo.setVerifiedType(baseInfo.getVerified_type());
				}
			}
			activeUsers.put(activeUserInfo, t.getValue());
		}
		result.setAddVRatio((float) sinaAddV / sinaAll);
		result.setVerifiedInfos(verifiedInfos);
		result.setActiveUsers(activeUsers);
		result.setTopNPictures(sortedToHashMap(picCount, 100));

		return result;
	}

	/**
	 * 将HashMap排序成HashMap
	 */
	public static HashMap<String, Integer> sortedToHashMap(HashMap<String, Integer> temp, int N) {

		List<Map.Entry<String, Integer>> resulttemp = new ArrayList<>(temp.entrySet());
		Collections.sort(resulttemp, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});

		HashMap<String, Integer> sortedtemp = new HashMap<>();
		for (int k = 0; k < Math.min(temp.size(), N); k++) {
			String[] t = resulttemp.get(k).toString().split("=");
			sortedtemp.put(t[0], Integer.parseInt(t[1]));
		}

		return sortedtemp;
	}

	public static String getUserUrl(UsePicInfo usePicInfo) {
		if ("sina".equals(usePicInfo.getType())) {
			return "http://www.weibo.com/u/" + usePicInfo.getBind_username();
		} else if ("tencent".equals(usePicInfo.getType())) {
			return "http://t.qq.com/" + usePicInfo.getBind_username();
		} else if ("163".equals(usePicInfo.getType())) {
			return "http://t.163.com/" + usePicInfo.getBind_username();
		} else {
			return "http://t.sohu.com/" + usePicInfo.getBind_username();
		}
	}
}

