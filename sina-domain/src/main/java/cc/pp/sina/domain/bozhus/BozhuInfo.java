package cc.pp.sina.domain.bozhus;

public class BozhuInfo {

	private final int influence;
	private final int active;
	private final int wbnum;
	private final int fannum;
	//	private float exsit_fan_rate;
	//	private float act_fan_rate;
	private final float malerate;
	private final float vrate;
	private final int act_fan;
	private final long fan_fans;
	private final long act_fan_fans;
	private final float wb_avg_daily;
	private final float wb_avg_repost_lastweek;
	private final float wb_avg_repost_lastmonth;
	private final float wb_avg_repost;
	private final float orig_wb_rate;
	private final float orig_wb_avg_repost;
	private final float wb_avg_valid_repost_lastweek;
	private final float wb_avg_valid_repost_lastmonth;
	private final float rt_user_avg_quality;
	private final long avg_valid_fan_cover_last100;

	public BozhuInfo(Builder builder) {
		this.influence = builder.influence;
		this.active = builder.active;
		this.wbnum = builder.wbnum;
		this.fannum = builder.fannum;
		this.malerate = builder.malerate;
		this.vrate = builder.vrate;
		this.act_fan = builder.act_fan;
		this.fan_fans = builder.fan_fans;
		this.act_fan_fans = builder.act_fan_fans;
		this.wb_avg_daily = builder.wb_avg_daily;
		this.wb_avg_repost_lastweek = builder.wb_avg_repost_lastweek;
		this.wb_avg_repost_lastmonth = builder.wb_avg_repost_lastmonth;
		this.wb_avg_repost = builder.wb_avg_repost;
		this.orig_wb_rate = builder.orig_wb_rate;
		this.orig_wb_avg_repost = builder.orig_wb_avg_repost;
		this.wb_avg_valid_repost_lastweek = builder.wb_avg_valid_repost_lastweek;
		this.wb_avg_valid_repost_lastmonth = builder.wb_avg_valid_repost_lastmonth;
		this.rt_user_avg_quality = builder.rt_user_avg_quality;
		this.avg_valid_fan_cover_last100 = builder.avg_valid_fan_cover_last100;
	}

	public static class Builder {

		private int influence;
		private int active;
		private int wbnum;
		private int fannum;
		//	private float exsit_fan_rate;
		//	private float act_fan_rate;
		private float malerate;
		private float vrate;
		private int act_fan;
		private long fan_fans;
		private long act_fan_fans;
		private float wb_avg_daily;
		private float wb_avg_repost_lastweek;
		private float wb_avg_repost_lastmonth;
		private float wb_avg_repost;
		private float orig_wb_rate;
		private float orig_wb_avg_repost;
		private float wb_avg_valid_repost_lastweek;
		private float wb_avg_valid_repost_lastmonth;
		private float rt_user_avg_quality;
		private long avg_valid_fan_cover_last100;

		public Builder setInfluence(int influence) {
			this.influence = influence;
			return this;
		}

		public Builder setActive(int active) {
			this.active = active;
			return this;
		}

		public Builder setWbnum(int wbnum) {
			this.wbnum = wbnum;
			return this;
		}

		public Builder setFannum(int fannum) {
			this.fannum = fannum;
			return this;
		}

		public Builder setMalerate(float malerate) {
			this.malerate = malerate;
			return this;
		}

		public Builder setVrate(float vrate) {
			this.vrate = vrate;
			return this;
		}

		public Builder setAct_fan(int act_fan) {
			this.act_fan = act_fan;
			return this;
		}

		public Builder setFan_fans(long fan_fans) {
			this.fan_fans = fan_fans;
			return this;
		}

		public Builder setAct_fan_fans(long act_fan_fans) {
			this.act_fan_fans = act_fan_fans;
			return this;
		}

		public Builder setWb_avg_daily(float wb_avg_daily) {
			this.wb_avg_daily = wb_avg_daily;
			return this;
		}

		public Builder setWb_avg_repost_lastweek(float wb_avg_repost_lastweek) {
			this.wb_avg_repost_lastweek = wb_avg_repost_lastweek;
			return this;
		}

		public Builder setWb_avg_repost_lastmonth(float wb_avg_repost_lastmonth) {
			this.wb_avg_repost_lastmonth = wb_avg_repost_lastmonth;
			return this;
		}

		public Builder setWb_avg_repost(float wb_avg_repost) {
			this.wb_avg_repost = wb_avg_repost;
			return this;
		}

		public Builder setOrig_wb_rate(float orig_wb_rate) {
			this.orig_wb_rate = orig_wb_rate;
			return this;
		}

		public Builder setOrig_wb_avg_repost(float orig_wb_avg_repost) {
			this.orig_wb_avg_repost = orig_wb_avg_repost;
			return this;
		}

		public Builder setWb_avg_valid_repost_lastweek(float wb_avg_valid_repost_lastweek) {
			this.wb_avg_valid_repost_lastweek = wb_avg_valid_repost_lastweek;
			return this;
		}

		public Builder setWb_avg_valid_repost_lastmonth(float wb_avg_valid_repost_lastmonth) {
			this.wb_avg_valid_repost_lastmonth = wb_avg_valid_repost_lastmonth;
			return this;
		}

		public Builder setRt_user_avg_quality(float rt_user_avg_quality) {
			this.rt_user_avg_quality = rt_user_avg_quality;
			return this;
		}

		public Builder setAvg_valid_fan_cover_last100(long avg_valid_fan_cover_last100) {
			this.avg_valid_fan_cover_last100 = avg_valid_fan_cover_last100;
			return this;
		}

		public BozhuInfo build() {
			return new BozhuInfo(this);
		}
	}

	public int getInfluence() {
		return influence;
	}

	public int getActive() {
		return active;
	}

	public int getWbnum() {
		return wbnum;
	}

	public int getFannum() {
		return fannum;
	}

	public float getMalerate() {
		return malerate;
	}

	public float getVrate() {
		return vrate;
	}

	public int getAct_fan() {
		return act_fan;
	}

	public long getFan_fans() {
		return fan_fans;
	}

	public long getAct_fan_fans() {
		return act_fan_fans;
	}

	public float getWb_avg_daily() {
		return wb_avg_daily;
	}

	public float getWb_avg_repost_lastweek() {
		return wb_avg_repost_lastweek;
	}

	public float getWb_avg_repost_lastmonth() {
		return wb_avg_repost_lastmonth;
	}

	public float getWb_avg_repost() {
		return wb_avg_repost;
	}

	public float getOrig_wb_rate() {
		return orig_wb_rate;
	}

	public float getOrig_wb_avg_repost() {
		return orig_wb_avg_repost;
	}

	public float getWb_avg_valid_repost_lastweek() {
		return wb_avg_valid_repost_lastweek;
	}

	public float getWb_avg_valid_repost_lastmonth() {
		return wb_avg_valid_repost_lastmonth;
	}

	public float getRt_user_avg_quality() {
		return rt_user_avg_quality;
	}

	public long getAvg_valid_fan_cover_last100() {
		return avg_valid_fan_cover_last100;
	}

}
