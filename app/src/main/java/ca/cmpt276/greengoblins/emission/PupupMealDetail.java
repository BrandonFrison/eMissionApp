package ca.cmpt276.greengoblins.emission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PupupMealDetail extends AppCompatActivity {
    private TextView mMealInfo;
    boolean mViewMyMeal = false;  // true when user click the check box to view his own meals
    private Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupup_meal_detail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        //===============================================================
        mDeleteButton = findViewById(R.id.delete_button);
        mDeleteButton.setVisibility(View.INVISIBLE);
        mMealInfo = findViewById(R.id.popup_meal_info);
        String MealInfoText = "Meal Name:    \n\n" +
                "Main protein:    \n\n" +
                "Restaurant:    \n\n" +
                "Location:  ";
        mMealInfo.setText(MealInfoText);

        if(mViewMyMeal ==true ){//when user click the check box to view his own meals
            mDeleteButton.setVisibility(View.VISIBLE);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteMeal();
                }
            });
        }

    }

    private void DeleteMeal() {
    }
}
