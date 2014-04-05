package edu.tj.sse.runeveryday.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import edu.tj.sse.runeveryday.database.entity.Plan;
import edu.tj.sse.runeveryday.database.entity.RunData;
import edu.tj.sse.runeveryday.database.entity.User;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
	private static final String TAG = "DatabaseHelper";
	// 数据库名称
	private static final String DATABASE_NAME = "runeveryday.db";
	// 数据库version  
    private static final int DATABASE_VERSION = 1;
    
    private Dao<User, Integer> userDao = null;
    private Dao<RunData, Integer> rundataDao = null;
    private Dao<Plan, Integer> planDao = null;
    
    /*
     * RuntimeExceptionDao这个东西是针对JDBC和一些其他的SQL的。对于Android平台主要是处理了过多繁琐的try…catch…的书写，和一些语法错误带来的崩溃，建议使用。
     */
    private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;
    private RuntimeExceptionDao<RunData, Integer> rundataRuntimeDao = null;
    private RuntimeExceptionDao<Plan, Integer> planRuntimeDao = null;
    
    public DatabaseHelper(Context context)
    {  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
       // 可以用配置文件来生成 数据表，有点繁琐，不喜欢用  
       // super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);  
    }
    
    public DatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion)  
    {  
        super(context, databaseName, factory, databaseVersion);  
    }

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO 自动生成的方法存根
		try  
        {  
            //建立User表  
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, RunData.class);
            TableUtils.createTable(connectionSource, Plan.class);
            //初始化DAO  
            userDao = getUserDao();
            rundataDao=getRunDataDao();
            planDao=getPlanDao();
            userRuntimeDao = getUserDataDao();
            rundataRuntimeDao=getRundataDataDao();
            planRuntimeDao=getPlanDataDao();
        }
        catch (SQLException e)  
        {  
            Log.e(TAG+"创建数据库失败", e.toString());  
            e.printStackTrace();  
        }  
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		// TODO 自动生成的方法存根
		try
        {  
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, RunData.class, true);
            TableUtils.dropTable(connectionSource, Plan.class, true);
        }
        catch (SQLException e)  
        {
            Log.e(TAG+"更新数据库失败", e.toString());  
            e.printStackTrace();  
        }  
	}
    
	public Dao<User, Integer> getUserDao() throws SQLException  
    {  
        if (userDao == null)  
            userDao = getDao(User.class);  
        return userDao;  
    }
	public Dao<RunData, Integer> getRunDataDao() throws SQLException  
    {
        if (rundataDao == null)  
        	rundataDao = getDao(RunData.class);  
        return rundataDao;
    }
	public Dao<Plan, Integer> getPlanDao() throws SQLException  
    {  
        if (planDao == null)  
        	planDao = getDao(Plan.class);  
        return planDao;  
    }
	
	public RuntimeExceptionDao<User, Integer> getUserDataDao()  
    {  
        if (userRuntimeDao == null)
        {
            userRuntimeDao = getRuntimeExceptionDao(User.class);  
        }  
        return userRuntimeDao;  
    }
	public RuntimeExceptionDao<RunData, Integer> getRundataDataDao()  
    {  
        if (rundataRuntimeDao == null)
        {  
            rundataRuntimeDao = getRuntimeExceptionDao(RunData.class);  
        }  
        return rundataRuntimeDao;  
    }
	public RuntimeExceptionDao<Plan, Integer> getPlanDataDao()  
    {  
        if (planRuntimeDao == null)
        {  
            planRuntimeDao = getRuntimeExceptionDao(Plan.class);  
        }
        return planRuntimeDao;  
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
        userDao = null;
        rundataDao = null;
        planDao = null;
    }  
}
