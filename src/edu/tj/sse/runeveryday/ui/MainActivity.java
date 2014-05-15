package edu.tj.sse.runeveryday.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import edu.tj.sse.runeveryday.R;

public class MainActivity extends Activity {
	//private LayoutInflater drawerInflater;
	private SlidingMenu menu;
	
	private TextView personalTextView;
	private TextView achievementTextView;
	private TextView planTextView;
	private TextView historyTextView;
	private TextView stateTextView;
	private TextView shareTextView;
	private TextView settingsTextView;

	private int Screen_width;
	private int Screen_length;
	
	private int temprature;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		GetScreen();
		
		
		LinearLayout Layout = (LinearLayout) findViewById(R.id.Main_Title);
		Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 15));
		
		Layout = (LinearLayout) findViewById(R.id.Main_background);
		Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 3));

		Layout = (LinearLayout) findViewById(R.id.Main_background2);
		Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 3));
		
		Layout = (LinearLayout) findViewById(R.id.Main_Button);
		Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
				Screen_length / 9));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				Screen_length/8);
		lp.setMargins(Screen_width / 10, Screen_width / 20, 0, 0);
		TextView textView = (TextView) findViewById(R.id.Main_tempreture);
		textView.setLayoutParams(lp);
		textView.setTextSize(Screen_width / 15);
		textView.setText("20°");
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp1.setMargins(Screen_width / 20,Screen_width / 20, 0, 0);
		textView = (TextView) findViewById(R.id.Main_shidu);
		textView.setLayoutParams(lp1);
		textView.setTextSize(Screen_width / 60);
		textView.setText("湿度   52%");
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(Screen_width / 3*2, Screen_width / 15, 0, 0);
		textView = (TextView) findViewById(R.id.Main_advice);
		textView.setLayoutParams(lp2);
		textView.setTextSize(Screen_width / 60);
		textView.setText("适宜运动");
		
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
				Screen_width / 10*9,
				Screen_length/4);
		lp3.setMargins(Screen_width / 20, Screen_width / 40, 0, 0);
		textView = (TextView) findViewById(R.id.Main_content);
		textView.setLayoutParams(lp3);
		textView.setTextSize(Screen_width / 60);
		textView.setText("Hanoi dispatched vessels to waters - claimed by Vietnam - where China moved a drilling rig, and Beijing has the right to take countermeasures in accordance with international law.");
		
		textView = (TextView) findViewById(R.id.Main_plan);
		textView.setTextSize(Screen_width / 30);
		
		textView = (TextView) findViewById(R.id.Main_time_text);
		textView.setTextSize(Screen_width / 60);
		textView.setText("第二周 第三天");
		
		init();
	}

	private void init() {
		createSlidingMenu();
		
		personalTextView = (TextView) findViewById(R.id.personalTextView);
		achievementTextView = (TextView) findViewById(R.id.achievementTextView);
		planTextView = (TextView) findViewById(R.id.planTextView);
		historyTextView = (TextView) findViewById(R.id.historyTextView);
		stateTextView = (TextView) findViewById(R.id.stateTextView);
		shareTextView = (TextView) findViewById(R.id.shareTextView);
		settingsTextView = (TextView) findViewById(R.id.settingsTextView);
		
		personalTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
				startActivity(intent);
			}
		});
		achievementTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, AchievementActivity.class);
				startActivity(intent);
			}
		});
		planTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		historyTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});
		shareTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, ShareActivity.class);
				startActivity(intent);
			}
		});
		stateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, StateActivity.class);
				startActivity(intent);
			}
		});
		settingsTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	public void GetScreen() {
		// TODO Auto-generated method stub

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Screen_width = dm.widthPixels;
		Screen_length = dm.heightPixels;
	}
	
	private void createSlidingMenu() {
		menu = new SlidingMenu(getApplicationContext());
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setFadeDegree(0.35f);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
		// menu.setBehindWidth(400);//设置SlidingMenu菜单的宽度
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.drawer);
	}

}
