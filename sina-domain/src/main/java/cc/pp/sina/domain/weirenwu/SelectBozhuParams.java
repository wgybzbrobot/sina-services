package cc.pp.sina.domain.weirenwu;

public class SelectBozhuParams {

	private int low;
	private int high;

	public SelectBozhuParams() {
		//
	}

	public SelectBozhuParams(int low, int high) {
		this.low = low;
		this.high = high;
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
