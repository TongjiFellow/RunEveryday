package edu.tj.sse.runeveryday.ui;

import edu.tj.sse.runeveryday.R;
import android.app.Activity;
import android.os.Bundle;

public class DeviceActivity extends Activity {
	public static final String EXTRA_DEVICE = "EXTRA_DEVICE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device);
		
	}
}
