package edu.tj.sse.runeveryday.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import edu.tj.sse.runeveryday.R;

public class MainActivity extends Activity {
	// private LayoutInflater drawerInflater;
	private SlidingMenu menu;

	private TextView personalTextView;
	private TextView achievementTextView;
	private TextView planTextView;
	private TextView historyTextView;
	private TextView stateTextView;
	private TextView shareTextView;
	private TextView settingsTextView;
	private TextView nicknameTextView;

	private int Screen_width;
	private int Screen_length;

	private Integer[] layout = { R.id.Main_Title, R.id.Main_background, R.id.Main_background2,
			R.id.Main_Button };
	private LinearLayout Layout;

	private Integer[] text = { R.id.Main_tempreture, R.id.Main_shidu, R.id.Main_advice,
			R.id.Main_content, R.id.Main_plan, R.id.Main_time_text };
	private TextView Text;

	private int temprature = 20;
	private int shidu = 50;
	private String advice = "";
	private String plan = "Welcome to use our application. You can input your self-information to create your running plan! \n\nHave a good time!";
	private int week = 1;
	private int day = 1;
	private Handler handler;
	private Timer timer;
	private String name = "User";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		GetScreen();

		init();

		Timer();
		handler();
		
		menu.toggle(false);
	}

	private void init() {
		createSlidingMenu();

		nicknameTextView = (TextView) findViewById(R.id.nickname);
		personalTextView = (TextView) findViewById(R.id.personalTextView);
		achievementTextView = (TextView) findViewById(R.id.achievementTextView);
		planTextView = (TextView) findViewById(R.id.planTextView);
		historyTextView = (TextView) findViewById(R.id.historyTextView);
		stateTextView = (TextView) findViewById(R.id.stateTextView);
		shareTextView = (TextView) findViewById(R.id.shareTextView);
		settingsTextView = (TextView) findViewById(R.id.settingsTextView);

		nicknameTextView.setText(name);
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
				Intent intent = new Intent(MainActivity.this, PlanActivity.class);
				startActivity(intent);
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

		init_layout();
		init_text();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		menu.toggle(false);
	}

	private void Timer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.arg1 = temprature;
				message.arg2 = shidu;
				handler.sendMessage(message);
			}
		}, 1000, 1000);
	}

	private void handler() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Text = (TextView) findViewById(text[0]);
				Text.setText(msg.arg1 + "°");
				Text = (TextView) findViewById(text[1]);
				Text.setText("湿度   " + msg.arg2 + "%");
				if (msg.arg1 < -5 || msg.arg1 > 32 || msg.arg2 < 40 || msg.arg2 > 80) {
					advice = "不适宜跑步";
				} else {
					advice = "适宜跑步";
				}
				Text = (TextView) findViewById(text[2]);
				Text.setText(advice);
			};
		};
	}

	private void init_layout() {
		int tem = 1;
		for (int i = 0; i < layout.length; i++) {
			Layout = (LinearLayout) findViewById(layout[i]);
			switch (i) {
			case 0:
				tem = 15;
				break;
			case 1:
				tem = 3;
				break;
			case 2:
				tem = 3;
				break;
			case 3:
				tem = 9;
				break;
			}
			Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width, Screen_length / tem));
		}
	}

	private void init_text() {
		for (int i = 0; i < text.length; i++) {
			Text = (TextView) findViewById(text[i]);
			switch (i) {
			case 0:
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, Screen_length / 8);
				lp.setMargins(Screen_width / 10, Screen_width / 20, 0, 0);
				Text.setLayoutParams(lp);
				Text.setTextSize(Screen_width / 15);
				Text.setText(temprature + "°");
				break;
			case 1:
				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp1.setMargins(Screen_width / 20, Screen_width / 20, 0, 0);
				Text.setLayoutParams(lp1);
				Text.setTextSize(Screen_width / 60);
				Text.setText("湿度   " + shidu + "%");
				break;
			case 2:
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp2.setMargins(Screen_width / 3 * 2, Screen_width / 15, 0, 0);
				Text.setLayoutParams(lp2);
				Text.setTextSize(Screen_width / 60);
				Text.setText(advice);
				break;
			case 3:
				LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
						Screen_width / 10 * 9, Screen_length / 4);
				lp3.setMargins(Screen_width / 20, Screen_width / 40, 0, 0);
				Text.setLayoutParams(lp3);
				Text.setTextSize(Screen_width / 60);
				Text.setText(plan);
				break;
			case 4:
				Text.setTextSize(Screen_width / 30);
				break;
			case 5:
				Text.setTextSize(Screen_width / 60);
				Text.setText("第" + week + "周 第" + day + "天");
				break;
			default:
				break;
			}
		}
	}

	public void GetScreen() {
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
