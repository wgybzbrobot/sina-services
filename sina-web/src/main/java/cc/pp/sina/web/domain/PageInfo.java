package cc.pp.sina.web.domain;

import java.util.Collection;

/**
 * 批量结果的分页包装
 * Created by chenwei on 14-2-17.
 */
public class PageInfo {

	private int total;
	private int page;
	private int perPage;
	private Collection<?> datas;

	public PageInfo(int total, int page, int perPage, Collection<?> datas) {
		this.total = total;
		this.page = page;
		this.perPage = perPage;
		this.datas = datas;
	}

	public int getTotal() {
		return total;
	}

	public int getPage() {
		return page;
	}

	public int getPerPage() {
		return perPage;
	}

	public Collection<?> getDatas() {
		return datas;
	}

}
