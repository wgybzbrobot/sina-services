package cc.pp.nlp.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.fudan.nlp.keyword.Extractor;
import edu.fudan.nlp.keyword.WordExtract;
import edu.fudan.nlp.tag.CWSTagger;

/**
 * 关键词抽取使用示例
 * @author jyzhao,ltian
 *
 */
public class KeyWordTest {

	public static Extractor key = null;;
	public static CWSTagger seg = null;

	static {
		try {
			key = new WordExtract("models/seg.c7.110918.gz","models/stopwords");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String readFile(String dicPath) throws FileNotFoundException{
		String s;
		StringBuffer ss = new StringBuffer();

		//	    try {
		//	    	FileReader in = new FileReader(dicPath);
		//		    BufferedReader readin = new BufferedReader(in);
		//			while((s = readin.readLine()) != null){
		//				ss.append(s);
		//			}
		//		} catch (IOException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		try{
			InputStreamReader read = new InputStreamReader(new FileInputStream(dicPath), "UTF-8");
			BufferedReader in = new BufferedReader(read);
			while((s = in.readLine()) != null){
				ss.append(s);
			}
			in.close();
		}catch(Exception e){

		}


		return String.valueOf(ss);
	}

	public static void writeFile(String filePath, String detail)throws Exception {
		try{
			File file = new File(filePath);
			FileWriter filewriter = new FileWriter(file, true);
			filewriter.write(detail);
			filewriter.close();
		}catch(IOException e){
			throw e;
		}catch (Exception ex) {
			throw ex;
		}
	}

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();

		System.out.println(key.extract(readFile("./example-data/text/1.txt"), 100,true));

		double t = (System.currentTimeMillis() - time) / 1000.0;
		System.out.println(t);

	}
}


