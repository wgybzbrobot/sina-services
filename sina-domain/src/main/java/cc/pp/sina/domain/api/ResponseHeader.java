package cc.pp.sina.domain.api;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ResponseHeader {

	private int status;
	private long QTime;
	private Params params;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getQTime() {
		return QTime;
	}

	@JsonIgnore
	public void setQTime(long QTime) {
		this.QTime = QTime;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}


}
