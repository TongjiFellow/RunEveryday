package edu.tj.sse.runeveryday.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.business.PlanBase;
import edu.tj.sse.runeveryday.database.entity.Training;

public class MainActivity extends BaseActivity {
	// private LayoutInflater drawerInflater;

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

	private PlanBase planBase;
	private Training currentTraining;
	// private String plan =
	// "Welcome to use our application. You can input your self-information to create your running plan! \n\nHave a good time!";

	private Handler handler;
	private Timer timer;
	private String name = "User";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		planBase = new PlanBase(MainActivity.this);
		currentTraining = planBase.getCurrentTraining();

		GetScreen();
		init();
		Timer();
		handler();

		ImageView imageView = (ImageView) findViewById(R.id.main_button);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		menu.toggle(false);
	}

	private void init() {
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
				Text.setText(currentTraining.getWork());
				break;
			case 4:
				Text.setTextSize(Screen_width / 30);
				break;
			case 5:
				Text.setTextSize(Screen_width / 60);
				Text.setText(currentTraining.getWeek() + currentTraining.getDay());
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

}
