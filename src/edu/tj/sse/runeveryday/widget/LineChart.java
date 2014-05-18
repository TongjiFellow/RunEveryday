package edu.tj.sse.runeveryday.widget;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import edu.tj.sse.runeveryday.R;
import edu.tj.sse.runeveryday.utils.RundataBase;

public class LineChart {
	public static final int MODE_DAY = 0;
	public static final int MODE_WEEK = 1;
	public static final int MODE_MONTH = 2;
	public static final int MODE_YEAR = 3;

	private int mode = MODE_WEEK;

	private static final String[] DAY = { "5:00", "8:00", "11:00", "14:00", "17:00", "20:00" };
	private static final String[] WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	private static final String[] MONTH = { "1", "5", "10", "15", "20", "25", "30" };
	private static final String[] YEAR = { "Jan", "Mar", "May", "July", "Sep", "Nov" };

	private Context context;
	private int bgColor;
	private int bgColor1;

	private GraphicalView chartView;

	private XYMultipleSeriesDataset dataset;
	private XYMultipleSeriesRenderer renderer;
	
	//Database
	RundataBase rundataBase;

	public View execute(Context context) {
		this.context = context;
		
		rundataBase = new RundataBase(context);
		
		bgColor = context.getResources().getColor(R.color.text_orange);
		bgColor1 = context.getResources().getColor(R.color.text_white);
		initDataset();
		initRenderer();
		dataset.clear();
		dataset.addSeries(rundataBase.getWeekHistoryData());
		chartView = ChartFactory.getLineChartView(context, dataset, renderer);
		return chartView;
	}

	private void initDataset() {
		dataset = new XYMultipleSeriesDataset();
		String seriesTitle = "Series " + (dataset.getSeriesCount() + 1);
		XYSeries series = new XYSeries(seriesTitle);
		series.add(0, 1.05);
		series.add(1, 0.35);
		series.add(2, 0.62);
		series.add(3, 0.35);
		series.add(4, 0.91);
		series.add(5, 1.05);
		series.add(6, 1.12);
		dataset.addSeries(series);
	}

	private void initRenderer() {
		renderer = new XYMultipleSeriesRenderer();
		renderer.setApplyBackgroundColor(true);
		// 折线图背景
		renderer.setBackgroundColor(Color.TRANSPARENT);
		// 外围背景。必须使用 Color.argb 方式来设置，否则无效
		renderer.setMarginsColor(Color.argb(00, 11, 11, 11));

		// X轴颜色
		renderer.setAxesColor(bgColor);
		// 曲线图标题
		// renderer.setChartTitle("跑步记录");
		
		renderer.setShowGrid(true);
		renderer.setGridColor(bgColor1);
		
		renderer.setChartTitleTextSize(20);
		// 在scrollview中可以滑动
		renderer.setInScroll(true);
		// 坐标颜色，文字大小
		renderer.setLabelsColor(bgColor);
		renderer.setLabelsTextSize(18);
		// 图例字号
		renderer.setLegendTextSize(18);
		// 不显示图例
		renderer.setShowLegend(false);
		// 设置外边框（上下左右）
		// renderer.setMargins(new int[] { 30, 30, 5, 25 });
		// 上左下右
		renderer.setMargins(new int[] { 30, 55, 5, 55 });
		// 设置是否允许拖动（貌似无效，应该是必须有scrollview才行）
		renderer.setPanEnabled(true);
		// 设置是否允许放大和缩小，必须通过缩放按钮才能生效
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(false);
		// renderer.setAxisTitleTextSize(25);
		// 曲线图中“点”的大小
		renderer.setPointSize(5);
		// renderer.setGridColor(Color.TRANSPARENT);
		renderer.setYLabelsPadding(15);
		// renderer.setXLabelsPadding(20);
		// X轴、Y轴的文字颜色
		renderer.setYLabelsColor(0, bgColor);
		renderer.setXLabelsColor(bgColor);

		// create a new renderer for the new series
		XYSeriesRenderer tRenderer = new XYSeriesRenderer();
		renderer.addSeriesRenderer(tRenderer);
		// set some renderer properties
		tRenderer.setPointStyle(PointStyle.CIRCLE);
		tRenderer.setFillPoints(true);
		tRenderer.setDisplayChartValues(true);
		tRenderer.setDisplayChartValuesDistance(10);

		// enable the chart click events
		renderer.setClickEnabled(true);
		renderer.setSelectableBuffer(10);

		renderer.setYTitle("时间/h");

		renderer.clearXTextLabels();
		for (int i = 0; i < WEEK.length; ++i) {
			renderer.addXTextLabel(i, WEEK[i]);
		}
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(6);

		renderer.setXLabels(0);
		renderer.setYAxisMin(0);

		renderer.setAxisTitleTextSize(20);
	}

