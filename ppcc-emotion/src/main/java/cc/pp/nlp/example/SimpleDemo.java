package cc.pp.nlp.example;

import java.io.IOException;

import cc.pp.mmseg4j.Seg;
import cc.pp.mmseg4j.SimpleSeg;
import cc.pp.nlp.api.Complex;

/**
 *
 * @author chenlb 2009-3-14 上午12:38:40
 */
public class SimpleDemo extends Complex {

	@Override
	protected Seg getSeg() {
		return new SimpleSeg(dic);
	}

	public static void main(String[] args) throws IOException {
		new SimpleDemo().run(args);
	}

}
