package edu.tj.sse.runeveryday.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import edu.tj.sse.runeveryday.R;

public class BaseActivity extends Activity {
	protected SlidingMenu menu;
	protected TextView nicknameTextView;
	protected TextView personalTextView;
	protected TextView achievementTextView;
	protected TextView planTextView;
	protected TextView historyTextView;
	protected TextView stateTextView;
	protected TextView shareTextView;
	protected TextView settingsTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		} else if  (keyCode == KeyEvent.KEYCODE_MENU) {
			menu.toggle(true);
		}
		return false;
	}

	protected void initSlidingMenu(Activity activity) {
		createSlidingMenu();

		nicknameTextView = (TextView) activity.findViewById(R.id.nickname);
		personalTextView = (TextView) activity.findViewById(R.id.personalTextView);
		achievementTextView = (TextView) activity.findViewById(R.id.achievementTextView);
		planTextView = (TextView) activity.findViewById(R.id.planTextView);
		historyTextView = (TextView) activity.findViewById(R.id.historyTextView);
		stateTextView = (TextView) activity.findViewById(R.id.stateTextView);
		shareTextView = (TextView) activity.findViewById(R.id.shareTextView);
		settingsTextView = (TextView) activity.findViewById(R.id.settingsTextView);

		nicknameTextView.setText("User");
		personalTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				goTo(PersonalActivity.class);
			}
		});
		achievementTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				goTo(AchievementActivity.class);
			}
		});
		planTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				goTo(PlanActivity.class);
			}
		});
		historyTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				goTo(HistoryActivity.class);
			}
		});
		shareTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				ShareActivity.shareString = "RunEveryDay是一款不错的跑步的软件，我正在使用，你也快来用吧！";
				goTo(ShareActivity.class);
			}
		});
		stateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				goTo(StateActivity.class);
			}
		});
		settingsTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setCurrentTextViewHigh(arg0);
				goTo(SettingsActivity.class);
			}
		});
	}

	protected void goTo(Class<?> cls) {
		Intent intent = new Intent(BaseActivity.this, cls);
		startActivity(intent);
		menu.toggle(false);
		BaseActivity.this.finish();
	}

	private void setCurrentTextViewHigh(View view) {
		personalTextView.setBackgroundColor(Color.rgb(249, 228, 164));
		achievementTextView.setBackgroundColor(Color.rgb(249, 228, 164));
		planTextView.setBackgroundColor(Color.rgb(249, 228, 164));
		historyTextView.setBackgroundColor(Color.rgb(249, 228, 164));
		stateTextView.setBackgroundColor(Color.rgb(249, 228, 164));
		shareTextView.setBackgroundColor(Color.rgb(249, 228, 164));
		settingsTextView.setBackgroundColor(Color.rgb(249, 228, 164));

		//view.setBackgroundColor(Color.rgb(249, 228, 85));
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
