package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ca.cmpt276.greengoblins.emission.R;

public class MealImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Meal> mealList;

    public MealImageAdapter(Context c, List<Meal> mealList) {
        this.mContext = c;
        this.mealList = mealList;
    }

    public int getCount() {
        return mealList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("LOAD_EXCEPTION", mealList.get(position).getMealName());
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(R.drawable.treepledge); //set meal image reference here
        return imageView;
    }


}