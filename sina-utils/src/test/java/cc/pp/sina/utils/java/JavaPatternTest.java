package cc.pp.sina.utils.java;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JavaPatternTest {

	@Test
	public void testAllNums() {
		assertTrue(JavaPattern.isAllNum("1234569870"));
	}

	@Test
	public void testNotAllNums() {
		assertFalse(JavaPattern.isAllNum("123@456%d987s0"));
	}

	@Test
	public void testAllNotNums() {
		assertFalse(JavaPattern.isAllNum("shdjjdfn^@dd"));
	}

}
