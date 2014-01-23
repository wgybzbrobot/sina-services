package edu.fudan.ml.inf.struct;

import java.util.Arrays;

import edu.fudan.ml.feature.templet.Templet;
import edu.fudan.ml.feature.templet.TempletGroup;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.Results;

/**
 * 修改成可并行版本 2011.9.15
 * @author xpqiu
 *
 */
public class LinearViterbi extends AbstractViterbi {

	private static final long serialVersionUID = -8237762672065700553L;

	protected int ysize;
	protected int[] orders;
	protected TempletGroup templets;

	public LinearViterbi(TempletGroup templets, int ysize) {
		this.ysize = ysize;
		this.templets = templets;
	}

	public Results<int[]> getBest(Instance carrier, int nbest)	{

		Node[][] node = initialLattice(carrier);

		doForwardViterbi(node, carrier);

		Results<int[]> res = getPath(node, nbest);

		return res;
	}

	protected Node[][] initialLattice(Instance carrier) {
		int[][] data = (int[][]) carrier.getData();
		
		int length = data.length;
		
		Node[][] lattice = buildLattice(length);

		for (int l = 0; l < length; l++) {
			for (int c = 0; c < ysize; c++) {
				for (int i = 0; i < templets.size(); i++) {
					Templet t = templets.get(i);
					if (data[l][i] == -1)
						continue;
					if (t.getOrder() == 0) {
						lattice[l][c].score += weights[data[l][i] + c];
					} else if (l > 0 && t.getOrder() == 1) {
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

	protected Node[][] buildLattice(int length) {
		Node[][] lattice = new Node[length][];
		for (int i = 0; i < length; i++) {
			lattice[i] = new Node[ysize];
			for (int j = 0; j < ysize; j++) {
				lattice[i][j] = new Node(ysize);
			}
		}
		return lattice;
	}

	protected void doForwardViterbi(Node[][] lattice, Instance carrier) {
		for (int l = 1; l < lattice.length; l++) {
			for (int c = 0; c < ysize; c++) {
				double bestScore = Double.NEGATIVE_INFINITY;
				int bestPath = -1;
				for (int p = 0; p < ysize; p++) {
					double score = lattice[l - 1][p].score + lattice[l][c].score;
					score += lattice[l][c].trans[p];
					if (score > bestScore) {
						bestScore = score;
						bestPath = p;
					}
				}
				lattice[l][c].addScore(bestScore, bestPath);
			}
		}
	}

	protected Results<int[]> getPath(Node[][] lattice,int nbest) {

		Results<int[]> res = new Results<int[]>(nbest);
		if(lattice.length==0)
			return res;
		double max = Double.NEGATIVE_INFINITY;
		int cur = 0;
		for (int c = 0; c < ysize; c++) {
			if (lattice[lattice.length - 1][c].score > max) {
				max = lattice[lattice.length - 1][c].score;
				cur = c;
			}
		}

		for (int n = 0; n < nbest; n++) {
			int[] path = new int[lattice.length];
			Arrays.fill(path, -1);
			path[lattice.length-1] = cur;
			for (int l = lattice.length - 1; l > 0; l--) {
				cur = lattice[l][cur].prev;
				path[l-1] = cur;
			}
			res.addPred(max, path);
		}

		return res;
	}

	public final class Node {

		double score = 0;
		private int prev = -1;
		double[] trans = null;

		public Node(int n) {
			score = 0;
			prev = -1;
			trans = new double[n];
		}

		public void addScore(double score, int path) {
			this.score = score;
			this.prev = path;
		}

		public void clear() {
			score = 0;
			prev = -1;
			Arrays.fill(trans, 0);
		}

	}

}
