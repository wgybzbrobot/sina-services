package edu.fudan.ml.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import edu.fudan.ml.types.Instance;

/**
 * 读入序列标记的数据
 * 格式为
 * x1 y1
 * x2 y2
 * 
 * 不同样本以空行分开
 * @author xpqiu
 *
 */
public class SequenceReader extends Reader {

	BufferedReader reader;
	Instance cur;

	public SequenceReader(String file) {
		this(file, "UTF-8");
	}

	public SequenceReader(String file, String charsetName) {
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), charsetName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public SequenceReader(InputStream is) {

		reader = new BufferedReader(new InputStreamReader(
				is));
	}

	public boolean hasNext() {
		cur = readSequence();
		return (cur != null);
	}

	public Instance next() {
		return cur;
	}

	private Instance readSequence() {
		cur = null;
		try {
			ArrayList<String> seq = new ArrayList<String>();
			String content = null;
			while ((content = reader.readLine()) != null) {
				content = content.trim();
				if (content.matches("^$")){
					if(seq.size()>0)
						break;
					else
						continue;
				}

				seq.add(content);
			}
			if (seq.size() > 0){
				cur = new Instance(seq, null);
				cur.setSource(seq);
			}
			seq = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cur;
	}

	public static void main(String[] args) {
		SequenceReader sr = new SequenceReader("example-data/chunk/cross/train.txt");
		Instance inst = null;
		int count = 0;
		while (sr.hasNext()) {
			inst = sr.next();
			System.out.print(".");
			inst = null;
			count++;
		}
		System.out.println(count);
	}
}
