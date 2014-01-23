package cc.pp.sina.domain.users;

public class UsersSelectParams {

	private String tablename;
	private int low;
	private int high;

	public UsersSelectParams() {
		//
	}

	public UsersSelectParams(String tablename, int low, int high) {
		this.tablename = tablename;
		this.low = low;
		this.high = high;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

}
