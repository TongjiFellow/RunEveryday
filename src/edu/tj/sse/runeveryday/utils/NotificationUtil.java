package edu.tj.sse.runeveryday.utils;

import edu.tj.sse.runeveryday.service.WeatherNotificationService;
import android.app.Application;
import android.content.Intent;

/*
 * A tool which can send messages to WeatherNotificationService
 * You can also directly interact with WeatherNotificationService, 
 * 	then this is a example
 */
public class NotificationUtil{

	/*
	 * AppContext is used to get the Context object
	 * Attention::it should be registered in AndroidManifest.xml
	 */
	public  static class AppContext extends Application {
		private static AppContext instance;

		public static AppContext getInstance() {
			return instance;
		}
	    @Override
	    public void onCreate() {
	        // TODO Auto-generated method stub
	        super.onCreate();
	        instance = this;
	    }
	}
	
	public static void updateWeather(double temperature,double humidity){
		Intent startIntent = new Intent(AppContext.getInstance(), WeatherNotificationService.class);
		startIntent.putExtra("temperature", temperature);
		startIntent.putExtra("humidity", humidity);
		AppContext.getInstance().startService(startIntent);
	}
	
	public static void stopWeatherNotificationService(){
		Intent stopIntent = new Intent(AppContext.getInstance(), WeatherNotificationService.class);
		AppContext.getInstance().stopService(stopIntent);
	}
}
