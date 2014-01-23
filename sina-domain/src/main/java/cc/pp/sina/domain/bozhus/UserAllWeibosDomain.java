package cc.pp.sina.domain.bozhus;

import java.util.HashMap;
import java.util.List;

/**
 * 用户所有微博特性数据
 * @author wgybzb
 *
 */
public class UserAllWeibosDomain {

	private final long weibosum;
	private final float oriratio;
	private final float aveorirepcom;
	private final float averepcom;
	private final HashMap<String, Integer> wbsource;
	private final float averepcombyweek;
	private final float averepcombymonth;
	private final List<String> lastweiboids;

	public UserAllWeibosDomain(Builder builder) {
		this.weibosum = builder.weibosum;
		this.oriratio = builder.oriratio;
		this.aveorirepcom = builder.aveorirepcom;
		this.averepcom = builder.averepcom;
		this.wbsource = builder.wbsource;
		this.averepcombyweek = builder.averepcombyweek;
		this.averepcombymonth = builder.averepcombymonth;
		this.lastweiboids = builder.lastweiboids;
	}

	public static class Builder {

		private long weibosum;
		private float oriratio;
		private float aveorirepcom;
		private float averepcom;
		private HashMap<String, Integer> wbsource = null;
		private float averepcombyweek;
		private float averepcombymonth;
		private List<String> lastweiboids = null;

		public Builder() {
			//
		}

		public Builder setWeibosum(long weibosum) {
			this.weibosum = weibosum;
			return this;
		}

		public Builder setOriratio(float oriratio) {
			this.oriratio = oriratio;
			return this;
		}

		public Builder setAveorirepcom(float aveorirepcom) {
			this.aveorirepcom = aveorirepcom;
			return this;
		}

		public Builder setAverepcom(float averepcom) {
			this.averepcom = averepcom;
			return this;
		}

		public Builder setWbsource(HashMap<String, Integer> wbsource) {
			this.wbsource = wbsource;
			return this;
		}

		public Builder setAverepcombyweek(float averepcombyweek) {
			this.averepcombyweek = averepcombyweek;
			return this;
		}

		public Builder setAverepcombymonth(float averepcombymonth) {
			this.averepcombymonth = averepcombymonth;
			return this;
		}

		public Builder setLastweiboids(List<String> lastweiboids) {
			this.lastweiboids = lastweiboids;
			return this;
		}

		public UserAllWeibosDomain build() {
			return new UserAllWeibosDomain(this);
		}

	}

	public long getWeibosum() {
		return weibosum;
	}

	public float getOriratio() {
		return oriratio;
	}

	public float getAveorirepcom() {
		return aveorirepcom;
	}

	public float getAverepcom() {
		return averepcom;
	}

	public HashMap<String, Integer> getWbsource() {
		return wbsource;
	}

	public float getAverepcombyweek() {
		return averepcombyweek;
	}

	public float getAverepcombymonth() {
		return averepcombymonth;
	}

	public List<String> getLastweiboids() {
		return lastweiboids;
	}

}
