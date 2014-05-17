package edu.tj.sse.runeveryday.utils;

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

	public ArrayList<XYSeries> getDayHistoryData() {
		ArrayList<XYSeries> list = new ArrayList<XYSeries>();
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
				XYSeries series=new XYSeries("");
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				Log.d(Tag, "x:"+x+" y:"+y);
				
				series.add(x, y);
				list.add(series);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<XYSeries> getWeekHistoryData() {
		ArrayList<XYSeries> list = new ArrayList<XYSeries>();
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
				Log.d(Tag, "x:"+result.get(n)[1]+" y:"+result.get(n)[0]);
				
				XYSeries series=new XYSeries("");
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				int sunday=cal.get(Calendar.DAY_OF_MONTH);
				
				Log.d(Tag, "x:"+x+" y:"+y+"");
				series.add(x-(double)sunday, y);
				list.add(series);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<XYSeries> getMonthHistoryData() {
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
				Log.d(Tag, "x:"+result.get(n)[1]+" y:"+result.get(n)[0]);
				
				XYSeries series=new XYSeries("");
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				series.add(x, y);
				list.add(series);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<XYSeries> getYearHistoryData() {
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
				Log.d(Tag, "x:"+result.get(n)[1]+" y:"+result.get(n)[0]);
				
				XYSeries series=new XYSeries("");
				double y=Double.parseDouble(result.get(n)[0]);
				double x=Double.parseDouble(result.get(n)[1]);
				
				series.add(x, y);
				list.add(series);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

}
