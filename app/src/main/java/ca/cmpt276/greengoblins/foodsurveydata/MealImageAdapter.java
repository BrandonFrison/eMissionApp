package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import ca.cmpt276.greengoblins.emission.R;

public class MealImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Meal> mealList;
    private StorageReference firebaseStorageReference;

    public MealImageAdapter(Context c, List<Meal> mealList) {
        this.mContext = c;
        this.mealList = mealList;
        firebaseStorageReference = FirebaseStorage.getInstance().getReference().child("MealPics");
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(185, 185));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // Gets meal pic
        Task<Uri> downloadUrl = firebaseStorageReference.child(mealList.get(position).getMealID()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(position < mealList.size()){
                            setMealPic(parent.getContext(), firebaseStorageReference.child(mealList.get(position).getMealID()), imageView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        setMealPic(parent.getContext(), firebaseStorageReference.child("defaultPic.png"), imageView);
                    }
                });

        return imageView;
    }

    public void setMealPic(Context context, StorageReference storageReference, ImageView imageView) {
        Glide
                .with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .override(150,150)
                .centerCrop()
                .into(imageView);
    }
}