	public void changeMode(int m) {
		mode = m;
		if (mode == MODE_DAY) {
			dataset.clear();
//			XYSeries series = new XYSeries("");
//			series.add(5.1, 1.1);
//			series.add(8.5, 0.5);
//			series.add(9.1, 1.3);
//			series.add(11.7, 3.4);
//			series.add(14.3, 2.2);
//			series.add(17.0, 2.0);
//			series.add(20.0, 1.0);
//			dataset.addSeries(series);
			dataset.addSeries(rundataBase.getDayHistoryData());
			
			renderer.clearXTextLabels();
			renderer.addXTextLabel(5, DAY[0]);
			renderer.addXTextLabel(8, DAY[1]);
			renderer.addXTextLabel(11, DAY[2]);
			renderer.addXTextLabel(14, DAY[3]);
			renderer.addXTextLabel(17, DAY[4]);
			renderer.addXTextLabel(20, DAY[5]);
			renderer.setXAxisMin(5);
			renderer.setXAxisMax(20);
		} else if (mode == MODE_WEEK) {
			dataset.clear();
//			String seriesTitle = "Series " + (dataset.getSeriesCount() + 1);
//			XYSeries series = new XYSeries(seriesTitle);
//			series.add(0, 1.05);
//			series.add(1, 0.35);
//			series.add(2, 0.62);
//			series.add(3, 0.35);
//			series.add(4, 0.91);
//			series.add(5, 1.05);
//			series.add(6, 1.12);
//			dataset.addSeries(series);
			dataset.addSeries(rundataBase.getWeekHistoryData());
			
			renderer.clearXTextLabels();
			for (int i = 0; i < WEEK.length; ++i) {
				renderer.addXTextLabel(i, WEEK[i]);
			}
			renderer.setXAxisMin(0);
			renderer.setXAxisMax(6);
		} else if (mode == MODE_MONTH) {
			dataset.clear();
//			XYSeries series = new XYSeries("");
//			series.add(1, 1.1);
//			series.add(2, 0.5);
//			series.add(9, 1.3);
//			series.add(11, 3.4);
//			series.add(14, 2.2);
//			series.add(17.0, 2.0);
//			series.add(20.0, 1.0);
//			series.add(30.0, 1.0);
//			dataset.addSeries(series);
			dataset.addSeries(rundataBase.getMonthHistoryData());
			
			renderer.clearXTextLabels();
			renderer.addXTextLabel(1, MONTH[0]);
			renderer.addXTextLabel(5, MONTH[1]);
			renderer.addXTextLabel(10, MONTH[2]);
			renderer.addXTextLabel(15, MONTH[3]);
			renderer.addXTextLabel(20, MONTH[4]);
			renderer.addXTextLabel(25, MONTH[5]);
			renderer.addXTextLabel(30, MONTH[6]);
			renderer.setXAxisMin(1);
			renderer.setXAxisMax(31);
		} else if (mode == MODE_YEAR) {
			dataset.clear();
//			XYSeries series = new XYSeries("");
//			series.add(1, 1.1);
//			series.add(3, 0.5);
//			series.add(4, 1.3);
//			series.add(7, 3.4);
//			series.add(10, 2.2);
//			series.add(11, 2.0);
//			series.add(12.0, 1.0);
//			dataset.addSeries(series);
			dataset.addSeries(rundataBase.getYearHistoryData());
			
			renderer.clearXTextLabels();
			renderer.addXTextLabel(1, YEAR[0]);
			renderer.addXTextLabel(3, YEAR[1]);
			renderer.addXTextLabel(5, YEAR[2]);
			renderer.addXTextLabel(7, YEAR[3]);
			renderer.addXTextLabel(9, YEAR[4]);
			renderer.addXTextLabel(11, YEAR[5]);
			renderer.setXAxisMin(1);
			renderer.setXAxisMax(12);
		}
		repaint();
	}

	public void repaint() {
		chartView.repaint();
	}
}
