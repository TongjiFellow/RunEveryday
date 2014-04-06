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
	// ���ݿ�����
	private static final String DATABASE_NAME = "runeveryday.db";
	// ���ݿ�version  
    private static final int DATABASE_VERSION = 1;
    
    private Dao<User, Integer> userDao = null;
    private Dao<RunData, Integer> rundataDao = null;
    private Dao<Plan, Integer> planDao = null;
    
    /*
     * RuntimeExceptionDao������������JDBC��һЩ������SQL�ġ�����Androidƽ̨��Ҫ�Ǵ����˹��෱����try��catch������д����һЩ�﷨��������ı���������ʹ�á�
     */
    private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;
    private RuntimeExceptionDao<RunData, Integer> rundataRuntimeDao = null;
    private RuntimeExceptionDao<Plan, Integer> planRuntimeDao = null;
    
    public DatabaseHelper(Context context)
    {  
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
       // �����������ļ������� ���ݱ��е㷱������ϲ����  
       // super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);  
    }
    
    public DatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion)  
    {  
        super(context, databaseName, factory, databaseVersion);  
    }

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO �Զ����ɵķ������
		try  
        {  
            //����User��  
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, RunData.class);
            TableUtils.createTable(connectionSource, Plan.class);
            //��ʼ��DAO  
            userDao = getUserDao();
            rundataDao=getRunDataDao();
            planDao=getPlanDao();
            userRuntimeDao = getUserDataDao();
            rundataRuntimeDao=getRundataDataDao();
            planRuntimeDao=getPlanDataDao();
        }
        catch (SQLException e)  
        {  
            Log.e(TAG+"�������ݿ�ʧ��", e.toString());  
            e.printStackTrace();  
        }  
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		// TODO �Զ����ɵķ������
		try
        {  
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, RunData.class, true);
            TableUtils.dropTable(connectionSource, Plan.class, true);
        }
        catch (SQLException e)  
        {
            Log.e(TAG+"�������ݿ�ʧ��", e.toString());  
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
     * �ͷ� DAO 
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
