package edu.tj.sse.runeveryday.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import edu.tj.sse.runeveryday.database.entity.Plan;
import edu.tj.sse.runeveryday.database.entity.RunData;
import edu.tj.sse.runeveryday.database.entity.Training;
import edu.tj.sse.runeveryday.database.entity.User;
import edu.tj.sse.runeveryday.database.entity.V3Data;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	// 数据库名称
	private static final String DATABASE_NAME = "runeveryday.db";
	// 数据库version
	private static final int DATABASE_VERSION = 1;

	private Dao<User, Integer> userDao = null;
	private Dao<RunData, Integer> rundataDao = null;
	private Dao<Plan, Integer> planDao = null;
	private Dao<Training, Integer> trainingDao = null;
	private Dao<V3Data, Integer> v3dataDao = null;

	/*
	 * RuntimeExceptionDao这个东西是针对JDBC和一些其他的SQL的。对于Android平
	 * 台主要是处理了过多繁琐的try…catch…的书写，和一些语法错误带来的崩溃，建议使用 。
	 */
	private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;
	private RuntimeExceptionDao<RunData, Integer> rundataRuntimeDao = null;
	private RuntimeExceptionDao<Plan, Integer> planRuntimeDao = null;
	private RuntimeExceptionDao<Training, Integer> trainingRuntimeDao = null;
	private RuntimeExceptionDao<V3Data, Integer> v3dataRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		File databasefile=new File(context.getCacheDir().getParent()+File.separator+"databases"+File.separator+DATABASE_NAME);
		//Log.d(TAG, "databasefile:"+databasefile.getAbsolutePath());
		try {
	        int byteread = 0;
			if(!databasefile.exists()){
				databasefile.getParentFile().mkdirs();
			}
			InputStream inStream=context.getResources().getAssets().open(DATABASE_NAME);
			FileOutputStream fs = new FileOutputStream(databasefile);
			byte[] buffer = new byte[1444];
            while ( (byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 可以用配置文件来生成 数据表，有点繁琐，不喜欢用
		// super(context, DATABASE_NAME, null, DATABASE_VERSION,
		// R.raw.ormlite_config);
	}

	public DatabaseHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			// 建立User表
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, RunData.class);
			TableUtils.createTable(connectionSource, Plan.class);
			TableUtils.createTable(connectionSource, Training.class);
			TableUtils.createTable(connectionSource, V3Data.class);
			// 初始化DAO
			userDao = getUserDao();
			rundataDao = getRunDataDao();
			planDao = getPlanDao();
			trainingDao=getTrainingDao();
			v3dataDao=getV3DataDao();
			
			userRuntimeDao = getUserDataDao();
			rundataRuntimeDao = getRundataDataDao();
			planRuntimeDao = getPlanDataDao();
			trainingRuntimeDao=getTrainingRuntimeDao();
			v3dataRuntimeDao=getV3DataRuntimeDao();
		} catch (SQLException e) {
			Log.e(TAG + "创建数据库失败", e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, RunData.class, true);
			TableUtils.dropTable(connectionSource, Plan.class, true);
			TableUtils.dropTable(connectionSource, Training.class, true);
			TableUtils.dropTable(connectionSource, V3Data.class, true);
		} catch (SQLException e) {
			Log.e(TAG + "更新数据库失败", e.toString());
			e.printStackTrace();
		}
	}

	public Dao<User, Integer> getUserDao() throws SQLException {
		if (userDao == null)
			userDao = getDao(User.class);
		return userDao;
	}

	public Dao<RunData, Integer> getRunDataDao() throws SQLException {
		if (rundataDao == null)
			rundataDao = getDao(RunData.class);
		return rundataDao;
	}

	public Dao<Plan, Integer> getPlanDao() throws SQLException {
		if (planDao == null)
			planDao = getDao(Plan.class);
		return planDao;
	}
	public Dao<Training, Integer> getTrainingDao() throws SQLException {
		if (trainingDao == null)
			trainingDao = getDao(Training.class);
		return trainingDao;
	}
	public Dao<V3Data, Integer> getV3DataDao() throws SQLException {
		if (v3dataDao == null)
			v3dataDao = getDao(V3Data.class);
		return v3dataDao;
	}

	public RuntimeExceptionDao<User, Integer> getUserDataDao() {
		if (userRuntimeDao == null) {
			userRuntimeDao = getRuntimeExceptionDao(User.class);
		}
		return userRuntimeDao;
	}

	public RuntimeExceptionDao<RunData, Integer> getRundataDataDao() {
		if (rundataRuntimeDao == null) {
			rundataRuntimeDao = getRuntimeExceptionDao(RunData.class);
		}
		return rundataRuntimeDao;
	}

	public RuntimeExceptionDao<Plan, Integer> getPlanDataDao() {
		if (planRuntimeDao == null) {
			planRuntimeDao = getRuntimeExceptionDao(Plan.class);
		}
		return planRuntimeDao;
	}
	public RuntimeExceptionDao<Training, Integer> getTrainingRuntimeDao() {
		if (trainingRuntimeDao == null) {
			trainingRuntimeDao = getRuntimeExceptionDao(Training.class);
		}
		return trainingRuntimeDao;
	}
	public RuntimeExceptionDao<V3Data, Integer> getV3DataRuntimeDao() {
		if (v3dataRuntimeDao == null) {
			v3dataRuntimeDao = getRuntimeExceptionDao(V3Data.class);
		}
		return v3dataRuntimeDao;
	}

	/**
	 * 释放 DAO
	 */
	@Override
	public void close() {
		super.close();
		userRuntimeDao = null;
		rundataRuntimeDao = null;
		planRuntimeDao = null;
		trainingRuntimeDao=null;
		v3dataRuntimeDao=null;
		
		userDao = null;
		rundataDao = null;
		planDao = null;
		trainingDao=null;
		v3dataDao=null;
	}

}
