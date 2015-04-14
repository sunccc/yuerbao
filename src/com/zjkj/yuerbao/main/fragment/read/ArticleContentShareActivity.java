package com.zjkj.yuerbao.main.fragment.read;

import java.util.ArrayList;

import org.jsoup.helper.StringUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.ConstantsAPI.WXApp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.ReAddActivity;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.preference.TencentData;
import com.zjkj.yuerbao.common.preference.WeiBoData;
import com.zjkj.yuerbao.common.preference.WeiChatData;
import com.zjkj.yuerbao.common.util.FileUtils;
import com.zjkj.yuerbao.common.util.weixin.WXUtils;
import com.zjkj.yuerbao.wxapi.WXEntryActivity;

public class ArticleContentShareActivity extends Activity {
	private IWXAPI api;
	private Bundle extras;
	private String title;
	private String lastUpdate;
	private String imgLinkUrl;
	private String content;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		System.out.println("开始");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("重启");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("恢复");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("暂停");
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println("停止");
		super.onStop();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		System.out.println("结束");
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("销毁");
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 初始化分享数据
		extras = getIntent().getBundleExtra("articleContent");
		title = extras.getString("title");
		lastUpdate = extras.getString("lastUpdate");
		imgLinkUrl = extras.getString("imgLinkUrl");
		content = extras.getString("content");

		api = WXAPIFactory.createWXAPI(this, WeiChatData.APP_ID);
		WXEntryActivity.weiboShareSDK = WeiboShareSDK.createWeiboAPI(this,
				WeiBoData.APP_KEY);
		WXEntryActivity.mTencent = Tencent.createInstance(TencentData.APP_ID,
				this);

		setContentView(R.layout.article_content_share);

		TextView cancelText = (TextView) findViewById(R.id.tv_cancel);
		cancelText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	/**
	 * 
	 * TODO 分享到微信用户
	 * 
	 * @param view
	 * 
	 * @return void
	 *
	 */
	public void shareWeiChatFriend(View view) {
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = title;
		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		msg.title = title;
		msg.description = content;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		// SendMessageToWX.Req req = new SendMessageToWX.Req().type=2
		// 调用api接口发送数据到微信
		api.sendReq(req);
		finish();

	}

	/**
	 *
	 * TODO 分享到朋友圈
	 * 
	 * @param view
	 *            视图
	 * @return void
	 *
	 */
	public void shareFriendsRound(View view) {
		// 初始化一个WXTextObject对象
		WXTextObject textObj = new WXTextObject();
		textObj.text = title;

		// 用WXTextObject对象初始化一个WXMediaMessage对象
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// 发送文本类型的消息时，title字段不起作用
		msg.title = title;
		msg.description = content;

		// 构造一个Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		// req.scene = SendMessageToWX.Req.WXSceneFavorite;

		// 调用api接口发送数据到微信
		api.sendReq(req);
		finish();
	}

	/**
	 *
	 * TODO 生成一个请求的唯一标识字段
	 * 
	 * @param type
	 *            类型
	 * @return String
	 *
	 */
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/**
	 * 
	 * TODO 分享到QQ
	 * 
	 * @param view
	 * @return void
	 */

