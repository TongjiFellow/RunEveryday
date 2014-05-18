package edu.tj.sse.runeveryday.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.service.BleDeviceInfo;
import edu.tj.sse.runeveryday.service.BluetoothLeService;
import edu.tj.sse.runeveryday.utils.CustomTimer;
import edu.tj.sse.runeveryday.utils.CustomTimerCallback;

public class StateActivity extends BaseActivity {
	private static final String TAG = "ScanView";
	private final int SCAN_TIMEOUT = 10; // Seconds
	private final int CONNECT_TIMEOUT = 10; // Seconds

	// Requests to other activities
	private static final int REQ_ENABLE_BT = 0;
	private static final int REQ_DEVICE_ACT = 1;

	private static final int NO_DEVICE = -1;
	private boolean mInitialised = false;

	private DeviceListAdapter mDeviceAdapter = null;
	private TextView mEmptyMsg;
	private TextView mStatus;
	private Button mBtnScan = null;
	private ListView mDeviceListView = null;
	private ProgressBar mProgressBar;

	private static final int STATUS_DURATION = 5;
	private Intent mDeviceIntent;

	private CustomTimer mScanTimer = null;
	private CustomTimer mConnectTimer = null;
	@SuppressWarnings("unused")
	private CustomTimer mStatusTimer;
	private Context mContext;

