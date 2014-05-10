package edu.tj.sse.runeveryday.service;

import java.text.NumberFormat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.ui.MainActivity;

/*
 * A service using to present weather notification
 */
public class WeatherNotificationService extends Service{

	private Notification.Builder mBuilder;
	private NotificationManager weatherNM;
	private final int WeatherNotification_ID=25846; //An identifier for this notification unique within application
	private RemoteViews contentView;
	private NumberFormat nt = NumberFormat.getPercentInstance();
	
	@Override  
    public void onCreate() {
        super.onCreate();
        //设置百分数精确度2即保留两位小数
      	nt.setMinimumFractionDigits(2);
        weatherNM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder=new Notification.Builder(WeatherNotificationService.this);
        mBuilder.setSmallIcon(R.drawable.temperature)
        		.setAutoCancel(true);
        
        contentView=new RemoteViews(getPackageName(),R.layout.notification);
        
        Intent notificationIntent = new Intent(this, MainActivity.class);//when click turn to main Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,  
                notificationIntent, 0);
        
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);
        startForeground(WeatherNotification_ID, mBuilder.build());
    }
	
	/*
	 * @intent should be binded with values which to be presented
	 */
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		double temperature=intent.getDoubleExtra("temperature", 0.0);
		double humitidy=intent.getDoubleExtra("hunitidy", 0.0);
		
		contentView.setTextViewText(R.id.temperature_value, ""+temperature);
		contentView.setTextViewText(R.id.humidity_value, nt.format(humitidy));
		
		weatherNM.notify(WeatherNotification_ID, mBuilder.build());
        return super.onStartCommand(intent, flags, startId);  
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();  
    }  
  
    @Override
    public IBinder onBind(Intent intent) {  
        return null;  
    } 
}
