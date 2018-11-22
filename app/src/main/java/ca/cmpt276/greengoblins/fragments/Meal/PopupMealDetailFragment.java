package ca.cmpt276.greengoblins.fragments.Meal;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;


public class PopupMealDetailFragment extends DialogFragment {

    private MainActivity mMainActivity;

    boolean mViewMyMeal = false;  // true when user click the check box to view his own meals
    private Button mDeleteButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = (MainActivity) getActivity();

        mMainActivity.setContentView(R.layout.activity_popup_meal_detail);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mMainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getDialog().getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getDialog().getWindow().setAttributes(params);
        //===============================================================
        mDeleteButton = mMainActivity.findViewById(R.id.delete_button);
        mDeleteButton.setVisibility(View.INVISIBLE);

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
