package cc.pp.nlp.example;

import tmsvm.TmsPredict;
import cc.pp.nlp.api.Complex;
import cc.pp.nlp.api.Emotion;

public class EmotionDemo {

	public static void main(String args[]) {
        /****************库加载******************/
		TmsPredict libsvm = new TmsPredict("model/","tms.config");
		Complex mmseg = new Complex();
		Emotion app = new Emotion(libsvm, mmseg);
		/**************************************/

		/****************八种情感测试******************/
		System.out.println("8种情感：");
		long start = System.currentTimeMillis();
		String temp = app.UserEmotion8("很好");
		long end = System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： " + (end-start) + "ms");
		System.out.println(temp);

		/****************两种情感测试******************/
		System.out.println("2种情感：");
		start = System.currentTimeMillis();
		temp = app.UserEmotion2("感觉不行");
		end = System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间： " + (end-start) + "ms");
		System.out.println(temp);

	}


}

