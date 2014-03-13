package cc.pp.sina.dao.demo;

import java.util.List;

import cc.pp.sina.dao.common.MybatisConfig;
import cc.pp.sina.dao.t2.T2Interactions;
import cc.pp.sina.domain.t2.T2SinaInteractionsInfo;
import cc.pp.sina.domain.t2.T2TencentInteractionsInfo;
import cc.pp.sina.utils.json.JsonUtils;

public class T2InteractionsDemo {

	public static void main(String[] args) {

		T2Interactions interactions = new T2Interactions(MybatisConfig.ServerEnum.local);
		long sinaUid = 123456789L;
		String tencentUid = "wgybzb";
		int date = 20140220;
		int allcount = 100;
		String emotionratio = "test";

		/**
		 * 新浪
		 */
		for (int i = 0; i < 10; i++) {
			interactions.insertT2SinaInteractions(sinaUid, date + i, allcount + i * 2, allcount + i, emotionratio);
		}
		T2SinaInteractionsInfo result1 = interactions.selectT2SinaInteractions(sinaUid, date);
		System.out.println(JsonUtils.toJson(result1));
		List<T2SinaInteractionsInfo> resultList1 = interactions.selectT2SinaInteractionsList(sinaUid, 5);
		System.out.println(JsonUtils.toJson(resultList1));
		for (int i = 0; i < 10; i++) {
			interactions.deleteT2SinaInteractions(sinaUid, date + i);
		}

		/**
		 * 腾讯
		 */
		for (int i = 0; i < 10; i++) {
			interactions
					.insertT2TencentInteractions(tencentUid, date + i, allcount + i * 2, allcount + i, emotionratio);
		}
		T2TencentInteractionsInfo result2 = interactions.selectT2TencentInteractions(tencentUid, date);
		System.out.println(JsonUtils.toJson(result2));
		List<T2TencentInteractionsInfo> resultList2 = interactions.selectT2TencentInteractionsList(tencentUid, 5);
		System.out.println(JsonUtils.toJson(resultList2));
		for (int i = 0; i < 10; i++) {
			interactions.deleteT2TencentInteractions(tencentUid, date + i);
		}

	}

}
