package edu.fudan.nlp.parser.dep.yamada;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.fudan.ml.data.Reader;
import edu.fudan.ml.types.Instance;

/**
 * @author Feng Ji
 */

public class CoNLLReader extends Reader {

	BufferedReader reader = null;
	Sentence next = null;
	List<String[]> carrier = new ArrayList<String[]>();

	public CoNLLReader(String filepath) throws IOException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				filepath), "UTF-8"));
		advance();
	}

	private void advance() throws IOException {
		String line = null;
		carrier.clear();
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.matches("^$"))
				break;
			carrier.add(line.split("\\t+|\\s+"));
		}

		next = null;
		if (!carrier.isEmpty()) {
			String[] forms = new String[carrier.size()];
			String[] postags = new String[carrier.size()];
			int[] heads = new int[carrier.size()];
			for (int i = 0; i < carrier.size(); i++) {
				String[] tokens = carrier.get(i);
				forms[i] = tokens[1];
				if (tokens[4].endsWith("ADV")) {
					int p = tokens[4].indexOf("ADV");
					if (p != 0)
						tokens[4] = tokens[4].substring(0, p);
				}
				postags[i] = tokens[4];
				heads[i] = -1;
				if (!tokens[8].equals("_"))
					heads[i] = Integer.parseInt(tokens[8]) - 1;
			}

			next = new Sentence(forms, postags, heads);
		}
	}

	public boolean hasNext() {
		return (next != null);
	}

	public Instance next() {
		Sentence cur = next;
		try {
			advance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cur;
	}
}
