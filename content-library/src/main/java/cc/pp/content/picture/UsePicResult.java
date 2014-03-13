package cc.pp.content.picture;

import java.util.HashMap;

public class UsePicResult {

	private int activeNum;
	private HashMap<ActiveUserInfo, Integer> activeUsers;
	private HashMap<String, Integer> topNPictures;
	private float addVRatio;
	private HashMap<String, Integer> verifiedInfos;

	public int getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(int activeNum) {
		this.activeNum = activeNum;
	}

	public HashMap<ActiveUserInfo, Integer> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(HashMap<ActiveUserInfo, Integer> activeUsers) {
		this.activeUsers = activeUsers;
	}

	public HashMap<String, Integer> getTopNPictures() {
		return topNPictures;
	}

	public void setTopNPictures(HashMap<String, Integer> topNPictures) {
		this.topNPictures = topNPictures;
	}

	public float getAddVRatio() {
		return addVRatio;
	}

	public void setAddVRatio(float addVRatio) {
		this.addVRatio = addVRatio;
	}

	public HashMap<String, Integer> getVerifiedInfos() {
		return verifiedInfos;
	}

	public void setVerifiedInfos(HashMap<String, Integer> verifiedInfos) {
		this.verifiedInfos = verifiedInfos;
	}

}
