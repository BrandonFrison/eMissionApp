package ca.cmpt276.greengoblins.emission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.cmpt276.greengoblins.foodsurveydata.Meal;
import ca.cmpt276.greengoblins.fragments.Meal.MealListFragment;

import static ca.cmpt276.greengoblins.emission.AddMealActivity.ImgUtils.saveImageToGallery;


public class AddMealActivity extends AppCompatActivity {
    private ImageView mAddMealImageView;
    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;
    private ImageView imageview;
    private Uri imageUri;

    private FirebaseAuth mAuthenticator;

    private EditText mMealNameInputField;
    private EditText mMainProteinIngredientInputField;
    private EditText mRestaurantNameInputField;
    private EditText mLocationInputField;
    private EditText mDescriptionInputField;

    private Button mPostMeal;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meals);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9),(int)(height*0.9));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        //================================================================
        mAuthenticator = FirebaseAuth.getInstance();

        mMealNameInputField = (EditText) findViewById(R.id.add_meal_name);
        mMainProteinIngredientInputField = (EditText) findViewById(R.id.main_protein);
        mRestaurantNameInputField = (EditText) findViewById(R.id.restaurant_name);
        mLocationInputField = (EditText) findViewById(R.id.restaurant_location);
        mDescriptionInputField = (EditText) findViewById(R.id.add_meal_description);
        mPostMeal = (Button) findViewById(R.id.post_meal);

        mPostMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(publishMeal()) {
                    finish();
                }
            }
        });


        mAddMealImageView = findViewById(R.id.add_meal_image);
        mAddMealImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChoosePicDialog();

                }
            });



    }

    private boolean publishMeal() {
        boolean isPosted = false;

        String mealName = mMealNameInputField.getText().toString().trim();
        String mainProteinIngredient = mMainProteinIngredientInputField.getText().toString().trim();
        String restaurantName = mRestaurantNameInputField.getText().toString().trim();
        String location = mLocationInputField.getText().toString().trim();
        String description = mDescriptionInputField.getText().toString().trim();

        if(isInputValid(mealName, mainProteinIngredient, restaurantName, location)) {
            final DatabaseReference mealDatabase;
            mealDatabase = FirebaseDatabase.getInstance().getReference("Meals");

            Bundle bundle = getIntent().getExtras();
            String mealCreatorID = bundle.getString("userID");

            String mealID = mealDatabase.push().getKey();
            Meal meal = new Meal(mealName, mainProteinIngredient, restaurantName, location, description, mealCreatorID, mealID);
            mealDatabase.child(mealID).setValue(meal);

            clearInputFields();
            Toast.makeText(getApplicationContext(), R.string.meal_successfully_published, Toast.LENGTH_SHORT).show();

            isPosted = true;
        }

        return isPosted;
    }

    private boolean isInputValid(String mealName, String mainProteinIngredient, String restaurantName, String location) {
        boolean isValid = true;

        if( mealName.isEmpty() ){
            Toast.makeText( getApplicationContext(), R.string.empty_meal_name_message, Toast.LENGTH_SHORT ).show();
            isValid = false;
        }
        else if( mainProteinIngredient.isEmpty() ){
            Toast.makeText( getApplicationContext(), R.string.empty_main_protein_ingredient_message, Toast.LENGTH_SHORT ).show();
            isValid = false;
        }
        else if( restaurantName.isEmpty() ) {
            Toast.makeText(getApplicationContext(), R.string.empty_restaurant_name_message, Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else if( location.isEmpty() ) {
            Toast.makeText(getApplicationContext(), R.string.empty_location_message, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    void clearInputFields() {
        mMealNameInputField.getText().clear();
        mMainProteinIngredientInputField.getText().clear();
        mRestaurantNameInputField.getText().clear();
        mLocationInputField.getText().clear();
        mDescriptionInputField.getText().clear();
    }

    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Meal Image");
        String[] items = { "Select from Album", "Take Photo"};
        builder.setNegativeButton("Cancel", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Select from Album
                        select_photo();
                        break;
                    case 1: //Take Photo
                        take_photo();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    private void take_photo() {
        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)) {
            File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(this, "com.llk.study.activity.PhotoActivity", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }else
        {

            Toast.makeText(this, "No sd card found",Toast.LENGTH_LONG).show();
        }
    }

    private void select_photo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO :
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        mAddMealImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_PHOTO :
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT > 19) {
                        handleImgeOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri,null);
            }else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mAddMealImageView.setImageBitmap(bitmap);
            saveImageToGallery(getApplicationContext(),bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }else {
                    Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public static class ImgUtils {
        public static boolean saveImageToGallery(Context context, Bitmap bmp) {
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "eMissionImage";
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
