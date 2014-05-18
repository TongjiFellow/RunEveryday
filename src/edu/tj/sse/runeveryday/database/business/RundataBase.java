package edu.tj.sse.runeveryday.database.business;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.model.XYSeries;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import edu.tj.sse.runeveryday.database.DatabaseHelper;
import edu.tj.sse.runeveryday.database.entity.RunData;

public class RundataBase {
	
	private DatabaseHelper dbhDatabaseHelper;
	private RuntimeExceptionDao<RunData,Integer> rundataDao;
	private Context context;
	private SimpleDateFormat formater;
	private final String Tag="RundataBase";
	public RundataBase(Context context){
		this.context=context;
		dbhDatabaseHelper = new DatabaseHelper(context);
		rundataDao=dbhDatabaseHelper.getRundataDataDao();
		formater=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/*
	 * 存储数据
	 */
	public void addRundata(RunData rundata){
		rundataDao.create(rundata);
	}
	
	/*
	 * 更新数据
	 */
	public void updateRundata(RunData rundata){
		rundataDao.update(rundata);
	}
	
	/*
	 * 查询跑步次数
	 */
	public int queryRunningNum(){
		int total_number=0;
		GenericRawResults<String[]> rawResults1 = rundataDao
				.queryRaw("select count(id) from rundata");
		List<String[]> results1 = null;
		try {
			results1 = rawResults1.getResults();
			for (int k = 0; k < results1.size(); k++) {
				for (int n = 0; n < results1.get(k).length; n++) {
					total_number = Integer.parseInt(results1.get(k)[n]);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total_number;
	}
	
	/*
	 * 查询跑步总用时，单位小时
	 */
	public float querySumRunningTime(){
		float time=0f;
		GenericRawResults<String[]> rawResults2 = rundataDao
				.queryRaw("select sum(usetime) from rundata");
		List<String[]> results2 = null;
		try {
			results2 = rawResults2.getResults();
			results2 = rawResults2.getResults();
			for (int k = 0; k < results2.size(); k++) {
				for (int n = 0; n < results2.get(k).length; n++) {
					int total_seconds = Integer.parseInt(results2.get(k)[n]);
					time = (float) total_seconds / 3600.0f;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	
	/*
	 * 查询跑步总里程
	 * 单位公里
	 */
	public float querySumRunningDistance(){
		float distance=0f;
		GenericRawResults<String[]> rawResults3 = rundataDao
				.queryRaw("select sum(distance) from rundata");
		List<String[]> results3 = null;
		try {
			results3 = rawResults3.getResults();
			for (int k = 0; k < results3.size(); k++) {
				for (int n = 0; n < results3.get(k).length; n++) {
					int total_meter = Integer.parseInt(results3.get(k)[n]);
					distance = (float) total_meter / 1000.0f;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return distance;
	}
	public XYSeries getDayHistoryData() {
		XYSeries series=new XYSeries("");
		Date date=new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		String queryString="select sum(usetime),substr(datetime,12,2) from rundata where datetime(datetime) > '"+formater.format(date)+"' group by substr(datetime,12,2)";
		Log.d(Tag, "getDayHistoryData's query string:"+queryString);
		
		GenericRawResults<String[]> rawResult=rundataDao.queryRaw(queryString);
		try {
			List<String[]> result=rawResult.getResults();
			for(int n=0;n<result.size();n++){
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				Log.d(Tag, "x:"+x+" y:"+y);
				
				series.add(x, y/3600);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return series;
	}

	public XYSeries getWeekHistoryData() {
		XYSeries series=new XYSeries("");
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date=cal.getTime();
		String queryString="select sum(usetime),substr(datetime,9,2) from rundata where datetime(datetime) > '"+formater.format(date)+"' group by substr(datetime,9,2)";
		Log.d(Tag, "getWeekHistoryData's query string:"+queryString);
		
		GenericRawResults<String[]> rawResult=rundataDao.queryRaw(queryString);
		try {
			List<String[]> result=rawResult.getResults();
			for(int n=0;n<result.size();n++){
				
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				int sunday=cal.get(Calendar.DAY_OF_MONTH);
				series.add(x-(double)sunday, y/3600);
				
				Log.d(Tag, "x:"+x+" y:"+y);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return series;
	}

	public XYSeries getMonthHistoryData() {
		XYSeries series=new XYSeries("");
		ArrayList<XYSeries> list = new ArrayList<XYSeries>();
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date=cal.getTime();
		String queryString="select sum(usetime),substr(datetime,9,2) from rundata where datetime(datetime) > '"+formater.format(date)+"' group by substr(datetime,9,2)";
		Log.d(Tag, "getMonthHistoryData's query string:"+queryString);
		
		GenericRawResults<String[]> rawResult=rundataDao.queryRaw(queryString);
		try {
			List<String[]> result=rawResult.getResults();
			for(int n=0;n<result.size();n++){
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				Log.d(Tag, "x:"+x+" y:"+y/3600);
				
				series.add(x, y);
				list.add(series);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return series;
	}

	public XYSeries getYearHistoryData() {
		XYSeries series=new XYSeries("");
		ArrayList<XYSeries> list = new ArrayList<XYSeries>();
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date date=cal.getTime();
		String queryString="select sum(usetime),substr(datetime,6,2) from rundata where datetime(datetime) > '"+formater.format(date)+"' group by substr(datetime,6,2)";
		Log.d(Tag, "getYearHistoryData's query string"+queryString);
		
		GenericRawResults<String[]> rawResult=rundataDao.queryRaw(queryString);
		try {
			List<String[]> result=rawResult.getResults();
			for(int n=0;n<result.size();n++){
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				Log.d(Tag, "x:"+x+" y:"+y);
				
				series.add(x, y/3600);
				list.add(series);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return series;
	}

}
