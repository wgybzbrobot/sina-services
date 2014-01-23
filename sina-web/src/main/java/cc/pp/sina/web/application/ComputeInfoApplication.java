package cc.pp.sina.web.application;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cc.pp.sina.dao.bozhus.ComputeInfo;
import cc.pp.sina.web.resource.FansAnalysisResource;

/**
 * 待废弃
 * @author wgybzb
 *
 */
public class ComputeInfoApplication extends Application {

	//	private static Logger logger = LoggerFactory.getLogger(ComputeInfoApplication.class);

	private final ComputeInfo computeInfo;

	public ComputeInfoApplication() {
		computeInfo = new ComputeInfo();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router();
		router.attach("/compute/uid/{uid}/fans", FansAnalysisResource.class);
		return router;
	}

	/**
	 * 新浪粉丝分析结果数据
	 */
	public String getFansAnalysisResult(long uid) {
		return computeInfo.getFansAnalysisResult(uid);
	}

}
