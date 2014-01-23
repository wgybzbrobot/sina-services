package cc.pp.sina.domain.tool;

import java.util.HashMap;
import java.util.List;

public class TransUidData {

	private String identify;
	private List<HashMap<String, String>> data;

	public TransUidData() {
		//
	}

	public TransUidData(String identify, List<HashMap<String, String>> data) {
		this.identify = identify;
		this.data = data;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public List<HashMap<String, String>> getData() {
		return data;
	}

	public void setData(List<HashMap<String, String>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TransUidData[" + "identify=" + identify + ", data=" + data.toString() + ']';
	}

}
