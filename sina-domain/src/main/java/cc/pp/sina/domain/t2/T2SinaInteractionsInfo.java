package cc.pp.sina.domain.t2;

public class T2SinaInteractionsInfo {

	private long username;
	private int date;
	private int fanscount;
	private int allcount;
	private String emotionratio;

	public T2SinaInteractionsInfo() {
		//
	}

	public T2SinaInteractionsInfo(long username, int date, int fanscount, int allcount, String emotionratio) {
		this.username = username;
		this.date = date;
		this.fanscount = fanscount;
		this.allcount = allcount;
		this.emotionratio = emotionratio;
	}

	public long getUsername() {
		return username;
	}

	public void setUsername(long username) {
		this.username = username;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getFanscount() {
		return fanscount;
	}

	public void setFanscount(int fanscount) {
		this.fanscount = fanscount;
	}

	public int getAllcount() {
		return allcount;
	}

	public void setAllcount(int allcount) {
		this.allcount = allcount;
	}

	public String getEmotionratio() {
		return emotionratio;
	}

	public void setEmotionratio(String emotionratio) {
		this.emotionratio = emotionratio;
	}

}
