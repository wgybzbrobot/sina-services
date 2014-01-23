package cc.pp.sina.domain.bozhus;

public class PPUserFansInfo {

	private final long username;
	private final String fansuids;
	private final int fanscount;

	public PPUserFansInfo(Builder builder) {
		this.username = builder.username;
		this.fansuids = builder.fansuids;
		this.fanscount = builder.fanscount;
	}

	public static class Builder {

		private final long username;
		private final String fansuids;
		private final int fanscount;

		public Builder(long username, String fansuids, int fanscount) {
			this.username = username;
			this.fansuids = fansuids;
			this.fanscount = fanscount;
		}

		public PPUserFansInfo build() {
			return new PPUserFansInfo(this);
		}
	}

	public long getUsername() {
		return username;
	}

	public String getFansuids() {
		return fansuids;
	}

	public int getFanscount() {
		return fanscount;
	}

}
