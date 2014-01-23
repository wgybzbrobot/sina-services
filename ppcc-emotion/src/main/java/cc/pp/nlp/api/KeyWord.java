package cc.pp.nlp.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cc.pp.nlp.keyword.CWSTagger;
import cc.pp.nlp.keyword.Extractor;
import cc.pp.nlp.keyword.POSTagger;
import cc.pp.nlp.keyword.StopWords;
import cc.pp.nlp.keyword.WordExtract;

import net.sf.json.JSONArray;


public class KeyWord {
	
	/*********初始化***********/
	private StopWords sw;
	private CWSTagger seg;
	private POSTagger pos;
	private Extractor key;
	private JuHuaSuanDic jhsdic;
	
	/**
	 * @author WG
	 * @param str:第一个变量是字符串
	 * @param wordcharacter:第二个参数是需要保留的词性（词性之间用半角空格隔开）
	 * 主要有：时间短语, 介词, 标点, 形容词, 人称代词, 拟声词, 地名, 省略词, 语气词, 指示代词, 
	 *       叹词, 表情符, 网址, 从属连词, 机构名, 型号名, 事件名, 副词, 序数词, 把动词, 方位词, 
	 *       名词, 形谓词, 能愿动词, 结构助词, 品牌名, 趋向动词, 数词, 时态词, 被动词, 限定词, 
	 *       并列连词, 人名, 量词, 动词, 品牌, 疑问代词, 实体名, 结构词
	 * 地名 省略词 机构名 事件名 名词 品牌名 人名 品牌 实体名--------------名词性质  名词
	 * 把动词 形谓词 能愿动词 趋向动词 时态词 被动词 动词----------------动词性质 动词
	 * 人称代词 形容词 副词 方位词 结构助词 数词 限定词 量词 疑问代词 结构词---形容词等修饰成分 形容词
	 * @param keywordnum:第三个变量是要抽取的关键词数
	 * @throws Exception
	 */
	public KeyWord() throws Exception {
		sw = new StopWords("models/stopwords");
		seg = new CWSTagger("models/seg.m");
		pos = new POSTagger(seg,"models/pos.m");
		key = new WordExtract(seg,pos,sw);
	}
	
	/**
	 * @return Json数据结果-----应用于关键词提取
	 * @return
	 * @throws IOException
	 */
//	public String extractKeyword(String str, String wordcharacter, int keywordnum) throws IOException 
//	{
//		String result = null;
//		LinkedHashMap<String,Integer> temp = new LinkedHashMap<String,Integer>();
//		if ((str == "") || (keywordnum == 0)) {
//			temp = null;
//		} else if (wordcharacter == "") {
//			temp =  (LinkedHashMap<String, Integer>) key.extract(str, keywordnum);
//     	} else {
//			temp =  (LinkedHashMap<String, Integer>) key.extract(str, wordcharacter, keywordnum);
//		}
//		if (temp != null) {
//			HashMap<String,String> keyword = new HashMap<String,String>();
//			int i = 0;
//			for (String key : temp.keySet()) {
//				keyword.put(Integer.toString(i++), key);
//			}
//			JSONArray jsonresult = JSONArray.fromObject(keyword);
//			result = jsonresult.toString();
//		} 
//		
//		return result;
//	}
	
	/**
	 * 标准Json数据----应用于聚划算
	 * @param str
	 * @param keywordnum
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public String extractKeyword(String str, String wordcharacter, int keywordnum) throws IOException 
	{
		Result result = new Result();
		JSONArray jsonresult = null;
		LinkedHashMap<String,Integer> temp = new LinkedHashMap<String,Integer>();
		if ((str == "") || (keywordnum == 0)) {
			temp = null;
		} else if (wordcharacter == "") {
			temp =  (LinkedHashMap<String, Integer>) key.extract(str, keywordnum);
     	} else {
			temp =  (LinkedHashMap<String, Integer>) key.extract(str, wordcharacter, keywordnum);
		}
		if (temp != null) {
			result.setTotal(Integer.toString(temp.size()));
			for (String key : temp.keySet()) {
				HashMap<String,String> word = new HashMap<String,String>();
				String[] dic = jhsdic.getCategory(key);
				word.put("type", dic[0]);
				word.put("weight", Integer.toString(temp.get(key)));
				word.put("word", dic[1]);
				result.getWordList().add(word);
			}
			jsonresult = JSONArray.fromObject(result);
		} 
		String dealresult = jsonresult.toString();
		dealresult = dealresult.substring(1, dealresult.length() - 1);
		
		return dealresult;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//第一个参数是字符串 ，第三个参数是需要抽取多少个关键词
		long time1 = System.currentTimeMillis();
		KeyWord mykeyword = new KeyWord();
		long time2 = System.currentTimeMillis();
		System.out.println(mykeyword.extractKeyword("我要团购杭州的电影票和美食", "",50));
		long time3 = System.currentTimeMillis();
		System.out.println(time3 - time2);
	}

}

