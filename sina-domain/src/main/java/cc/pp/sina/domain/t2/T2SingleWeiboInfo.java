package cc.pp.sina.domain.t2;

public class T2SingleWeiboInfo {

	private String type;
	private long wid;
	private String url;
	private String weiboresult;

	public T2SingleWeiboInfo() {
		//
	}

	public T2SingleWeiboInfo(String type, long wid, String url, String weiboresult) {
		this.type = type;
		this.wid = wid;
		this.url = url;
		this.weiboresult = weiboresult;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getWid() {
		return wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWeiboresult() {
		return weiboresult;
	}

	public void setWeiboresult(String weiboresult) {
		this.weiboresult = weiboresult;
	}

}
