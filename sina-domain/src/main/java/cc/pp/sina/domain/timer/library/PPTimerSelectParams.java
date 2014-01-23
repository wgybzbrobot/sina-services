package cc.pp.sina.domain.timer.library;

public class PPTimerSelectParams {

	private String tablename;
	private long starttime;
	private long endtime;

	public PPTimerSelectParams(String tablename, long starttime, long endtime) {
		this.tablename = tablename;
		this.starttime = starttime;
		this.endtime = endtime;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

}
