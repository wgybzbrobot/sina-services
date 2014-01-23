package edu.fudan.nlp.chinese;

import java.util.regex.Pattern;

public class Chars {
	/**
	 * 半角或全角数字英文
	 * @param str
	 * @return 0,1
	 */
	public static boolean isChar(String str){ 
		char[] c = str.toCharArray();
		for (int i = 0; i< c.length; i++) {
			if(!isChar(c[i]))
				return false;				
		}
		return true;
	}
	/**
	 * 半角或全角数字英文
	 * @param c
	 * @return 0,1
	 */
	public static boolean isChar(char c){ 
		if((c>32&&c<127)||c == 12288||(c> 65280&& c< 65375)){
			return true; 
		}else{
			return false;
		}
	}
	
	/**
	 * 得到语句的字符信息,没有复杂处理
	 * 
	 * @param str
	 * @return h:汉字 e:字母 d：数字 p：标点 s:空格
	 */
	public static char[] getTag_Simple(String str) {
		char[] tag = new char[str.length()];
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			
			if(Character.isLowerCase(c) || Character.isUpperCase(c))
				tag[i] = 'e';
			else if(Character.isDigit(c))
				tag[i] = 'd';
			else if(Character.isWhitespace(c) || Character.isSpaceChar(c))
				tag[i] = 's';
			else if(Pattern.matches("\\pP|\\pS", c + "")) {
				tag[i] = 'p';
			}
			else 
				tag[i] = 'h';
		}
		return tag;
	}
	
	
	/**
	 * 得到语句的字符信息
	 * 
	 * @param str
	 * @return h:汉字 e:字母 d：数字 p：标点 s:空格
	 */
	public static char[] getTag(String str) {
		char[] tag = new char[str.length()];
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||
					(c>='a'+65248&&c<='z'+65248)||(c>='A'+65248&&c<='Z'+65248)){
				tag[i] = 'e';
			}else if(c=='-'||c=='—'||c=='/'){//连接号
				if(i>1&&tag[i-1]=='e'){//前面是字母/数字
					tag[i] = 'e';
				}else if(i>1&&tag[i-1]=='d'){//前面是数字
					tag[i] = 'd';					
				}
				else if(i>1&&tag[i-1]=='d'&&i<str.length()-1&&isChar(tag[i+1])){//前面是数字，后面是字母
					tag[i] = 'e';
				}else
					tag[i] = 'h';
			}
			else if((c>='0'&&c<='9')||(c>='0'+65248&&c<='9'+65248)){
				tag[i] = 'd';
			}else if(c=='.'&&i>1&&tag[i-1]=='d'){//小数点
				tag[i]='d';
			}else if(c=='%'&&i>1&&tag[i-1]=='d'){//%
				tag[i]='d';
			}else if("()。!,\"'（）！，””<>《》".indexOf(c)!=-1){
				tag[i] = 'p';
			}else if(c==12288||c==32){
				tag[i] = 's';
			}else{			
				tag[i] = 'h';
			}
		}
		return tag;
	}

	/**	 * 
	 * @param str
	 * @return D:纯数字串 E：纯字母  M：数字字母混合 O:其他
	 */
	public static String getType(String str){
		char[] tag = getTag(str);
		String s = String.valueOf(tag);
		if(s.contains("h")||s.contains("s"))
			return "O";
		else if(s.contains("d")){
			if(s.contains("e"))
				return "M";
			else
				return "D";
		}else if(s.contains("e")){
			return "E";
		}
		return "O";
	}
}
