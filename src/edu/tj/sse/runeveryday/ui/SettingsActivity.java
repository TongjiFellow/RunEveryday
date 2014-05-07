package edu.tj.sse.runeveryday.ui;

import edu.tj.sse.runeveryday.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_settings);
		addPreferencesFromResource(R.xml.activity_settings);
		
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return false;
	}
}
