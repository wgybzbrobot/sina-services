package cc.pp.sina.domain.t2;

public class T2SinaUserInfo {

	private long username;
	private String fansresult;

	public T2SinaUserInfo() {
		//
	}

	public T2SinaUserInfo(long username, String fansresult) {
		this.username = username;
		this.fansresult = fansresult;
	}

	public long getUsername() {
		return username;
	}

	public void setUsername(long username) {
		this.username = username;
	}

	public String getFansresult() {
		return fansresult;
	}

	public void setFansresult(String fansresult) {
		this.fansresult = fansresult;
	}



}
