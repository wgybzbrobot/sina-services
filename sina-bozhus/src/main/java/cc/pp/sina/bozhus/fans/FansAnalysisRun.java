package cc.pp.sina.bozhus.fans;

import java.util.concurrent.atomic.AtomicInteger;

import cc.pp.sina.domain.bozhus.FansAnalysisResult;
import cc.pp.sina.utils.json.JsonUtils;

public class FansAnalysisRun implements Runnable {

	private final long uid;

	public FansAnalysisRun(long uid) {
		this.uid = uid;
	}

	private static AtomicInteger count = new AtomicInteger(0);

	@Override
	public void run() {

		System.out.println(count.addAndGet(1));

		FansAnalysisResult result = FansAnalysis.analysis(uid);
		if (result != null) {
			FansAnalysisInfoUtils.intsertFansAnalysisResult(uid, JsonUtils.toJsonWithoutPretty(result));
		}

	}

}
