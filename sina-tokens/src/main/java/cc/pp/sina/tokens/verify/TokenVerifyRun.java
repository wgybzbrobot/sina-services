package cc.pp.sina.tokens.verify;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenVerifyRun implements Runnable {

	private final String token;
	private final List<String> tokens;
	private final List<String> verifiedToken;

	private static AtomicInteger count = new AtomicInteger(0);

	public TokenVerifyRun(String token, List<String> tokens, List<String> verifiedToken) {
		this.token = token;
		this.tokens = tokens;
		this.verifiedToken = verifiedToken;
	}

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		try {
			if (TokenVerify.getTokenExpirein(token) > 0) {
				verifiedToken.add(token);
			}
		} catch (Exception e) {
			tokens.remove(token);
		}
	}

}
