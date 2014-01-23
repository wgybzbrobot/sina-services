/*
 * 文件名：LinearMax.java
 * 版权：Copyright 2008-20012 复旦大学 All Rights Reserved.
 * 修改人：xpqiu
 * 修改时间：2009 Sep 7, 2009 5:28:29 PM
 * 修改内容：新增
 *
 * 修改人：〈修改人〉
 * 修改时间：YYYY-MM-DD
 * 修改内容：〈修改内容〉
 */
package edu.fudan.ml.inf.hier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.fudan.ml.feature.generator.Generator;
import edu.fudan.ml.inf.Inferencer;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.LabelAlphabet;
import edu.fudan.ml.types.Results;
import edu.fudan.ml.types.SparseVector;
import edu.fudan.ml.types.Tree;

/**
 * 树层次结构求最大
 * @author xpqiu
 * @version 1.0 LinearMax package edu.fudan.ml.solver
 */
public class MultiLinearMax extends Inferencer	{


	private static final long serialVersionUID = 460812009958228912L;
	private LabelAlphabet alphabet;
	private Tree tree;
	int numThread;
	ExecutorService pool;
	
	private SparseVector[] weights;
	private Generator featureGen;
	private int numClass;
	Set<Integer> leafs =null;
	private boolean isUseTarget = true;


	public MultiLinearMax(Generator featureGen, LabelAlphabet alphabet, Tree tree,int n) {
		this.featureGen = featureGen;
		this.alphabet = alphabet;
		numThread = n;
		this.tree = tree;
		pool = Executors.newFixedThreadPool(numThread);
		
		numClass = alphabet.size();
		if(tree==null){
			leafs = alphabet.toSet();
		}else
			leafs= tree.getLeafs();
	}

	/**
	 * 
	 */
	public Results getBest(Instance inst, int n) {
		Integer target =null;
		if(isUseTarget)
			target = (Integer) inst.getTarget();

		SparseVector fv = featureGen.getVector(inst);
		
		//每个类对应的内积
		double[] sw = new double[alphabet.size()];
		Callable<Double>[] c= new Multiplesolve[numClass];
		Future<Double>[] f = new Future[numClass];
		
		for (int i = 0; i < numClass; i++) {
			c[i] = new Multiplesolve(fv,i);
			f[i] = pool.submit(c[i]);
		}

		
		
		//执行任务并获取Future对象
		for (int i = 0; i < numClass; i++){ 			
			try {
				sw[i] = (Double) f[i].get();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		Results res = new Results(n);
		if(target!=null){
			res.buildOracle();
		}
		

		

		Iterator<Integer> it = leafs.iterator();
		
		while(it.hasNext()){
			double score=0.0;
			Integer i = it.next();
			
			if(tree!=null){//计算含层次信息的内积
				ArrayList<Integer> anc = tree.getPath(i);
				for(int j=0;j<anc.size();j++){
					score += sw[anc.get(j)];
				}
			}else{
				score = sw[i];
			}
			
			//给定目标范围是，只计算目标范围的值
			if(target!=null&&target.equals(i)){
				res.addOracle(score,i);
			}else{
				res.addPred(score,i);
			}
							
		}		
		return res;
	}

	class Multiplesolve implements Callable {
		SparseVector fv;
		int idx;
		public  Multiplesolve(SparseVector fv,int i) {
			this.fv = fv;
			idx = i;
		}

		public Double call() {

			// sum up xi*wi for each class
			double score = fv.dotProduct(weights[idx]);
			return score;

		}
	}
	public void setWeight(SparseVector[] weights) {
		this.weights = weights;

	}

	public void isUseTarget(boolean b) {
		isUseTarget = b;
		
	}

	public Results getBest(Instance inst) {
		return getBest(inst, 1);
	}
}
