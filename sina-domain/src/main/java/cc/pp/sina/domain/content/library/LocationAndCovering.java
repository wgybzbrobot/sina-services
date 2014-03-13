package cc.pp.sina.domain.content.library;

import java.util.HashMap;

/**
 * 用户的区域和粉丝覆盖量
 * @author wgybzb
 *
 */
public class LocationAndCovering {

	private int[] province = new int[101]; // 区域分析
	private HashMap<Long, Long> coverings = new HashMap<>(); //粉丝覆盖量

	public int[] getProvince() {
		return province;
	}

	public void setProvince(int[] province) {
		this.province = province;
	}

	public HashMap<Long, Long> getCoverings() {
		return coverings;
	}

	public void setCoverings(HashMap<Long, Long> coverings) {
		this.coverings = coverings;
	}

}
