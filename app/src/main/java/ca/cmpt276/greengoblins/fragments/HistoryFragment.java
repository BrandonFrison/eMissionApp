package ca.cmpt276.greengoblins.fragments;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.FoodSurveyHistoryManager;

public class HistoryFragment extends Fragment
        implements OnChartValueSelectedListener{

    private MainActivity mMainActivity;
    private FoodSurveyHistoryManager mFoodSurveyHistory;
   /* private ArrayList<ConsumptionTable> mTableHistory;
    private ArrayList<String> mTableDates;*/
    private LineChart chart;
    private Spinner mDropdownChooseMonth;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    private View mView;
    private MPPointF mOffset;
    private TextView mMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.toolbar_history);

        mMainActivity = (MainActivity) getActivity();
        mView = getView();
        mFoodSurveyHistory = new FoodSurveyHistoryManager();
        mMarker = (TextView) view.findViewById(R.id.marker);

        int currentMonth = getCurrentMonth();

        //load history tables from saved files
        mFoodSurveyHistory.loadTablesByMonth(mMainActivity, currentMonth+1);

        mDropdownChooseMonth = (Spinner) view.findViewById(R.id.spinnerChooseMonth);
        mDropdownChooseMonth.setSelection(currentMonth);
        mDropdownChooseMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFoodSurveyHistory.loadTablesByMonth(mMainActivity, position+1);
                Log.d("LOAD_EXCEPTION", "loading month: " + String.valueOf(position+1));
                setData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        {   // // Chart Style // //
            chart = view.findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);
            chart.setNoDataText(getString(R.string.no_data_saved));

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);
           /* final String xVal[]={"Val1","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3","Val2","Val3"};
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xVal[(int) value]; // xVal is a string array
                }

            });*/

            //axis range
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(31f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            //yAxis.setAxisMaximum(500f);
            yAxis.setAxisMinimum(0f);
        }


        // add data
        setData();

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }

    private int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
    private int getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }
    private int getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();
        int numberOfTables = mFoodSurveyHistory.getNumberOfTables();
            for (int i = 1; i <= numberOfTables; i++) {
            String fullFormatedDate = mFoodSurveyHistory.getTableDates().get(i-1);
            String dayOfMonth = fullFormatedDate.substring(fullFormatedDate.length()-2, fullFormatedDate.length());
            int dayOfMonthValue;
            try{
                dayOfMonthValue = Integer.parseInt( dayOfMonth );
                Log.d("LOAD_EXCEPTION", dayOfMonth);
            }catch (NumberFormatException exception){
                dayOfMonthValue = 0;
            }
            values.add( new Entry(
                    dayOfMonthValue,
                    mFoodSurveyHistory.getTableHistory().get(i-1).calculateTotalCO2e(),
                    ResourcesCompat.getDrawable(mMainActivity.getResources(),
                    R.drawable.star,
             null) ));
        }

        LineDataSet set1;
        if(values.isEmpty()){
            Log.d("LOAD_EXCEPTION", "values is empty");
            String chartLabel = getString(R.string.history_chart_label);
            values.add(new Entry(0,0));
            set1 = new LineDataSet(values, chartLabel);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
            chart.invalidate();
            return;
        }

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            String chartLabel = getString(R.string.history_chart_label);
            set1 = new LineDataSet(values, chartLabel);

            set1.setDrawIcons(true);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(mMainActivity, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
        chart.animateX(1500);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String formattedSnackbarMessage = String.format( getString(R.string.snackbar_history_element), Math.round(e.getX()), e.getY());
        Snackbar mySnackbar = Snackbar.make(mView, formattedSnackbarMessage, Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.snackbar_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formattedDate = String.valueOf(getCurrentYear()%100) + "-" + String.valueOf(getCurrentMonth()+1) + "-" + String.valueOf(getCurrentDay());
                Log.d("LOAD_EXCEPTION", "trying to delete: " + formattedDate);
                mFoodSurveyHistory.deleteTableByDate(mMainActivity, formattedDate);
                setData();
            }
        });
        mySnackbar.show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}
