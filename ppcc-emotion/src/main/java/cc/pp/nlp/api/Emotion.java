package cc.pp.nlp.api;

import java.io.IOException;

import libsvm.SvmResult;

import org.json.JSONException;

import tmsvm.TmsPredict;

public class Emotion {

	private final TmsPredict libsvm;
	private final Complex mmseg;

	public Emotion(TmsPredict svm, Complex mmseg) {
		this.libsvm = svm;
		this.mmseg = mmseg;
	}

	/**
	 *其中，weibotext是经过处理的微博内容；
	 *输出结果为正负情感值。JSON格式
	 * @param weibotext
	 */
	public String UserEmotion2(String weibotext) {

		String temp = "";
		try {
			temp = "0\t" + mmseg.segWords(weibotext, "^")+"^";
		} catch (IOException e) {
			e.printStackTrace();
		}

		String str_splitTag = "\\^"; //标题和内容经过分词后，各个词的分割符号

		SvmResult result = libsvm.getemotionby2(temp,libsvm,str_splitTag);
		double label = result.getLabel();
		double score = result.getScore();
		String json = "{\"label\":"+label+",\"score\":"+score+"}";

		return json;
	}


	/**
	 *其中，weibotext是经过处理的微博内容；
	 *输出结果为正负情感值。JSON格式
	 *
	 * @param weibotext
	 * @throws JSONException
	 */
	public String UserEmotion8(String weibotext) {

		Complex mmseg = new Complex();
		String temp = "";
		try {
			temp = mmseg.segWords(weibotext, "\\^");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String str_splitTag = "\\^"; //标题和内容经过分词后，各个词的分割符号

		SvmResult result = libsvm.getemotionby8(temp,libsvm,str_splitTag);
		double label = result.getLabel();
		double score = result.getScore();
		String json = "{\"label\":"+label+",\"score\":"+score+"}";

		return json;
	}

}

