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
		System.out.println("��ʼ");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("����");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("�ָ�");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		System.out.println("��ͣ");
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		System.out.println("ֹͣ");
		super.onStop();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		System.out.println("����");
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("����");
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ��ʼ����������
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
	 * TODO ����΢���û�
	 * 
	 * @param view
	 * 
	 * @return void
	 *
	 */
	public void shareWeiChatFriend(View view) {
		// ��ʼ��һ��WXTextObject����
		WXTextObject textObj = new WXTextObject();
		textObj.text = title;
		// ��WXTextObject�����ʼ��һ��WXMediaMessage����
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// �����ı����͵���Ϣʱ��title�ֶβ�������
		msg.title = title;
		msg.description = content;

		// ����һ��Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		// SendMessageToWX.Req req = new SendMessageToWX.Req().type=2
		// ����api�ӿڷ������ݵ�΢��
		api.sendReq(req);
		finish();

	}

	/**
	 *
	 * TODO ��������Ȧ
	 * 
	 * @param view
	 *            ��ͼ
	 * @return void
	 *
	 */
	public void shareFriendsRound(View view) {
		// ��ʼ��һ��WXTextObject����
		WXTextObject textObj = new WXTextObject();
		textObj.text = title;

		// ��WXTextObject�����ʼ��һ��WXMediaMessage����
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = textObj;
		// �����ı����͵���Ϣʱ��title�ֶβ�������
		msg.title = title;
		msg.description = content;

		// ����һ��Req
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		// req.scene = SendMessageToWX.Req.WXSceneFavorite;

		// ����api�ӿڷ������ݵ�΢��
		api.sendReq(req);
		finish();
	}

	/**
	 *
	 * TODO ����һ�������Ψһ��ʶ�ֶ�
	 * 
	 * @param type
	 *            ����
	 * @return String
	 *
	 */
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	/**
	 * 
	 * TODO ����QQ
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

		// �����
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
	 * ���첽��ʽ����QQZone����
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
	 * TODO ����QQZone
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
			System.out.println("����ȡ��");
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub

			System.out.println("����ɹ�");
			System.out.println(response.toString());
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			System.out.println("����");
		}
	};

	/**
	 * ���첽��ʽ����QQZone����
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
	 * TODO ��������΢��
	 * 
	 * @param view
	 *            void
	 */
	public void shareSinaWeiBo(View view) {

		sendMultiMessage(true, true, false, false, false, false);
	}

	/**
	 * 
	 * TODO ����΢����������
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
	 * TODO ����΢������ͼƬ
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
	 * TODO ��������΢��
	 * 
	 * @param hasText
	 * @param hasImage
	 * @param hasWebpage
	 * @param hasMusic
	 * @param hasVideo
	 * @param hasVoice
	 *            void
	 */
	// ֻ���ϴ�140����
	private void sendMultiMessage(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo,
			boolean hasVoice) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();// ��ʼ��΢���ķ�����Ϣ
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
				ArticleContentShareActivity.this, request); // ����������Ϣ��΢��������΢���������
	}

	/**
	 * 
	 * TODO ��Ӧ��ע�ᵽ��Ѷ΢��
	 * 
	 * @param appid
	 * @param app_secket
	 *            void
	 */
	@SuppressLint("ShowToast")
	private void auth(long appid, String app_secket) {
		final Context context = this.getApplicationContext();
		// ע�ᵱǰӦ�õ�appid��appkeysec����ָ��һ��OnAuthListener
		// OnAuthListener����Ȩ������ʵʩ����
		AuthHelper.register(this, appid, app_secket, new OnAuthListener() {

			// �����ǰ�豸û�а�װ��Ѷ΢���ͻ��ˣ�������
			@Override
			public void onWeiBoNotInstalled() {
				AuthHelper.unregister(ArticleContentShareActivity.this);
				Intent i = new Intent(ArticleContentShareActivity.this,
						Authorize.class);
				startActivity(i);
			}

			// �����ǰ�豸û��װָ���汾��΢���ͻ��ˣ�������
			@Override
			public void onWeiboVersionMisMatch() {
				AuthHelper.unregister(ArticleContentShareActivity.this);
				Intent i = new Intent(ArticleContentShareActivity.this,
						Authorize.class);
				startActivity(i);
			}

			// �����Ȩʧ�ܣ�������
			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(ArticleContentShareActivity.this,
						"result : " + result, 1000).show();
				AuthHelper.unregister(ArticleContentShareActivity.this);
			}

			// ��Ȩ�ɹ���������
			// ��Ȩ�ɹ������е���Ȩ��Ϣ�Ǵ����WeiboToken��������ģ����Ը��ݾ����ʹ�ó���������Ȩ��Ϣ��ŵ��Լ�������λ�ã�
			// �������ŵ���applicationcontext��
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
	 * TODO �������ݵ���Ѷ΢��
	 * 
	 * @param view
	 *            void
	 */
	// ֻ���ϴ�140����
	public void shareTencentWeibo(View view) {

		/**
		 * ��ת��һ��ת����� ���Դ���һЩ���� ��contentΪת������ video_urlΪת����ƵURL pic_urlΪת��ͼƬURL
		 */
		// ��Ѷ΢����Ȩע��
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
