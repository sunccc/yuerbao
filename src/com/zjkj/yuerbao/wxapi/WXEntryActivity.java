package com.zjkj.yuerbao.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.Tencent;
import com.zjkj.yuerbao.R;
import com.zjkj.yuerbao.common.SharedPreferencesHelper;
import com.zjkj.yuerbao.common.preference.AppData;
import com.zjkj.yuerbao.common.preference.TencentData;
import com.zjkj.yuerbao.common.preference.WeiBoData;
import com.zjkj.yuerbao.common.preference.WeiChatData;
import com.zjkj.yuerbao.common.util.qq.listener.QQBaseUiListener;
import com.zjkj.yuerbao.common.util.qq.util.Util;
import com.zjkj.yuerbao.common.util.weibo.listener.WeiboBaseUiListener;
import com.zjkj.yuerbao.common.util.weixin.WXUtils;
import com.zjkj.yuerbao.login.RegisterActivity;
import com.zjkj.yuerbao.main.MainActivity;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private JSONObject userInfo;// 微信用户信息
	private Handler handler;
	private SharedPreferencesHelper sp;
	private Intent intent;
	private String code = "";
	public static Tencent mTencent;
	public static IWeiboShareAPI weiboShareSDK;
	private AuthInfo authInfo;
	private SsoHandler mSsoHandler;
	private QQBaseUiListener baseUiListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// 微信登录（将app注册到微信）
		System.out.println("WXEntryActivity init");
		super.onCreate(savedInstanceState);
		intent = new Intent();
		intent.setClass(this, MainActivity.class);
		WXUtils.regToWx(this);

		setContentView(R.layout.activity_login);
		// qq登录
		mTencent = Tencent.createInstance(TencentData.APP_ID, this);

		// 微博登录,创建授权认证信息
		authInfo = new AuthInfo(this, WeiBoData.APP_KEY,
				WeiBoData.REDIRECT_URL, WeiBoData.SCOPE);

		weiboShareSDK = WeiboShareSDK.createWeiboAPI(this, WeiBoData.APP_KEY);
		weiboShareSDK.registerApp();

		sp = new SharedPreferencesHelper(this, AppData.PREFS_NAME);

		ImageView wxLogin = (ImageView) findViewById(R.id.WXLogin);
		// 注册微信(可放在应用启动界面或后台线程中)
		wxLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送微信授权申请
				wxLogin();
			}
		});

		baseUiListener = new QQBaseUiListener(this, mTencent);
		ImageView qqLogin = (ImageView) findViewById(R.id.qqImage);
		qqLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qqLogin();
			}
		});

		ImageView wbLogin = (ImageView) findViewById(R.id.weiboImage);
		mSsoHandler = new SsoHandler(this, authInfo);
		wbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wbLogin();
			}
		});

		TextView regText = (TextView) findViewById(R.id.regText);
		regText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(WXEntryActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	// QQ登录
	public void qqLogin() {

		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", baseUiListener);
		}
	}

	// 微博登录
	public void wbLogin() {

		mSsoHandler.authorize(new WeiboBaseUiListener(this));
	}

	public void wxLogin() {
		WXUtils.sendAuthReq();
	}

	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SENDAUTH:
			break;
		default:
			break;
		}

	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		String result = null;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if (resp.getType() == 1) {
				SendAuth.Resp sendResp = (SendAuth.Resp) resp;
				// SendAuth.Resp.type=1
				code = sendResp.code;
				Toast.makeText(getApplicationContext(), "正在跳转请稍等！",
						Toast.LENGTH_LONG).show();
				getWeiChatUserInfoThread();
			} else if (resp.getType() == 2) {
				Toast.makeText(getApplicationContext(), "成功分享",
						Toast.LENGTH_LONG).show();
			}
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			this.finish();
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			break;
		default:
			result = "发送返回";
			break;
		}

	}

	// 创建微信用户信息
	public void getWeiChatUserInfoThread() {

		// 获取app（accessToken,openId等）信息
		WxCodeThread wxCodeThread = new WxCodeThread();
		wxCodeThread.start();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				switch (msg.what) {
				case 1:
					new WxUserInfoThread(bundle).start();
					break;
				case 2:
					// 获取微信用户个人信息
					System.out.println("回调到微信用户信息-昵称：:"
							+ sp.getValue("nickname"));
					finish();
					startActivity(intent);
					break;
				}

			}

		};

	}

	/**
	 * 
	 * 用接收到的code来获取app（accessToken,openId等）信息
	 * 
	 */
	class WxCodeThread extends Thread implements Runnable {
		public void run() {
			// 获取access_token
			// JSONObject accessToken = WXUtils.getTokenByCode(code);
			JSONObject accessToken = Util
					.getHtmlJSONObject(WeiChatData.TOKEN_URL + code);

			try {
				String accessTokenStr = accessToken.getString("access_token");
				String openId = accessToken.getString("openid");
				Bundle accessTokenBundle = new Bundle();
				accessTokenBundle.putString("access_token", accessTokenStr);
				accessTokenBundle.putString("openid", openId);
				Message message = Message.obtain();
				message.what = 1;
				message.setData(accessTokenBundle);
				handler.sendMessage(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 获取微信用户基本信息
	class WxUserInfoThread extends Thread implements Runnable {
		private Bundle bundle;

		public WxUserInfoThread(Bundle bundle) {
			// TODO Auto-generated constructor stub
			this.bundle = bundle;
		}

		public void run() {
			try {
				userInfo = WXUtils.getUserInfo(
						bundle.getString("access_token"),
						bundle.getString("openId"));
				// sp.putValue("openid", userInfo.getString("openid"));
				sp.putValue("LoginType", "WeiChatUser");
				sp.putValue("nickname", userInfo.getString("nickname"));
				// sp.putValue("sex", userInfo.getString("sex"));
				// sp.putValue("province", userInfo.getString("province"));
				// sp.putValue("city", userInfo.getString("city"));
				// sp.putValue("country", userInfo.getString("country"));
				// sp.putValue("headimgurl", userInfo.getString("headimgurl"));
				// sp.putValue("privilege", userInfo.getString("privilege"));
				// sp.putValue("unionid", userInfo.getString("unionid"));
				Message message = Message.obtain();
				message.what = 2;
				handler.sendMessage(message);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		mTencent.onActivityResult(requestCode, resultCode, data);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		finish();
		startActivity(intent);
	}

}