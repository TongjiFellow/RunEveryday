package edu.tj.sse.runeveryday.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.tj.sse.runeveryday.database.entity.RunData;
import edu.tj.sse.runeveryday.service.BluetoothLeService;
import edu.tj.sse.runeveryday.service.Sensor;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class BaseSensorTagActivity extends BaseActivity {
	private static String TAG = "BaseSensorTagActivity";
	// BLE
	protected BluetoothLeService mBtLeService = null;
	protected BluetoothGatt mBtGatt = null;
	protected BluetoothManager mBtmManager = null;
	protected List<BluetoothGattService> mServiceList = null;
	protected static final int GATT_TIMEOUT = 100; // milliseconds
	protected boolean mServicesRdy = false;
	protected List<Sensor> mEnabledSensors = new ArrayList<Sensor>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mBtLeService = BluetoothLeService.getInstance();

		if (mBtLeService != null) {
			mBtGatt = BluetoothLeService.getBtGatt();
			mBtmManager = BluetoothLeService.getBtManager();
			List<BluetoothDevice> device = mBtmManager
					.getDevicesMatchingConnectionStates(BluetoothProfile.GATT, new int[] {
							BluetoothProfile.STATE_CONNECTED, BluetoothProfile.STATE_CONNECTING });
			if (device == null || device.size() == 0) {
				showDialog();
			}
		} else {
			showDialog();
		}
		mServiceList = new ArrayList<BluetoothGattService>();

		updateSensorList();

		// Start service discovery
		if (!mServicesRdy && mBtGatt != null) {
			if (mBtLeService.getNumServices() == 0)
				discoverServices();
			else
				displayServices();
		}
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(BaseSensorTagActivity.this);
		builder.setTitle("没有连接SensorTag，去连接SensorTag？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(BaseSensorTagActivity.this, StateActivity.class);
				startActivity(intent);
				finish();
			}
		});
		builder.create().show();
	}

	protected void updateSensorList() {
		mEnabledSensors.clear();
		for (int i = 0; i < Sensor.SENSOR_LIST.length; i++) {
			Sensor sensor = Sensor.SENSOR_LIST[i];
			mEnabledSensors.add(sensor);
		}
	}

	protected void discoverServices() {
		if (mBtGatt.discoverServices()) {
			Log.i(TAG, "START SERVICE DISCOVERY");
			mServiceList.clear();
		} else {
		}
	}

	protected void displayServices() {
		mServicesRdy = true;

		try {
			mServiceList = mBtLeService.getSupportedGattServices();
		} catch (Exception e) {
			e.printStackTrace();
			mServicesRdy = false;
		}

		// Characteristics descriptor readout done
		if (mServicesRdy) {
			// setStatus("Service discovery complete");
			enableSensors(true);
			enableNotifications(true);
		} else {
			// setError("Failed to read services");
		}
	}

	protected void enableSensors(boolean enable) {
		for (Sensor sensor : mEnabledSensors) {
			UUID servUuid = sensor.getService();
			UUID confUuid = sensor.getConfig();

			// Skip keys
			if (confUuid == null)
				break;

			BluetoothGattService serv = mBtGatt.getService(servUuid);
			BluetoothGattCharacteristic charac = serv.getCharacteristic(confUuid);
			byte value = enable ? sensor.getEnableSensorCode() : Sensor.DISABLE_SENSOR_CODE;
			mBtLeService.writeCharacteristic(charac, value);
			mBtLeService.waitIdle(GATT_TIMEOUT);
		}
	}

	protected void enableNotifications(boolean enable) {
		for (Sensor sensor : mEnabledSensors) {
			UUID servUuid = sensor.getService();
			UUID dataUuid = sensor.getData();
			BluetoothGattService serv = mBtGatt.getService(servUuid);
			BluetoothGattCharacteristic charac = serv.getCharacteristic(dataUuid);

			mBtLeService.setCharacteristicNotification(charac, enable);
			mBtLeService.waitIdle(GATT_TIMEOUT);
		}
	}

	protected static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter fi = new IntentFilter();
		fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		fi.addAction(BluetoothLeService.ACTION_DATA_WRITE);
		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
		return fi;
	}

}
