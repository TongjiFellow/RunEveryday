package edu.tj.sse.runeveryday.database.business;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import edu.tj.sse.runeveryday.database.DatabaseHelper;
import edu.tj.sse.runeveryday.database.entity.User;

public class UserBase {

	private DatabaseHelper dbhDatabaseHelper;
	private RuntimeExceptionDao<User, Integer> userDao;
	private Context context;
	public UserBase(Context context){
		this.context=context;
		dbhDatabaseHelper=new DatabaseHelper(this.context);
		userDao=dbhDatabaseHelper.getUserDataDao();
	}
	
}
