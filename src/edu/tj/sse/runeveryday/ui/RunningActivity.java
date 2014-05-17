package edu.tj.sse.runeveryday.ui;

import static edu.tj.sse.runeveryday.service.SensorTag.UUID_ACC_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_HUM_DATA;
import static edu.tj.sse.runeveryday.service.SensorTag.UUID_IRT_DATA;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.service.BluetoothLeService;
import edu.tj.sse.runeveryday.service.Sensor;
import edu.tj.sse.runeveryday.utils.CalcUtil;
import edu.tj.sse.runeveryday.utils.Point3D;
import edu.tj.sse.runeveryday.utils.V3;

public class RunningActivity extends Activity {
	public static final String TAG = "RunningActivity";

	public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

	private static final int ID_OFFSET = 0;
	private static final int ID_ACC = 0;
	private static final int ID_AMB = 1;
	private static final int ID_HUM = 2;

	// GUI
	private TableLayout table;
	private TextView mAccValue;
	private TextView mAmbValue;
	private TextView mHumValue;
	private TextView mStatus;

	private TextView speedTextView;
	private TextView caloriesTextView;
	private TextView distanceTextView;

	// cal
	private CalcUtil calcUtil;

	private DecimalFormat decimal = new DecimalFormat("+0.00;-0.00");

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
		setContentView(R.layout.activity_running);

		// Intent intent = getIntent();

		table = (TableLayout) findViewById(R.id.services_browser_layout);

		// UI widgets
		mAccValue = (TextView) findViewById(R.id.accelerometerTxt);
		mAmbValue = (TextView) findViewById(R.id.ambientTemperatureTxt);
		mHumValue = (TextView) findViewById(R.id.humidityTxt);
		mStatus = (TextView) findViewById(R.id.status);

		speedTextView = (TextView) findViewById(R.id.speedTextView);
		caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
		distanceTextView = (TextView) findViewById(R.id.distanceTextView);

		calcUtil = new CalcUtil();

		// Notify activity that UI has been inflated

		// BLE
		mBtLeService = BluetoothLeService.getInstance();
		// mBluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE);
		mServiceList = new ArrayList<BluetoothGattService>();

		updateSensorList();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

		// Create GATT object
		mBtGatt = BluetoothLeService.getBtGatt();

