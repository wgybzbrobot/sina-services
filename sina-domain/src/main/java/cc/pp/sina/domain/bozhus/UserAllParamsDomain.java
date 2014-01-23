package cc.pp.sina.domain.bozhus;

/**
 * 博主用户所有可能的特性数据
 * @author wgybzb
 *
 */
public class UserAllParamsDomain {

	private final String username; // 1
	private final String nickname; // 2
	private final String description; // 3
	private final int fanscount; // 4
	private final int weibocount; // 5
	private final int verify; // 6
	private final float averagewbs; // 7
	private final int influence; // 8
	private final int activation; // 9
	private final int activecount; // 10
	private final float addvratio; // 11
	private final float activeratio; // 12
	private final float maleratio; // 13
	private final String top5provinces; // 14
	private final int createdtime; // 15
	private final float fansexistedratio; // 16
	private final long allfanscount; // 17
	private final long allactivefanscount; // 18
	private final float oriratio; // 19
	private final float aveorirepcom; // 20
	private final float averepcom; // 21
	private final String wbsource; // 22
	private final float averepcombyweek; // 23
	private final float averepcombymonth; // 24
	private final float avereposterquality; // 25
	private final long aveexposionsum; // 26
	private final String usertags; // 27
	private final float validrepcombyweek; // 28
	private final float validrepcombymonth; // 29
	private final int pricesource; // 30、价格来源
	private final float softretweet; // 31-软广转发
	private final float softtweet; // 32-软广直发
	private final float hardretweet; // 33-硬广转发
	private final float hardtweet; // 34-硬广直发
	private final String fansage; // 35、粉丝年龄
	private final String fanstags; // 36、粉丝标签
	private final String identitytype; // 37、身份类型
	private final String industrytype; // 38、行业类型

	public UserAllParamsDomain(Builder builder) {
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
		this.oriratio = builder.oriratio;
		this.aveorirepcom = builder.aveorirepcom;
		this.averepcom = builder.averepcom;
		this.wbsource = builder.wbsource;
		this.averepcombyweek = builder.averepcombyweek;
		this.averepcombymonth = builder.averepcombymonth;
		this.avereposterquality = builder.avereposterquality;
		this.aveexposionsum = builder.aveexposionsum;
		this.usertags = builder.usertags;
		this.validrepcombyweek = builder.validrepcombyweek;
		this.validrepcombymonth = builder.validrepcombymonth;
		this.pricesource = builder.pricesource;
		this.softtweet = builder.softtweet;
		this.softretweet = builder.softretweet;
		this.hardtweet = builder.hardtweet;
		this.hardretweet = builder.hardretweet;
		this.fansage = builder.fansage;
		this.fanstags = builder.fanstags;
		this.identitytype = builder.identitytype;
		this.industrytype = builder.industrytype;
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
		private String top5provinces = "";
		private int createdtime;
		private float fansexistedratio;
		private long allfanscount;
		private long allactivefanscount;
		private float oriratio;
		private float aveorirepcom;
		private float averepcom;
		private String wbsource = "";
		private float averepcombyweek;
		private float averepcombymonth;
		private float avereposterquality;
		private long aveexposionsum;
		private String usertags = "";
		private float validrepcombyweek;
		private float validrepcombymonth;
		private int pricesource;
		private float softretweet;
		private float softtweet;
		private float hardretweet;
		private float hardtweet;
		private String fansage = "";
		private String fanstags = "";
		private String identitytype = "";
		private String industrytype = "";

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

		public Builder setTop5provinces(String top5provinces) {
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

		public Builder setOriratio(float oriratio) {
			this.oriratio = oriratio;
			return this;
		}

		public Builder setAveorirepcom(float aveorirepcom) {
			this.aveorirepcom = aveorirepcom;
			return this;
		}

		public Builder setAverepcom(float averepcom) {
			this.averepcom = averepcom;
			return this;
		}

		public Builder setWbsource(String wbsource) {
			this.wbsource = wbsource;
			return this;
		}

		public Builder setAverepcombyweek(float averepcombyweek) {
			this.averepcombyweek = averepcombyweek;
			return this;
		}

		public Builder setAverepcombymonth(float averepcombymonth) {
			this.averepcombymonth = averepcombymonth;
			return this;
		}

		public Builder setAvereposterquality(float avereposterquality) {
			this.avereposterquality = avereposterquality;
			return this;
		}

		public Builder setAveexposionsum(long aveexposionsum) {
			this.aveexposionsum = aveexposionsum;
			return this;
		}

		public Builder setUsertags(String usertags) {
			this.usertags = usertags;
			return this;
		}

		public Builder setValidrepcombyweek(float validrepcombyweek) {
			this.validrepcombyweek = validrepcombyweek;
			return this;
		}

		public Builder setValidrepcombymonth(float validrepcombymonth) {
			this.validrepcombymonth = validrepcombymonth;
			return this;
		}

		public Builder setPricesource(int pricesource) {
			this.pricesource = pricesource;
			return this;
		}

		public Builder setSoftretweet(float softretweet) {
			this.softretweet = softretweet;
			return this;
		}

		public Builder setSofttweet(float softtweet) {
			this.softtweet = softtweet;
			return this;
		}

		public Builder setHardretweet(float hardretweet) {
			this.hardretweet = hardretweet;
			return this;
		}

		public Builder setHardtweet(float hardtweet) {
			this.hardtweet = hardtweet;
			return this;
		}

		public Builder setFansage(String fansage) {
			this.fansage = fansage;
			return this;
		}

		public Builder setFanstags(String fanstags) {
			this.fanstags = fanstags;
			return this;
		}

		public Builder setIdentitytype(String identitytype) {
			this.identitytype = identitytype;
			return this;
		}

		public Builder setIndustrytype(String industrytype) {
			this.industrytype = industrytype;
			return this;
		}

		public UserAllParamsDomain build() {
			return new UserAllParamsDomain(this);
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

	public String getTop5provinces() {
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

	public float getOriratio() {
		return oriratio;
	}

	public float getAveorirepcom() {
		return aveorirepcom;
	}

	public float getAverepcom() {
		return averepcom;
	}

	public String getWbsource() {
		return wbsource;
	}

	public float getAverepcombyweek() {
		return averepcombyweek;
	}

	public float getAverepcombymonth() {
		return averepcombymonth;
	}

	public float getAvereposterquality() {
		return avereposterquality;
	}

	public long getAveexposionsum() {
		return aveexposionsum;
	}

	public String getUsertags() {
		return usertags;
	}

	public float getValidrepcombyweek() {
		return validrepcombyweek;
	}

	public float getValidrepcombymonth() {
		return validrepcombymonth;
	}

	public String getFansage() {
		return fansage;
	}

	public String getFanstags() {
		return fanstags;
	}

	public String getIdentitytype() {
		return identitytype;
	}

	public String getIndustrytype() {
		return industrytype;
	}

	public int getPricesource() {
		return pricesource;
	}

	public float getSoftretweet() {
		return softretweet;
	}

	public float getSofttweet() {
		return softtweet;
	}

	public float getHardretweet() {
		return hardretweet;
	}

	public float getHardtweet() {
		return hardtweet;
	}

}
