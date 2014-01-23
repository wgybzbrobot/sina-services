package cc.pp.sina.domain.query;

import java.util.ArrayList;
import java.util.List;

public class QueryResult {

	private final List<String> data;
	private final int totalNumber;

	public QueryResult(Builder builder) {
		this.data = builder.data;
		this.totalNumber = builder.totalNumber;
	}

	public static class Builder {

		private List<String> data = new ArrayList<>();
		private int totalNumber;

		public Builder() {
			//
		}

		public Builder setData(List<String> data) {
			this.data = data;
			return this;
		}

		public Builder setTotalNumber(int totalNumber) {
			this.totalNumber = totalNumber;
			return this;
		}

		public QueryResult build() {
			return new QueryResult(this);
		}

	}

	public List<String> getData() {
		return data;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

}
