package cc.pp.sina.bozhus.weibos;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;

public class UserAllWeibosParamsTest {

	@Ignore
	public void testrandWeibos() {

		List<String> temp = new ArrayList<String>();
		temp.add("1111");
		temp.add("2222");
		temp.add("3333");
		temp.add("4444");
		temp.add("5555");
		temp.add("6666");
		temp.add("7777");
		temp.add("8888");
		temp.add("9999");
		temp.add("0000");
		System.out.println(UserAllWeibosParams.randWeibos(temp, 8));
	}

}
