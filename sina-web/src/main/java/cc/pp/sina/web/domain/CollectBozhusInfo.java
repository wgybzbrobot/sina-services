package cc.pp.sina.web.domain;

import java.util.HashMap;

public class CollectBozhusInfo {

	private final HashMap<String, String> gender; // 粉丝男性比例
	private final HashMap<String, String> ageClass; // 粉丝年龄层次
	private final HashMap<String, String> province; // 粉丝区域前5分布
	private final long allFans; // 总粉丝覆盖量

	public CollectBozhusInfo(Builder builder) {
		this.gender = builder.gender;
		this.ageClass = builder.ageClass;
		this.province = builder.province;
		this.allFans = builder.allFans;
	}

	public static class Builder {

		private HashMap<String, String> gender; // 粉丝男性比例
		private HashMap<String, String> ageClass; // 粉丝年龄层次
		private HashMap<String, String> province; // 粉丝区域前5分布
		private long allFans; // 总粉丝覆盖量

		public Builder() {
			//
		}

		public Builder setGender(HashMap<String, String> gender) {
			this.gender = gender;
			return this;
		}

		public Builder setAgeClass(HashMap<String, String> ageClass) {
			this.ageClass = ageClass;
			return this;
		}

		public Builder setProvince(HashMap<String, String> province) {
			this.province = province;
			return this;
		}

		public Builder setAllFans(long allFans) {
			this.allFans = allFans;
			return this;
		}

		public CollectBozhusInfo build() {
			return new CollectBozhusInfo(this);
		}

	}

	public HashMap<String, String> getGender() {
		return gender;
	}

	public HashMap<String, String> getAgeClass() {
		return ageClass;
	}

	public HashMap<String, String> getProvince() {
		return province;
	}

	public long getAllFans() {
		return allFans;
	}

}
