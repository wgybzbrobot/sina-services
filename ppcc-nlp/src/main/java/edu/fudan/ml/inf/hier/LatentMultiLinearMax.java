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
import java.util.Arrays;
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
public class LatentMultiLinearMax extends Inferencer {

	private static final long serialVersionUID = 1729099580633672184L;
	private LabelAlphabet alphabet;
	private Tree tree;
	int numThread;
	ExecutorService pool;
	private int numLatent;
	private SparseVector[] weights;
	private Generator featureGen;
	private int numClass;
	private int numLatentClass;
	Set<Integer> leafs =null;
	private boolean isUseTarget = true;
	
	
	public LatentMultiLinearMax(Generator featureGen, LabelAlphabet alphabet, Tree tree,int n,int numLatent) {
		this.featureGen = featureGen;
		this.alphabet = alphabet;
		numThread = n;
		this.tree = tree;
		this.numLatent = numLatent;
		pool = Executors.newFixedThreadPool(numThread);

		numClass = alphabet.size();
		numLatentClass = numClass*numLatent;

		if(tree==null){
			leafs = alphabet.toSet();
		}else
			leafs= tree.getLeafs();
	}

	/**
	 * 
	 */
	public Results getBest(Instance inst, int n) {

		Integer target = null;
		if(isUseTarget)
			target= (Integer) inst.getTarget();

		SparseVector fv = featureGen.getVector(inst);


		//每个类对应的内积
		double[] sw = new double[numLatentClass];
		Multiplesolve[] c= new Multiplesolve [numLatentClass];
		Future<Double>[] f = new Future[numLatentClass];

		for (int i = 0; i < numLatentClass; i++) {
			c[i] = new Multiplesolve(fv,i);
			f[i] = pool.submit(c[i]);
		}


		//执行任务并获取Future对象
		for (int i = 0; i < numLatentClass; i++){ 			
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
		//每个类对应的真正分配到的隐藏类
		int[] maxlatent;
		if(tree==null){
			maxlatent = new int[1];
		}else{
			maxlatent = new int[tree.getDepth()];
		}
		int[] o = null;
		int[] l = null;
		Iterator<Integer> it = leafs.iterator();
		//计算含层次信息的内积
		while(it.hasNext()){
			double score;
			Integer i = it.next();			
			if(tree!=null){//计算含层次信息的内积
				score=0.0;
				ArrayList<Integer> anc = tree.getPath(i);
				for(int j=0;j<anc.size();j++){//

					double max = Double.NEGATIVE_INFINITY;
					int start = anc.get(j)*numLatent;
					for(int k=start;k<start+numLatent;k++){
						if(sw[k]>max){
							max = sw[k];
							maxlatent[j] = k;
						}
					}
					score += max;
				}			
			}else{
				int start = i*numLatent;
				double max = Double.NEGATIVE_INFINITY;
				for(int k=start;k<start+numLatent;k++){
					if(sw[k]>max){
						max = sw[k];
						maxlatent[0] = k;
					}
				}
				score = max;
			}
			//对于标注的样本，将正确、错误预测分开

			if(target!=null&&target.equals(i)){
				int idx = res.addOracle(score,i);
				if(idx==0)
					o = Arrays.copyOf(maxlatent, maxlatent.length);
			}else{
				int idx = res.addPred(score,i);

				if(idx==0)
					l = Arrays.copyOf(maxlatent, maxlatent.length);
			}

		}
		res.other = new Object[]{o,l};
		return res;
	}



	class Multiplesolve implements Callable<Double> {
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
