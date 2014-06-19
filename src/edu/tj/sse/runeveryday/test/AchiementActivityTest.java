package edu.tj.sse.runeveryday.test;

import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.ui.MainActivity;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.widget.ImageView;

public class AchiementActivityTest extends InstrumentationTestCase {

	private MainActivity main = null;

	private Integer[] Trophy = { R.id.Achieve_number_1, R.id.Achieve_number_2,
			R.id.Achieve_number_3, R.id.Achieve_number_4,
			R.id.Achieve_distance_1, R.id.Achieve_distance_2,
			R.id.Achieve_distance_3, R.id.Achieve_distance_4,
			R.id.Achieve_time_1, R.id.Achieve_time_2, R.id.Achieve_time_3,
			R.id.Achieve_time_4 };

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

	public void test_trophy() {
		for (int i = 0; i < 12; i++) {
			ImageView imageView = (ImageView) main.findViewById(Trophy[0]);
			assertNotNull(imageView);
		}
	}

	public void test_click_imageview() {
		ImageView imageview = (ImageView) main.findViewById(R.id.main_button);
		assertNotNull(imageview);
		TouchUtils.clickView(this, imageview);
	}
}