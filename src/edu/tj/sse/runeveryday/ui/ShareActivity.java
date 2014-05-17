package edu.tj.sse.runeveryday.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import edu.tj.sse.runeveryday.R;

public class ShareActivity extends Activity implements OnClickListener {

	public static String shareString = null;
	public static Bitmap Bmp = null;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);
		shareString = "RunEveryDay是一款不错的跑步的软件，我正在使用，你也快来用吧！";
		button = (Button) findViewById(R.id.Share_sina);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.Share_Qzone);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.Share_other);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Share_sina:

			break;
		case R.id.Share_Qzone:

			break;
			
		case R.id.Share_other:

			break;
		}
	}
}