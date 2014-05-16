package edu.tj.sse.runeveryday.widget;

import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import edu.tj.sse.runeveryday.R;

public class BarChart {
	private Context context;
	private int bgColor;

	private GraphicalView chartView;

	public XYMultipleSeriesDataset dataset;
	public XYMultipleSeriesRenderer renderer;

	public View execute(Context context) {
		this.context = context;
		bgColor = context.getResources().getColor(R.color.text_orange);
		initDataset();
		initRenderer();
		chartView = ChartFactory.getBarChartView(context, dataset,
				renderer, Type.DEFAULT);
		return chartView;
	}

	private void initDataset() {
		dataset = new XYMultipleSeriesDataset();
		final int nr = 10;
		Random r = new Random();
		for (int i = 0; i < 3; i++) {
			CategorySeries series = new CategorySeries("球队 " + (i + 1));
			for (int k = 0; k < nr; k++) {
				series.add(100 + r.nextInt() % 100);
			}
			dataset.addSeries(series.toXYSeries());
		}
	}

	public void initRenderer() {
		renderer = new XYMultipleSeriesRenderer();
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		renderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.RED);
		renderer.addSeriesRenderer(r);

		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.TRANSPARENT);
		renderer.setMarginsColor(context.getResources().getColor(R.color.bg_color));
		renderer.setZoomEnabled(false, false);
		renderer.setPanEnabled(false, false);
		renderer.setLabelsTextSize(15);
		renderer.setLabelsColor(Color.BLACK);

		setChartSettings(renderer);
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		// renderer.setChartTitle("战绩分析");
		renderer.setXTitle("时间");
		renderer.setYTitle("时长");
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(10.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(210);
	}

	public void repaint() {
		chartView.repaint();
	}
}
