package cc.pp.sina.web.resource;

import cc.pp.sina.bozhus.single.weibo.Status;
import cc.pp.sina.bozhus.single.weibo.Statuses;
import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 14-2-14.
 */
public class WeibosResource extends ServerResource {

	@Get
	public Statuses getWeibo() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String ids = form.getFirstValue("ids");
		String[] idsStr = ids.split(",");
		long[] idsLong = new long[idsStr.length];
		for (int i = 0; i < idsLong.length; i++) {
			idsLong[i] = Long.valueOf(idsStr[i]);
		}
		return Statuses.showBatch(idsLong);
	}

}
