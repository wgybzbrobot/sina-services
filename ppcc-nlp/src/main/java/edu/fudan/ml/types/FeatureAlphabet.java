package edu.fudan.ml.types;

import gnu.trove.TObjectIntIterator;

public final class FeatureAlphabet extends AbstractAlphabet {

	private static final long serialVersionUID = -6187935479742068611L;

	private int last;

	public FeatureAlphabet() {
		super();
		last = 0;
	}

	public int lookupIndex(String str) {
		return lookupIndex(str, 1);
	}

	public int lookupIndex(String str, int indent) {
		if (indent < 1)
			throw new IllegalArgumentException(
					"Invalid Argument in FeatureAlphabet: " + indent);
		int ret = -1;
		if (!frozen && !data.containsKey(str)) {
			data.put(str, last);
			ret = last;
			last += indent;
		} else if (data.containsKey(str)) {
			ret = data.get(str);
		}
		return ret;
	}

	public int size() {
		return last;
	}

	public int nonZeroSize() {
		return this.data.size();
	}

	public boolean hasIndex(int id) {
		return data.containsValue(id);
	}
	
	public int remove(String str)	{
		int ret = -1;
		if (data.containsKey(str))	{
			ret = data.remove(str);
		}
		return ret;
	}
	
	public boolean adjust(String str, int adjust)	{
		return data.adjustValue(str, adjust);
	}
	
	public TObjectIntIterator<String> iterator()	{
		return data.iterator();
	}
}
