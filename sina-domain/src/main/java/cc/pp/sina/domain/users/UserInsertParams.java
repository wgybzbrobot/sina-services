package cc.pp.sina.domain.users;


public class UserInsertParams {

	private final String tablename;
	private final long id; //1
	private final String screen_name;//2
	private final String name;
	private final int province;//3
	private final int city;//4
	private final String location;//5
	private final String description;//6
	private final String url;//7
	private final String profile_image_url;//8
	private final String domain;//9
	private final String gender;//10
	private final int followers_count;//11
	private final int friends_count;//12
	private final int statuses_count;//13
	private final int favourites_count;//14
	private final Long created_at;//15
	private final boolean verified;//16
	private final int verified_type;//17
	private final String avatar_large;//18
	private final int bi_followers_count;//19
	private final String remark;//20
	private final String verified_reason;//21
	private final String weihao;//22
	private final long lasttime;

	public UserInsertParams(Builder builder) {
		this.tablename = builder.tablename;
		this.id = builder.id;
		this.screen_name = builder.screen_name;
		this.name = builder.name;
		this.province = builder.province;
		this.city = builder.city;
		this.location = builder.location;
		this.description = builder.description;
		this.url = builder.url;
		this.profile_image_url = builder.profile_image_url;
		this.domain = builder.domain;
		this.gender = builder.gender;
		this.followers_count = builder.followers_count;
		this.friends_count = builder.friends_count;
		this.statuses_count = builder.statuses_count;
		this.favourites_count = builder.favourites_count;
		this.created_at = builder.created_at;
		this.verified = builder.verified;
		this.verified_type = builder.verified_type;
		this.avatar_large = builder.avatar_large;
		this.bi_followers_count = builder.bi_followers_count;
		this.remark = builder.remark;
		this.verified_reason = builder.verified_reason;
		this.weihao = builder.weihao;
		this.lasttime = builder.lasttime;
	}

	public static class Builder {

		private final String tablename;
		private final long id;
		private String screen_name = "";
		private String name = "";
		private int province;
		private int city;
		private String location = "";
		private String description = "";
		private String url = "";
		private String profile_image_url = "";
		private String domain = "";
		private String gender = "";
		private int followers_count;
		private int friends_count;
		private int statuses_count;
		private int favourites_count;
		private Long created_at;
		private boolean verified;
		private int verified_type;
		private String avatar_large = "";
		private int bi_followers_count;
		private String remark = "";
		private String verified_reason = "";
		private String weihao = "";
		private long lasttime;

		public Builder(String tablename, long id) {
			this.tablename = tablename;
			this.id = id;
		}

		public Builder setScreen_name(String screen_name) {
			this.screen_name = screen_name;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setProvince(int province) {
			this.province = province;
			return this;
		}

		public Builder setCity(int city) {
			this.city = city;
			return this;
		}

		public Builder setLocation(String location) {
			this.location = location;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setUrl(String url) {
			this.url = url;
			return this;
		}

		public Builder setProfile_image_url(String profile_image_url) {
			this.profile_image_url = profile_image_url;
			return this;
		}

		public Builder setDomain(String domain) {
			this.domain = domain;
			return this;
		}

		public Builder setGender(String gender) {
			this.gender = gender;
			return this;
		}

		public Builder setFollowers_count(int followers_count) {
			this.followers_count = followers_count;
			return this;
		}

		public Builder setFriends_count(int friends_count) {
			this.friends_count = friends_count;
			return this;
		}

		public Builder setStatuses_count(int statuses_count) {
			this.statuses_count = statuses_count;
			return this;
		}

		public Builder setFavourites_count(int favourites_count) {
			this.favourites_count = favourites_count;
			return this;
		}

		public Builder setCreated_at(Long created_at) {
			this.created_at = created_at;
			return this;
		}

		public Builder setVerified(boolean verified) {
			this.verified = verified;
			return this;
		}

		public Builder setVerified_type(int verified_type) {
			this.verified_type = verified_type;
			return this;
		}

		public Builder setAvatar_large(String avatar_large) {
			this.avatar_large = avatar_large;
			return this;
		}

		public Builder setBi_followers_count(int bi_followers_count) {
			this.bi_followers_count = bi_followers_count;
			return this;
		}

		public Builder setRemark(String remark) {
			this.remark = remark;
			return this;
		}

		public Builder setVerified_reason(String verified_reason) {
			this.verified_reason = verified_reason;
			return this;
		}

		public Builder setWeihao(String weihao) {
			this.weihao = weihao;
			return this;
		}

		public Builder setLasttime(long lasttime) {
			this.lasttime = lasttime;
			return this;
		}

		public UserInsertParams build() {
			return new UserInsertParams(this);
		}

	}

	public String getTablename() {
		return tablename;
	}

	public long getId() {
		return id;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public String getName() {
		return name;
	}

	public int getProvince() {
		return province;
	}

	public int getCity() {
		return city;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public String getDomain() {
		return domain;
	}

	public String getGender() {
		return gender;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public Long getCreated_at() {
		return created_at;
	}

	public boolean isVerified() {
		return verified;
	}

	public int getVerified_type() {
		return verified_type;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public int getBi_followers_count() {
		return bi_followers_count;
	}

	public String getRemark() {
		return remark;
	}

	public String getVerified_reason() {
		return verified_reason;
	}

	public String getWeihao() {
		return weihao;
	}

	public long getLasttime() {
		return lasttime;
	}

}
