package cc.pp.sina.domain.mapred;

public class SimpleBozhuInfoTwo {

	private final int wbnum;
	private final int fannum;
	private final int act_fan;
	private final float exsit_fan_rate;
	private final float act_fan_rate;

	public SimpleBozhuInfoTwo(Builder builder) {
		wbnum = builder.wbnum;
		fannum = builder.fannum;
		act_fan = builder.act_fan;
		exsit_fan_rate = builder.exsit_fan_rate;
		act_fan_rate = builder.act_fan_rate;
	}

	public static class Builder {

		private int wbnum;
		private int fannum;
		private int act_fan;
		private float exsit_fan_rate;
		private float act_fan_rate;

		public Builder setWbnum(int wbnum) {
			this.wbnum = wbnum;
			return this;
		}

		public Builder setFannum(int fannum) {
			this.fannum = fannum;
			return this;
		}

		public Builder setActfan(int act_fan) {
			this.act_fan = act_fan;
			return this;
		}

		public Builder setExsitFanRate(float exsit_fan_rate) {
			this.exsit_fan_rate = exsit_fan_rate;
			return this;
		}

		public Builder setActFanRate(float act_fan_rate) {
			this.act_fan_rate = act_fan_rate;
			return this;
		}

		public SimpleBozhuInfoTwo build() {
			return new SimpleBozhuInfoTwo(this);
		}

	}

	public int getWbnum() {
		return wbnum;
	}

	public int getFannum() {
		return fannum;
	}

	public int getAct_fan() {
		return act_fan;
	}

	public float getExsit_fan_rate() {
		return exsit_fan_rate;
	}

	public float getAct_fan_rate() {
		return act_fan_rate;
	}

}
