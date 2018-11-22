package ca.cmpt276.greengoblins.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.Convertor;
import ca.cmpt276.greengoblins.foodsurveydata.MealPlan;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReduceFragment extends Fragment {
    final static double METRO_VANCOUVER_POPULATION = 2.463E6;

    private MainActivity mMainActivity;
    private Spinner mMealPlanDropdown;
    private Button mJoinGreenFoodChallenge;
    private TextView mSavingsAfterNewPlan;
    private TextView mCollectiveSavings;
    ConsumptionTable mOldMealPlan;
    HorizontalBarChart mCO2eComparisonChart;
    public  float DrivenConvectionNumber = .200f;  // 200 g per 1 km
    //round number  to 2 decimal
    public DecimalFormat df = new DecimalFormat(".00");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reduce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMainActivity = (MainActivity) getActivity();
        mMainActivity.setTitle(R.string.toolbar_reduce);

        Bundle historyBundle = getArguments();
        mOldMealPlan = (ConsumptionTable) historyBundle.getSerializable("resultTable");
        mSavingsAfterNewPlan = (TextView) view.findViewById(R.id.meal_plan_savings);
        mCollectiveSavings = (TextView) view.findViewById(R.id.meal_plan_collective_savings);

        mCO2eComparisonChart = (HorizontalBarChart) view.findViewById(R.id.meal_plan_co2e_chart);
        // Disables interactivity, removes description, and removes label
        applyChartSettings(mCO2eComparisonChart);

        setGraph(0, mOldMealPlan.calculateTotalCO2e()); // Set newPlanCO2e 0 as default

        mMealPlanDropdown = (Spinner) view.findViewById(R.id.reduce_meal_list);
        String[] mMealOptions = getResources().getStringArray(R.array.reduce_meal_plan_list);

        ArrayAdapter<String> dropdownFilterAdapter = new ArrayAdapter<String>(mMainActivity.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, mMealOptions);
        dropdownFilterAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mMealPlanDropdown.setAdapter(dropdownFilterAdapter);
        mMealPlanDropdown.setPrompt(getResources().getString(R.string.select_meal_plan_prompt));

        mMealPlanDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MealPlan newMealPlan = new MealPlan();
                double co2eForNewPlan = 0;
                switch(i){
                    case 0:
                        co2eForNewPlan = newMealPlan.switchToChickenOnly(mOldMealPlan);
                        break;
                    case 1:
                        // Reduce all meat consumption by 50% but keep vegetable consumption the same
                        co2eForNewPlan = newMealPlan.reduceMeatByHalf(mOldMealPlan);
                        break;
                    case 2:
                        // Remove all meats except for fish
                        co2eForNewPlan = newMealPlan.switchToFishOnly(mOldMealPlan);
                        break;
                    case 3:
                        co2eForNewPlan = newMealPlan.removeMeatFromPlan(mOldMealPlan);
                        break;
                }
                calculateSavingsAndUpdateGraph(co2eForNewPlan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mJoinGreenFoodChallenge = (Button) view.findViewById(R.id.join_the_green_food_challenge);
        mJoinGreenFoodChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new PledgeFragment();
                mMainActivity.startFragment( newFragment, true, false);
            }
        });

    }

        private void calculateSavingsAndUpdateGraph(double co2eForNewPlan) {
            double savings = mOldMealPlan.calculateTotalCO2e() - co2eForNewPlan;

            // If negative savings
            if(savings < 0) {
                String toastString = getResources().getString(R.string.meal_plan_negative_savings_toast);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastString, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            String savingsText = getString(R.string.meal_plan_savings);
            savingsText = String.format( savingsText, savings );
            mMainActivity.setCO2SavedWithAlternate((int) savings);
            //String savingsText = "You have saved: " + String.format(Locale.US, "%.1f", savings) + " kg CO2e / year";
            mSavingsAfterNewPlan.setText(savingsText);

            // TO DO: Implement collective savings using convertor class
            double collectiveSavings = savings * METRO_VANCOUVER_POPULATION;
            String collectiveSavingsText = getString(R.string.meal_plan_collective_savings);
            collectiveSavingsText = String.format( collectiveSavingsText, collectiveSavings, Convertor.toKmDriven(collectiveSavings));
            /*String collectiveSavingsText = "If everyone in Metro Vancouver made these " +
                    "changes, together we could save: " +  String.format(Locale.US, "%.1f", collectiveSavings) + " kg CO2e per year!\n"+
                    "That's the same carbon footprint as driving "+ df.format((collectiveSavings/ DrivenConvectionNumber))+" km!";*/

            mCollectiveSavings.setText(collectiveSavingsText);

            setGraph((float) co2eForNewPlan, mOldMealPlan.calculateTotalCO2e());
        }

        private void setGraph(float newPlanCO2e, float oldPlanCO2e) {
            mCO2eComparisonChart.invalidate(); // Refreshes chart
            ArrayList<BarEntry> co2eValues = new ArrayList<>();
            //float spaceBetweenBars = 1.5f;

            co2eValues.add(new BarEntry(0, newPlanCO2e));
            co2eValues.add(new BarEntry(1, oldPlanCO2e));

            // Add data
            BarDataSet co2eValuesDataSet;
            co2eValuesDataSet = new BarDataSet(co2eValues, "CO2 Values");
            co2eValuesDataSet.setColors(getResources().getColor(R.color.colorPrimary, null));
            co2eValuesDataSet.setValueTextSize(13f);
            BarData co2eData = new BarData(co2eValuesDataSet);

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
            //chart.getAxisLeft().setAxisMinimum(MIN_VALUE_FOR_CHART);

            // Disable all axises
            //chart.getAxisRight().setEnabled(false);
            //chart.getXAxis().setEnabled(true);
            //chart.getXAxis().setDrawGridLines(false);
            //chart.getAxisLeft().setEnabled(true);

            XAxis xAxis = mCO2eComparisonChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(12f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            String[] labels = { getString(R.string.new_plan), getString(R.string.old_plan) };
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        }
    }
