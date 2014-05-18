package edu.tj.sse.runeveryday.ui;

import static edu.tj.sse.runeveryday.service.SensorTag.UUID_ACC_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_HUM_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_IRT_DATA;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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
import edu.tj.sse.runeveryday.database.entity.V3Data;
import edu.tj.sse.runeveryday.service.BluetoothLeService;
import edu.tj.sse.runeveryday.service.Sensor;
import edu.tj.sse.runeveryday.utils.Point3D;
import edu.tj.sse.runeveryday.utils.V3;

public class MainActivity extends BaseActivity {
	// private LayoutInflater drawerInflater;
	private static final String TAG = "MainActivity";
	private Context context;

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
	// private String plan =
	// "Welcome to use our application. You can input your self-information to create your running plan! \n\nHave a good time!";

	private Handler handler;
	private Timer timer;
	private String name = "User";

	// UI
	private TextView tempretureTextView;
	private TextView humidityTextView;

	// BLE
	private BluetoothLeService mBtLeService = null;
	// private BluetoothDevice mBluetoothDevice = null;
	private BluetoothGatt mBtGatt = null;
	private List<BluetoothGattService> mServiceList = null;
	private static final int GATT_TIMEOUT = 100; // milliseconds
	private boolean mServicesRdy = false;
	private boolean mIsReceiving = false;

	// SensorTag
	private List<Sensor> mEnabledSensors = new ArrayList<Sensor>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		initSlidingMenu(this);

		tempretureTextView = (TextView) findViewById(R.id.Main_tempreture);
		humidityTextView = (TextView) findViewById(R.id.Main_shidu);

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
				Intent intent = new Intent(MainActivity.this, RunningActivity.class);
				startActivity(intent);
			}
		});

//		// BLE
//		mBtLeService = BluetoothLeService.getInstance();
//		// mBluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE);
//		mServiceList = new ArrayList<BluetoothGattService>();
//
//		updateSensorList();
//		// TODO
//		registerReceiver(receiver, makeGattUpdateIntentFilter());
//
//		// Create GATT object
//		mBtGatt = BluetoothLeService.getBtGatt();
//
//		// Start service discovery
//		if (!mServicesRdy && mBtGatt != null) {
//			if (mBtLeService.getNumServices() == 0)
//				discoverServices();
//			else
//				displayServices();
//		}
	}

//	private void updateSensorList() {
//		mEnabledSensors.clear();
//
//		for (int i = 0; i < Sensor.SENSOR_LIST.length; i++) {
//			Sensor sensor = Sensor.SENSOR_LIST[i];
//			mEnabledSensors.add(sensor);
//		}
//	}
//
//	private void discoverServices() {
//		if (mBtGatt.discoverServices()) {
//			Log.i(TAG, "START SERVICE DISCOVERY");
//			mServiceList.clear();
//		} else {
//		}
//	}
//
//	private void displayServices() {
//		mServicesRdy = true;
//
//		try {
//			mServiceList = mBtLeService.getSupportedGattServices();
//		} catch (Exception e) {
//			e.printStackTrace();
//			mServicesRdy = false;
//		}
//
//		// Characteristics descriptor readout done
//		if (mServicesRdy) {
//			enableSensors(true);
//			enableNotifications(true);
//		} else {
//		}
//	}
//
//	private void enableSensors(boolean enable) {
//		for (Sensor sensor : mEnabledSensors) {
//			UUID servUuid = sensor.getService();
//			UUID confUuid = sensor.getConfig();
//
//			// Skip keys
//			if (confUuid == null)
//				break;
//
//			BluetoothGattService serv = mBtGatt.getService(servUuid);
//			BluetoothGattCharacteristic charac = serv.getCharacteristic(confUuid);
//			byte value = enable ? sensor.getEnableSensorCode() : Sensor.DISABLE_SENSOR_CODE;
//			mBtLeService.writeCharacteristic(charac, value);
//			mBtLeService.waitIdle(GATT_TIMEOUT);
//		}
//
//	}
//
//	private void enableNotifications(boolean enable) {
//		for (Sensor sensor : mEnabledSensors) {
//			UUID servUuid = sensor.getService();
//			UUID dataUuid = sensor.getData();
//			BluetoothGattService serv = mBtGatt.getService(servUuid);
//			BluetoothGattCharacteristic charac = serv.getCharacteristic(dataUuid);
//
//			mBtLeService.setCharacteristicNotification(charac, enable);
//			mBtLeService.waitIdle(GATT_TIMEOUT);
//		}
//	}
//
//	private final BroadcastReceiver receiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			final String action = intent.getAction();
//			int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
//					BluetoothGatt.GATT_SUCCESS);
//
//			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//				if (status == BluetoothGatt.GATT_SUCCESS) {
//					// TODO
//					displayServices();
//				} else {
//					Toast.makeText(getApplication(), "Service discovery failed", Toast.LENGTH_LONG)
//							.show();
//					return;
//				}
//			} else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
//				// Notification
//				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
//				onCharacteristicChanged(uuidStr, value);
//			} else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
//				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
//				// onCharacteristicWrite(uuidStr, status);
//			} else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
//				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
//				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
//				// onCharacteristicsRead(uuidStr, value, status);
//			}
//
//			if (status != BluetoothGatt.GATT_SUCCESS) {
//				// setError("GATT error code: " + status);
//			}
//		}
//	};
//
//	public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
//		Point3D v;
//
//		if (uuidStr.equals(UUID_IRT_DATA.toString())) {
//			v = Sensor.IR_TEMPERATURE.convert(rawValue);
//			tempretureTextView.setText("" + v.x);
//		}
//
//		if (uuidStr.equals(UUID_HUM_DATA.toString())) {
//			v = Sensor.HUMIDITY.convert(rawValue);
//			humidityTextView.setText("" + v.x);
//		}
//	}
//
//	private static IntentFilter makeGattUpdateIntentFilter() {
//		final IntentFilter fi = new IntentFilter();
//		fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
//		fi.addAction(BluetoothLeService.ACTION_DATA_WRITE);
//		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
//		return fi;
//	}

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

}
