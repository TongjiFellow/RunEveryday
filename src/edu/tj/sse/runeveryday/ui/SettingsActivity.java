package edu.tj.sse.runeveryday.ui;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.business.RundataBase;
import edu.tj.sse.runeveryday.service.WeatherNotificationService;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener,
		OnPreferenceClickListener {

	private final String Tag = "SettingsActivity";

	private SwitchPreference choose_language;
	private SwitchPreference apply_voice;
	private SwitchPreference is_notify;
	private SwitchPreference app_isAlarm;
	private SwitchPreference auto_connect;
	private Preference share_app;
	private Preference app_about;
	private Preference app_reset;
	private AlarmManager alarmManager;

	private Boolean last_isAlarm = false;
	private long DAY = 1000 * 60 * 60 * 24;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		addPreferencesFromResource(R.xml.activity_settings);
		choose_language = (SwitchPreference) findPreference("app_language");
		apply_voice = (SwitchPreference) findPreference("apply_voice");
		is_notify = (SwitchPreference) findPreference("app_isnotify");
		app_isAlarm = (SwitchPreference) findPreference("app_isAlarm");
		auto_connect = (SwitchPreference) findPreference("auto_connect");
		app_isAlarm.setOnPreferenceChangeListener(this);
		choose_language.setOnPreferenceChangeListener(this);
		apply_voice.setOnPreferenceChangeListener(this);
		is_notify.setOnPreferenceChangeListener(this);
		auto_connect.setOnPreferenceChangeListener(this);

		//get the system locale
		Configuration config = getResources().getConfiguration();
		if(config.locale.equals(Locale.SIMPLIFIED_CHINESE)){
			choose_language.setChecked(false);
		}
		else{
			choose_language.setChecked(true);
		}
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(SettingsActivity.this);
		app_isAlarm.setSummary(settings.getString("last_alarm", ""));
		last_isAlarm = settings.getBoolean("app_isAlarm", false);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.getKey().equals("app_isAlarm")) {
			if (last_isAlarm.equals(newValue))
				return true;
			Log.d("Settings", "last_isAlarm:" + last_isAlarm + " newValue:" + (Boolean) newValue);
			last_isAlarm = (Boolean) newValue;
			if ((Boolean) newValue) {
				// 当前设备上的系统时间
				Calendar cal = Calendar.getInstance();
				// 弹出设置时间的窗口
				new TimePickerDialog(this, new OnTimeSetListener() {

					// 会被运行两遍
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// 启动指定组件
						if (app_isAlarm.isChecked())
							return;
						Intent intent = new Intent(SettingsActivity.this, AlarmActivity.class);
						// 创建PendingIntent对象，封装Intent
						PendingIntent pi = PendingIntent.getActivity(SettingsActivity.this, 0,
								intent, 0);
						Calendar setCal = Calendar.getInstance();
						// 根据用户选择的时间设置定时器时间
						// setCal.setTimeInMillis(System.currentTimeMillis());
						// setCal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
						setCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
						setCal.set(Calendar.MINUTE, minute);
						// 设置AlarmManager将在Calendar对应的时间启动指定组件
						// long firstTime = SystemClock.elapsedRealtime();
						long systemTime = System.currentTimeMillis();
						Log.d("Settings",
								Long.toString(systemTime) + " "
										+ Long.toString(setCal.getTimeInMillis()) + " " + setCal);
						if (systemTime > setCal.getTimeInMillis()) {
							setCal.add(Calendar.DAY_OF_MONTH, 1);
						}
						// long timegap=setCal.getTimeInMillis()-systemTime;
						// firstTime+=timegap;
						// Log.d("Settings","firstTime:"+Long.toString(firstTime));
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
								setCal.getTimeInMillis(), DAY, pi);

						String tmpS = format(hourOfDay) + ":" + format(minute);
						app_isAlarm.setSummary(tmpS);
						app_isAlarm.setChecked(true);
						SharedPreferences.Editor editor = PreferenceManager
								.getDefaultSharedPreferences(SettingsActivity.this).edit();
						editor.putString("last_alarm", tmpS);
						editor.commit();
						// 显示闹铃设置成功的提示信息
						Toast.makeText(SettingsActivity.this, "闹铃设置成功", Toast.LENGTH_SHORT).show();
					}
				}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
				return false;
			} else {
				Intent intent = new Intent(SettingsActivity.this, AlarmActivity.class);
				PendingIntent sender = PendingIntent.getActivity(SettingsActivity.this, 0, intent,
						0);
				// 由AlarmManager中移除
				// AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
				alarmManager.cancel(sender);
				SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
						SettingsActivity.this).edit();
				editor.putString("last_alarm", "");
				editor.commit();
				app_isAlarm.setSummary("");
				// 以Toast提示已删除设定，并更新显示的闹钟时间
				Toast.makeText(SettingsActivity.this, "闹钟已经取消", Toast.LENGTH_LONG).show();
			}
		} else if (preference.getKey().equals("app_language")) {
			Locale language = null;
			if ((Boolean) newValue) {// English
				language = Locale.ENGLISH;
			} else {// Chinese
				language = Locale.SIMPLIFIED_CHINESE;
			}
			Resources resource = getResources();
			Configuration config = resource.getConfiguration();
			config.locale = language;
			resource.updateConfiguration(config, null);

			// restart the application
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);

			// SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			// Boolean language=settings.getBoolean("app_language", false);
			// Log.d(Tag, "app_language value is:"+language);
		} else if (preference.getKey().equals("apply_voice")) {
			// do nothing, just store the new value
		} else if (preference.getKey().equals("app_isnotify")) {
			// stop the notification service when turn off
			if (!(Boolean) newValue) {
				Intent stopIntent = new Intent(this, WeatherNotificationService.class);
				stopService(stopIntent);
			}
		} else if (preference.getKey().equals("auto_connect")) {

		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		return true;
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		// Log.d(Tag, "onPreferenceTreeClick:"+preference.getTitle());
		if (preference.getKey().equals("share_app")) {
			String sharewords="我正在使用天天跑步，很不错哟！";
			ShareActivity.shareString=sharewords;
			Intent intent=new Intent(SettingsActivity.this,ShareActivity.class);
			startActivity(intent);
			
		} else if (preference.getKey().equals("app_about")) {
			Builder adInfo = new AlertDialog.Builder(this);
			adInfo.setTitle(R.string.about_title);
			adInfo.setMessage(R.string.about_content);
			adInfo.show();
		} else if (preference.getKey().equals("app_reset")) {
			// clear all data include cache SharedPreferences database
			String datapath = this.getCacheDir().getParent();
			delFolder(datapath);

			// exit settings activity, not use finish it will store the current settings
			// or use: android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
		return false;
	}

	private void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			Log.e(Tag, "删除文件夹操作出错");
			e.printStackTrace();
		}
	}

	private void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	private void setViewFontSize(View view, int size) {
		if (view instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) view;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setViewFontSize(parent.getChildAt(i), size);
			}
		} else if (view instanceof TextView) {
			TextView textview = (TextView) view;
			textview.setTextSize(size);
		}
	}

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
}
