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
import android.widget.TextView;

public class MainActivityTest extends InstrumentationTestCase {

	private MainActivity main = null;
	private PlanBase planBase;
	private Training currentTraining;

	public static class AppContext extends Application {
		private static AppContext instance;

		public static AppContext getInstance() {
			return instance;
		}

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
			instance = this;
		}
	}

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
		planBase = new PlanBase(main);
		currentTraining = planBase.getCurrentTraining();
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

	public void test_temperature() throws Throwable {
		TextView textView = (TextView) main.findViewById(R.id.Main_tempreture);
		assertNotNull(textView);
		assertEquals("20¡ã", textView.getText());
	}

	public void test_humidity() throws Throwable {
		TextView textView = (TextView) main.findViewById(R.id.Main_shidu);
		assertNotNull(textView);
		assertEquals("Êª¶È   50%", textView.getText());
	}

	public void test_plan() throws Throwable {
		TextView textView = (TextView) main.findViewById(R.id.Main_content);
		assertNotNull(textView);
		assertEquals(currentTraining.getWork(), textView.getText());
	}

	public void test_click_imageview() {
		ImageView imageview = (ImageView) main.findViewById(R.id.main_button);
		assertNotNull(imageview);
		TouchUtils.clickView(this, imageview);
	}
}