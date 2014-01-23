package cc.pp.sina.domain.mapred;

public class SimpleBozhuInfoOne {

	private final float exsit_fan_rate;
	private final float act_fan_rate;

	public SimpleBozhuInfoOne(Builder builder) {
		exsit_fan_rate = builder.exsit_fan_rate;
		act_fan_rate = builder.act_fan_rate;
	}

	public static class Builder {

		private float exsit_fan_rate;
		private float act_fan_rate;

		public Builder() {
			//
		}

		public Builder setExistFanRate(float exsit_fan_rate) {
			this.exsit_fan_rate = exsit_fan_rate;
			return this;
		}

		public Builder setActFanRate(float act_fan_rate) {
			this.act_fan_rate = act_fan_rate;
			return this;
		}

		public SimpleBozhuInfoOne build() {
			return new SimpleBozhuInfoOne(this);
		}
	}

	public float getExsit_fan_rate() {
		return exsit_fan_rate;
	}

	public float getAct_fan_rate() {
		return act_fan_rate;
	}
}
