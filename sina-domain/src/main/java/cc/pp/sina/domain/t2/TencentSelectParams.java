package cc.pp.sina.domain.t2;

public class TencentSelectParams {

	private String username;
	private int maxnum;

	public TencentSelectParams() {
		//
	}

	public TencentSelectParams(String username, int maxnum) {
		this.username = username;
		this.maxnum = maxnum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

}
