package com.alieeen.smartchair.fragments.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alieeen.smartchair.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

/**
 * Created by alinekborges on 20/05/15.
 */
public class StatisticsFragment extends Fragment implements OnChartValueSelectedListener {

    private LineChart encoderChart;
    private LineChart angleChart;
    private int year = 2015;

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.statistics_fragment, container, false);

        encoderChart = (LineChart) v.findViewById(R.id.chart1);
        angleChart = (LineChart) v.findViewById(R.id.chart2);

        setUpEncoderChart();
        setUpAngleChart();


/*
                new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i = 0; i < 5000; i++) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            addEntryEncoder();
                            addEntryAngle();
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/

        return v;
    }

    private void setUpAngleChart() {
        angleChart.setOnChartValueSelectedListener(this);

        // no description text
        angleChart.setDescription("");
        angleChart.setNoDataTextDescription("No data :(");


        // enable value highlighting
        angleChart.setHighlightEnabled(true);

        // enable touch gestures
        angleChart.setTouchEnabled(true);

        // enable scaling and dragging
        angleChart.setDragEnabled(true);
        angleChart.setScaleEnabled(true);
        angleChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        angleChart.setPinchZoom(false);


        // set an alternative background color
        angleChart.setBackgroundColor(getResources().getColor(R.color.chart2));

        LineData data = new LineData();
        data.setValueTextColor(Color.DKGRAY);

        // add empty data
        angleChart.setData(data);


        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = angleChart.getLegend();


        // modify the legend ...
         l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tf);
        l.setTextColor(Color.DKGRAY);
        l.setTextSize(16);
        String[] name = {"Encoder"};
        l.setLabels(name);


        XAxis xl = angleChart.getXAxis();
        //xl.setTypeface(tf);
        xl.setTextColor(Color.DKGRAY);
        xl.setDrawGridLines(false);
        //xl.setAvoidFirstLastClipping(true);
        //xl.setSpaceBetweenLabels(1);
        xl.setEnabled(false);

        YAxis leftAxis = angleChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.DKGRAY);

        leftAxis.setAxisMaxValue(20f);
        leftAxis.setAxisMinValue(-20f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = angleChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    private void setUpEncoderChart() {

        encoderChart.setOnChartValueSelectedListener(this);

        // no description text
        encoderChart.setDescription("");
        encoderChart.setNoDataTextDescription("No data :(");


        // enable value highlighting
        encoderChart.setHighlightEnabled(true);

        // enable touch gestures
        encoderChart.setTouchEnabled(true);

        // enable scaling and dragging
        encoderChart.setDragEnabled(true);
        encoderChart.setScaleEnabled(true);
        encoderChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        encoderChart.setPinchZoom(false);


        // set an alternative background color
        encoderChart.setBackgroundColor(getResources().getColor(R.color.chart1));
        encoderChart.setBackgroundColor(getResources().getColor(R.color.chart1));

        LineData data = new LineData();
        data.setValueTextColor(Color.DKGRAY);

        // add empty data
        encoderChart.setData(data);



        //Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = encoderChart.getLegend();

        // modify the legend ...
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tf);
        l.setTextColor(Color.DKGRAY);
        l.setTextSize(16);

        XAxis xl = encoderChart.getXAxis();
        //xl.setTypeface(tf);
        xl.setTextColor(Color.DKGRAY);
        xl.setDrawGridLines(false);
        //xl.setAvoidFirstLastClipping(true);
        //xl.setSpaceBetweenLabels(1);
        xl.setEnabled(false);

        YAxis leftAxis = encoderChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setAxisMaxValue(80f);
        leftAxis.setAxisMinValue(30f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = encoderChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addEntryEncoder(float value) {


        LineData data = encoderChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntryEncoder(...); // can be called as well

            if (set == null) {
                set = createSet("encoder");
                data.addDataSet(set);
            }

            // add a new x-value first

            Entry entry = new Entry(value, set.getEntryCount());
            data.addEntry(entry,0);

            // let the chart know it's data has changed
            encoderChart.notifyDataSetChanged();

            // limit the number of visible entries



            encoderChart.setVisibleYRange(100, YAxis.AxisDependency.LEFT);

            // move to the latest entry
            encoderChart.moveViewToX(data.getXValCount() - 121);

            encoderChart.setScaleMinima((float) data.getXValCount() / 10f, 1f);
            //encoderChart.centerViewPort(float val, int xindex)
            //encoderChart.centerViewTo(data.getXValCount(), 50, YAxis.AxisDependency.RIGHT);

            // this automatically refreshes the chart (calls invalidate())
            encoderChart.moveViewTo(data.getXValCount() - 7, 55f,
                    YAxis.AxisDependency.LEFT);

            // redraw the chart
            encoderChart.invalidate();


        }
    }

    private void addEntryEncoder() {

        LineData data = encoderChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntryEncoder(...); // can be called as well

            if (set == null) {
                set = createSet("velocity");
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(mMonths[data.getXValCount() % 12] + " "
                    + (year + data.getXValCount() / 12));
            data.addEntry(new Entry((float) (Math.random() * 10) + 50f, set.getEntryCount()), 0);

            // let the chart know it's data has changed
            encoderChart.notifyDataSetChanged();

            // limit the number of visible entries



             encoderChart.setVisibleYRange(100, YAxis.AxisDependency.LEFT);

            // move to the latest entry
            encoderChart.moveViewToX(data.getXValCount() - 121);

            encoderChart.setScaleMinima((float) data.getXValCount() / 10f, 1f);
            //encoderChart.centerViewPort(float val, int xindex)
            //encoderChart.centerViewTo(data.getXValCount(), 50, YAxis.AxisDependency.RIGHT);

            // this automatically refreshes the chart (calls invalidate())
             encoderChart.moveViewTo(data.getXValCount() - 7, 55f,
                     YAxis.AxisDependency.LEFT);

            // redraw the chart
             encoderChart.invalidate();


        }
    }

    private void addEntryAngle(float value) {

        LineData data = angleChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntryEncoder(...); // can be called as well

            if (set == null) {
                set = createSet("angle");
                data.addDataSet(set);
            }

            Entry entry = new Entry(value, set.getEntryCount());
            data.addEntry(entry,0);

            // let the chart know it's data has changed
            angleChart.notifyDataSetChanged();

            // limit the number of visible entries



            angleChart.setVisibleYRange(40, YAxis.AxisDependency.LEFT);

            // move to the latest entry
            angleChart.moveViewToX(data.getXValCount() - 121);

            angleChart.setScaleMinima((float) data.getXValCount() / 10f, 1f);
            //encoderChart.centerViewPort(float val, int xindex)
            //encoderChart.centerViewTo(data.getXValCount(), 50, YAxis.AxisDependency.RIGHT);

            // this automatically refreshes the chart (calls invalidate())
            angleChart.moveViewTo(data.getXValCount() - 7, 0f,
                    YAxis.AxisDependency.LEFT);

            // redraw the chart
            angleChart.invalidate();


        }
    }

    private void addEntryAngle() {

        LineData data = angleChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            // set.addEntryEncoder(...); // can be called as well

            if (set == null) {
                set = createSet("angle");
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(mMonths[data.getXValCount() % 12] + " "
                    + (year + data.getXValCount() / 12));


            data.addEntry(new Entry((float) (Math.random() * 2.5f) - 5f, set.getEntryCount()), 0);

            // let the chart know it's data has changed
            angleChart.notifyDataSetChanged();

            // limit the number of visible entries



            angleChart.setVisibleYRange(40, YAxis.AxisDependency.LEFT);

            // move to the latest entry
            angleChart.moveViewToX(data.getXValCount() - 121);

            angleChart.setScaleMinima((float) data.getXValCount() / 10f, 1f);
            //encoderChart.centerViewPort(float val, int xindex)
            //encoderChart.centerViewTo(data.getXValCount(), 50, YAxis.AxisDependency.RIGHT);

            // this automatically refreshes the chart (calls invalidate())
            angleChart.moveViewTo(data.getXValCount() - 7, 0f,
                    YAxis.AxisDependency.LEFT);

            // redraw the chart
            angleChart.invalidate();


        }
    }

    private LineDataSet createSet(String name) {

        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(ColorTemplate.getHoloBlue());
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}



