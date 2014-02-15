package cc.pp.sina.bozhus.single.weibo;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;

public class StatusTest {

	@Ignore
	public void testShow() throws Exception {
		Status status = Status.show(3657970825260207L);
		assertEquals("我的口罩到了", status.getText());
	}

	@Ignore
	public void testShowBatch() {
		Statuses statuses = Statuses.showBatch(new long[] { 3657970825260207L, 3665123979923029L });
		assertEquals(2, statuses.getTotal_number());
		assertEquals("我的口罩到了", statuses.getStatuses().get(0).getText());
	}

	@Ignore
	public void testQueryId() {
		assertEquals(3657970825260207L, Status.queryId("Aok5km4q3"));
	}

}
