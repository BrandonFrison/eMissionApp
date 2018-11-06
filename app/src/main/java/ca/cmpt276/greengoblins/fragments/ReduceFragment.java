package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.MealPlan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Locale;

public class ReduceFragment extends Fragment {

    final static int MIN_VALUE_FOR_CHART = 0;
    final static double METRO_VANCOUVER_POPULATION = 2.463E6;

    private Button mMealPlan1;
    private Button mMealPlan2;
    private Button mMealPlan3;
    private Button mMealPlan4;
    private TextView mSavingsAfterNewPlan;
    private TextView mCollectiveSavings;
    ConsumptionTable mOldMealPlan;
    HorizontalBarChart mCO2eComparisonChart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reduce CO2e emissions");

        mOldMealPlan = (ConsumptionTable) getActivity().getIntent().getSerializableExtra("resultTable");
        mSavingsAfterNewPlan = (TextView) view.findViewById(R.id.meal_plan_savings);
        mCollectiveSavings = (TextView) view.findViewById(R.id.meal_plan_collective_savings);

        mCO2eComparisonChart = (HorizontalBarChart) view.findViewById(R.id.meal_plan_co2e_chart);
        setGraph(0, mOldMealPlan.calculateTotalCO2e()); // Set newPlanCO2e 0 as default

        // Vegetarian meal plan
        mMealPlan1 = (Button) view.findViewById(R.id.meal_plan_1);
        mMealPlan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealPlan chickenOnlyMealPlan = new MealPlan();
                double co2eForNewPlan = chickenOnlyMealPlan.switchToChickenOnly(mOldMealPlan);
                calculateSavingsAndUpdateGraph(co2eForNewPlan);
            }

        });
        // Reduce all meat consumption by 50% but keep vegetable consumption the same
        mMealPlan2 = (Button) view.findViewById(R.id.meal_plan_2);
        mMealPlan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealPlan reducedMeatMealPlan = new MealPlan();
                double co2eForNewPlan = reducedMeatMealPlan.reduceMeatByHalf(mOldMealPlan);
                calculateSavingsAndUpdateGraph(co2eForNewPlan);
            }
        });

        // Remove all meats except for fish
        mMealPlan3 = (Button) view.findViewById(R.id.meal_plan_3);
        mMealPlan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlan fishOnlyMealPlan = new MealPlan();
                double co2eForNewPlan = fishOnlyMealPlan.switchToFishOnly(mOldMealPlan);
                calculateSavingsAndUpdateGraph(co2eForNewPlan);
            }
        });

        mMealPlan4 = (Button) view.findViewById(R.id.meal_plan_4);
        mMealPlan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealPlan vegetarianMealPlan = new MealPlan();
                double co2eForNewPlan = vegetarianMealPlan.removeMeatFromPlan(mOldMealPlan);
                calculateSavingsAndUpdateGraph(co2eForNewPlan);
            }
        });

    }
        private void calculateSavingsAndUpdateGraph(double co2eForNewPlan) {
            double savings = mOldMealPlan.calculateTotalCO2e() - co2eForNewPlan;
            String savingsText = "You have saved: " + String.format(Locale.US, "%.1f", savings) + " CO2e";
            mSavingsAfterNewPlan.setText(savingsText);

            // TO DO: Implement collective savings using convertor class
            double collectiveSavings = savings * METRO_VANCOUVER_POPULATION;
            String collectiveSavingsText = "If everyone in Metro Vancouver implemented these " +
                    "changes, together we could save: " +  String.format(Locale.US, "%.1f", collectiveSavings) + " CO2e!";

            mCollectiveSavings.setText(collectiveSavingsText);

            setGraph((float) co2eForNewPlan, mOldMealPlan.calculateTotalCO2e());
        }

        private void setGraph(float newPlanCO2e, float oldPlanCO2e) {
            mCO2eComparisonChart.invalidate(); // Refreshes chart
            ArrayList<BarEntry> co2eValues = new ArrayList<>();
            float barWidth = 1f;
            float spaceBetweenBars = 1.5f;

            co2eValues.add(new BarEntry(1 * spaceBetweenBars, newPlanCO2e));
            co2eValues.add(new BarEntry(2 * spaceBetweenBars, oldPlanCO2e));

            // Add data
            BarDataSet co2eValuesDataSet;
            co2eValuesDataSet = new BarDataSet(co2eValues, "CO2 Values");
            co2eValuesDataSet.setColors(getResources().getColor(R.color.colorPrimary));
            co2eValuesDataSet.setValueTextSize(13f);
            BarData co2eData = new BarData(co2eValuesDataSet);

            // Disables interactivity, removes description, and removes label
            applyChartSettings(mCO2eComparisonChart);

            // Set chart to present CO2e data
            mCO2eComparisonChart.setData(co2eData);
        }

        // Disables interactivity, removes description, and removes label
        private void applyChartSettings(HorizontalBarChart chart) {
            // Disable interactivity
            chart.setTouchEnabled(false);
            chart.setDragEnabled(false);
            chart.setScaleEnabled(false);
            chart.setScaleXEnabled(false);
            chart.setScaleYEnabled(false);
            chart.setPinchZoom(false);
            chart.setDoubleTapToZoomEnabled(false);

            // Remove chart description
            Description desc = new Description();
            desc.setText("");
            chart.setDescription(desc);

            // Remove legend
            Legend legend = chart.getLegend();
            legend.setEnabled(false);

            //Start axis from min value which is 0
            chart.getAxisLeft().setAxisMinimum(MIN_VALUE_FOR_CHART);

            // Disable all axises
            chart.getAxisRight().setEnabled(false);
            chart.getXAxis().setEnabled(false);
            chart.getXAxis().setDrawGridLines(false);
            chart.getAxisLeft().setEnabled(false);
        }

    }

