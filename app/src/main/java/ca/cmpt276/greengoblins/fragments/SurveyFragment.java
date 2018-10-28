package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.cmpt276.greengoblins.emission.R;

public class SurveyFragment extends Fragment {

    private ArrayList<TextView> mServingValueFields;
    private TextView mTotalServingValueField;

    private ArrayList<Integer> mServingSizeAmounts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mServingValueFields = new ArrayList<TextView>();
        mServingSizeAmounts = new ArrayList<Integer>();

        // Numeric TextView instantiation
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewBeefAmount));
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewPorkAmount));
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewChickenAmount));
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewFishAmount));
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewEggsAmount));
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewBeansAmount));
        mServingValueFields.add((TextView) view.findViewById(R.id.textViewVegetablesAmount));

        for( int i = 0; i < mServingValueFields.size(); i++){
            mServingSizeAmounts.add(0);
        }

        mTotalServingValueField = view.findViewById(R.id.textViewTotalAmount);
    }

    public void setServing(int servingIndex, int value){
        int servingValue = mServingSizeAmounts.get(servingIndex) + value;

        if ( servingValue < 0)
            mServingSizeAmounts.set(servingIndex, 0);
        else
            mServingSizeAmounts.set(servingIndex, servingValue);

        updateFields();
    }

    private int getFieldsTotal(){
        int result = 0;
        for( int i = 0; i < mServingSizeAmounts.size(); i++ ){
            result += mServingSizeAmounts.get( i );
        }
        return result;
    }

    private void updateFields(){
        for( int i = 0; i < mServingValueFields.size(); i++){
            mServingValueFields.get( i ).setText( String.valueOf( mServingSizeAmounts.get( i ) ) );
        }
        mTotalServingValueField.setText( String.valueOf( getFieldsTotal() ) );
    }
}
