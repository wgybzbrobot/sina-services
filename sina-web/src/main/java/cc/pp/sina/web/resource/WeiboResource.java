package cc.pp.sina.web.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import cc.pp.sina.bozhus.single.weibo.Status;

/**
 * Created by chenwei@pp.cc on 14-1-12.
 */
public class WeiboResource extends ServerResource {

	private long wid;

	@Override
	protected void doInit() throws ResourceException {
		wid = Long.valueOf((String) getRequest().getAttributes().get("wid"));
	}

	@Get
	public Status getWeibo() {
		return Status.show(wid);
	}

}
