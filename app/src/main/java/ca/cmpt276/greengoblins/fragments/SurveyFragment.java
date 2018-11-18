package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.cmpt276.greengoblins.emission.ExplanationActivity;
import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.FoodSurveyHistoryManager;

import static java.lang.Integer.parseInt;

public class SurveyFragment extends Fragment implements View.OnClickListener {

    private MainActivity mMainActivity;

    private ArrayList<TextView> mServingValueFields;
    private ArrayList<ImageButton> mButtons;

    private TextView mTotalServingValueField;
    private RadioGroup mGenderSelection;
    private TextView mTotalServingNeededField;
    private ArrayList<Integer> mServingSizeAmounts;
    private EditText mAgeSelection;
    private Spinner mPresetMealSelection;
    private RadioButton mGenderMale;
    private RadioButton mGenderFemale;

    private FloatingActionButton mActionButton;
    double multiplier = 1.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.toolbar_calculator);
        mServingValueFields = new ArrayList<TextView>();
        mServingSizeAmounts = new ArrayList<Integer>();
        mMainActivity = (MainActivity) getActivity();

        // Numeric TextView instantiation
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewBeefAmount ));
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewPorkAmount ));
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewChickenAmount ));
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewFishAmount ));
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewEggsAmount ));
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewBeansAmount ));
        mServingValueFields.add( (TextView) view.findViewById(R.id.textViewVegetablesAmount ));

        mTotalServingNeededField = (TextView)view.findViewById(R.id.textViewTotalNeededAmount);
        mGenderSelection = (RadioGroup)view.findViewById(R.id.radioGroupGender);
        mPresetMealSelection = (Spinner)view.findViewById(R.id.spinnerChooseMealPreset);
        mAgeSelection = (EditText)view.findViewById(R.id.editTextAgeAmount);
        mGenderFemale = (RadioButton)view.findViewById(R.id.radioButtonFemale);
        mGenderMale = (RadioButton)view.findViewById(R.id.radioButtonMale);
        ImageView ExplanationButton =(ImageView)view.findViewById(R.id.explanationButton);

        mButtons = new ArrayList<ImageButton>();
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonBeefleftarrow ) );
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonBeefrightarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonPorkleftarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonPorkrightarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonChickenleftarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonChickenrightarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonFishleftarrow ) );
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonFishrightarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonEggsleftarrow ) );
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonEggsrightarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonBeansleftarrow ) );
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonBeansrightarrow ));
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonVegetablesleftarrow ) );
        mButtons.add( (ImageButton) view.findViewById( R.id.imageButtonVegetablesrightarrow ));

        for ( ImageButton setValueButton : mButtons ){
            setValueButton.setOnClickListener(this);
        }

        mGenderSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()            {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                View radioButton = mGenderSelection.findViewById(selectedId);
                int position = mGenderSelection.indexOfChild(radioButton);
                if(mAgeSelection.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
                }else{
                    multiplier = setAmountNeeded(parseInt(mAgeSelection.getText().toString()), position);
                }
                //Toast.makeText(getContext(),"mult is " + multiplier + "id is " + position,Toast.LENGTH_LONG).show();
            }
        });

        mAgeSelection.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(mAgeSelection.getText().toString().equals("")) {
                   //do nothing as it would crash inputting null into the function
                    return false;
                }else{
                    if (mGenderMale.isChecked()) {
                        multiplier = setAmountNeeded(parseInt(mAgeSelection.getText().toString()), 0);
                    } else if (mGenderFemale.isChecked()) {
                        multiplier = setAmountNeeded(parseInt(mAgeSelection.getText().toString()), 1);
                    }
                }
                return false;
            }
        });



        mPresetMealSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillAmountInGrams(position);
                mPresetMealSelection.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for( int i = 0; i < mServingValueFields.size(); i++) {
            mServingSizeAmounts.add(0);
        }

        mTotalServingValueField = view.findViewById(R.id.textViewTotalAmount);

        mActionButton = mMainActivity.getActionButton();
        mActionButton.show();
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSurvey();
                saveSurvey();
            }
        });

        ExplanationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PopPage= new Intent(getContext(), ExplanationActivity.class);
                startActivity(PopPage);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButtonBeefleftarrow:
                setServing(0, -10);
                break;
            case R.id.imageButtonBeefrightarrow:
                setServing(0, 10);
                break;
            case R.id.imageButtonPorkleftarrow:
                setServing(1, -10);
                break;
            case R.id.imageButtonPorkrightarrow:
                setServing(1, 10);
                break;
            case R.id.imageButtonChickenleftarrow:
                setServing(2, -10);
                break;
            case R.id.imageButtonChickenrightarrow:
                setServing(2, 10);
                break;
            case R.id.imageButtonFishleftarrow:
                setServing(3, -10);
                break;
            case R.id.imageButtonFishrightarrow:
                setServing(3, 10);
                break;
            case R.id.imageButtonEggsleftarrow:
                setServing(4, -10);
                break;
            case R.id.imageButtonEggsrightarrow:
                setServing(4, 10);
                break;
            case R.id.imageButtonBeansleftarrow:
                setServing(5, -10);
                break;
            case R.id.imageButtonBeansrightarrow:
                setServing(5, 10);
                break;
            case R.id.imageButtonVegetablesleftarrow:
                setServing(6, -10);
                break;
            case R.id.imageButtonVegetablesrightarrow:
                setServing(6, 10);
                break;
        }
    }

    public void setServingFromPreset(int servingIndex, int value){
        mServingSizeAmounts.set(servingIndex,(int)(value*multiplier));
        updateFields();
    }

    public void setServing(int servingIndex, int value){
        int servingValue = mServingSizeAmounts.get(servingIndex) + (int)(value*multiplier);

        if ( servingValue < 0)
            mServingSizeAmounts.set(servingIndex, 0);
        else
            mServingSizeAmounts.set(servingIndex, servingValue);

        updateFields();
    }

    public void fillAmountInGrams(int mealPlanid){
        switch (mealPlanid){
            case 0:
                break;
            case 1:
                setServingFromPreset(0, 30);
                setServingFromPreset(1, 30);
                setServingFromPreset(2, 30);
                setServingFromPreset(3, 30);
                setServingFromPreset(4, 30);
                setServingFromPreset(5, 30);
                setServingFromPreset(6, 45);
                break;
            case 2:
                setServingFromPreset(0, 0);
                setServingFromPreset(1, 0);
                setServingFromPreset(2, 0);
                setServingFromPreset(3, 0);
                setServingFromPreset(4, 50);
                setServingFromPreset(5, 60);
                setServingFromPreset(6, 115);
                break;
            case 3:
                setServingFromPreset(0, 0);
                setServingFromPreset(1, 0);
                setServingFromPreset(2, 0);
                setServingFromPreset(3, 0);
                setServingFromPreset(4, 0);
                setServingFromPreset(5, 85);
                setServingFromPreset(6, 140);
                break;
            case 4:
                setServingFromPreset(0, 40);
                setServingFromPreset(1, 40);
                setServingFromPreset(2, 40);
                setServingFromPreset(3, 40);
                setServingFromPreset(4, 20);
                setServingFromPreset(5, 20);
                setServingFromPreset(6, 25);
                break;
            case 5:
                setServingFromPreset(0, 80);
                setServingFromPreset(1, 20);
                setServingFromPreset(2, 20);
                setServingFromPreset(3, 20);
                setServingFromPreset(4, 20);
                setServingFromPreset(5, 20);
                setServingFromPreset(6, 45);
                break;
            case 6:
                setServingFromPreset(0, 20);
                setServingFromPreset(1, 20);
                setServingFromPreset(2, 80);
                setServingFromPreset(3, 20);
                setServingFromPreset(4, 20);
                setServingFromPreset(5, 20);
                setServingFromPreset(6, 45);
                break;
            case 7:
                setServingFromPreset(0, 20);
                setServingFromPreset(1, 80);
                setServingFromPreset(2, 20);
                setServingFromPreset(3, 20);
                setServingFromPreset(4, 20);
                setServingFromPreset(5, 20);
                setServingFromPreset(6, 45);
                break;
            case 8:
                setServingFromPreset(0, 20);
                setServingFromPreset(1, 20);
                setServingFromPreset(2, 20);
                setServingFromPreset(3, 80);
                setServingFromPreset(4, 20);
                setServingFromPreset(5, 20);
                setServingFromPreset(6, 45);
                break;
            default:
                break;
        }
    }

    public double setAmountNeeded(int age, int gender){
        double value = 1.0;
        switch (gender){
            //male
            case 0:
                if(age > 13){
                    mTotalServingNeededField.setText("225");
                }else if(age <= 13 && age > 9){
                    mTotalServingNeededField.setText("150");
                    value = 0.68;
                }else{
                    mTotalServingNeededField.setText("75");
                    value = 0.35;
                }
                break;
                //female
            case 1:
                if(age > 13){
                    mTotalServingNeededField.setText("150");
                    value = 0.68;
                }else{
                    mTotalServingNeededField.setText("75");
                    value = 0.35;
                }
                break;
                //not set
            default:
                break;
        }
        return value;
    }

    private void saveSurvey(){
        ConsumptionTable newTable = mMainActivity.createDefaultFoodTable();
        int fieldValue;
        for ( int i = 0; i < mServingValueFields.size(); i++) {
            fieldValue = parseInt(mServingValueFields.get(i).getText().toString());
            newTable.addServing(fieldValue);
        }
        FoodSurveyHistoryManager foodSaver = new FoodSurveyHistoryManager();
        foodSaver.saveTableByDate(mMainActivity, newTable);
    }

    private void submitSurvey(){
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("survey_data", mServingSizeAmounts );

        Fragment resultFragment = new HistoryFragment();
        resultFragment.setArguments( bundle );

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frame_activity_content, resultFragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        mActionButton.hide();
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
