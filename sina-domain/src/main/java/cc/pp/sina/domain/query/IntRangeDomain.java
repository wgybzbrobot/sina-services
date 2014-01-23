package cc.pp.sina.domain.query;

public class IntRangeDomain {

	private final String fieldName;
	private final int low;
	private final int high;

	public IntRangeDomain(Builder builder) {
		this.fieldName = builder.fieldName;
		this.low = builder.low;
		this.high = builder.high;
	}

	public static class Builder {

		private final String fieldName;
		private final int low;
		private final int high;

		public Builder(String fieldName, int low, int high) {
			this.fieldName = fieldName;
			this.low = low;
			this.high = high;
		}

		public IntRangeDomain build() {
			return new IntRangeDomain(this);
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

}
