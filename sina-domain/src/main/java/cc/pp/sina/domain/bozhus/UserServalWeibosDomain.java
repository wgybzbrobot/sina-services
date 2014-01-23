package cc.pp.sina.domain.bozhus;

/**
 * 用户多条微博传播数据
 * @author wgybzb
 *
 */
public class UserServalWeibosDomain {

	private final float avereposterquality; // 真实比例
	private final long aveexposionsum; // 不含该用户的粉丝量

	public UserServalWeibosDomain(Builder builder) {
		this.avereposterquality = builder.avereposterquality;
		this.aveexposionsum = builder.aveexposionsum;
	}

	public static class Builder {

		private final float avereposterquality;
		private final long aveexposionsum;

		public Builder(float avereposterquality, long aveexposionsum) {
			this.avereposterquality = avereposterquality;
			this.aveexposionsum = aveexposionsum;
		}

		public UserServalWeibosDomain build() {
			return new UserServalWeibosDomain(this);
		}
	}

	public float getAvereposterquality() {
		return avereposterquality;
	}

	public long getAveexposionsum() {
		return aveexposionsum;
	}

}
