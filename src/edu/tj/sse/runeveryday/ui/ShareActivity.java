package edu.tj.sse.runeveryday.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import edu.tj.sse.runeveryday.R;

public class ShareActivity extends BaseActivity implements OnClickListener,
		Callback {

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	public static String shareString = null;
	public static Bitmap Bmp = null;

	private static final String FILE_NAME = "/RunEveryDay.jpg";
	private String image = null;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);
		initSlidingMenu(this);
		
		button = (Button) findViewById(R.id.Share_sina);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.Share_Qzone);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.Share_other);
		button.setOnClickListener(this);
	}

	private void init_sina() {
		ShareSDK.initSDK(this, "1d26631204a0");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("Id", "1");
		hashMap.put("SortId", "1");
		hashMap.put("AppKey", "492372454");
		hashMap.put("AppSecret", "f7ec82c2dab5f40898b41d7b49186ae4");
		hashMap.put("RedirectUrl", "http://www.baidu.com");
		hashMap.put("ShareByAppClient", "true");
		hashMap.put("Enable", "true");
		ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, hashMap);
	}

	private void init_Qzone() {
		ShareSDK.initSDK(this, "1d26631204a0");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("Id", "2");
		hashMap.put("SortId", "2");
		hashMap.put("AppId", "101087103");
		hashMap.put("AppKey", "bf326328887bbdb357b95ccf26ea66ef");
		hashMap.put("ShareByAppClient", "true");
		hashMap.put("Enable", "true");
		ShareSDK.setPlatformDevInfo(QZone.NAME, hashMap);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		OnekeyShare oks = new OnekeyShare();
		switch (v.getId()) {
		case R.id.Share_sina:
			init_sina();
			oks.setPlatform(SinaWeibo.NAME);

			oks.setText(shareString);

			oks.setNotification(R.drawable.icon, "Gtpass");
			if (Bmp != null) {
				storage();
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())
						&& Environment.getExternalStorageDirectory().exists()) {
					image = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + FILE_NAME;
				} else {
					image = getApplication().getFilesDir().getAbsolutePath()
							+ FILE_NAME;
				}
				oks.setImagePath(image);
			}
			oks.setSilent(false);
			oks.show(ShareActivity.this);
			break;
		case R.id.Share_Qzone:
			init_Qzone();
			oks.setNotification(R.drawable.icon, "Gtpass");
			oks.setTitle("RunEveryDay");
			oks.setTitleUrl("http://sharesdk.cn");
			oks.setText(shareString);
			if (Bmp != null) {
				storage();
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())
						&& Environment.getExternalStorageDirectory().exists()) {
					image = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + FILE_NAME;
				} else {
					image = getApplication().getFilesDir().getAbsolutePath()
							+ FILE_NAME;
				}
				oks.setImagePath(image);
			}
			oks.setSite("RunEveryDay");
			oks.setSiteUrl("http://sharesdk.cn");
			// 是否直接分享（true则直接分享）
			oks.setSilent(false);
			oks.setPlatform(QZone.NAME);

			oks.show(ShareActivity.this);

			break;

		case R.id.Share_other:
			Intent intent=new Intent(Intent.ACTION_SEND);   
	        intent.setType("image/*");   
	        intent.putExtra(Intent.EXTRA_SUBJECT, "RunEveryDay");   
	        intent.putExtra(Intent.EXTRA_TEXT, shareString);    
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	        startActivity(Intent.createChooser(intent, getTitle()));
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	public void onCancel(Platform platform, int action) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public void onComplete(Platform platform, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform platform, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TOAST: {
			String text = String.valueOf(msg.obj);
			Toast.makeText(ShareActivity.this, text, Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_ACTION_CCALLBACK: {
			switch (msg.arg1) {
			case 1: { // 成功, successful notification
				showNotification(2000, getString(R.string.share_completed));
			}
				break;
			case 2: { // 失败, fail notification
				String expName = msg.obj.getClass().getSimpleName();
				if ("WechatClientNotExistException".equals(expName)
						|| "WechatTimelineNotSupportedException"
								.equals(expName)) {
					showNotification(2000,
							getString(R.string.wechat_client_inavailable));
				} else if ("GooglePlusClientNotExistException".equals(expName)) {
					showNotification(2000,
							getString(R.string.google_plus_client_inavailable));
				} else if ("QQClientNotExistException".equals(expName)) {
					showNotification(2000,
							getString(R.string.qq_client_inavailable));
				} else {
					showNotification(2000, getString(R.string.share_failed));
				}
			}
				break;
			case 3: { // 取消, cancel notification
				showNotification(2000, getString(R.string.share_canceled));
			}
				break;
			}
		}
			break;
		case MSG_CANCEL_NOTIFY: {
			NotificationManager nm = (NotificationManager) msg.obj;
			if (nm != null) {
				nm.cancel(msg.arg1);
			}
		}
			break;
		}
		Bmp=null;
		return false;
	}

	private void storage() {
		File f = new File(Environment.getExternalStorageDirectory() + "");
		if (!f.exists()) {
			f.mkdir();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(
					Environment.getExternalStorageDirectory() + ""
							+ File.separator + "RunEveryDay.jpg");
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 在状态栏提示分享操作,the notification on the status bar
	private void showNotification(long cancelTime, String text) {
		try {
			Context app = getApplicationContext();
			NotificationManager nm = (NotificationManager) app
					.getSystemService(Context.NOTIFICATION_SERVICE);
			final int id = Integer.MAX_VALUE / 13 + 1;
			nm.cancel(id);

			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.icon, text, when);
			PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
			notification.setLatestEventInfo(app, "sharesdk test", text, pi);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(id, notification);

			if (cancelTime > 0) {
				Message msg = new Message();
				msg.what = MSG_CANCEL_NOTIFY;
				msg.obj = nm;
				msg.arg1 = id;
				UIHandler.sendMessageDelayed(msg, cancelTime, this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}