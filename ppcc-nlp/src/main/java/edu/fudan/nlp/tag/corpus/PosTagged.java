package edu.fudan.nlp.tag.corpus;

import java.util.ArrayList;

public class PosTagged {
	//没有用了,因为ctb_v6/template和ctb_v6/processed/postagged.train已经有了
	
	
	// pre + id + suf构成文件名
	private static final String pre = "ctb_v6/data/postagged/chtb_"; //前缀
	private static final String suf = ".pos";	//后缀
	private static final int id_l = 4; //id位数
	private static final int id_s = 2000; //id起始
	private static final int id_e = 3145; //id结束
	
	private static int id = id_s; //现在遍历到的id
	
	public static void setID(int id) {
		PosTagged.id = id;
	}
	
	public static String getFileName() {
		if(id < id_s || id > id_e)
			return null;
		return pre + String.format("%04d", id++) + suf; 
	}
	
	public static String[] getContent(String fileName) {
		return null;
	}
	
	public static void main(String[] args) {
		ArrayList<String[]> content = new ArrayList<String[]>();
		
		while(true) {
			String file = getFileName();
			if(file == null)
				break;
			
			String[] re = getContent(file);
			if(re != null)
				content.add(re);
		}
	}
}
