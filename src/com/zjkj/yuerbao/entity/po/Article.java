package com.zjkj.yuerbao.entity.po;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文章管理
 * 
 * @author zhb
 */
public class Article {

	// @Column(id = true, autoIncrement = true, order = OrderType.DESC, comment
	// = "序号")
	private Integer id;

	// @Column(comment = "年龄段,0:孕期、1：产后")
	private String ages;

	// @Column(comment = "阶段，孕期以周记，产后以月记")
	private String period;

	// @Column(comment = "天数")
	private Integer dayNum;

	// @Column(length = 5, comment = "分类")
	private String classify;

	// @Column(notNull = true, comment = "标题")
	private String title;

	// @Column(notNull = true, type = ColumnType.MEDIUMTEXT, comment = "内容")
	private String content;

	// @Column(length = 100, comment = "图片")
	private String imgLink;

	// @Column(type = ColumnType.DATETIME, comment = "最后更新时间")
	private Date lastUpdate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAges() {
		return ages;
	}

	public void setAges(String ages) {
		this.ages = ages;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Integer getDayNum() {
		return dayNum;
	}

	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
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

	public String getContentSubStr() {
		return content.substring(0, 50);
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public String getUpdateTime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		sdf.format(lastUpdate);
		return sdf.format(lastUpdate);
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public String toString() {
		return "文章   [id=" + id + ", title=" + title + ", imgLink=" + imgLink
				+ ", lastUpdate=" + lastUpdate + ", content=" + content
				+ ", classify=" + classify + "]";
	}

	public Article(Integer id, String ages, String period, Integer dayNum,
			String classify, String title, String content, String imgLink,
			Date lastUpdate) {
		super();
		this.id = id;
		this.ages = ages;
		this.period = period;
		this.dayNum = dayNum;
		this.classify = classify;
		this.title = title;
		this.content = content;
		this.imgLink = imgLink;
		this.lastUpdate = lastUpdate;
	}

}
