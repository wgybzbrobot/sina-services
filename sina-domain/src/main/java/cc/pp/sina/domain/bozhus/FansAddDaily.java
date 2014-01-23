package cc.pp.sina.domain.bozhus;

public class FansAddDaily {

	private final String username;
	private final String addfansuids;
	private final int addfanscount;
	private final int allfanscount;

	public FansAddDaily(Builder builder) {
		this.username = builder.username;
		this.addfansuids = builder.addfansuids;
		this.addfanscount = builder.addfanscount;
		this.allfanscount = builder.allfanscount;
	}

	public static class Builder {

		private final String username;
		private String addfansuids;
		private int addfanscount;
		private int allfanscount;

		public Builder(String username) {
			this.username = username;
		}

		public Builder setAddfanscount(int addfanscount) {
			this.addfanscount = addfanscount;
			return this;
		}

		public Builder setAddfansuids(String addfansuids) {
			this.addfansuids = addfansuids;
			return this;
		}

		public Builder setAllfanscount(int allfanscount) {
			this.allfanscount = allfanscount;
			return this;
		}

		public FansAddDaily build() {
			return new FansAddDaily(this);
		}

	}

	public String getUsername() {
		return username;
	}

	public String getAddfansuids() {
		return addfansuids;
	}

	public int getAddfanscount() {
		return addfanscount;
	}

	public int getAllfanscount() {
		return allfanscount;
	}

}
