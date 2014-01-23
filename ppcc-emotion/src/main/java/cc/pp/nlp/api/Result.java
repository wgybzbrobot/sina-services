package cc.pp.nlp.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Result {
	
	private String total = new String();
	@SuppressWarnings("rawtypes")
	List<HashMap> list = new ArrayList<HashMap>(); 
	
	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getTotal() {
		return this.total;
	}
	
	@SuppressWarnings("rawtypes")
	public List<HashMap> getWordList() {
		return this.list;
	}

}
