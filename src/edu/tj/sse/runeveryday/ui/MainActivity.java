package edu.tj.sse.runeveryday.ui;

import static edu.tj.sse.runeveryday.service.SensorTag.UUID_HUM_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_IRT_DATA;

import java.util.Timer;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.business.PlanBase;
import edu.tj.sse.runeveryday.database.entity.Training;
import edu.tj.sse.runeveryday.service.BluetoothLeService;
import edu.tj.sse.runeveryday.service.Sensor;
import edu.tj.sse.runeveryday.utils.Point3D;

public class MainActivity extends BaseSensorTagActivity {
	private static final String TAG = "MainActivity";
	private Context context;

	private RunEverydayApplication app;

	private int Screen_width;
	private int Screen_length;

	private Integer[] layout = { R.id.Main_background, R.id.Main_background2, R.id.Main_Button };
	private LinearLayout Layout;

	private Integer[] text = { R.id.Main_tempreture, R.id.Main_shidu, R.id.Main_advice,
			R.id.Main_content, R.id.Main_plan, R.id.Main_time_text };
	private TextView Text;

	private int temprature = 20;
	private int shidu = 50;
	private String advice = "";

	private PlanBase planBase;
	private Training currentTraining;

	private Handler handler;
	private Timer timer;

	// UI
	private TextView tempretureTextView;
	private TextView humidityTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		app = (RunEverydayApplication) getApplication();
		context = getApplicationContext();
		initSlidingMenu(this);

		tempretureTextView = (TextView) findViewById(R.id.Main_tempreture);
		humidityTextView = (TextView) findViewById(R.id.Main_shidu);

		planBase = new PlanBase(MainActivity.this);
		currentTraining = planBase.getCurrentTraining();

		GetScreen();
		init();
		ImageView imageView = (ImageView) findViewById(R.id.main_button);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RunningActivity.class);
				startActivity(intent);
			}
		});

		registerReceiver(receiver, makeGattUpdateIntentFilter());
	}

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
					BluetoothGatt.GATT_SUCCESS);

			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					displayServices();
				} else {
					Toast.makeText(getApplication(), "Service discovery failed", Toast.LENGTH_LONG)
							.show();
					return;
				}
			} else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
				// Notification
				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				onCharacteristicChanged(uuidStr, value);
			} else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
				// String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				// onCharacteristicWrite(uuidStr, status);
			} else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
				// String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				// byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				// onCharacteristicsRead(uuidStr, value, status);
			}

			if (status != BluetoothGatt.GATT_SUCCESS) {
				// setError("GATT error code: " + status);
			}
		}
	};

	public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
		Point3D v;
		Point3D v1;
		if (uuidStr.equals(UUID_IRT_DATA.toString())) {
			v = Sensor.IR_TEMPERATURE.convert(rawValue);
			tempretureTextView.setText(String.format("%.1f", v.x) + "°");
		}
		if (uuidStr.equals(UUID_HUM_DATA.toString())) {
			v1 = Sensor.HUMIDITY.convert(rawValue);
			humidityTextView.setText("湿度： " + String.format("%.1f", v1.x) + "%");
		}
	}

	@Override
	protected void goTo(Class<?> cls) {
		Intent intent = new Intent(MainActivity.this, cls);
		startActivity(intent);
		menu.toggle(false);
		// MainActivity.this.finish();
	}

	private void init() {
		init_layout();
		init_text();
	}

	private void init_layout() {
		for (int i = 0; i < layout.length; i++) {
			Layout = (LinearLayout) findViewById(layout[i]);
			switch (i) {
			case 0:
				Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
						Screen_length / 3));
				break;
			case 1:
				Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
						Screen_length / 14 * 5));
				break;
			case 2:
				Layout.setLayoutParams(new LinearLayout.LayoutParams(Screen_width,
						Screen_length / 9));
				break;
			}

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
