package cc.pp.sina.domain.tool;

public class TransUidSelectParams {

	private String tablename;
	private String identify;

	public TransUidSelectParams(String tablename, String identify) {
		this.tablename = tablename;
		this.identify = identify;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

}
