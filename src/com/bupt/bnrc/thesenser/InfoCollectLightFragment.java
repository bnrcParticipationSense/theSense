package com.bupt.bnrc.thesenser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.bnrc.thesenser.model.DataModel;
import com.bupt.bnrc.thesenser.utils.DataCache;
import com.bupt.bnrc.thesenser.utils.Logger;
import com.bupt.bnrc.thesenser.utils.TimeController;

public class InfoCollectLightFragment extends Fragment {
	private GraphicalView mChartView = null;
	private XYMultipleSeriesDataset mDataset = null;
	private XYMultipleSeriesRenderer mRenderer = null;

	private XYSeries mCurrentSeries;
	private XYSeriesRenderer mCurrentRenderer;

	private Collection collection = null;
	private TextView mLightView;
	private DataCache mDataCache = null;

	Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_info_collect_light,
				container, false);
		initView(view);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDataCache = DataCache.getInstance();

		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		// Renderer
		// Basic
		mRenderer.setMargins(new int[] { 100, 150, 30, 100 });
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(0xffE8E8E7);
		mRenderer.setMarginsColor(0xffE8E8E7);
		mRenderer.setLegendTextSize(15);
		mRenderer.setPointSize(5);
		mRenderer.setShowLegend(true);
		mRenderer.setClickEnabled(false);
		// Axes
		mRenderer.setAxisTitleTextSize(50);
		mRenderer.setBarSpacing(50);
		mRenderer.setYTitle("Light");
		// mRenderer.setAxesColor(Color.BLACK);
		// Labels
		// mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setLabelsTextSize(35);
		mRenderer.setXLabels(0);
		mRenderer.setYLabels(10);
		mRenderer.setYAxisMin(0);
		mRenderer.setYAxisMax(1000);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setXLabelsAngle(-45f);
		mRenderer.setXLabelsAlign(Align.CENTER);
		mRenderer.setYLabelsAlign(Align.RIGHT);

		mRenderer.setPanEnabled(false, false);
		mRenderer.setZoomEnabled(false, false);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// refreshAll();
		// handler.postDelayed(refreshRunnable, 3000);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// handler.removeCallbacks(refreshRunnable);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//collection = Collection.getCollection(getActivity());
		collection = Collection.getCollection();
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mLightView = (TextView) view.findViewById(R.id.info_light_data);

		XYSeries series = new XYSeries("");
		mDataset.addSeries(series);
		mCurrentSeries = series;
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(20);
		renderer.setChartValuesTextSize(15);
		mCurrentRenderer = renderer;

		// set data
		mCurrentSeries.clear();
		LinearLayout parentLayout = (LinearLayout) view
				.findViewById(R.id.chart_layout);
		mChartView = ChartFactory.getLineChartView(getActivity(), mDataset,
				mRenderer);
		parentLayout.addView(mChartView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		// 平均值
		Date day = TimeController.getToday();
		day = TimeController.getDayDiffDay(-6, day);
		for(int i=1;i<=7;i++) {
			mRenderer.addXTextLabel(i, TimeController.getDayString(day));
			Integer light = getAverageLight(day);
			mCurrentSeries.add(i, light);
			day = TimeController.getTomorrow(day);
		}
		
		mChartView.repaint();
	}

	private Integer getAverageLight(Date day) {
		// TODO Auto-generated method stub
		List<DataModel> datas = DataModel.findDatasByDay(day, getActivity());
		int size = datas.size();
		Integer average = 0;
		if(size > 0) {
			Float sum = (float) 0;
			for(int i = 0; i< size;i++) {
				sum+=datas.get(i).getLightIntensity();
			}
			average = (int) (sum/size);
		}
		return average;
	}

	public void refreshAll() {
		// TODO Auto-generated method stub
		refreshDataView();
		refreshChart();
	}

	private void refreshChart() {
		// TODO Auto-generated method stub
		Float lightNum = collection.getLight();
		int index = mDataCache.addLightData(lightNum);
		if (index == 1) {
			mCurrentSeries.clear();
		}
		mCurrentSeries.add(index, lightNum);
		mChartView.repaint();
	}

	public void refreshDataView() {
		// TODO Auto-generated method stub
		Float lightNum = collection.getLight();
		mLightView.setText(lightNum.toString());
	}

	Runnable refreshRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				refreshAll();
				handler.postDelayed(this, 3000);
			} catch (Exception e) {
				// TODO: handle exception
				Logger.e(e.getMessage());
			}
		}
	};
}
