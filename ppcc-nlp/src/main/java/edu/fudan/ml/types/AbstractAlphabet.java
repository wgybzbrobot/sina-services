package edu.fudan.ml.types;

import gnu.trove.TObjectIntHashMap;

import java.io.Serializable;

public abstract class AbstractAlphabet implements Serializable {

	private static final long serialVersionUID = -6803250687142456011L;
	
	protected TObjectIntHashMap<String> data;
	protected boolean frozen;
	
	AbstractAlphabet()	{
		data = new TObjectIntHashMap<String>();
		frozen = false;
	}
	
	public boolean isStopIncrement() {
		return frozen;
	}

	/**
	 * 不再增加新的词
	 * @param stopIncrement
	 */
	public void setStopIncrement(boolean stopIncrement) {
		this.frozen = stopIncrement;
	}
	
	public abstract int lookupIndex(String str);
	
	public abstract int size();

}