	// BLE management
	private boolean mBleSupported = true;
	private boolean mScanning = false;
	private int mNumDevs = 0;
	private int mConnIndex = NO_DEVICE;
	private List<BleDeviceInfo> mDeviceInfoList;
	private static BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBtAdapter = null;
	private BluetoothDevice mBluetoothDevice = null;
	private BluetoothLeService mBluetoothLeService = null;
	private IntentFilter mFilter;
	private String[] mDeviceFilter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_state);
		initSlidingMenu(this);
		
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_LONG).show();
			mBleSupported = false;
		}

		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBtAdapter = mBluetoothManager.getAdapter();

		if (mBtAdapter == null) {
			Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_LONG).show();
			mBleSupported = false;
		}

		mDeviceInfoList = new ArrayList<BleDeviceInfo>();
		Resources res = getResources();
		mDeviceFilter = res.getStringArray(R.array.device_filter);

		mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);

		mStatus = (TextView) findViewById(R.id.status);
		mBtnScan = (Button) findViewById(R.id.btn_scan);
		mDeviceListView = (ListView) findViewById(R.id.device_list);
		mDeviceListView.setClickable(true);
		mDeviceListView.setOnItemClickListener(mDeviceClickListener);
		mEmptyMsg = (TextView) findViewById(R.id.no_device);

		// Progress bar to use during scan and connection
		mProgressBar = (ProgressBar) findViewById(R.id.pb_busy);
		mProgressBar.setMax(SCAN_TIMEOUT);

		onScanViewReady();
	}

	public void onScanViewReady() {
		// Initial state of widgets
		updateGuiState();

		if (!mInitialised) {
			// Broadcast receiver
			registerReceiver(mReceiver, mFilter);

			if (mBtAdapter.isEnabled()) {
				// Start straight away
				startBluetoothLeService();
			} else {
				// Request BT adapter to be turned on
				Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQ_ENABLE_BT);
			}
			mInitialised = true;
		} else {
			notifyDataSetChanged();
		}
	}

	private void startBluetoothLeService() {
		boolean f;

		Intent bindIntent = new Intent(this, BluetoothLeService.class);
		startService(bindIntent);
		f = bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
		if (f)
			Log.d(TAG, "BluetoothLeService - success");
		else {
			Toast.makeText(getApplicationContext(), "Bind to BluetoothLeService failed",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_DEVICE_ACT:
			// When the device activity has finished: disconnect the device
			if (mConnIndex != NO_DEVICE) {
				mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
			}
			break;

		case REQ_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {

				Toast.makeText(this, R.string.bt_on, Toast.LENGTH_SHORT).show();
			} else {
				// User did not enable Bluetooth or an error occurred
				Toast.makeText(this, R.string.bt_not_on, Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		default:
			Log.e(TAG, "Unknown request code");
			break;
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
				// Bluetooth adapter state change
				switch (mBtAdapter.getState()) {
				case BluetoothAdapter.STATE_ON:
					mConnIndex = NO_DEVICE;
					startBluetoothLeService();
					break;
				case BluetoothAdapter.STATE_OFF:
					Toast.makeText(context, R.string.app_closing, Toast.LENGTH_LONG).show();
					finish();
					break;
				default:
					Log.w(TAG, "Action STATE CHANGED not processed ");
					break;
				}

				updateGuiState();
			} else if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				// GATT connect
				int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
						BluetoothGatt.GATT_FAILURE);
				if (status == BluetoothGatt.GATT_SUCCESS) {
					setBusy(false);
					startDeviceActivity();
				} else
					setError("Connect failed. Status: " + status);
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				// GATT disconnect
				int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
						BluetoothGatt.GATT_FAILURE);
				stopDeviceActivity();
				if (status == BluetoothGatt.GATT_SUCCESS) {
					setBusy(false);
					setStatus(mBluetoothDevice.getName() + " disconnected", STATUS_DURATION);
				} else {
					setError("Disconnect failed. Status: " + status);
				}
				mConnIndex = NO_DEVICE;
				mBluetoothLeService.close();
			} else {
				Log.w(TAG, "Unknown action: " + action);
			}

		}
	};

	private void startDeviceActivity() {
		mDeviceIntent = new Intent(this, RunningActivity.class);
		mDeviceIntent.putExtra(DeviceActivity.EXTRA_DEVICE, mBluetoothDevice);
		startActivityForResult(mDeviceIntent, REQ_DEVICE_ACT);
	}

	private void stopDeviceActivity() {
		finishActivity(REQ_DEVICE_ACT);
	}

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName componentName, IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize BluetoothLeService");
				finish();
				return;
			}
			final int n = mBluetoothLeService.numConnectedDevices();
			if (n > 0) {
				runOnUiThread(new Runnable() {
					public void run() {
						setError("Multiple connections!");
					}
				});
			} else {
				startScan();
				Log.i(TAG, "BluetoothLeService connected");
			}
		}

		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
			Log.i(TAG, "BluetoothLeService disconnected");
		}
	};

	public void onBtnScan(View view) {
		if (mScanning) {
			stopScan();
		} else {
			startScan();
		}
	}

	void notifyDataSetChanged() {
		List<BleDeviceInfo> deviceList = mDeviceInfoList;
		if (mDeviceAdapter == null) {
			mDeviceAdapter = new DeviceListAdapter(getApplicationContext(), deviceList);
		}
		mDeviceListView.setAdapter(mDeviceAdapter);
		mDeviceAdapter.notifyDataSetChanged();
		if (deviceList.size() > 0) {
			mEmptyMsg.setVisibility(View.GONE);
		} else {
			mEmptyMsg.setVisibility(View.VISIBLE);
		}
	}

	void updateGui(boolean scanning) {
		if (mBtnScan == null)
			return; // UI not ready
		setBusy(scanning);

		if (scanning) {
			mScanTimer = new CustomTimer(mProgressBar, SCAN_TIMEOUT, mPgScanCallback);
			// TODO
			// mStatus.setTextAppearance(mContext, R.style.statusStyle_Busy);
			mBtnScan.setText("Stop");
			mStatus.setText("Scanning...");
			mEmptyMsg.setText(R.string.nodevice);
			updateGuiState();
		} else {
			// Indicate that scanning has stopped
			// TODO
			// mStatus.setTextAppearance(mContext, R.style.statusStyle_Success);
			mBtnScan.setText("Scan");
			mEmptyMsg.setText(R.string.scan_advice);
			setProgressBarIndeterminateVisibility(false);
			mDeviceAdapter.notifyDataSetChanged();
		}
	}

	public void updateGuiState() {
		boolean mBtEnabled = mBtAdapter.isEnabled();

		if (mBtEnabled) {
			if (mScanning) {
				// BLE Host connected
				if (mConnIndex != NO_DEVICE) {
					String txt = mBluetoothDevice.getName() + " connected";
					setStatus(txt);
				} else {
					setStatus(mNumDevs + " devices");
				}
			}
		} else {
			mDeviceInfoList.clear();
			notifyDataSetChanged();
		}
	}

	private void startScan() {
		// Start device discovery
		if (mBleSupported) {
			mNumDevs = 0;
			mDeviceInfoList.clear();
			notifyDataSetChanged();
			scanLeDevice(true);
			updateGui(mScanning);
			if (!mScanning) {
				setError("Device discovery start failed");
				setBusy(false);
			}
		} else {
			setError("BLE not supported on this device");
		}
	}

	private void stopScan() {
		mScanning = false;
		updateGui(false);
		scanLeDevice(false);
	}

	private boolean scanLeDevice(boolean enable) {
		if (enable) {
			mScanning = mBtAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBtAdapter.stopLeScan(mLeScanCallback);
		}
		return mScanning;
	}

	// Device scan callback.
	// NB! Nexus 4 and Nexus 7 (2012) only provide one scan result per scan
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				public void run() {
					// Filter devices
					if (checkDeviceFilter(device)) {
						if (!deviceInfoExists(device.getAddress())) {
							// New device
							BleDeviceInfo deviceInfo = createDeviceInfo(device, rssi);
							addDevice(deviceInfo);
						} else {
							// Already in list, update RSSI info
							BleDeviceInfo deviceInfo = findDeviceInfo(device);
							deviceInfo.updateRssi(rssi);
							notifyDataSetChanged();
						}
					}
				}
			});
		}
	};

	private BleDeviceInfo createDeviceInfo(BluetoothDevice device, int rssi) {
		BleDeviceInfo deviceInfo = new BleDeviceInfo(device, rssi);
		return deviceInfo;
	}

	private BleDeviceInfo findDeviceInfo(BluetoothDevice device) {
		for (int i = 0; i < mDeviceInfoList.size(); i++) {
			if (mDeviceInfoList.get(i).getBluetoothDevice().getAddress()
					.equals(device.getAddress())) {
				return mDeviceInfoList.get(i);
			}
		}
		return null;
	}

	private boolean checkDeviceFilter(BluetoothDevice device) {
		int n = mDeviceFilter.length;
		if (n > 0) {
			boolean found = false;
			for (int i = 0; i < n && !found; i++) {
				found = device.getName().equals(mDeviceFilter[i]);
			}
			return found;
		} else
			// Allow all devices if the device filter is empty
			return true;
	}

	private void addDevice(BleDeviceInfo device) {
		mNumDevs++;
		mDeviceInfoList.add(device);
		notifyDataSetChanged();
		if (mNumDevs > 1)
			setStatus(mNumDevs + " devices");
		else
			setStatus("1 device");
	}

	private boolean deviceInfoExists(String address) {
		for (int i = 0; i < mDeviceInfoList.size(); i++) {
			if (mDeviceInfoList.get(i).getBluetoothDevice().getAddress().equals(address)) {
				return true;
			}
		}
		return false;
	}

	void setStatus(String txt) {
		mStatus.setText(txt);
		// TODO
		// mStatus.setTextAppearance(mContext, R.style.statusStyle_Success);
	}

	void setStatus(String txt, int duration) {
		setStatus(txt);
		mStatusTimer = new CustomTimer(null, duration, mClearStatusCallback);
	}

	void setError(String txt) {
		setBusy(false);
		stopTimers();
		mStatus.setText(txt);
		// TODO
		// mStatus.setTextAppearance(mContext, R.style.statusStyle_Failure);
	}

	void setBusy(boolean f) {
		if (mProgressBar == null)
			return;
		if (f) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			stopTimers();
			mProgressBar.setVisibility(View.GONE);
		}
	}

	private void stopTimers() {
		if (mScanTimer != null) {
			mScanTimer.stop();
			mScanTimer = null;
		}
		if (mConnectTimer != null) {
			mConnectTimer.stop();
			mConnectTimer = null;
		}
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			Log.d(TAG, "item click");
			mConnectTimer = new CustomTimer(mProgressBar, CONNECT_TIMEOUT, mPgConnectCallback);
			onDeviceClick(pos);
		}
	};

	public void onDeviceClick(int pos) {
		if (mScanning)
			stopScan();

		setBusy(true);
		mBluetoothDevice = mDeviceInfoList.get(pos).getBluetoothDevice();
		if (mConnIndex == NO_DEVICE) {
			setStatus("Connecting");
			mConnIndex = pos;
			onConnect();
		} else {
			setStatus("Disconnecting");
			if (mConnIndex != NO_DEVICE) {
				mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
			}
		}
	}

	void onConnect() {
		if (mNumDevs > 0) {
			int connState = mBluetoothManager.getConnectionState(mBluetoothDevice,
					BluetoothGatt.GATT);

			switch (connState) {
			case BluetoothGatt.STATE_CONNECTED:
				mBluetoothLeService.disconnect(null);
				break;
			case BluetoothGatt.STATE_DISCONNECTED:
				boolean ok = mBluetoothLeService.connect(mBluetoothDevice.getAddress());
				if (!ok) {
					setError("Connect failed");
				}
				break;
			default:
				setError("Device busy (connecting/disconnecting)");
				break;
			}
		}
	}

	private CustomTimerCallback mPgScanCallback = new CustomTimerCallback() {
		public void onTimeout() {
			onScanTimeout();
		}

		public void onTick(int i) {
		}
	};

	// Listener for connect/disconnect expiration
	private CustomTimerCallback mPgConnectCallback = new CustomTimerCallback() {
		public void onTimeout() {
			onConnectTimeout();
		}

		public void onTick(int i) {
		}
	};

	public void onScanTimeout() {
		runOnUiThread(new Runnable() {
			public void run() {
				stopScan();
			}
		});
	}

	public void onConnectTimeout() {
		runOnUiThread(new Runnable() {
			public void run() {
				setError("Connection timed out");
			}
		});
		if (mConnIndex != NO_DEVICE) {
			mBluetoothLeService.disconnect(mBluetoothDevice.getAddress());
			mConnIndex = NO_DEVICE;
		}
	}

	// Listener for connect/disconnect expiration
	private CustomTimerCallback mClearStatusCallback = new CustomTimerCallback() {
		public void onTimeout() {
			runOnUiThread(new Runnable() {
				public void run() {
					setStatus("");
				}
			});
			mStatusTimer = null;
		}

		public void onTick(int i) {
		}
	};

	class DeviceListAdapter extends BaseAdapter {
		private List<BleDeviceInfo> mDevices;
		private LayoutInflater mInflater;

		public DeviceListAdapter(Context context, List<BleDeviceInfo> devices) {
			mInflater = LayoutInflater.from(context);
			mDevices = devices;
		}

		public int getCount() {
			return mDevices.size();
		}

		public Object getItem(int position) {
			return mDevices.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewGroup vg;

			if (convertView != null) {
				vg = (ViewGroup) convertView;
			} else {
				vg = (ViewGroup) mInflater.inflate(R.layout.element_device, null);
			}

			BleDeviceInfo deviceInfo = mDevices.get(position);
			BluetoothDevice device = deviceInfo.getBluetoothDevice();
			int rssi = deviceInfo.getRssi();
			String descr = device.getName() + "\n" + device.getAddress() + "\nRssi: " + rssi
					+ " dBm";
			((TextView) vg.findViewById(R.id.descr)).setText(descr);

			return vg;
		}
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
}
