package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.MealPlan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class ReduceFragment extends Fragment {
    final static int MIN_VALUE_FOR_CHART = 0;
    final static double METRO_VANCOUVER_POPULATION = 2.463E6;

    MainActivity mMainActivity;
    private Button mMealPlan1;
    private Button mMealPlan2;
    private Button mMealPlan3;
    private Button mMealPlan4;
    private Button mJoinGreenFoodChallenge;
    private TextView mSavingsAfterNewPlan;
    private TextView mCollectiveSavings;
    ConsumptionTable mOldMealPlan;
    HorizontalBarChart mCO2eComparisonChart;
    private double savings = 0.0d;
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
        getActivity().setTitle("Reduce CO2e");

        mMainActivity = (MainActivity) getActivity();
        Bundle historyBundle = getArguments();
        mOldMealPlan = (ConsumptionTable) historyBundle.getSerializable("resultTable");
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

        mJoinGreenFoodChallenge = (Button) view.findViewById(R.id.join_the_green_food_challenge);
        mJoinGreenFoodChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSavings();
            }
        });
    }

    private void saveSavings(){
       if(savings > 0){
           if( !mMainActivity.checkUserLogin() ) {
               mMainActivity.popupLogin();
           } else {
               Bundle bundle = new Bundle();
               bundle.putDouble("savings_data", savings);

               Fragment pledgeFragment = new MakePledgeFragment();
               pledgeFragment.setArguments( bundle );

               FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace( R.id.frame_activity_content, pledgeFragment );
               fragmentTransaction.addToBackStack(null);
               fragmentTransaction.commit();
           }
       }
    }

        private void calculateSavingsAndUpdateGraph(double co2eForNewPlan) {
            savings = mOldMealPlan.calculateTotalCO2e() - co2eForNewPlan;

            // If negative savings
            if(savings < 0) {
                String toastString = getResources().getString(R.string.meal_plan_negative_savings_toast);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastString, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            String savingsText = "You have saved: " + String.format(Locale.US, "%.1f", savings) + " kg CO2e / year";
            mSavingsAfterNewPlan.setText(savingsText);

            // TO DO: Implement collective savings using convertor class
            double collectiveSavings = savings * METRO_VANCOUVER_POPULATION;
            String collectiveSavingsText = "If everyone in Metro Vancouver made these " +
                    "changes, together we could save: " +  String.format(Locale.US, "%.1f", collectiveSavings) + " kg CO2e per year!\n"+
                    "That's the same carbon footprint as driving "+ df.format((collectiveSavings/ DrivenConvectionNumber))+" km!";

            mCollectiveSavings.setText(collectiveSavingsText);

            setGraph((float) co2eForNewPlan, mOldMealPlan.calculateTotalCO2e());
        }

        private void setGraph(float newPlanCO2e, float oldPlanCO2e) {
            mCO2eComparisonChart.invalidate(); // Refreshes chart
            ArrayList<BarEntry> co2eValues = new ArrayList<>();
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
