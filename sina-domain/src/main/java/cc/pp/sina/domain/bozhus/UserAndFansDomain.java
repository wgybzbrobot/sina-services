package cc.pp.sina.domain.bozhus;

import java.util.HashMap;

/**
 * 用户及其粉丝特性数据
 * @author wgybzb
 *
 */
public class UserAndFansDomain {

	private final String username;
	private final String nickname;
	private final String description;
	private final int fanscount;
	private final int weibocount;
	private final int verify;
	private final float averagewbs;
	private final int influence;
	private final int activation;
	private final int activecount;
	private final float addvratio;
	private final float activeratio;
	private final float maleratio;
	private final HashMap<String, String> top5provinces;
	private final int createdtime;
	private final float fansexistedratio;
	private final long allfanscount;
	private final long allactivefanscount;

	public UserAndFansDomain(Builder builder) {
		this.username = builder.username;
		this.nickname = builder.nickname;
		this.description = builder.description;
		this.fanscount = builder.fanscount;
		this.weibocount = builder.weibocount;
		this.verify = builder.verify;
		this.averagewbs = builder.averagewbs;
		this.influence = builder.influence;
		this.activation = builder.activation;
		this.activecount = builder.activecount;
		this.addvratio = builder.addvratio;
		this.activeratio = builder.activeratio;
		this.maleratio = builder.maleratio;
		this.top5provinces = builder.top5provinces;
		this.createdtime = builder.createdtime;
		this.fansexistedratio = builder.fansexistedratio;
		this.allfanscount = builder.allfanscount;
		this.allactivefanscount = builder.allactivefanscount;
	}

	public static class Builder {

		private final String username;
		private String nickname = "";
		private String description = "";
		private int fanscount;
		private int weibocount;
		private int verify;
		private float averagewbs;
		private int influence;
		private int activation;
		private int activecount;
		private float addvratio;
		private float activeratio;
		private float maleratio;
		private HashMap<String, String> top5provinces = null;
		private int createdtime;
		private float fansexistedratio;
		private long allfanscount;
		private long allactivefanscount;

		public Builder(String username) {
			this.username = username;
		}

		public Builder setNickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setFanscount(int fanscount) {
			this.fanscount = fanscount;
			return this;
		}

		public Builder setWeibocount(int weibocount) {
			this.weibocount = weibocount;
			return this;
		}

		public Builder setVerify(int verify) {
			this.verify = verify;
			return this;
		}

		public Builder setAveragewbs(float averagewbs) {
			this.averagewbs = averagewbs;
			return this;
		}

		public Builder setInfluence(int influence) {
			this.influence = influence;
			return this;
		}

		public Builder setActivation(int activation) {
			this.activation = activation;
			return this;
		}

		public Builder setActivecount(int activecount) {
			this.activecount = activecount;
			return this;
		}

		public Builder setAddvratio(float addvratio) {
			this.addvratio = addvratio;
			return this;
		}

		public Builder setActiveratio(float activeratio) {
			this.activeratio = activeratio;
			return this;
		}

		public Builder setMaleratio(float maleratio) {
			this.maleratio = maleratio;
			return this;
		}

		public Builder setTop5provinces(HashMap<String, String> top5provinces) {
			this.top5provinces = top5provinces;
			return this;
		}

		public Builder setCreatedtime(int createdtime) {
			this.createdtime = createdtime;
			return this;
		}

		public Builder setFansexistedratio(float fansexistedratio) {
			this.fansexistedratio = fansexistedratio;
			return this;
		}

		public Builder setAllfanscount(long allfanscount) {
			this.allfanscount = allfanscount;
			return this;
		}

		public Builder setAllactivefanscount(long allactivefanscount) {
			this.allactivefanscount = allactivefanscount;
			return this;
		}

		public UserAndFansDomain build() {
			return new UserAndFansDomain(this);
		}
	}

	public String getUsername() {
		return username;
	}

	public String getNickname() {
		return nickname;
	}

	public String getDescription() {
		return description;
	}

	public int getFanscount() {
		return fanscount;
	}

	public int getWeibocount() {
		return weibocount;
	}

	public int getVerify() {
		return verify;
	}

	public float getAveragewbs() {
		return averagewbs;
	}

	public int getInfluence() {
		return influence;
	}

	public int getActivation() {
		return activation;
	}

	public int getActivecount() {
		return activecount;
	}

	public float getAddvratio() {
		return addvratio;
	}

	public float getActiveratio() {
		return activeratio;
	}

	public float getMaleratio() {
		return maleratio;
	}

	public HashMap<String, String> getTop5provinces() {
		return top5provinces;
	}

	public int getCreatedtime() {
		return createdtime;
	}

	public float getFansexistedratio() {
		return fansexistedratio;
	}

	public long getAllfanscount() {
		return allfanscount;
	}

	public long getAllactivefanscount() {
		return allactivefanscount;
	}

}