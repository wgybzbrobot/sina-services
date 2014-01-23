package cc.pp.nlp.demo;
import edu.fudan.nlp.parser.dep.yamada.Parser;
import edu.fudan.nlp.tag.POSTagger;
/**
 * Yamada依存句法分析使用示例
 * @author xpqiu
 *
 */
public class YamadaParser {

	private static Parser parser;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		parser = new Parser("models/chs.dep.ymd.gz");

		String word = "中国进出口银行与中国银行加强合作";
		test(word);
		
//		parser = new Parser("models/en.dep.ymd.gz");
//		word = "I eat apple.";
//		test(word);
		

	}
	/**
	 * 只输入句子，不带词性
	 * @throws Exception 
	 */
	private static void test(String word) throws Exception {		
		POSTagger tag = new POSTagger("models/pos.c7.110919.gz");
		String[][] s = tag.tag2Array(word);
		int[] heads = new int[s[0].length];
		try {
			heads = parser.parse(s[0], s[1]);
		} catch (Exception e) {			
			e.printStackTrace();
		}

		for(int i = 0; i < heads.length; i++)
			System.out.printf("%s\t%s\t%d\n", s[0][i], s[1][i], heads[i]);
	}

}