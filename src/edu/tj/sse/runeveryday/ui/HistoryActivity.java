package edu.tj.sse.runeveryday.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.widget.LineChart;

public class HistoryActivity extends BaseActivity {
	private Context context;

	// UI
	private TextView historyDayTextView;
	private TextView historyWeekTextView;
	private TextView historyMonthTextView;
	private TextView historyYearTextView;

	LineChart lineChart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		initSlidingMenu(this);

		historyDayTextView = (TextView) findViewById(R.id.historyDayTextView);
		historyWeekTextView = (TextView) findViewById(R.id.historyWeekTextView);
		historyMonthTextView = (TextView) findViewById(R.id.historyMonthTextView);
		historyYearTextView = (TextView) findViewById(R.id.historyYearTextView);

		context = getApplicationContext();
		modifyViewHeight();

		addListeners();

		LinearLayout chartLinearLayout = (LinearLayout) findViewById(R.id.chartLinearLayout);

		lineChart = new LineChart();
		View chartView = lineChart.execute(context);
		if (chartView != null) {
			chartLinearLayout.addView(chartView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));

			lineChart.repaint();
		}
	}

	private void addListeners() {
		ImageView arrowImageView = (ImageView) findViewById(R.id.arrowImg);
		ScrollView scrollView = (ScrollView) findViewById(R.id.historyScrollView);
		scrollView.setOnTouchListener(new ScrollViewTouchedListener(arrowImageView));

		findViewById(R.id.arrowRelativeLayout).setOnClickListener(
				new OnClickArrowListener(scrollView));

		historyDayTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				historyDayTextView.setBackgroundColor(Color.argb(100, 255, 240, 0));
				historyWeekTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyMonthTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyYearTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				lineChart.changeMode(LineChart.MODE_DAY);
			}
		});
		historyWeekTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				historyDayTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyWeekTextView.setBackgroundColor(Color.argb(100, 255, 240, 0));
				historyMonthTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyYearTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				lineChart.changeMode(LineChart.MODE_WEEK);
			}
		});
		historyMonthTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				historyDayTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyWeekTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyMonthTextView.setBackgroundColor(Color.argb(100, 255, 240, 0));
				historyYearTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				lineChart.changeMode(LineChart.MODE_MONTH);
			}
		});
		historyYearTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				historyDayTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyWeekTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyMonthTextView.setBackgroundColor(Color.argb(0, 255, 240, 0));
				historyYearTextView.setBackgroundColor(Color.argb(100, 255, 240, 0));
				lineChart.changeMode(LineChart.MODE_YEAR);
			}
		});
	}

	private class OnClickArrowListener implements OnClickListener {
		private final ScrollView scrollView;

		public OnClickArrowListener(ScrollView scrollView) {
			this.scrollView = scrollView;
		}

		@Override
		public void onClick(View view) {
			ImageView arrowImageView = (ImageView) view.findViewById(R.id.arrowImg);
			if (scrollView.getScrollY() < (scrollView.getMeasuredHeight() / 2)) {
				scrollView.smoothScrollTo(0, scrollView.getBottom());
				arrowImageView.setImageResource(R.drawable.arrow_to_top);
			} else {
				scrollView.smoothScrollTo(0, 0);
				arrowImageView.setImageResource(R.drawable.arrow_to_bottom);
			}
		}
	}
	

	private class ScrollViewTouchedListener implements OnTouchListener {
		ImageView arrowImageView;

		private ScrollViewTouchedListener(ImageView arrowImageView) {
			this.arrowImageView = arrowImageView;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				ScrollView scrollView = (ScrollView) view;
				if (scrollView.getScrollY() < (scrollView.getMeasuredHeight() / 2)) {
					scrollView.smoothScrollTo(0, 0);
					arrowImageView.setImageResource(R.drawable.arrow_to_bottom);
				} else {
					scrollView.smoothScrollTo(0, scrollView.getBottom());
					arrowImageView.setImageResource(R.drawable.arrow_to_top);
				}
				return true;
			}
			return false;
		}
	};

	private void modifyViewHeight() {
		int statusBarHeight = getResources().getDimensionPixelSize(
				getResources().getIdentifier("status_bar_height", "dimen", "android"));
		int sideContentHeight = statusBarHeight
				+ getResources().getDimensionPixelSize(R.dimen.title_bar_height)
				+ getResources().getDimensionPixelSize(R.dimen.history_down_arrow_height);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int onePhaseLayoutHeight = metrics.heightPixels - sideContentHeight;

		setLayoutHeight(findViewById(R.id.history_details_relativelayout), onePhaseLayoutHeight * 2);
		setLayoutHeight(findViewById(R.id.historyChartRelativeLayout), onePhaseLayoutHeight);
		setLayoutHeight(findViewById(R.id.historyTextRelativeLayout), onePhaseLayoutHeight);
	}

	private void setLayoutHeight(View view, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = height;
		view.setLayoutParams(layoutParams);
	}
}
