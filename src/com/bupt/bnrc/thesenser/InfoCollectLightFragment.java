package com.bupt.bnrc.thesenser;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoCollectLightFragment extends Fragment {
	private GraphicalView mChartView = null;
	private XYMultipleSeriesDataset mDataset = null; 
	private XYMultipleSeriesRenderer mRenderer = null; 
	
	private XYSeries mCurrentSeries; 
	private XYSeriesRenderer mCurrentRenderer;
	
	private Collection collection = null; 
	
	private TextView mLightView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_info_collect_light, container, false);
		initLineChartBuilder(view);
		initView(view);
		// RefreshData();
		
		
		return view;
	}
	
	private void initView(View view) {
		// TODO Auto-generated method stub
		collection = new Collection(getActivity());
		mLightView = (TextView)view.findViewById(R.id.info_light_data);
		// collection.setDataModel();
		Float lightNum = collection.getLight();
		mLightView.setText(lightNum.toString());
		
	}
	
	private void initLineChartBuilder(View view) {
		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		// Renderer
		// Basic
		mRenderer.setMargins(new int[] { 100, 150, 30, 100 });
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
	    mRenderer.setMarginsColor(Color.TRANSPARENT);
        mRenderer.setLegendTextSize(15);
        mRenderer.setPointSize(5);
		mRenderer.setShowLegend(true); 
		mRenderer.setClickEnabled(false);
		// Axes
		mRenderer.setAxisTitleTextSize(50);
		mRenderer.setBarSpacing(50);
	    mRenderer.setYTitle("光照强度");
//		mRenderer.setAxesColor(Color.BLACK);
		// Labels
	    //mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setLabelsTextSize(35);
		mRenderer.setXLabels(0);
		mRenderer.setYLabels(8);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setXLabelsAngle(-45f);
		mRenderer.setXLabelsAlign(Align.CENTER);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		
		mRenderer.setPanEnabled(false,false);
		mRenderer.setZoomEnabled(false, false);
		
		

		double[] xAxisCoord=new double[]{1,2,3,4,5,6,7};
		double[] yAxisCoord=new double[]{45,32,58,15,38,51,27};
		String[] xAxisLabel=new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		
		
		XYSeries series = new XYSeries("");
		mDataset.addSeries(series);
		mCurrentSeries = series;
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		renderer.setPointStyle(PointStyle.CIRCLE);
	    renderer.setFillPoints(true);
	    renderer.setDisplayChartValues(true);
	    renderer.setDisplayChartValuesDistance(10);
	    mCurrentRenderer = renderer;
		
	    // set data
		mCurrentSeries.clear();
		for (int i = 0; i < xAxisLabel.length; i++) {
			mCurrentSeries.add(xAxisCoord[i], yAxisCoord[i]);
			mRenderer.addXTextLabel(xAxisCoord[i], xAxisLabel[i]);
		}
		LinearLayout parentLayout = (LinearLayout)view.findViewById(R.id.chart_layout);
		mChartView = ChartFactory.getLineChartView(getActivity(), mDataset, mRenderer);
		parentLayout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mChartView.repaint();
	}
	
	private void Refresh() {
		// TODO Auto-generated method stub
		
	}

	public void refreshDataView() {
		// TODO Auto-generated method stub
		Float lightNum = collection.getLight();
		mLightView.setText(lightNum.toString());
	}
}
