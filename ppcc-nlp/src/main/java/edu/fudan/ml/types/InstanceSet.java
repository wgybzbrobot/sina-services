package edu.fudan.ml.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import edu.fudan.ml.data.Reader;
import edu.fudan.ml.data.SequenceReader;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.SeriesPipes;

/**
 * 样本集合
 * 
 * @author xpqiu
 * 
 */
public class InstanceSet extends ArrayList<Instance> {

	private static final long serialVersionUID = 3449458306217680806L;

	private Pipe pipes = null;
	private AlphabetFactory factory = null;
	public int numFeatures = 0;

	public InstanceSet(Pipe pipes) {
		this.pipes = pipes;
	}

	public InstanceSet(Pipe pipes, AlphabetFactory factory) {
		this.pipes = pipes;
		this.factory = factory;
	}

	public InstanceSet() {
	}

	public InstanceSet[] split(int i, int n) {
		// shuffled.shuffle();
		int length = this.size();
		InstanceSet[] ne = new InstanceSet[2];
		ne[0] = new InstanceSet(pipes, factory);
		ne[1] = new InstanceSet(pipes, factory);
		ne[1].addAll(subList(i * length / n, (1 + i) * length / n));
		if (i > 0) {
			ne[0].addAll(subList(0, i * length / n));
			for (int j = (1 + i) * length / n; j < length; j++) {
				ne[0].add(get(j));
			}
		} else {
			ne[0].addAll(subList((i + 1) * length / n, length));
		}
		return ne;
	}

	public void loadThruPipes(Reader reader) throws Exception {

		// 通过迭代加入样本
		while (reader.hasNext()) {
			Instance inst = reader.next();
			if (pipes != null)
				pipes.addThruPipe(inst);
			this.add(inst);
		}
	}

	/**
	 * 分步骤批量处理数据，每个Pipe处理完所有数据再进行下一个Pipe
	 * 
	 * @param reader
	 * @throws Exception
	 */
	public void loadThruStagePipes(Reader reader) throws Exception {
		SeriesPipes p = (SeriesPipes) pipes;
		// 通过迭代加入样本
		Pipe p1 = p.getPipe(0);
		while (reader.hasNext()) {
			Instance inst = reader.next();
			if(inst!=null){
				if (p1 != null)
					p1.addThruPipe(inst);
				this.add(inst);
			};
		}
		for (int i = 1; i < p.size(); i++)
			p.getPipe(i).process(this);
	}

	public void shuffle() {
		Collections.shuffle(this);
	}

	public void shuffle(Random r) {
		Collections.shuffle(this, r);
	}

	public Pipe getPipes() {
		return pipes;
	}

	public Instance getInstance(int idx) {
		if (idx < 0 || idx > this.size())
			return null;
		return this.get(idx);
	}

	public AlphabetFactory getAlphabetFactory() {
		return factory;
	}

	public void addAll(InstanceSet subset) {
		this.addAll(subset);
	}

	public void setPipes(Pipe pipes) {
		this.pipes = pipes;
	}

	public void setAlphabetFactory(AlphabetFactory factory) {
		this.factory = factory;
	}

}
