package cc.pp.sina.bozhus.common;

import java.util.HashMap;

import cc.pp.sina.algorithms.top.sort.PPSort;

import com.sina.weibo.model.Status;

public class WeiboPropsAnalysis {

	/**
	 * 转发微博质量
	 */
	public static int analysisQuality(Status reposter) {

		if ((reposter.getText().length() < 7) && (reposter.getText().contains("转发微博"))) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 微博来源分布
	 */
	public static HashMap<String, Integer> analysisTopNWbSources(HashMap<String, Integer> wbsources, int N) {

		String[] sortedWbSources = PPSort.sortedToStrings(wbsources);
		HashMap<String, Integer> toppNWbSources = new HashMap<>();
		for (int i = 0; i < Math.min(N, wbsources.size()); i++) {
			toppNWbSources.put(sortedWbSources[i].substring(0, sortedWbSources[i].indexOf("=")),
					Integer.parseInt(sortedWbSources[i].substring(sortedWbSources[i].indexOf("=") + 1)));
		}

		return toppNWbSources;
	}

}
