package edu.tj.sse.runeveryday.database.business;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import edu.tj.sse.runeveryday.database.DatabaseHelper;
import edu.tj.sse.runeveryday.database.entity.V3Data;

public class V3DataBase {
	
	private DatabaseHelper dbhDatabaseHelper;
	private RuntimeExceptionDao<V3Data,Integer> v3dataDao;
	private Context context;
	
	public V3DataBase(Context context){
		this.context=context;
		dbhDatabaseHelper=new DatabaseHelper(this.context);
		v3dataDao=dbhDatabaseHelper.getV3DataRuntimeDao();
	}
	
	/*
	 * add a V3data into database
	 */
	public void addV3Data(V3Data v3Data){
		v3dataDao.create(v3Data);
	}
}
