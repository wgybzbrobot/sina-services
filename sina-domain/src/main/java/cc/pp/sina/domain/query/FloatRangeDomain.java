package cc.pp.sina.domain.query;

public class FloatRangeDomain {

	private final String fieldName;
	private final float low;
	private final float high;

	public FloatRangeDomain(Builder builder) {
		this.fieldName = builder.fieldName;
		this.low = builder.low;
		this.high = builder.high;
	}

	public static class Builder {

		private final String fieldName;
		private final float low;
		private final float high;

		public Builder(String fieldName, float low, float high) {
			this.fieldName = fieldName;
			this.low = low;
			this.high = high;
		}

		public FloatRangeDomain build() {
			return new FloatRangeDomain(this);
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public float getLow() {
		return low;
	}

	public float getHigh() {
		return high;
	}

}
