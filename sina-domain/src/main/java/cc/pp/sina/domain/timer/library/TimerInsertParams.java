package cc.pp.sina.domain.timer.library;

public class TimerInsertParams {

	private String tablename;
	private String date;
	private String name;
	private int count;

	public TimerInsertParams(String tablename, String date, String name, int count) {
		this.tablename = tablename;
		this.date = date;
		this.name = name;
		this.count = count;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}