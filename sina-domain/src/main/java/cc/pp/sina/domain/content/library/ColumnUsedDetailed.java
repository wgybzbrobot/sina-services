package cc.pp.sina.domain.content.library;

import java.util.ArrayList;
import java.util.List;

import cc.pp.sina.domain.bozhus.BozhuBaseInfo;

public class ColumnUsedDetailed {

	private int countentCount; // 内容数
	private int userCount; // 用户数
	private long allCoveringNum; //粉丝覆盖量
	private List<BozhuBaseInfo> topNUserBaseInfos = new ArrayList<>();// 使用次数前50的用户信息

	public int getCountentCount() {
		return countentCount;
	}

	public void setCountentCount(int countentCount) {
		this.countentCount = countentCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public long getAllCoveringNum() {
		return allCoveringNum;
	}
	public void setAllCoveringNum(long allCoveringNum) {
		this.allCoveringNum = allCoveringNum;
	}
	public List<BozhuBaseInfo> getTopNUserBaseInfos() {
		return topNUserBaseInfos;
	}
	public void setTopNUserBaseInfos(List<BozhuBaseInfo> topNUserBaseInfos) {
		this.topNUserBaseInfos = topNUserBaseInfos;
	}


}
