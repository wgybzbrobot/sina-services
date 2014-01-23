package cc.pp.sina.domain.content.library;

/**
 * 内容库每条内容信息（不完全）
 * @author wgybzb
 *
 */
public class ContentInfo {

	private final int cid;
	private final int tid;
	private final String content;
	private final String picture;
	private final String video;
	private final String music;

	public ContentInfo(Builder builder) {
		this.cid = builder.cid;
		this.tid = builder.tid;
		this.content = builder.content;
		this.picture = builder.picture;
		this.video = builder.video;
		this.music = builder.music;
	}

	public static class Builder {

		private int cid;
		private int tid;
		private String content = "";
		private String picture = "";
		private String video = "";
		private String music = "";

		public Builder() {
			//
		}

		public Builder setCid(int cid) {
			this.cid = cid;
			return this;
		}

		public Builder setTid(int tid) {
			this.tid = tid;
			return this;
		}

		public Builder setContent(String content) {
			this.content = content;
			return this;
		}

		public Builder setPicture(String picture) {
			this.picture = picture;
			return this;
		}

		public Builder setVideo(String video) {
			this.video = video;
			return this;
		}

		public Builder setMusic(String music) {
			this.music = music;
			return this;
		}

		public ContentInfo build() {
			return new ContentInfo(this);
		}

	}

	public int getCid() {
		return cid;
	}

	public int getTid() {
		return tid;
	}

	public String getContent() {
		return content;
	}

	public String getPicture() {
		return picture;
	}

	public String getVideo() {
		return video;
	}

	public String getMusic() {
		return music;
	}

}
