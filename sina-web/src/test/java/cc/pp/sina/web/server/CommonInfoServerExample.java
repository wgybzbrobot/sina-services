package cc.pp.sina.web.server;

import java.io.IOException;

/**
 * Created by chenwei@pp.cc on 14-1-12.
 */
public class CommonInfoServerExample {

	public static void main(String[] args) throws IOException {
		CommonInfoServer server = new CommonInfoServer();
		server.start();
		System.in.read();
		server.stop();
	}

}
