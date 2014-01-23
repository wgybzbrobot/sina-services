package edu.fudan.ml.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;

import edu.fudan.ml.types.Instance;

public class DocumentReader extends Reader {

	LinkedList<File> files;
	Instance cur;
	Charset charset;
	
	public DocumentReader(String path) {
		this(path, "UTF-8");
	}
	
	public DocumentReader(String path, String charsetName) {
		files = new LinkedList<File>();
		allPath(new File(path));
		charset = Charset.forName(charsetName);
	}

	private void allPath(File handle) {
		if (handle.isFile())
			files.add(handle);
		else if (handle.isDirectory()) {
			for (File sub : Arrays.asList(handle.listFiles()))
				allPath(sub);
		}
	}

	public boolean hasNext() {
		if (files.isEmpty())
			return false;
		nextDocument();
		return true;
	}

	public Instance next() {
		return cur;
	}

	private void nextDocument() {
		StringBuffer buff = new StringBuffer();
		File f = files.poll();
		try {
			BufferedReader cf = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), charset));
			String line = null;
			while((line = cf.readLine()) != null)	{
				buff.append(line);
				buff.append('\n');
			}
			cf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cur = new Instance(buff.toString(), f.getPath());
		buff = null;
	}
}
