package edu.tj.sse.runeveryday.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// drawerInflater = getLayoutInflater();
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
