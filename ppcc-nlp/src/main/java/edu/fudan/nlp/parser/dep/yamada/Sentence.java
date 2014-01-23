package edu.fudan.nlp.parser.dep.yamada;

import java.io.*;

import edu.fudan.ml.types.AlphabetFactory;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;

/**
 * Instance 对应着一句句子实例
 * 
 * 句子实例包括词序列，相应的Postag序列，每个词对应的中心词（用该词在句子中的下标表示） 完成从输入流中读入句子实例，保存句子实例。
 * 
 * @author cshen
 * @version Feb 16, 2009
 */
public class Sentence extends Instance {
	int[] forms = null;
	int[] postags = null;
	int[] heads = null;
	int length = 0;
	
	AlphabetFactory factory = AlphabetFactory.buildFactory();
	LabelAlphabet wordAlphabet = factory.buildLabelAlphabet("word");
	LabelAlphabet postagAlphabet = factory.buildLabelAlphabet("postag");

	public Sentence(String[] forms) {
		this.forms = new int[forms.length];
		for (int i = 0; i < forms.length; i++) {
			this.forms[i] = wordAlphabet.lookupIndex(forms[i]);
		}
		length = forms.length;
	}

	public Sentence(String[] forms, String[] postags) {
		if (forms.length != postags.length)
			throw new IllegalArgumentException(
					"length of words and postags are not equal!");

		this.forms = new int[forms.length];
		this.postags = new int[postags.length];
		for (int i = 0; i < forms.length; i++) {
			this.forms[i] = wordAlphabet.lookupIndex(forms[i]);
			this.postags[i] = postagAlphabet.lookupIndex(postags[i]);
		}
		length = forms.length;
		this.heads = new int[length];
	}

	public Sentence(String[] forms, String[] postags, int[] heads) {
		this(forms, postags);
		this.heads = heads;
	}

	public int length() {
		return length;
	}

	public void clearDependency() {
		for (int i = 0; i < heads.length; i++) {
			heads[i] = -1;
		}
	}

	public String getWord(int n) {
		if (n > length || n < 0)
			throw new IllegalArgumentException(
					"index should be less than length or great than 0!");
		return wordAlphabet.lookupString(forms[n]);
	}

	public String getTag(int n) {
		if (n > length || n < 0)
			throw new IllegalArgumentException(
					"index should be less than length or great than 0!");
		return postagAlphabet.lookupString(postags[n]);
	}

	public void writeInstance(BufferedWriter outputWriter) throws Exception {
		int[] golds = (int[]) this.getSource();
		for (int i = 0; i < forms.length; i++) {
			outputWriter.write(wordAlphabet.lookupString(forms[i]));
			outputWriter.write("\t");
			outputWriter.write(postagAlphabet.lookupString(postags[i]));
			outputWriter.write("\t");
			outputWriter.write(String.valueOf(golds[i]+1));
			outputWriter.write("\t");
			outputWriter.write(String.valueOf(heads[i]+1));
			outputWriter.newLine();
		}
		outputWriter.newLine();
		outputWriter.flush();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buf.append(wordAlphabet.lookupString(forms[i]));
			buf.append("/");
			buf.append(postagAlphabet.lookupString(postags[i]));
			buf.append(" ");
		}
		return buf.toString().trim();
	}
}
