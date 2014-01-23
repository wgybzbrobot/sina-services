package cc.pp.sina.bozhus.single.weibo;

import java.util.Date;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import cc.pp.sina.utils.json.JsonUtils;

/**
 * User: chenwei@pp.cc
 * Date: 14-1-12
 * Time: 下午4:28.
 */
public class Status extends BaseSinaApi {

	private Date created_at;
	private long id;
	private String mid;
	private String idstr;
	private String text;
	private String source;
	private Boolean favorited;
	private Boolean truncated;
	private String in_reply_to_status_id;
	private String in_reply_to_user_id;
	private String in_reply_to_screen_name;
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;
	private Object geo;
	private Object user; // TODO 换成确定的类型
	private Object retweeted_status;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;
	private int mlevel;
	private Object visible;
	private Object pic_urls;
	private Object ad;

	public static long queryId(String mid) {
		final String resource = "/2/statuses/queryid.json";
		HttpGet httpget = new HttpGet(String.format("%s%s?access_token=%s&mid=%s&type=1&isBase62=1", server, resource, tokenService.getRandomToken(), mid));
		try (CloseableHttpResponse response = httpclient.execute(httpget)) {
			Map<?, ?> map = JsonUtils.getObjectMapper().readValue(response.getEntity().getContent(), Map.class);
			return Long.valueOf((String) map.get("id"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Status show(long id) {
		final String resource = "/2/statuses/show.json";
		HttpGet httpget = new HttpGet(String.format("%s%s?access_token=%s&id=%d", server, resource, tokenService.getRandomToken(), id));
		try (CloseableHttpResponse response = httpclient.execute(httpget)) {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return JsonUtils.getObjectMapper().readValue(response.getEntity().getContent(), Status.class);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public Object getAd() {
		return ad;
	}

	public void setAd(Object ad) {
		this.ad = ad;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public void setBmiddle_pic(String bmiddle_pic) {
		this.bmiddle_pic = bmiddle_pic;
	}

	public int getComments_count() {
		return comments_count;
	}

	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Boolean getFavorited() {
		return favorited;
	}

	public void setFavorited(Boolean favorited) {
		this.favorited = favorited;
	}

	public Object getGeo() {
		return geo;
	}

	public void setGeo(Object geo) {
		this.geo = geo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public String getIn_reply_to_status_id() {
		return in_reply_to_status_id;
	}

	public void setIn_reply_to_status_id(String in_reply_to_status_id) {
		this.in_reply_to_status_id = in_reply_to_status_id;
	}

	public String getIn_reply_to_user_id() {
		return in_reply_to_user_id;
	}

	public void setIn_reply_to_user_id(String in_reply_to_user_id) {
		this.in_reply_to_user_id = in_reply_to_user_id;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public int getMlevel() {
		return mlevel;
	}

	public void setMlevel(int mlevel) {
		this.mlevel = mlevel;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public void setOriginal_pic(String original_pic) {
		this.original_pic = original_pic;
	}

	public Object getPic_urls() {
		return pic_urls;
	}

	public void setPic_urls(Object pic_urls) {
		this.pic_urls = pic_urls;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}

	public Object getRetweeted_status() {
		return retweeted_status;
	}

	public void setRetweeted_status(Object retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public void setThumbnail_pic(String thumbnail_pic) {
		this.thumbnail_pic = thumbnail_pic;
	}

	public Boolean getTruncated() {
		return truncated;
	}

	public void setTruncated(Boolean truncated) {
		this.truncated = truncated;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public Object getVisible() {
		return visible;
	}

	public void setVisible(Object visible) {
		this.visible = visible;
	}

}
