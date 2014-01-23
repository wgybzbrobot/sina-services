package cc.pp.sina.domain.bozhus;

public class BozhuPrice {

	private final String source;
	private final String type;
	private final String price;

	public BozhuPrice(Builder builder) {
		this.source = builder.source;
		this.type = builder.type;
		this.price = builder.price;
	}

	public static class Builder {

		private String source;
		private String type;
		private String price;

		public Builder() {
			//
		}

		public Builder setSource(String source) {
			this.source = source;
			return this;
		}

		public Builder setType(String type) {
			this.type = type;
			return this;
		}

		public Builder setPrice(String price) {
			this.price = price;
			return this;
		}

		public BozhuPrice build() {
			return new BozhuPrice(this);
		}
	}

	public String getSource() {
		return source;
	}

	public String getType() {
		return type;
	}

	public String getPrice() {
		return price;
	}

}
