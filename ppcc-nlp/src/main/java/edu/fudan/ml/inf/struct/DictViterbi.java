package edu.fudan.ml.inf.struct;

import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.inf.struct.LinearViterbi.Node;
import edu.fudan.ml.types.Instance;

public final class DictViterbi extends LinearViterbi {

	private static final long serialVersionUID = 3111865714013105529L;

	public DictViterbi(TempletGroup templets, int ysize) {
		super(templets, ysize);
		this.orders = templets.getOrders();
	}

	public DictViterbi(LinearViterbi viterbi) {
		this(viterbi.templets, viterbi.ysize);
		this.weights = viterbi.getWeights();
	}

	protected Node[][] initialLattice(Instance carrier) {
		int[][] data = (int[][]) carrier.getData();

		int[] tempData = (int[]) carrier.getTempData();

		int length = data.length;

		Node[][] lattice = buildLattice(length);

		for (int l = 0; l < length; l++) {
			
			for (int c = 0; c < ysize; c++) {
				
				boolean skip = true;
				if (tempData[l] == c || tempData[l] == -1)
					skip = false;
				
				if (skip) {
					lattice[l][c].score = Double.NEGATIVE_INFINITY;
				}
				
				for (int i = 0; i < orders.length; i++) {
					if (data[l][i] == -1)
						continue;
					if (orders[i] == 0 && !skip) {
						lattice[l][c].score += weights[data[l][i] + c];
					} else if (l > 0 && orders[i] == 1) {
						for (int p = 0; p < ysize; p++) {
							int offset = p * ysize + c;
							lattice[l][c].trans[p] += weights[data[l][i]
									+ offset];
						}
					}
				}
			}
		}

		return lattice;
	}

}
