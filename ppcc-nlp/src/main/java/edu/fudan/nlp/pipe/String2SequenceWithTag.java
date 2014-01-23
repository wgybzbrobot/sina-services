/*
 * 文件名：SequenceFormat.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 描述：程序总入口
 * 修改人：xpqiu
 * 修改时间：2008-12-27
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 跟踪单号：〈跟踪单号〉
 * 修改单号：〈修改单号〉
 * 修改内容：〈修改内容〉
 */
package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import edu.fudan.ml.types.Instance;
import edu.fudan.nlp.chinese.Chars;

/**
 * 将字符串直接转换成序列
 * @author xpqiu
 *
 */
public class String2SequenceWithTag extends Pipe implements Serializable {


	private static final long serialVersionUID = -4538256414455428517L;
	boolean hasLabel = true;
	public static String seqDelimer = "\\s+";
	public static String tagDelimer = "_";
	public static Set<String> tagFilter=null;

	public String2SequenceWithTag(boolean b){
		hasLabel = b;
	}


	/**
	 * 将一个字符串转换成按列显示，每列一个字或连续英文token的信息，
	 * 第一列是字符，最后一列是BIO标签
	 * @param inst 样本
	 */
	@Override
	public void addThruPipe(Instance inst) {
		String str = (String) inst.getData();
		String seq = genSequence(str);
		String[] toks = seq.split("\n");
		List<String> data = Arrays.asList(toks);
		inst.setData(data);		
	}

	public static String genSequence(String sent){
		char[] tag = Chars.getTag(sent);
		StringBuilder sb = new StringBuilder();
		for(int j=0; j<sent.length(); j++) {
			char c = sent.charAt(j);	
			sb.append(c);
			if(j==sent.length()-1)
				sb.append('\n');
			else if(tag[j]=='h'||tag[j]=='p'){//当前是汉字或标点
				sb.append('\n');
			}else if(tag[j]=='e' || tag[j]=='d'){//当前是英文数字
				if(tag[j+1]=='e'|| tag[j+1]=='d'){//下一个也是英文数字
					continue;
				}else{//下一个不是英文数字
					sb.append("\n");
					if(tag[j+1]=='s'){
						j++;
					}
				}
			}else if(tag[j]=='p'){//当前是标点
				sb.append("\n");
			}					
		}
		sb.append('\n');
		return sb.toString();
	}

	public static String genSequenceWithLabel(String sent){
		StringBuilder sb = new StringBuilder();
		String[] wordArray = sent.split(seqDelimer);
		for(int i=0; i<wordArray.length; i++) {
			String word = wordArray[i];
			char[] tag = Chars.getTag(word);
			for(int j=0; j<word.length(); j++) {
				char c = word.charAt(j);	
				sb.append(c);
				if(tag[j]=='e' || tag[j]=='d'){//当前是英文数字
					if(j==word.length()-1){//结尾
						sb.append("\tS");
					}else if(tag[j+1]=='e'|| tag[j+1]=='d'){//下一个也是英文数字
						continue;
					}else{//下一个不是英文数字
						sb.append("\tB");
					}						
				}else if(tag[j]=='p'){//当前是标点
					sb.append("\tS");
				}else{
					sb.append('\t');
					if(j == 0) {
						if(word.length() == 1)
							sb.append('S');
						else
							sb.append('B');
					} else if(j == word.length()-1) {
						sb.append('E');
					} else {
						if(j==1) {
							sb.append("B1");
						} else if(j==2) {
							sb.append("B2");
						} else {
							sb.append('M');
						}
					}
				}
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}

	public static String genSequenceWithLabelSimple(String sent){
		sent = sent.trim();
		if(sent.length()==0)
			return sent;
		StringBuilder sb = new StringBuilder();
		String[] wordArray = sent.split(seqDelimer);
		for(int i=0; i<wordArray.length; i++) {
			//得到tag
			int idx = wordArray[i].lastIndexOf(tagDelimer);
			if(idx==-1||idx==wordArray[i].length()-1){
				System.err.println(wordArray[i]);
			}			
			String word = wordArray[i].substring(0,idx);
			String tag = wordArray[i].substring(idx+1);
			for(int j=0; j<word.length(); j++) {
				char c = word.charAt(j);	
				sb.append(c);
				sb.append('\t');
				if(tagFilter==null||tagFilter.contains(tag)){//不过滤或是保留项
					
					if(j == 0) {
						if(word.length() == 1)
							sb.append("S-"+tag);
						else
							sb.append("B-"+tag);
					} else if(j == word.length()-1) {
						sb.append("E-"+tag);
					} else {
						//					if(j==1) {
						//						sb.append("B1-"+tag);
						//					} else if(j==2) {
						//						sb.append("B2-"+tag);
						//					} else {
						//						sb.append("M-"+tag);
						//					}
						sb.append("M-"+tag);
					}
				}else{//过滤项
					sb.append("O");
				}
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}
	public static void main(String[] args){
		genSequenceWithLabelSimple("NN_NN");
	}
}
