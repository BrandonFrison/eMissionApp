package ca.cmpt276.greengoblins.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.List;

import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;


/**
 * For now this fragment will serve as the survey result. We may change this.
 */
public class HistoryFragment extends Fragment {

    final double DrivenConvectionNuder = .200;  // 1 kg per 5 km
    public ArrayList<Integer> servingSizes;
    TextView mCO2eDisplay;
    PieChart pieChart;
    boolean flag =false;

    float mCO2eScore = 0;
    ArrayList<String> categories;

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
        getActivity().setTitle("Previous CO2e");
        //pie chart part
        pieChart = (PieChart) view.findViewById(R.id.PieChart);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Emission");
        pieChart.setCenterTextSize(12);
        pieChart.getDescription().setEnabled(false);

        mCO2eDisplay = (TextView) view.findViewById(R.id.textViewResult);

        Bundle surveyBundle = getArguments();
        if(surveyBundle == null) {
            String resultText = "No data yet, please do the Co2e calculator";
            mCO2eDisplay.setText(resultText);
        }
        else{
            flag = true;
            servingSizes = surveyBundle.getIntegerArrayList("survey_data");
        }

        if(flag==true) { // pie chart will work only after receiving data
        ConsumptionTable servingTable = new ConsumptionTable( servingSizes );
        mCO2eScore = servingTable.calculateTotalCO2e();


            //round number  to 2 decimal
            final DecimalFormat df = new DecimalFormat(".00");

            String resultText = "Total CO2e Score: " + df.format(mCO2eScore) + " kg per year.\n\n" +
                    "That's equivalent to driving: " + df.format(mCO2eScore / DrivenConvectionNuder) + " km\n\n" +
                    "Click events on pie chart to get details.\n\n" +
                    "If you want to try different diets we provided, you can click the bottom button.";
            mCO2eDisplay.setText(resultText);

            //We should be able to get most or all of this from the new consumptiontable
            categories = new ArrayList<String>();
            categories.add(getString(R.string.table_category1));
            categories.add(getString(R.string.table_category2));
            categories.add(getString(R.string.table_category3));
            categories.add(getString(R.string.table_category4));
            categories.add(getString(R.string.table_category5));
            categories.add(getString(R.string.table_category6));
            categories.add(getString(R.string.table_category7));

            List<PieEntry> pieEntries = new ArrayList<PieEntry>();

            for (int i = 0; i < servingSizes.size(); i++) {
                if (servingSizes.get(i) > 0 && servingSizes.get(i) != null)
                    pieEntries.add(new PieEntry(servingSizes.get(i), categories.get(i)));
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
                    String toastText = categories.get(xCategory) + ": " + df.format(h.getY()) + " Co2e/g";
                    Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }
        // Reduce Carbon button
        // Takes user to another activity where they can choose a meal plan
        Button mReduceFootPrintButton = (Button) view.findViewById(R.id.reduce_footprint_button);
        mReduceFootPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* this should probably be fragment as well instead of new activity

               Intent intent = new Intent(ResultsActivity.this, MealPlanActivity.class);
                ConsumptionTable results = (ConsumptionTable) getIntent().getSerializableExtra("resultTable");
                intent.putExtra("resultTable", results);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("PieChartValue",servingSizes);
    }
}
