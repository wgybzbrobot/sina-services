package cc.pp.nlp.example;

import java.io.IOException;

import cc.pp.mmseg4j.MaxWordSeg;
import cc.pp.mmseg4j.Seg;
import cc.pp.nlp.api.Complex;

public class MaxWordDemo extends Complex {

	@Override
	protected Seg getSeg() {
		return new MaxWordSeg(dic);
	}

	public static void main(String[] args) throws IOException {
		new MaxWordDemo().run(args);
	}
}
