package cc.pp.content.library.demo;

import edu.fudan.nlp.tag.CWSTagger;

public class WordSegmentDemo {

	public static void main(String[] args) throws Exception {

		String str = "浙江杭州西湖区西溪路628号，天目山路，买东西去淘宝商城，你大爷";

		System.out.println("不使用词典：");
		long start1 = System.currentTimeMillis();
		CWSTagger tag = new CWSTagger("./models/seg.c7.110918.gz");
		long middle1 = System.currentTimeMillis();
		System.out.println("数据加载时间： " + (middle1 - start1));
		System.out.println(tag.tag(str));
		long end1 = System.currentTimeMillis();
		System.out.println("分词时间： " + (end1 - middle1));

		System.out.println("\n使用词典");
		long start2 = System.currentTimeMillis();
		tag = new CWSTagger("./models/seg.c7.110918.gz", "models/dict.txt");
		long middle2 = System.currentTimeMillis();
		System.out.println("数据加载时间： " + (middle2 - start2));
		System.out.println(tag.tag(str));
		long end2 = System.currentTimeMillis();
		System.out.println("分词时间： " + (end2 - middle2));
	}

}
