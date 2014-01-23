package cc.pp.sina.web.domain;

import java.util.ArrayList;
import java.util.List;

import cc.pp.sina.domain.bozhus.UserAllParamsDomain;

public class BozhusResult {

	private final List<UserAllParamsDomain> data;
	private final int totalNumber;

	public BozhusResult(Builder builder) {
		this.data = builder.data;
		this.totalNumber = builder.totalNumber;
	}

	public static class Builder {

		private List<UserAllParamsDomain> data = new ArrayList<UserAllParamsDomain>();
		private int totalNumber;

		public Builder() {
			//
		}

		public Builder setData(List<UserAllParamsDomain> data) {
			this.data = data;
			return this;
		}

		public Builder setTotalNumber(int totalNumber) {
			this.totalNumber = totalNumber;
			return this;
		}

		public BozhusResult build() {
			return new BozhusResult(this);
		}

	}

	public List<UserAllParamsDomain> getData() {
		return data;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

}
