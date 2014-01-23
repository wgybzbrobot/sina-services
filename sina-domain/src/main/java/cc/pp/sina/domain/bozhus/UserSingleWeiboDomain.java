package cc.pp.sina.domain.bozhus;

/**
 * 用户单条微博传播数据
 * @author wgybzb
 *
 */
public class UserSingleWeiboDomain {

	private final int[] reposterquality;
	private final long exposionsum; // 不含该用户的粉丝量

	public UserSingleWeiboDomain(Builder builder) {
		this.reposterquality = builder.reposterquality;
		this.exposionsum = builder.exposionsum;
	}

	public static class Builder {

		private int[] reposterquality = new int[2];
		private final long exposionsum;

		public Builder(int[] reposterquality, long exposionsum) {
			this.reposterquality = reposterquality;
			this.exposionsum = exposionsum;
		}

		public UserSingleWeiboDomain build() {
			return new UserSingleWeiboDomain(this);
		}
	}

	public int[] getReposterquality() {
		return reposterquality;
	}

	public long getExposionsum() {
		return exposionsum;
	}

}
