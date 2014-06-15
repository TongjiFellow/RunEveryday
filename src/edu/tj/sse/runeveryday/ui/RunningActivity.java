package edu.tj.sse.runeveryday.ui;

import static edu.tj.sse.runeveryday.service.SensorTag.UUID_ACC_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_HUM_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_IRT_DATA;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.business.PlanBase;
import edu.tj.sse.runeveryday.database.business.RundataBase;
import edu.tj.sse.runeveryday.database.business.V3DataBase;
import edu.tj.sse.runeveryday.database.entity.RunData;
import edu.tj.sse.runeveryday.database.entity.V3Data;
import edu.tj.sse.runeveryday.service.BluetoothLeService;
import edu.tj.sse.runeveryday.service.Sensor;
import edu.tj.sse.runeveryday.utils.CalcUtil;
import edu.tj.sse.runeveryday.utils.Point3D;
import edu.tj.sse.runeveryday.utils.V3;

public class RunningActivity extends BaseSensorTagActivity {
	public static final String TAG = "RunningActivity";

	public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

	private Context context;

	private int weight = 70;

	// private static final int ID_OFFSET = 0;
	// private static final int ID_ACC = 0;
	// private static final int ID_AMB = 1;
	// private static final int ID_HUM = 2;

	// GUI
	private TextView mAccValue;
	private TextView mAmbValue;
	private TextView mHumValue;
	private TextView mStatus;

	private TextView planTextView;
	private TextView timeTextView;
	private TextView speedTextView;
	private TextView caloriesTextView;
	private TextView distanceTextView;

	private ImageButton finishImageButton;
	private ImageButton pauseImageButton;

	// cal
	private CalcUtil calcUtil;

	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");

	// BLE
	// private BluetoothLeService mBtLeService = null;
	// private BluetoothGatt mBtGatt = null;
	// private List<BluetoothGattService> mServiceList = null;
	// private static final int GATT_TIMEOUT = 100; // milliseconds
	// private boolean mServicesRdy = false;
	// private boolean mIsReceiving = false;
	// private List<Sensor> mEnabledSensors = new ArrayList<Sensor>();

	private boolean isRunning = false;
	// Timer
	private Timer timer;
	private TimerTask task;
	private int count = 0;
	private Handler handler;

	// Database;
	PlanBase planBase;
	V3DataBase v3DataBase;
	RundataBase rundataBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_running);
		// initSlidingMenu(this);
		context = getApplicationContext();

		planBase = new PlanBase(context);
		v3DataBase = new V3DataBase(context);
		rundataBase = new RundataBase(context);

		// UI
		// table = (TableLayout) findViewById(R.id.services_browser_layout);
		mAccValue = (TextView) findViewById(R.id.accelerometerTxt);
		mAmbValue = (TextView) findViewById(R.id.ambientTemperatureTxt);
		mHumValue = (TextView) findViewById(R.id.humidityTxt);
		mStatus = (TextView) findViewById(R.id.status);
		planTextView = (TextView) findViewById(R.id.planTextView);
		timeTextView = (TextView) findViewById(R.id.timeTextView);
		speedTextView = (TextView) findViewById(R.id.speedTextView);
		caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
		distanceTextView = (TextView) findViewById(R.id.distanceTextView);
		finishImageButton = (ImageButton) findViewById(R.id.finishImageButton);
		pauseImageButton = (ImageButton) findViewById(R.id.pauseImageButton);
		planTextView.setText(planBase.getCurrentTraining().getWork());

		calcUtil = new CalcUtil();

		// 计时器
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (isRunning) {
						count++;
						timeTextView.setText(convertToTime(count));
						V3 t = calcUtil.getSpeed(System.currentTimeMillis());
						t = calcUtil.acce;
						double speed = Math.sqrt(t.x * t.x + t.y * t.y + t.z * t.z);
						speedTextView.setText(String.format("%.2f", speed));
						caloriesTextView.setText(String.format("%.2f",
								calcUtil.getCalories(70) / 1000));
						distanceTextView.setText(String.format("%.2f",
								calcUtil.getDistance() / 1000));
					}
					break;
				default:
					break;
				}
			}
		};
		task = new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};
		timer = new Timer(true);
		isRunning = true;
		timer.schedule(task, 1000, 1000);

		finishImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog();
			}
		});
		pauseImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isRunning = !isRunning;
				Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_LONG).show();
			}
		});

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
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
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				// GATT disconnect
				int status1 = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
						BluetoothGatt.GATT_FAILURE);
				Toast.makeText(context, "设备连接中断，请重新连接", Toast.LENGTH_LONG).show();
				if (status == BluetoothGatt.GATT_SUCCESS) {
				} else {
					setError("Disconnect failed. Status: " + status);
				}
			}
			if (status != BluetoothGatt.GATT_SUCCESS) {
				setError("GATT error code: " + status);
			}
		}
	};

	public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
		Point3D v;
		String msg;
		if (isRunning) {
			if (uuidStr.equals(UUID_ACC_DATA.toString())) {
				v = Sensor.ACCELEROMETER.convert(rawValue);
				msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z)
						+ "\n";
				mAccValue.setText(msg);
				calcUtil.setAcceleration(new V3(v.x, v.y, v.z));
				v3DataBase.addV3Data(new V3Data(v.x, v.y, v.z));
			}

			if (uuidStr.equals(UUID_IRT_DATA.toString())) {
				v = Sensor.IR_TEMPERATURE.convert(rawValue);
				msg = decimal.format(v.x) + "\n";
				mAmbValue.setText(msg);
			}

			if (uuidStr.equals(UUID_HUM_DATA.toString())) {
				v = Sensor.HUMIDITY.convert(rawValue);
				msg = decimal.format(v.x) + "\n";
				mHumValue.setText(msg);
			}
		}

	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		// if (!mIsReceiving) {
		// registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		// mIsReceiving = true;
		// }
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		// if (mIsReceiving) {
		// unregisterReceiver(mGattUpdateReceiver);
		// mIsReceiving = false;
		// }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
	}

	void setStatus(String txt) {
		mStatus.setText(txt);
	}

	void setError(String txt) {
		mStatus.setText(txt);
	}

	void setBusy(boolean f) {
		// if (f)
		// mStatus.setTextAppearance(this, R.style.statusStyle_Busy);
		// else
		// mStatus.setTextAppearance(this, R.style.statusStyle);
	}

	private String convertToTime(int t) {
		String time = "";
		int h = t / 3600;
		t = t - h * 3600;
		int m = t / 60;
		t = t - m * 60;
		int s = t;
		time = String.format("%02d:%02d:%02d", h, m, s);
		return time;
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(RunningActivity.this);
		builder.setTitle("结束跑步，并存储数据？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (count >= 1800) {
					// 设置当前任务完成
					planBase.setTrainingDone(planBase.getCurrentTraining());
				}
				double distance = calcUtil.getDistance();
				double calories = calcUtil.getCalories(weight);
				rundataBase.addRundata(new RunData((int) distance, count, (int) calories));
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			menu.toggle(true);
		}
		return false;
	}
}
