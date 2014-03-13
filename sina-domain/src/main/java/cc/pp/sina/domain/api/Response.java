package cc.pp.sina.domain.api;

import java.util.HashMap;
import java.util.List;

public class Response {

	private long numFound;
	private int start;
	private List<HashMap<String,Long>> docs;

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public List<HashMap<String, Long>> getDocs() {
		return docs;
	}

	public void setDocs(List<HashMap<String, Long>> docs) {
		this.docs = docs;
	}

}