		// Start service discovery
		if (!mServicesRdy && mBtGatt != null) {
			if (mBtLeService.getNumServices() == 0)
				discoverServices();
			else
				displayServices();
		}

	}

	private void updateSensorList() {
		mEnabledSensors.clear();

		for (int i = 0; i < Sensor.SENSOR_LIST.length; i++) {
			Sensor sensor = Sensor.SENSOR_LIST[i];
			mEnabledSensors.add(sensor);
		}
	}

	private void discoverServices() {
		if (mBtGatt.discoverServices()) {
			Log.i(TAG, "START SERVICE DISCOVERY");
			mServiceList.clear();
			setStatus("Service discovery started");
		} else {
			setError("Service discovery start failed");
		}
	}

	private void displayServices() {
		mServicesRdy = true;

		try {
			mServiceList = mBtLeService.getSupportedGattServices();
		} catch (Exception e) {
			e.printStackTrace();
			mServicesRdy = false;
		}

		// Characteristics descriptor readout done
		if (mServicesRdy) {
			setStatus("Service discovery complete");
			enableSensors(true);
			enableNotifications(true);
		} else {
			setError("Failed to read services");
		}
	}

	private void enableSensors(boolean enable) {
		for (Sensor sensor : mEnabledSensors) {
			UUID servUuid = sensor.getService();
			UUID confUuid = sensor.getConfig();

			// Skip keys
			if (confUuid == null)
				break;

			// Barometer calibration
			// if (confUuid.equals(SensorTag.UUID_BAR_CONF) && enable) {
			// calibrateBarometer();
			// }

			BluetoothGattService serv = mBtGatt.getService(servUuid);
			BluetoothGattCharacteristic charac = serv.getCharacteristic(confUuid);
			byte value = enable ? sensor.getEnableSensorCode() : Sensor.DISABLE_SENSOR_CODE;
			mBtLeService.writeCharacteristic(charac, value);
			mBtLeService.waitIdle(GATT_TIMEOUT);
		}

	}

	private void enableNotifications(boolean enable) {
		for (Sensor sensor : mEnabledSensors) {
			UUID servUuid = sensor.getService();
			UUID dataUuid = sensor.getData();
			BluetoothGattService serv = mBtGatt.getService(servUuid);
			BluetoothGattCharacteristic charac = serv.getCharacteristic(dataUuid);

			mBtLeService.setCharacteristicNotification(charac, enable);
			mBtLeService.waitIdle(GATT_TIMEOUT);
		}
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@SuppressLint("InlinedApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
					BluetoothGatt.GATT_SUCCESS);

			if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					// TODO
					displayServices();
					// checkOad();
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
				// Data written
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				onCharacteristicWrite(uuidStr, status);
			} else if (BluetoothLeService.ACTION_DATA_READ.equals(action)) {
				// Data read
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				byte[] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
				onCharacteristicsRead(uuidStr, value, status);
			}

			if (status != BluetoothGatt.GATT_SUCCESS) {
				setError("GATT error code: " + status);
			}
		}
	};

	private void onCharacteristicWrite(String uuidStr, int status) {
		Log.d(TAG, "onCharacteristicWrite: " + uuidStr);
	}

	public void onCharacteristicChanged(String uuidStr, byte[] rawValue) {
		Point3D v;
		String msg;

		if (uuidStr.equals(UUID_ACC_DATA.toString())) {
			v = Sensor.ACCELEROMETER.convert(rawValue);
			msg = decimal.format(v.x) + "\n" + decimal.format(v.y) + "\n" + decimal.format(v.z)
					+ "\n";
			mAccValue.setText(msg);
			calcUtil.setAcceleration(new V3(v.x, v.y, v.z));
			// TODO
			V3 t = calcUtil.getSpeed(System.currentTimeMillis());
			double speed = Math.sqrt(t.x * t.x + t.y * t.y + t.z * t.z);
			speedTextView.setText("x: " + t.x + " y: " + t.y + " y: " + t.z + " speed: " + speed);
			caloriesTextView.setText("" + calcUtil.getCalories(70));
			distanceTextView.setText("distance:" + calcUtil.getDistance());
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

	private void onCharacteristicsRead(String uuidStr, byte[] value, int status) {
		Log.i(TAG, "onCharacteristicsRead: " + uuidStr);
		// if (uuidStr.equals(SensorTag.UUID_BAR_CALI.toString())) {
		// Log.i(TAG, "CALIBRATED");
		// // Barometer calibration values are read.
		// List<Integer> cal = new ArrayList<Integer>();
		// for (int offset = 0; offset < 8; offset += 2) {
		// Integer lowerByte = (int) value[offset] & 0xFF;
		// Integer upperByte = (int) value[offset + 1] & 0xFF;
		// cal.add((upperByte << 8) + lowerByte);
		// }
		//
		// for (int offset = 8; offset < 16; offset += 2) {
		// Integer lowerByte = (int) value[offset] & 0xFF;
		// Integer upperByte = (int) value[offset + 1];
		// cal.add((upperByte << 8) + lowerByte);
		// }
		//
		// BarometerCalibrationCoefficients.INSTANCE.barometerCalibrationCoefficients = cal;
		// }
	}

	void setStatus(String txt) {
		mStatus.setText(txt);
		// mStatus.setTextAppearance(this, R.style.statusStyle_Success);
	}

	void setError(String txt) {
		mStatus.setText(txt);
		// mStatus.setTextAppearance(this, R.style.statusStyle_Failure);
	}

	void setBusy(boolean f) {
		// if (f)
		// mStatus.setTextAppearance(this, R.style.statusStyle_Busy);
		// else
		// mStatus.setTextAppearance(this, R.style.statusStyle);
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		if (!mIsReceiving) {
			registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
			mIsReceiving = true;
		}
		updateVisibility();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		if (mIsReceiving) {
			unregisterReceiver(mGattUpdateReceiver);
			mIsReceiving = false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// TODO
		// unregisterReceiver(mGattUpdateReceiver);
	}

	void updateVisibility() {
		showItem(ID_ACC);
		showItem(ID_AMB);
		showItem(ID_HUM);
	}

	private void showItem(int id) {
		View hdr = table.getChildAt(id * 2 + ID_OFFSET);
		View txt = table.getChildAt(id * 2 + ID_OFFSET + 1);
		int vc = View.VISIBLE;
		hdr.setVisibility(vc);
		txt.setVisibility(vc);
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter fi = new IntentFilter();
		fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		fi.addAction(BluetoothLeService.ACTION_DATA_WRITE);
		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
		return fi;
	}

}
