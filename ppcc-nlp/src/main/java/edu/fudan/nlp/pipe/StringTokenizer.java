package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.fudan.ml.types.Instance;

/**
 * 将样本处理成结构的数组
 * List\<String\> --> List\<String[]\>
 * @author xpqiu
 *
 */
public class StringTokenizer extends Pipe implements Serializable {

	
	private static final long serialVersionUID = 7676939314112735121L;
	
	String delimiter = "\t";
	
	public StringTokenizer(String delimer) {
		super();
		this.delimiter = delimer;
	}

	public StringTokenizer() {
		super();
	}

	

	public void addThruPipe(Instance instance) {
		List data0 = (List) instance.getData();
		List data1 = new ArrayList();
		for(int i=0; i<data0.size(); i++) {
			String s = (String) data0.get(i);
			String[] arr = s.split(delimiter);
			data1.add(arr);
		}
		instance.setData(data1);
	}
	public static void main(String[] args){
		String str = "	M";
		StringTokenizer st =new StringTokenizer();
		String[] arr = str.split(st.delimiter);
		System.out.println(str);
	}
	
}
