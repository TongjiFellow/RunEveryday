package edu.tj.sse.runeveryday.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.DatabaseHelper;
import edu.tj.sse.runeveryday.database.business.RundataBase;
import edu.tj.sse.runeveryday.database.entity.RunData;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AchievementActivity extends Activity implements
		android.view.View.OnClickListener {

	private int Screen_width;
	private int Screen_length;

	private LinearLayout Layout;
	private Integer[] layout = { R.id.Achieve_Background1,
			R.id.Achieve_Background2, R.id.Achieve_Background3,
			R.id.Achieve_Background4, R.id.Achieve_Background5,
			R.id.Achieve_Background6, R.id.Achieve_Background7 };

	private ImageView imageView;
	private Integer[] Trophy = { R.id.Achieve_number_1, R.id.Achieve_number_2,
			R.id.Achieve_number_3, R.id.Achieve_number_4,
			R.id.Achieve_distance_1, R.id.Achieve_distance_2,
			R.id.Achieve_distance_3, R.id.Achieve_distance_4,
			R.id.Achieve_time_1, R.id.Achieve_time_2, R.id.Achieve_time_3,
			R.id.Achieve_time_4 };

	private TextView textView;
	private Integer[] text = { R.id.Achieve_text_1, R.id.Achieve_text_2,
			R.id.Achieve_text_3, R.id.Achieve_text_4, R.id.Achieve_text_5,
			R.id.Achieve_text_6, R.id.Achieve_text_7, R.id.Achieve_text_8,
			R.id.Achieve_text_9, R.id.Achieve_text_10, R.id.Achieve_text_11,
			R.id.Achieve_text_12 };
	private String title;
	private String context;

	public static int total_number = 0;
	public static float time = 0; // 单位 小时
	public static float distance = 0; // 单位 公里

	private int[] isachive = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private boolean bool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_achievement);
		
		RundataBase rundataBase=new RundataBase(AchievementActivity.this);
		total_number=rundataBase.queryRunningNum();
		time=rundataBase.querySumRunningTime();
		distance=rundataBase.querySumRunningDistance();
		
		init();
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(this);

		TextView tx = new TextView(this);
		tx.setText(title);
		tx.setTextSize(Screen_width / 45);
		tx.setHeight(Screen_width / 7);
		tx.setGravity(Gravity.CENTER);

		builder.setCustomTitle(tx);

		if (bool)
			context += "          (已完成)";
		else
			context += "          (未完成)";

		builder.setMessage(context);

		builder.setNegativeButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	// 为背景布局
	private void setlayout() {
		int tem;
		for (int i = 0; i < 6; i++) {
			if (i % 2 == 1)
				tem = 25;
			else
				tem = 5;
			Layout = (LinearLayout) findViewById(layout[i]);
			Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
					Screen_length / tem));
		}
		Layout = (LinearLayout) findViewById(R.id.Achieve_Title);
		Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 15));
	}

	// 为文字布局
	private void settext() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		lp.setMargins(Screen_width / 15, 0, 0, 0);
		for (int i = 0; i < 12; i++) {
			textView = (TextView) findViewById(text[i]);
			textView.setLayoutParams(lp);
			if (i == 4 || i == 5 || i == 6 || i == 7)
				textView.setTextSize(Screen_width / 100);
			else
				textView.setTextSize(Screen_width / 90);
		}
	}

	// 调整奖杯大小
	private void changetrophy() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				Screen_width / 6, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(Screen_width / 16, 0, 0, 0);

		for (int i = 0; i < 12; i++) {
			imageView = (ImageView) findViewById(Trophy[i]);
			imageView.setLayoutParams(lp);
			if (isachive[i] == 1)
				imageView.setImageResource(R.drawable.achieve_1);
			imageView.setOnClickListener(this);
		}
		imageView = (ImageView) findViewById(R.id.Achieve_button);
		imageView.setOnClickListener(this);
	}

	public void GetScreen() {
		// TODO Auto-generated method stub

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Screen_width = dm.widthPixels;
		Screen_length = dm.heightPixels;
	}

	private void set_isachieve() {
		if (total_number >= 7)
			isachive[0] = 1;
		if (total_number >= 14)
			isachive[1] = 1;
		if (total_number >= 30)
			isachive[2] = 1;
		if (total_number >= 60)
			isachive[3] = 1;
		if (distance >= 1)
			isachive[4] = 1;
		if (distance >= 10)
			isachive[5] = 1;
		if (distance >= 50)
			isachive[6] = 1;
		if (distance >= 500)
			isachive[7] = 1;
		if (time >= 1)
			isachive[8] = 1;
		if (time >= 10)
			isachive[9] = 1;
		if (time >= 100)
			isachive[10] = 1;
		if (time >= 500)
			isachive[11] = 1;
	}

	private void init() {

		set_isachieve();
		GetScreen();
		setlayout();
		changetrophy();
		settext();
	}

	private Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println(statusBarHeight);
		// 获取屏幕长和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight - 160);
		view.destroyDrawingCache();
		return b;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Achieve_button:
			ShareActivity.Bmp = takeScreenShot(AchievementActivity.this);
			ShareActivity.shareString="我在RunEverDay里获得的奖杯，快来和我一起用它吧！";
			Intent intent = new Intent(AchievementActivity.this, ShareActivity.class);
			startActivity(intent);
			break;

		default:
			String[] tem = new String[2];
			tem = v.getContentDescription().toString().split(" ");
			context = tem[0];
			title = tem[1];
			if (isachive[Integer.parseInt(tem[2])] == 1)
				bool = true;
			else
				bool = false;
			dialog();
			break;
		}

	}
}
