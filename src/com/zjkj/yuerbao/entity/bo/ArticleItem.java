package com.zjkj.yuerbao.entity.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjkj.yuerbao.common.preference.ZJKJConstant;
import com.zjkj.yuerbao.common.util.FileUtils;
import com.zjkj.yuerbao.entity.bo.ArticleContent.ArticleType;
import com.zjkj.yuerbao.entity.po.Article;

/**
 * 处理文章内容的业务类
 * 
 * @author gxy
 * 
 */
public class ArticleItem {

	private long index = 0;

	public List<Article> getArticleItem(int currentPage) {
		// TODO Auto-generated method stub
		List list = new ArrayList();

		index = currentPage * 25;

		String articlesUrl = ZJKJConstant.ARTICLES_URL + index;

		String htmlStr = FileUtils.inputStreamToString(articlesUrl);
		JSONObject obj = JSONObject.parseObject(htmlStr);
		JSONArray articles = JSONArray.parseArray(obj.getString("root"));

		String articleUrl = ZJKJConstant.ARTICLE_INFO_URL;
		for (Object jsonObj : articles) {

			JSONObject articleJson = (JSONObject) jsonObj;

			// String articleStr =
			// FileUtils.inputStreamToStringByPost(articleUrl
			// + articleJson.getString("id"));
			// 文章信息
			// JSONObject articleJson1 = JSONObject.parseObject(articleStr);
			// String content = articleJson1.getString("content");
			// Document doc = Jsoup.parse(content);
			// Elements tagP = doc.getElementsByTag("p");

			Article article = new Article(articleJson.getInteger("id"),
					articleJson.getString("arges"),
					articleJson.getString("period"),
					articleJson.getInteger("dayNum"),
					articleJson.getString("classify"),
					articleJson.getString("title"),
					articleJson.getString("content"),
					// tagP.get(1).html(),
					articleJson.getString("images"),
					// tagP.get(0).attr("src"),
					articleJson.getDate("lastUpdate"));

			list.add(article);
		}
		return list;
	}

	public List<ArticleContent> getArticleDetail(Integer id) {

		List<ArticleContent> list = new ArrayList<ArticleContent>();

		String urlPath = ZJKJConstant.ARTICLE_INFO_URL + id;

		String htmlStr = FileUtils.inputStreamToString(urlPath);

		JSONObject articleJson = JSONObject.parseObject(htmlStr);

		String content = articleJson.getString("content");
		Document doc = Jsoup.parse(content);
		Elements tagP = doc.getElementsByTag("p");

		// 组织文章详情数据
		String title = articleJson.getString("title");

		String lastUpdateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(articleJson.getDate("lastUpdate"));

		String imgLinkUrl = tagP.get(0).getElementsByTag("img").attr("src");

		String contentStr = tagP.html();
		String beginStr = "/>";
		String endStr = "<br />";
		int contentStrLength = contentStr.length();
		int beginIndex = contentStr.indexOf(beginStr) + 2;
		int endIndex = 0;
		if (contentStr.endsWith(endStr)) {
			endIndex = contentStrLength - endStr.length();
			contentStr = contentStr.substring(beginIndex, endIndex);
		} else {
			contentStr = contentStr.substring(beginIndex, contentStrLength);
		}

		ArticleContent aTitle = new ArticleContent(ArticleType.TITLE, title);
		list.add(aTitle);
		ArticleContent aLastUpdate = new ArticleContent(ArticleType.LASTUPDATE,
				lastUpdateStr);
		list.add(aLastUpdate);
		ArticleContent aImg = new ArticleContent(ArticleType.IMG, imgLinkUrl);
		list.add(aImg);
		contentStr.replaceAll("<br />", "");
		contentStr.replaceAll("<br/>", "");
		ArticleContent aContent = new ArticleContent(ArticleType.CONTENT,
				contentStr);

		list.add(aContent);
		return list;

	}
}
