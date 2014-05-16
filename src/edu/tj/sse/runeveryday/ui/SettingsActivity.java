package edu.tj.sse.runeveryday.ui;

import java.io.File;
import java.util.Locale;

import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.service.WeatherNotificationService;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener,     
			OnPreferenceClickListener{

	private String Tag="SettingsActivity";
	private SwitchPreference choose_language;
	private SwitchPreference apply_voice;
	private SwitchPreference is_notify;
	private Preference sensor_manage;
	private Preference share_app;
	private Preference app_about;
	private Preference app_reset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_settings);
		choose_language=(SwitchPreference)findPreference("app_language");
		apply_voice=(SwitchPreference)findPreference("apply_voice");
		is_notify=(SwitchPreference)findPreference("app_isnotify");
		choose_language.setOnPreferenceChangeListener(this);
		apply_voice.setOnPreferenceChangeListener(this);
		is_notify.setOnPreferenceChangeListener(this);
	}
	
	@Override  
    public boolean onPreferenceChange(Preference preference, Object newValue) {
		//Log.d(Tag, "onPreferenceChange:"+preference.getTitle()+",new Value:"+(Boolean)newValue);
		if(preference.getKey().equals("app_language")){
			Locale language=null;
			if((Boolean)newValue){//English
				language=Locale.ENGLISH;
			}
			else{//Chinese
				language=Locale.SIMPLIFIED_CHINESE;
			}
			Resources resource = getResources();  
			Configuration config = resource.getConfiguration();
			config.locale = language;
			resource.updateConfiguration(config, null);
			
			//restart the application
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);  
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			this.startActivity(intent); 
			
			//SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			//Boolean language=settings.getBoolean("app_language", false);
			//Log.d(Tag, "app_language value is:"+language);
		}
		else if(preference.getKey().equals("apply_voice")){
			//do nothing, just store the new value
		}
		else if(preference.getKey().equals("app_isnotify")){
			//stop the notification service when turn off
			if(!(Boolean)newValue){
				Intent stopIntent = new Intent(this, WeatherNotificationService.class);
				stopService(stopIntent);
			}
		}
		return true;
	}
	
	@Override  
    public boolean onPreferenceClick(Preference preference) {
		return true;
	}
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		//Log.d(Tag, "onPreferenceTreeClick:"+preference.getTitle());
		if(preference.getKey().equals("sensor_manage")){
			
		}
		else if(preference.getKey().equals("share_app")){
			//not the share function
			//use to test the weatherNotificationService. Done\Success
			Intent startIntent = new Intent(this, WeatherNotificationService.class);
			startIntent.putExtra("temperature", 23.5);
			startIntent.putExtra("humidity", 0.345);
			startService(startIntent);
		}
		else if(preference.getKey().equals("app_about")){
			Builder adInfo=new AlertDialog.Builder(this);  
	        adInfo.setTitle(R.string.about_title);
	        adInfo.setMessage(R.string.about_content);
	        adInfo.show();
		}
		else if(preference.getKey().equals("app_reset")){
			//clear all data include cache SharedPreferences database
			String datapath=this.getCacheDir().getParent();
			delFolder(datapath);
			
			//exit settings activity, not use finish it will store the current settings
			//or use: android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
		return false;
	}
	private void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹

        }
        catch (Exception e) {
        	Log.e(Tag,"删除文件夹操作出错");
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
                }
                else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文件
                    delFolder(path+"/"+ tempList[i]);//再删除空文件夹
                }
        }
	}
	private void setViewFontSize(View view,int size)  
	{  
	    if(view instanceof ViewGroup)  
	    {  
	        ViewGroup parent = (ViewGroup)view;  
	        int count = parent.getChildCount();  
	        for (int i = 0; i < count; i++)  
	        {  
	            setViewFontSize(parent.getChildAt(i),size);  
	        }  
	    }  
	    else if(view instanceof TextView){  
	        TextView textview = (TextView)view;  
	        textview.setTextSize(size);  
	    }  
	}
}
