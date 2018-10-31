package ca.cmpt276.greengoblins.Subfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import ca.cmpt276.greengoblins.emission.R;

public class AllCo2ePledgeAmount extends Fragment {
    View view = null;
    TextView TotalCo2eAmount_Pledge;
    TextView ConvectionText;
    int convectionNum_tree = 63;
    int convectionNum_forest = 2;
    int convectionNum_driving = 2222;
    int TotalCo2ePledge = 7777777;
    int CurrentPledgedPeople = 11111;
    int AverageCo2ePerPerson = 77;
    int TotalCo2ePledgeDigits =7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allco2eamount,container,false);


        //total co2e
        TotalCo2eAmount_Pledge = view.findViewById(R.id.pledge_totalamount_text);
        String TotalCo2eAmount_Pledge_text= TotalCo2ePledge+"Tonnes of CO2e\n"+
                "Pledged to be saved in Metro Vancouver by \n\t\t" +
                CurrentPledgedPeople+"residents\n"+
                "That's "+AverageCo2ePerPerson+" CO2e per person!";
        SpannableString ss1=  new SpannableString(TotalCo2eAmount_Pledge_text);
        ss1.setSpan(new RelativeSizeSpan(2f), 0,TotalCo2ePledgeDigits, 0); // set size
        //ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, TotalCo2ePledgeDigits, 0);// set color
        TotalCo2eAmount_Pledge.setText(ss1);




        //convection
        ConvectionText = view.findViewById(R.id.convection_text);
        String ConvectionText_text = "Together,we've saved:\n"+
                "\t"+convectionNum_tree+" Trees\n"+
                "\t"+convectionNum_forest+" Forests\n"+
                "\t"+convectionNum_driving+" Km worth of driving";
        ConvectionText.setText(ConvectionText_text);



        return view;
    }

}