	public void shareQQ(View view) {
		Bundle params = new Bundle();
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content.substring(0, 50));
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.zhijiankeji.com.cn/");
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgLinkUrl);
		// params.putString(
		// shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE ?
		// QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL
		// : QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText()
		// .toString());
		// params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName.getText()
		// .toString());

		// 必须的
		// if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
		// params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,
		// imageUrl.getText().toString());
		// } else {
		// params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
		// imageUrl.getText().toString());
		// }
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		// params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
		// QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);

		doShareToQQ(this, params);
		finish();
	}

	/**
	 * 用异步方式启动QQZone分享
	 * 
	 * @param params
	 */
	private void doShareToQQ(final Activity activity, final Bundle params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				WXEntryActivity.mTencent.shareToQQ(activity, params,
						qqShareListener);
			}
		}).start();
		finish();
	}

	/**
	 * 
	 * TODO 分享到QQZone
	 * 
	 * @param view
	 *            void
	 */
	public void shareQQZone(View view) {
		Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
				content.substring(0, 49));
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.zhijiankeji.com.cn/");
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(imgLinkUrl);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

		doShareToQzone(this, params);
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			System.out.println("分享取消");
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub

			System.out.println("分享成功");
			System.out.println(response.toString());
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			System.out.println("出错");
		}
	};

	/**
	 * 用异步方式启动QQZone分享
	 * 
	 * @param params
	 */
	private void doShareToQzone(final Activity activity, final Bundle params) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				WXEntryActivity.mTencent.shareToQzone(activity, params,
						qqShareListener);
			}
		}).start();
		finish();
	}

	/**
	 * 
	 * TODO 分享到新浪微博
	 * 
	 * @param view
	 *            void
	 */
	public void shareSinaWeiBo(View view) {

		sendMultiMessage(true, true, false, false, false, false);
	}

	/**
	 * 
	 * TODO 新浪微博分享文章
	 * 
	 * @return TextObject
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.title = title;
		textObject.text = content.substring(0, 140);
		return textObject;
	}

	/**
	 * 
	 * TODO 新浪微博分享图片
	 * 
	 * @return ImageObject
	 */
	private ImageObject getImgObj() {
		// TODO Auto-generated method stub

		ImageObject imageObject = new ImageObject();

		BitmapDrawable bitmapDrawable = null;
		try {
			bitmapDrawable = new BitmapDrawable(
					FileUtils.getInputStream(imgLinkUrl));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageObject.setImageObject(bitmapDrawable.getBitmap());

		return imageObject;
	}

	/**
	 * TODO 分享到新浪微博
	 * 
	 * @param hasText
	 * @param hasImage
	 * @param hasWebpage
	 * @param hasMusic
	 * @param hasVideo
	 * @param hasVoice
	 *            void
	 */
	// 只能上传140个字
	private void sendMultiMessage(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo,
			boolean hasVoice) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();// 初始化微博的分享消息
		if (hasText) {
			weiboMessage.textObject = getTextObj();
		}
		if (hasImage) {
			weiboMessage.imageObject = getImgObj();
		}

		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		WXEntryActivity.weiboShareSDK.sendRequest(
				ArticleContentShareActivity.this, request); // 发送请求消息到微博，唤起微博分享界面
	}

	/**
	 * 
	 * TODO 将应用注册到腾讯微博
	 * 
	 * @param appid
	 * @param app_secket
	 *            void
	 */
	@SuppressLint("ShowToast")
	private void auth(long appid, String app_secket) {
		final Context context = this.getApplicationContext();
		// 注册当前应用的appid和appkeysec，并指定一个OnAuthListener
		// OnAuthListener在授权过程中实施监听
		AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

			// 如果当前设备没有安装腾讯微博客户端，走这里
			@Override
			public void onWeiBoNotInstalled() {
				AuthHelper.unregister(ArticleContentShareActivity.this);
				Intent i = new Intent(ArticleContentShareActivity.this,
						Authorize.class);
				startActivity(i);
			}

			// 如果当前设备没安装指定版本的微博客户端，走这里
			@Override
			public void onWeiboVersionMisMatch() {
				AuthHelper.unregister(ArticleContentShareActivity.this);
				Intent i = new Intent(ArticleContentShareActivity.this,
						Authorize.class);
				startActivity(i);
			}

			// 如果授权失败，走这里
			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(ArticleContentShareActivity.this,
						"result : " + result, 1000).show();
				AuthHelper.unregister(ArticleContentShareActivity.this);
			}

			// 授权成功，走这里
			// 授权成功后，所有的授权信息是存放在WeiboToken对象里面的，可以根据具体的使用场景，将授权信息存放到自己期望的位置，
			// 在这里，存放到了applicationcontext中
			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				Toast.makeText(ArticleContentShareActivity.this, "passed", 1000)
						.show();
				//
				Util.saveSharePersistent(context, "ACCESS_TOKEN",
						token.accessToken);
				Util.saveSharePersistent(context, "EXPIRES_IN",
						String.valueOf(token.expiresIn));
				Util.saveSharePersistent(context, "OPEN_ID", token.openID);
				// Util.saveSharePersistent(context, "OPEN_KEY", token.omasKey);
				Util.saveSharePersistent(context, "REFRESH_TOKEN", "");
				// Util.saveSharePersistent(context, "NAME", name);
				// Util.saveSharePersistent(context, "NICK", name);
				Util.saveSharePersistent(context, "CLIENT_ID", Util.getConfig()
						.getProperty("APP_KEY"));
				Util.saveSharePersistent(context, "AUTHORIZETIME",
						String.valueOf(System.currentTimeMillis() / 1000l));
				AuthHelper.unregister(ArticleContentShareActivity.this);
			}
		});

		AuthHelper.auth(this, "");
	}

	/**
	 * 
	 * TODO 分享内容到腾讯微博
	 * 
	 * @param view
	 *            void
	 */
	// 只能上传140个字
	public void shareTencentWeibo(View view) {

		/**
		 * 跳转到一键转播组件 可以传递一些参数 如content为转播内容 video_url为转播视频URL pic_url为转播图片URL
		 */
		// 腾讯微博授权注册
		String tencentWeiBoTken = Util.getSharePersistent(
				getApplicationContext(), "ACCESS_TOKEN");
		if (StringUtil.isBlank(tencentWeiBoTken)) {
			auth(TencentData.APP_KEY, TencentData.App_SECRET);
		} else {

			Intent i = new Intent(ArticleContentShareActivity.this,
					ReAddActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("content", content.substring(0, 140));

			// bundle.putString("video_url",
			// "http://www.tudou.com/programs/view/b-4VQLxwoX4/");
			bundle.putString("pic_url", imgLinkUrl);
			i.putExtras(bundle);
			startActivity(i);

		}
	}

}
