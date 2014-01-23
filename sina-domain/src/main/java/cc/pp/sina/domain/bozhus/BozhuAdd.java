package cc.pp.sina.domain.bozhus;

public class BozhuAdd {

	private final long username;
	private final String ptype;
	private final String nickname;
	private final String description;
	private final int verify;
	private final int isppuser;

	public BozhuAdd(Builder builder) {
		username = builder.username;
		ptype = builder.ptype;
		nickname = builder.nickname;
		description = builder.description;
		verify = builder.verify;
		isppuser = builder.isppuser;
	}

	public static class Builder {

		private long username;
		private String ptype;
		private String nickname;
		private String description;
		private int verify;
		private int isppuser;

		public Builder() {
			//
		}

		public Builder setUsername(long username) {
			this.username = username;
			return this;
		}

		public Builder setPtype(String ptype) {
			this.ptype = ptype;
			return this;
		}

		public Builder setNickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setVerify(int verify) {
			this.verify = verify;
			return this;
		}

		public Builder setIsppuser(int isppuser) {
			this.isppuser = isppuser;
			return this;
		}

		public BozhuAdd build() {
			return new BozhuAdd(this);
		}
	}

	public long getUsername() {
		return username;
	}

	public String getPtype() {
		return ptype;
	}

	public String getNickname() {
		return nickname;
	}

	public String getDescription() {
		return description;
	}

	public int getVerify() {
		return verify;
	}

	public int getIsppuser() {
		return isppuser;
	}

}
