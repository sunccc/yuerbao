package com.zjkj.yuerbao.entity.bo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文章管理
 * 
 * @author zhb
 */
public class ArticleContent {

	// @Column(id = true, autoIncrement = true, order = OrderType.DESC, comment
	// = "序号")
	private Integer id;
	private int articleType;

	// @Column(notNull = true, comment = "标题")
	private String title;

	// @Column(notNull = true, type = ColumnType.MEDIUMTEXT, comment = "内容")
	private String content;

	// @Column(length = 100, comment = "图片")
	private String imgLink;

	// @Column(type = ColumnType.DATETIME, comment = "最后更新时间")
	private String lastUpdate;

	public ArticleContent(int articleType, String value) {
		super();
		switch (articleType) {
		case ArticleType.TITLE:
			this.title = value;
			break;
		case ArticleType.LASTUPDATE:
			this.lastUpdate = value;
			break;
		case ArticleType.IMG:
			this.imgLink = value;
			break;
		case ArticleType.CONTENT:
			this.content = value;
			break;
		}
		this.articleType = articleType;
	}

	public Integer getId() {
		return id;
	}

	public int getArticleType() {
		return articleType;
	}

	public void setArticleType(int articleType) {
		articleType = articleType;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String toString(int articleType) {
		switch (articleType) {
		case ArticleType.TITLE:
			return this.title;
		case ArticleType.LASTUPDATE:
			return this.lastUpdate;
		case ArticleType.IMG:
			return this.imgLink;
		case ArticleType.CONTENT:
			return this.content;
		}

		return null;
	};

	public static abstract interface ArticleType {
		public static final int TITLE = 1;
		public static final int LASTUPDATE = 2;
		public static final int IMG = 3;
		public static final int CONTENT = 4;
	}
}
