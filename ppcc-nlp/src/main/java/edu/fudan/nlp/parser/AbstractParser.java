package edu.fudan.nlp.parser;


public abstract class AbstractParser {
	
	public abstract ParseTree getBestParse(String[] words);
	
	public abstract ParseTree getBestParse(String[] words, String[] pos);
	
	public abstract ParseTree getBestParse(String sentence);
}
