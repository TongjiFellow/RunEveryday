package edu.tj.sse.runeveryday.test;

import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.database.business.PlanBase;
import edu.tj.sse.runeveryday.database.entity.Training;
import edu.tj.sse.runeveryday.ui.MainActivity;
import android.app.Application;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlanActivityTest extends InstrumentationTestCase {

	private MainActivity main = null;
	private ListView listview = null;

	@Override
	protected void setUp() {

		try {
			super.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent();

		intent.setClassName("edu.tj.sse.runeveryday",
				MainActivity.class.getName());

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		main = (MainActivity) getInstrumentation().startActivitySync(intent);
	}

	@Override
	protected void tearDown() {

		main.finish();

		try {

			super.tearDown();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void test_list() {
		
		assertNotNull(listview);
	}
}