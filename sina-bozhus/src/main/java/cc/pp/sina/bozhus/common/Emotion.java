package cc.pp.sina.bozhus.common;

import java.io.Serializable;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.tokens.verify.HttpUtils;

public class Emotion implements Serializable {

	/**
	 * 默认序列化版本号
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(Emotion.class);

	private static final String BASEURL = "http://60.169.74.147:3305/EmotionAnalysis/emotion?";
	private static final String TOKEN = "2.003s_buBdcZIJC2bfa65d0adJGsU4B";
	private static final String TYPE = "2";

	/**
	 * 测试函数
	 */
	public static void main(String[] args) {
		System.out.println(Emotion.getEmotion("不好的"));
	}

	/**
	 * 获取情感信息
	 */
	public static String getEmotion(String content) {

		//知道token信息
		String queryString = "token=" + TOKEN;
		//知道类型
		queryString = queryString + "&type=" + TYPE;
		//对内容进行编码
		String contentEncode = null;
		try {
			contentEncode = URIUtil.encodeAll(content, "utf-8");
		} catch (URIException e) {
			logger.error("URIException: " + e.getMessage() + ", at Emotion");
			throw new RuntimeException(e);
			//			return null;
		}
		queryString = queryString + "&words=" + contentEncode;
		System.out.println(BASEURL + queryString);

		return HttpUtils.doGet(BASEURL + queryString, "utf-8");
	}

}
