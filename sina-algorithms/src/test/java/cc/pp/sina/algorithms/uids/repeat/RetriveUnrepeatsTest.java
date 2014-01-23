package cc.pp.sina.algorithms.uids.repeat;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

public class RetriveUnrepeatsTest {

	private static String oldUids = "111111,222222,333333,444444,555555,666666";
	private static String[] newUids = { "222222", "444444", "666666", "777777", "888888" };

	private String newFans;
	private String oldFans;

	@Before
	public void prepared() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("new_fans"),
				"utf-8"));
		newFans = br.readLine();
		br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("old_fans"), "utf-8"));
		oldFans = br.readLine();
	}

	@Test
	public void testUnRepeatUids() {
		assertEquals("777777,888888", RetriveUnrepeats.unRepeatUids(oldUids, newUids));
	}

	public void testContactUids() {
		assertEquals("111111,222222,333333,444444,555555,666666,777777,888888",
				RetriveUnrepeats.contactUids(oldUids, newUids));
	}

	@Test
	public void testStressUnRepeatUids() throws Exception {
		assertEquals(676, oldFans.split(",").length);
		assertEquals(5000, newFans.split(",").length);
		assertEquals(5000, RetriveUnrepeats.unRepeatUids(oldFans, newFans.split(",")).split(",").length);
	}

	@Test
	public void testStressContactUids() throws Exception {
		assertEquals(5676, RetriveUnrepeats.contactUids(oldFans, newFans.split(",")).split(",").length);
	}

}
