package ca.cmpt276.greengoblins.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.FoodSurveyHistoryManager;

public class ResultFragment extends Fragment {

    final double DrivenConvectionNuder = .200;  // 1 kg per 5 km
    public ArrayList<Integer> servingSizes;
    TextView mCO2eDisplay;
    private MainActivity mMainActivity;
    PieChart pieChart;
    boolean flag =false;
    boolean flag2 =false;
    ConsumptionTable servingTable;
    ConsumptionTable previousTable;
    float mCO2eScore = 0;
    ArrayList<String> categories;
    private FloatingActionButton mActionButton;
    List<PieEntry> pieEntries = new ArrayList<PieEntry>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            servingSizes = savedInstanceState.getIntegerArrayList("PieChartValue");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMainActivity = (MainActivity) getActivity();
        getActivity().setTitle(R.string.toolbar_history);
        //pie chart part
        pieChart = (PieChart) view.findViewById(R.id.PieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Emission");
        pieChart.setCenterTextSize(12);
        pieChart.getDescription().setEnabled(false);

        mCO2eDisplay = (TextView) view.findViewById(R.id.textViewResult);

        FoodSurveyHistoryManager tableLoader = new FoodSurveyHistoryManager();
        Calendar calendar = Calendar.getInstance();
        String yearTwoDigit = String.valueOf(calendar.get(Calendar.YEAR));
        yearTwoDigit = yearTwoDigit.substring( yearTwoDigit.length() - 2, yearTwoDigit.length() );
        String formattedCurrentDate = yearTwoDigit + "-" + String.valueOf(calendar.get(Calendar.MONTH)+1) + "-" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        previousTable = tableLoader.loadTableByDate(getActivity(), formattedCurrentDate);

        Bundle surveyBundle = getArguments();
        if(surveyBundle == null) {
            String resultText = "No data yet, please do the Co2e calculator";
            mCO2eDisplay.setText(resultText);
            if (previousTable != null) {
                //if previous meal plan is saved to csv file
                flag2 = true;
            }
        }else{
            flag = true;
            servingSizes = surveyBundle.getIntegerArrayList("survey_data");
        }

            categories = new ArrayList<String>();
            categories.add(getString(R.string.table_category1));
            categories.add(getString(R.string.table_category2));
            categories.add(getString(R.string.table_category3));
            categories.add(getString(R.string.table_category4));
            categories.add(getString(R.string.table_category5));
            categories.add(getString(R.string.table_category6));
            categories.add(getString(R.string.table_category7));

        if(flag) { // pie chart will work only after receiving data
            servingTable = new ConsumptionTable( servingSizes );
            setUpPieChart(servingTable);
        }else if(flag2){
            setUpPieChart(previousTable);
        }
        // Reduce Carbon button
        // Takes user to another activity where they can choose a meal plan

        mActionButton = mMainActivity.getActionButton();
        mActionButton.setImageResource(R.drawable.com_facebook_tooltip_black_bottomnub);
        mActionButton.show();
        mMainActivity.showActionButtonLabel(R.string.button_reduce_footprint);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    passToReduce(servingTable);
                }else if(flag2){
                    passToReduce(previousTable);
                }
            }
        });

    }

    private void passToReduce(ConsumptionTable tableUsed){
            Bundle bundle = new Bundle();
            bundle.putSerializable("resultTable", tableUsed);

            Fragment reduceFragment = new ReduceFragment();
            reduceFragment.setArguments(bundle);

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.startFragment(reduceFragment, true);

            /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_activity_content, reduceFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
    }

    private void setUpPieChart(ConsumptionTable tableUsed) {
            pieEntries.clear();         //fixes back press button messing up the pie chart
            mCO2eScore = tableUsed.calculateTotalCO2e();

            //round number  to 2 decimal
            final DecimalFormat df = new DecimalFormat(".00");

            String resultText = "Total CO2e Score: " + df.format(mCO2eScore) + " kg per year.\n\n" +
                    "That's equivalent to driving: " + df.format(mCO2eScore / DrivenConvectionNuder) + " km\n\n" +
                    "Click events on pie chart to get details.\n\n" +
                    "If you want to try different diets we provided, you can click the bottom button.";
            mCO2eDisplay.setText(resultText);

            //We should be able to get most or all of this from the new consumptiontable

            for (int i = 0; i < tableUsed.getSize(); i++) {
                if (tableUsed.getServingSize(i) > 0)
                    pieEntries.add(new PieEntry(tableUsed.calculateServingCO2e(i), categories.get(i)));
                else
                    continue;
            }

            //create the data set
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Emission");
            pieDataSet.setSliceSpace(2);
            pieDataSet.setValueTextSize(12);
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextColor(3);


            //add legend to chart
            Legend legend = pieChart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

            //create pie data object
            PieData pieData = new PieData(pieDataSet);
                pieChart.setData(pieData);
                pieChart.invalidate();

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    int xCategory = (int) h.getX();
                    String toastText = categories.get(xCategory) + ": " + df.format(h.getY()) + " kg of CO2e";
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("PieChartValue",servingSizes);
    }
}
