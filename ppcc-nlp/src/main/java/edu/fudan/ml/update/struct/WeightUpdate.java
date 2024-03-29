package edu.fudan.ml.update.struct;

import java.io.Serializable;

import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.update.Update;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntIterator;

/**
 * @deprecated
 * @author feng
 * 
 */
public class WeightUpdate implements Serializable, Update {

	private static final long serialVersionUID = 4198902147638417425L;
	TempletGroup templets;
	/**
	 * 模板个数
	 */
	int numTemplets;
	/**
	 * label个数
	 */
	int numLabels;
	/**
	 * 是否用损失函数
	 */
	private boolean useLoss = false;

	public WeightUpdate(TempletGroup templets, int numLabels, boolean useLoss2) {
		this.templets = templets;
		this.numTemplets = templets.size();
		this.numLabels = numLabels;
		this.useLoss = useLoss2;
	}

	public int update(Instance inst, double[] weights, Object predictLabel,
			double c) {
		return update(inst, weights, predictLabel, null, c);
	}

	/**
	 * data 每个元素为特征空间索引位置 1 ... T T列(模板个数) 1 N行(序列长度) 2 . data[r][t] N
	 * 第t个模板作用在第r个位置 得到feature的起始位置
	 * 
	 * target[r],predict[r] label的编号
	 * 
	 * @param c
	 * @param weights
	 */
	public int update(Instance inst, double[] weights, Object predictLabel,
			Object goldenLabel, double c) {
		int[][] data = (int[][]) inst.getData();
		int[] target;
		if (goldenLabel == null)
			target = (int[]) inst.getTarget();
		else
			target = (int[]) goldenLabel;
		int[] predict = (int[]) predictLabel;
		// 当前clique中不同label的个数
		int ne = 0;
		/**
		 * 偏移索引
		 * 
		 */
		int tS = 0, pS = 0;

		double diffW = 0;

		int loss = 0;

		int L = data.length;
		// 稀疏矩阵表示(f(x,y)-f(x,\bar{y}))
		TIntIntHashMap diffF = new TIntIntHashMap(); // 最多有2*L*numTemplets个不同

		for (int o = -templets.maxOrder - 1, l = 0; l < L; o++, l++) {
			tS = tS * numLabels % templets.numStates + target[l]; // 目标值：计算当前状态组合的y空间偏移
			pS = pS * numLabels % templets.numStates + predict[l];// 预测值：计算当前状态组合的y空间偏移
			if (predict[l] != target[l])
				ne++;
			if (o >= 0 && (predict[o] != target[o]))
				ne--; // 减去移出clique的节点的label差异

			if (ne > 0) { // 当前clique有不相同label
				loss++; // L(y,ybar)
				for (int t = 0; t < numTemplets; t++) {
					if (data[l][t] == -1)
						continue;
					int tI = data[l][t] + templets.offset[t][tS]; // 特征索引：找到对应weights的维数
					int pI = data[l][t] + templets.offset[t][pS]; // 特征索引：找到对应weights的维数
					if (tI != pI) {
						diffF.adjustOrPutValue(tI, 1, 1);
						diffF.adjustOrPutValue(pI, -1, -1);
						diffW += weights[tI] - weights[pI]; // w^T(f(x,y)-f(x,ybar))
					}
				}
			}
		}

		double diff = 0;
		TIntIntIterator it = diffF.iterator();
		for (int i = diffF.size(); i-- > 0;) {
			it.advance();
			diff += it.value() * it.value();
		}
		it = null;
		double alpha;
		double delta;
		if (useLoss) {
			delta = loss;
		} else
			delta = 1;
		if (diffW <= delta) {

			tS = 0;
			pS = 0;
			ne = 0;
			alpha = (delta - diffW) / diff;
			// System.out.println(alpha);
			alpha = Math.min(c, alpha);
			it = diffF.iterator();
			for (int i = diffF.size(); i-- > 0;) {
				it.advance();
				weights[it.key()] += it.value() * alpha;
			}

			return 1;
		} else {
			return 0;
		}
	}

}
