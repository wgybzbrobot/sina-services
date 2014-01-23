package edu.fudan.ml.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * 特征和标签索引字典管理器
 * @author xpqiu *
 */
public final class AlphabetFactory implements Serializable {

	private static final long serialVersionUID = 4949560459448660488L;
	private Map<String, AbstractAlphabet> maps = null;
	private static AlphabetFactory instance = null;

	private AlphabetFactory() {
		maps = new HashMap<String, AbstractAlphabet>();
	}

	private AlphabetFactory(Map<String, AbstractAlphabet> maps) {
		this.maps = maps;
	}

	public static AlphabetFactory buildFactory() {
		if (instance == null)
			instance = new AlphabetFactory();
		return instance;
	}
	
	public FeatureAlphabet buildFeatureAlphabet(String name)	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey(name))	{
			maps.put(name, new FeatureAlphabet());
			alphabet = maps.get(name);
		}else	{
			alphabet = maps.get(name);
			if (!(alphabet instanceof FeatureAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (FeatureAlphabet) alphabet;
	}
	/**
	 * 建立缺省的特征字典
	 * @return
	 */
	public FeatureAlphabet DefaultFeatureAlphabet()	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey("feature"))	{
			maps.put("feature", new FeatureAlphabet());
			alphabet = maps.get("feature");
		}else	{
			alphabet = maps.get("feature");
			if (!(alphabet instanceof FeatureAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (FeatureAlphabet) alphabet;
	}
	
	public FeatureAlphabet rebuildFeatureAlphabet(String name)	{
		AbstractAlphabet alphabet = null;
		if (maps.containsKey(name))	{
			alphabet = maps.get(name);
			if (!(alphabet instanceof FeatureAlphabet))	{
				throw new ClassCastException();
			}
		}
		maps.put(name, new FeatureAlphabet());
		return (FeatureAlphabet)maps.get(name);
	}
	
	public LabelAlphabet buildLabelAlphabet(String name)	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey(name))	{
			maps.put(name, new LabelAlphabet());
			alphabet = maps.get(name);
		}else	{
			alphabet = maps.get(name);
			if (!(alphabet instanceof LabelAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (LabelAlphabet) alphabet;
	}
	/**
	 * 建立缺省的标签字典
	 * @return
	 */
	public LabelAlphabet DefaultLabelAlphabet()	{
		AbstractAlphabet alphabet = null;
		if (!maps.containsKey("label"))	{
			maps.put("label", new LabelAlphabet());
			alphabet = maps.get("label");
		}else	{
			alphabet = maps.get("label");
			if (!(alphabet instanceof LabelAlphabet))	{
				throw new ClassCastException();
			}
		}
		return (LabelAlphabet) alphabet;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(maps);
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		maps = (Map<String, AbstractAlphabet>) in.readObject();
		instance = new AlphabetFactory(maps);
	}

	/**
	 * 得到类别数量 y
	 * @return
	 */
	public int getLabelSize() {
		return DefaultLabelAlphabet().size();
	}
	/**
	 * 得到特征数量 f(x,y)
	 * @return
	 */
	public int getFeatureSize() {
		return DefaultFeatureAlphabet().size();
	}

	/**
	 *  不再增加新的词
	 * @param b
	 */
	public void setStopIncrement(boolean b) {
		Iterator<String> it = maps.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			maps.get(key).setStopIncrement(b);
		}
	}
}
