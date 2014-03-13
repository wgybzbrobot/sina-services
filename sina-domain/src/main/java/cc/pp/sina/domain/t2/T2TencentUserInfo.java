package cc.pp.sina.domain.t2;

public class T2TencentUserInfo {

	private String username;
	private String fansresult;

	public T2TencentUserInfo() {
		//
	}

	public T2TencentUserInfo(String username, String fansresult) {
		this.username = username;
		this.fansresult = fansresult;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFansresult() {
		return fansresult;
	}

	public void setFansresult(String fansresult) {
		this.fansresult = fansresult;
	}

}
