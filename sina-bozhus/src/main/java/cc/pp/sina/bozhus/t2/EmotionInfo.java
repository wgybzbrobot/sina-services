package cc.pp.sina.bozhus.t2;

public class EmotionInfo {

	private double label;
	private double score;

	public EmotionInfo() {
		//
	}

	public EmotionInfo(double label, double score) {
		this.label = label;
		this.score = score;
	}

	public double getLabel() {
		return label;
	}

	public void setLabel(double label) {
		this.label = label;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

}
