package cc.pp.sina.tokens.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class TokenService {

	private static Logger logger = LoggerFactory.getLogger(TokenService.class);
	private final TokenJDBC tokenJDBC;
	private final Random random = new Random();
	private volatile List<String> sinaTokens;

	public TokenService() {
		Properties prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("tokens_db.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tokenJDBC = new TokenJDBC(prop.getProperty("db.token.driver"), prop.getProperty("db.token.url"), prop.getProperty("db.token.user"), prop.getProperty("db.token.password"));
		refresh();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					refresh();
				} catch (Exception e) {
					logger.warn("Unknown exception.", e);
				}
			}
		}, 1000 * 30, 1000 * 30);
	}

	public TokenService(List<String> testTokens) {
		sinaTokens = testTokens;
		tokenJDBC = null;
	}

	public TokenService(String driver, String url, String user, String password) {
		tokenJDBC = new TokenJDBC(driver, url, user, password);
		refresh();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					refresh();
				} catch (Exception e) {
					logger.warn("Unknown exception.", e);
				}
			}
		}, 1000 * 30, 1000 * 30);
	}

	public static void main(String[] args) {
		TokenService service = new TokenService();
		System.out.println("ok");
		System.out.println("sinaTokens: " + service.sinaTokens.size());
		service.stop();
	}

	/**
	 * 删除无效token
	 */
	public void deleteInValidToken(String token) {
		sinaTokens.remove(token);
	}

	/**
	 * 随机获取单个token
	 */
	public String getRandomToken() {
		return sinaTokens.get(random.nextInt(sinaTokens.size()));
	}

	public List<String> getRandomTokens() {
		return sinaTokens;
	}

	/**
	 * 获取皮皮用户列表：username
	 */
	public List<Long> getSinaUids() {
		return tokenJDBC.getSinaUids();
	}

	public void setSinaTokens(List<String> sinaTokens) {
		this.sinaTokens = sinaTokens;
	}

	public void stop() {
		tokenJDBC.stop();
	}

	private void refresh() {
		logger.debug("TokenService refresh...");
		long start = System.currentTimeMillis();
		sinaTokens = tokenJDBC.getRandom(10000);
		logger.debug("refresh result: tokenCount={}, execTime={}", sinaTokens.size(), System.currentTimeMillis() - start);
	}

}
