package edu.fudan.ml.feature.templet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Dictionary {

	private HashMap<String,HashSet> dicts;

	public Dictionary() {
		dicts = new HashMap<String,HashSet>();
	}
	public void load(String file,String tag) throws Exception{
		if(file == null) return;
		BufferedReader r = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(file)));
		HashSet dict = new HashSet<String>();
		while(true) {
			String s = r.readLine();
			if(s == null)
				break;
			dict.add(s);
		}
		r.close();
		dicts.put(tag,dict);
	}
	public void loadWithWeigth(String file, String tag) throws Exception{
		if(file == null) return;
		BufferedReader r = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(file),"utf8"));
		HashSet dict = new HashSet<String>();
		while(true) {
			String s = r.readLine();
			if(s == null)
				break;
			String[] strs = s.split("\\t");
			if(strs[0].contains(" "))
				continue;
			
			String key = tag+strs[1];
//			System.out.println(key+strs[0]);
			if(!dicts.containsKey(key)){
				HashSet set = new HashSet();
				dicts.put(key, set);
			}
			dicts.get(key).add(strs[0]);
				
		}
		r.close();
		System.out.println("Dictionary Number: "+dicts.size());
	}
	
	
	
	
	/**
	 * 返回词典标签
	 * @param word
	 * @return 词典列表
	 */
	public List<String> test(String word) {
		List<String> l = new ArrayList<String>();
		Iterator<String> it = dicts.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			if(dicts.get(key).contains(word)){
				l.add(key);
			}
		}
		return l;
	}
}
