package edu.fudan.ml.types;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectIntIterator;

public final class LabelAlphabet extends AbstractAlphabet {

	private static final long serialVersionUID = 2877624165731267884L;

	private TIntObjectHashMap<String> index;

	LabelAlphabet() {
		super();
		index = new TIntObjectHashMap<String>();
	}

	public int size() {
		return index.size();
	}

	public int lookupIndex(String str) {
		int ret = -1;
		if (!frozen && !data.containsKey(str))	{
			ret = index.size();
			data.put(str, ret);
			index.put(ret, str);
		}else if (data.containsKey(str)) {
			ret = data.get(str);
		}
		return ret;
	}

	public String lookupString(int id) {
		String ret = null;
		if (index.containsKey(id))	{
			ret = index.get(id);
		}
		return ret;
	}

	public String[] lookupString(int[] ids) {
		String[] vals = new String[ids.length];
		for(int i = 0; i < ids.length; i++)	{
			vals[i] = index.get(ids[i]);
		}
		return vals;
	}

	public Set<Integer> toSet() {
		Set<Integer> set = new HashSet<Integer>();
		for (TObjectIntIterator<String> it = data.iterator(); it.hasNext();) {
			it.advance();
			set.add(it.value());
		}
		return set;
	}
}
