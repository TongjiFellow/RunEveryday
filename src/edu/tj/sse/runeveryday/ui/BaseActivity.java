package edu.tj.sse.runeveryday.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import edu.tj.sse.runeveryday.R;

public class BaseActivity extends Activity {
	protected SlidingMenu menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initSlidingMenu() {
		createSlidingMenu();

		final TextView nicknameTextView = (TextView) findViewById(R.id.nickname);
		final TextView personalTextView = (TextView) findViewById(R.id.personalTextView);
		final TextView achievementTextView = (TextView) findViewById(R.id.achievementTextView);
		final TextView planTextView = (TextView) findViewById(R.id.planTextView);
		final TextView historyTextView = (TextView) findViewById(R.id.historyTextView);
		final TextView stateTextView = (TextView) findViewById(R.id.stateTextView);
		final TextView shareTextView = (TextView) findViewById(R.id.shareTextView);
		final TextView settingsTextView = (TextView) findViewById(R.id.settingsTextView);

		nicknameTextView.setText("User");
		personalTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BaseActivity.this, PersonalActivity.class);
				startActivity(intent);
			}
		});
		achievementTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BaseActivity.this, AchievementActivity.class);
				startActivity(intent);
			}
		});
		planTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BaseActivity.this, PlanActivity.class);
				startActivity(intent);
			}
		});
		historyTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				historyTextView.setBackgroundColor(Color.rgb(249, 228, 85));
				Intent intent = new Intent(BaseActivity.this, HistoryActivity.class);
				startActivity(intent);
			}
		});
		shareTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ShareActivity.shareString = "RunEveryDay是一款不错的跑步的软件，我正在使用，你也快来用吧！";
				Intent intent = new Intent(BaseActivity.this, ShareActivity.class);
				startActivity(intent);
			}
		});
		stateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BaseActivity.this, StateActivity.class);
				startActivity(intent);
			}
		});
		settingsTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
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
}
