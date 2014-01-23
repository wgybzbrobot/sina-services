package edu.fudan.ml.types;

/**
 * 表示单个样本(x,y)。
 * x,y分别对应data,target.
 * 
 * @author xpqiu
 *
 */
public class Instance {
	/**
	 * 样本值，相当于x
	 */
	protected Object data;
	/**
	 * 标签或类别，相当于y
	 */
	protected Object target;
	/**
	 * 数据来源等需要记录的信息
	 */
	protected Object clause;
	/**
	 * 保存数据的最原始版本
	 */
	private Object source;
	/**
	 * 临时数据
	 */
	private Object tempData;

	public Instance(){		
	}
	
	public Instance(Object data) {
		this(data, null, null);
	}

	
	public Instance(Object data, Object target) {
		this(data, target, null);
	}
	
	public Instance(Object data, Object target, Object clause) {
		this.data = data;
		this.target = target;
		this.clause = clause;
	}

	public Object getTarget() {
		//注释掉下面2行，可能会引起别的问题
//		if (target == null)
//			return data;
		
		return this.target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setClasue(String s) {
		this.clause = s;

	}

	public String getClasue() {
		return (String) this.clause;

	}


	public Object getSource() {
		return this.source;
	}


	public void setSource(Object source) {
		this.source = source;
	}


	public void setTempData(Object tempData) {
		this.tempData = tempData;
	}


	public Object getTempData() {
		return tempData;
	}
	
	public int length()	{
		int ret = 0;
		if (target instanceof Integer)
			ret = 1;
		else if (target instanceof int[])
			ret = ((int[])target).length;
		else if (target instanceof int[][])	{
			for(int i = 0; i < ((int[][])target).length; i++)
				ret += ((int[][])target)[i].length;
		}
		return ret;
	}
}
