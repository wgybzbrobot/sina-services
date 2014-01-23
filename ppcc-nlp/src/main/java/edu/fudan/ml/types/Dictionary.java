package edu.fudan.ml.types;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class Dictionary {
	private int MAX_LEN = 7;
	private int MIN_LEN = 2;
	private TreeSet<String> dict = new TreeSet<String>();

	public Dictionary(String path) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(path), "utf-8");
		MAX_LEN = Integer.MIN_VALUE;
		MIN_LEN = Integer.MAX_VALUE;
		while(scanner.hasNext()) {
			String s = scanner.next().trim();
			if(s.length() > MAX_LEN)
				MAX_LEN = s.length();
			if(s.length() < MIN_LEN)
				MIN_LEN = s.length();
			dict.add(s);
		}
	}
	
	public Dictionary(ArrayList<String> al) {
		MAX_LEN = Integer.MIN_VALUE;
		MIN_LEN = Integer.MAX_VALUE;
		for(int i = 0; i < al.size(); i++) {
			String s = al.get(i);
			if(s.length() > MAX_LEN)
				MAX_LEN = s.length();
			if(s.length() < MIN_LEN)
				MIN_LEN = s.length();
			dict.add(s);
		}
	}
	
	public int getMaxLen() {
		return MAX_LEN;
	}
	
	public int getMinLen() {
		return MIN_LEN;
	}
	
	public boolean contains(String s) {
		return dict.contains(s);
	}
}
