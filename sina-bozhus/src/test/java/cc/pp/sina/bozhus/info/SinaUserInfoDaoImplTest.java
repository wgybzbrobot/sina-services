package cc.pp.sina.bozhus.info;

import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Ignore;
import org.junit.Test;

import com.sina.weibo.api.Friendships;
import com.sina.weibo.api.Tags;
import com.sina.weibo.model.TagWapper;
import com.sina.weibo.model.WeiboException;

public class SinaUserInfoDaoImplTest {

	@Ignore
	public void testGetSinaUsersBatchTags() throws WeiboException {
		Tags tags = new Tags();
		List<TagWapper> tagWapper = tags.getTagsBatch("1704103183,1862087393", "2.00WN9bLBdcZIJC9a1bda5ccbHswkuB");
		System.out.println(JSONArray.fromObject(tagWapper));
	}

	@Ignore
	@Test
	public void testGetSinaUserFansIds() throws WeiboException {
		Friendships friendships = new Friendships();
		String[] followers = friendships.getFollowersIdsById("2886541382", 5000, 0, "2.00PkcTeCdcZIJCfb02525ab7FmqncB");
		System.out.println(Arrays.asList(followers));
	}

}
