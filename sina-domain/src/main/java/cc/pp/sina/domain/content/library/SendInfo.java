package cc.pp.sina.domain.content.library;

/**
 * 定时器发送内容纪录
 * @author wgybzb
 *
 */
public class SendInfo {

	private final int cid;
	private final long username;
	private final int status;
	private final int ispower;
	private final long sendtime;

	public SendInfo(Builder builder) {
		this.cid = builder.cid;
		this.username = builder.username;
		this.status = builder.status;
		this.ispower = builder.ispower;
		this.sendtime = builder.sendtime;
	}

	public static class Builder {

		private int cid;
		private long username;
		private int status;
		private int ispower;
		private long sendtime;

		public Builder() {
			//
		}

		public Builder setCid(int cid) {
			this.cid = cid;
			return this;
		}

		public Builder setUsername(long username) {
			this.username = username;
			return this;
		}

		public Builder setStatus(int status) {
			this.status = status;
			return this;
		}

		public Builder setIspower(int ispower) {
			this.ispower = ispower;
			return this;
		}

		public Builder setSendtime(long sendtime) {
			this.sendtime = sendtime;
			return this;
		}

		public SendInfo build() {
			return new SendInfo(this);
		}

	}

	public int getCid() {
		return cid;
	}

	public long getUsername() {
		return username;
	}

	public int getStatus() {
		return status;
	}

	public int getIspower() {
		return ispower;
	}

	public long getSendtime() {
		return sendtime;
	}

}
