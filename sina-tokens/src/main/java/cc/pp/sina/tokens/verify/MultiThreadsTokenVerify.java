package cc.pp.sina.tokens.verify;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有线程安全问题
 * @author wgybzb
 *
 */
public class MultiThreadsTokenVerify {

	private static Logger logger = LoggerFactory.getLogger(MultiThreadsTokenVerify.class);

	/**
	 * 多线程验证token
	 */
	public static List<String> verifyAllTokens(List<String> tokens) throws InterruptedException {

		final ThreadPoolExecutor pool = getThreadPoolExector();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				pool.shutdown();
			}
		}));

		List<String> verifiedToken = new ArrayList<String>();

		for (String token : tokens) {
			if (!pool.isShutdown()) {
				try {
					pool.execute(new TokenVerifyRun(token, tokens, verifiedToken));
				} catch (Exception e) {
					logger.error("Thread exception: " + Thread.currentThread().getName(), e);
				}
			}
		}

		pool.shutdown();
		pool.awaitTermination(30, TimeUnit.SECONDS);

		return verifiedToken;
	}

	/**
	 * 申请线程池
	 */
	private static ThreadPoolExecutor getThreadPoolExector() {

		final ThreadPoolExecutor result = new ThreadPoolExecutor(64, 64, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(128), new ThreadPoolExecutor.CallerRunsPolicy());
		result.setThreadFactory(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(Thread t, Throwable e) {
						e.printStackTrace();
						logger.error("Thread exception: " + t.getName(), e);
						result.shutdown();
					}

				});
				return t;
			}
		});

		return result;
	}

}
