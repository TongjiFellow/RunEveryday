package edu.tj.sse.runeveryday.ui;

import edu.tj.sse.runeveryday.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

public class AlarmActivity extends Activity {

	@Override  
	 protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(10*1000);
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alarm_title))
        		.setMessage(getResources().getString(R.string.alarm_content))
        .setPositiveButton(getResources().getString(R.string.alarm_shutdown), new DialogInterface.OnClickListener() {
              
               @Override
               public void onClick(DialogInterface dialog, int which) {
                      // TODO Auto-generated method stub
            	   vibrator.cancel();
            	   AlarmActivity.this.finish();
               }
        }).show();
 }
}
