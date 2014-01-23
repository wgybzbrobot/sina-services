package cc.pp.sina.solr.beans;

import org.apache.solr.client.solrj.beans.Field;

public class UserBean {

	@Field("username")
	long id;

	@Field("screenname")
	String screen_name;

	@Field
	String name;

	@Field
	int province;

	@Field
	int city;

	@Field
	String location;

	@Field
	String description;

	@Field
	String url;

	@Field("profileimageurl")
	String profile_image_url;

	@Field
	String domain;

	@Field
	String gender;

	@Field("followerscount")
	int followers_count;

	@Field("friendscount")
	int friends_count;

	@Field("statusescount")
	int statuses_count;

	@Field("favouritescount")
	int favourites_count;

	@Field("createdat")
	long created_at;

	@Field
	boolean verified;

	@Field("verifiedtype")
	int verified_type;

	@Field("avatarlarge")
	String avatar_large;

	@Field("bifollowerscount")
	int bi_followers_count;

	@Field
	String remark;

	@Field("verifiedreason")
	String verified_reason;

	@Field
	String weihao;

	@Field
	long lasttime;

}
