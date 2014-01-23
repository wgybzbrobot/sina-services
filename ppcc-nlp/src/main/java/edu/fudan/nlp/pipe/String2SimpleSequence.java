package edu.fudan.nlp.pipe;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import edu.fudan.ml.types.Instance;
import edu.fudan.nlp.chinese.Chars;

public class String2SimpleSequence extends Pipe implements Serializable {
	
	private static final long serialVersionUID = 45050330616309396L;

	private boolean isEnglishSegment;
	
	public String2SimpleSequence() {
		isEnglishSegment = false;
	}
	
	public String2SimpleSequence(boolean isEnglishSegment) {
		this.isEnglishSegment = isEnglishSegment;
	}
	
	public void addThruPipe(Instance inst) {
		String str = (String) inst.getData();
		String seq;
		if(isEnglishSegment)
			seq = genSequenceEnglishSegment(str);
		else
			seq = genSequence(str);
		String[] toks = seq.split("\n");
		List data = Arrays.asList(toks);
		inst.setData(data);
	}
	
	/*
	 * String2Sequence虽然处理了一些复杂情况,但是有Bug
	 * String2SimpleSequence很好的处理了一般的情况
	 */	
	public static String genSequence(String sent){
		char[] tag = Chars.getTag_Simple(sent);
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < sent.length(); j++) {
			char c = sent.charAt(j);	
			
			if(tag[j]=='h' || tag[j]=='p'){//当前是汉字或标点
				sb.append(c);
				sb.append("\n");
			} else if(tag[j]=='s') {
				//空格不要
			} else if(tag[j]=='e'){//当前是英文
				sb.append(c);
				if(j + 1 < sent.length() && tag[j+1] == 'e'){//下一个也是英文
					continue;
				}else{//下一个不是英文
					sb.append("\n");
				}
			} else if(tag[j]=='d'){//当前是数字
				sb.append(c);
				if(j + 1 < sent.length() && tag[j+1] == 'd'){//下一个也是数字
					continue;
				}else{//下一个不是数字
					sb.append("\n");
				}
			}	
		}
		sb.append('\n');
		return sb.toString();
	}
	
	
	/*
	 * String2Sequence虽然处理了一些复杂情况,但是有Bug
	 * String2SimpleSequence很好的处理了一般的情况
	 * String2SimpleSequence2不将英文合并
	 */	
	public static String genSequenceEnglishSegment(String sent){
		char[] tag = Chars.getTag_Simple(sent);
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < sent.length(); j++) {
			char c = sent.charAt(j);	
			
			if(tag[j]=='h' || tag[j]=='p' || tag[j] == 'e'){//当前是汉字或标点或英文
				sb.append(c);
				sb.append("\n");
			} else if(tag[j]=='s') {
				//空格不要
			} else if(tag[j]=='d'){//当前是数字
				sb.append(c);
				if(j + 1 < sent.length() && tag[j+1] == 'd'){//下一个也是数字
					continue;
				}else{//下一个不是数字
					sb.append("\n");
				}
			}	
		}
		sb.append('\n');
		return sb.toString();
	}
}
