package cc.pp.sina.domain.t2;

public class SinaSelectParams {

	private long username;
	private int maxnum;

	public SinaSelectParams() {
		//
	}

	public SinaSelectParams(long username, int maxnum) {
		this.username = username;
		this.maxnum = maxnum;
	}

	public long getUsername() {
		return username;
	}

	public void setUsername(long username) {
		this.username = username;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}


}
