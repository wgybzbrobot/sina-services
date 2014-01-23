package cc.pp.sina.web.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.pp.sina.domain.tool.TransUidData;
import cc.pp.sina.utils.json.JsonUtils;
import cc.pp.sina.utils.threads.pool.ApplyThreadPool;
import cc.pp.sina.web.application.ToolApplication;
import cc.pp.sina.web.domain.ErrorResponse;

/**
 * 用户域名转换成用户模块
 * @author wgybzb
 *
 */
public class TransUidResource extends ServerResource {

	private static Logger logger = LoggerFactory.getLogger(TransUidResource.class);

	private String identify;
	private ToolApplication application;
	private static final ThreadPoolExecutor pool = ApplyThreadPool.getThreadPoolExector();

	@Override
	public void doInit() {
		identify = (String) getRequest().getAttributes().get("identify");
		application = (ToolApplication) getApplication();
	}

	@Get("txt")
	public String getTransUidInfos() {
		if (identify == null || identify.length() == 0) {
			return "error";
		}
		logger.info("Request Url: " + getReference() + ".");
		String dataFile = ToolApplication.TRANS_UID_PATH + ToolApplication.TRANS_UID_DATA + "_" + identify;
		String resultFile = ToolApplication.TRANS_UID_PATH + ToolApplication.TRANS_UID_RESULT + "_" + identify;

		// 读取无序结果到HashMap中
		HashMap<String, String> unsortedResult = new HashMap<>();
		String str;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(resultFile)));) {
			while ((str = br.readLine()) != null) {
				unsortedResult.put(str.split(",")[0], str);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// 按顺序读取结果
		StringBuffer result = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(dataFile)));) {
			while ((str = br.readLine()) != null) {
				if (str.length() < 6) {
					result.append("     ").append("\n");
				} else {
					result.append(unsortedResult.get(str)).append("\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		//		new File(dataFile).delete();
		//		new File(resultFile).delete();

		return result.toString();
	}

	@Post("json")
	public Representation acceptData(final TransUidData transUidData) {

		pool.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("identify=" + transUidData.getIdentify() + ",dataSize=" + transUidData.getData().size());
				application.batchTrans(transUidData.getIdentify(), transUidData.getData());
			}
		});

		return new JsonRepresentation(JsonUtils.toJson(new ErrorResponse.Builder(0, getRequest().getResourceRef()
				.getIdentifier() + "/" + transUidData.getIdentify()).build()));
	}

}
