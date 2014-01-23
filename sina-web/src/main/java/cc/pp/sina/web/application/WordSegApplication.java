package cc.pp.sina.web.application;

import java.io.IOException;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.analyzer.mmseg4j.examples.Complex;
import cc.pp.sina.web.resource.WordSegResource;

public class WordSegApplication extends Application {

	private static Logger logger = LoggerFactory.getLogger(WordSegApplication.class);

	private static Complex complex;

	public WordSegApplication() {
		complex = new Complex();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/text/{text}", WordSegResource.class);
		return router;
	}

	/**
	 * 分词
	 */
	public String getWords(String text) {
		String words = "";
		try {
			words = complex.segWords(text, ",");
		} catch (IOException e) {
			logger.info("WordsSeg error.");
		}
		return words;
	}

}
