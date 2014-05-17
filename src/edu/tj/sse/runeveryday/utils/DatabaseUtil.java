package edu.tj.sse.runeveryday.utils;

import java.util.ArrayList;

public class DatabaseUtil {
	private static DatabaseUtil databaseUtil = null;

	private DatabaseUtil() {

	}

	public static DatabaseUtil getIntance() {
		if (databaseUtil == null)
			databaseUtil = new DatabaseUtil();

		return databaseUtil;
	}

	/**
	 * @return 从数据库返回当前计划
	 */
	public String getCurrentPlan() {
		String plan = "";

		return plan;
	}

	/**
	 * @return 从数据库返回当前阶段 例如：第二周第二天
	 */
	public String getCurrentPhase() {
		String phase = "";

		return phase;
	}

	private class TimeValue {
		public String time = "";
		public float duration = 0;
	}

	public ArrayList<TimeValue> getDayHistoryData() {
		ArrayList<TimeValue> list = new ArrayList<TimeValue>();

		return list;
	}

	public ArrayList<TimeValue> getWeekHistoryData() {
		ArrayList<TimeValue> list = new ArrayList<TimeValue>();

		return list;
	}

	public ArrayList<TimeValue> getMonthHistoryData() {
		ArrayList<TimeValue> list = new ArrayList<TimeValue>();

		return list;
	}

	public ArrayList<TimeValue> getYearHistoryData() {
		ArrayList<TimeValue> list = new ArrayList<TimeValue>();

		return list;
	}

}
