package ca.cmpt276.greengoblins.emission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.greengoblins.PlaceModel.CustomInfoWindowAdapter;
import ca.cmpt276.greengoblins.PlaceModel.PlaceInfo;
import ca.cmpt276.greengoblins.PlaceModel.PlaceAutoCompleteAdapter;
import ca.cmpt276.greengoblins.fragments.Meal.MakeMealFragment;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_PERMISSION = 1234;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final float MAP_ZOOM = 15f;

    private LatLngBounds currentBounds;
    private AutoCompleteTextView searchBar;
    private ImageView locationButton, infoButton, nearbyButton, passLocationButton;

    private Boolean permissionFlag = false;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("map activity", "onMapReady: called");
        map = googleMap;

        if (permissionFlag) {
            getCurrentLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);


            initialization();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        searchBar = (AutoCompleteTextView) findViewById(R.id.search_bar_map);
        locationButton = (ImageView)findViewById(R.id.icon_gps);
        infoButton = (ImageView)findViewById(R.id.icon_info);
        nearbyButton = (ImageView)findViewById(R.id.icon_nearby);
        passLocationButton = (ImageView)findViewById(R.id.icon_passlocation);
        getLocationPermission();

        }

        private void initialization(){

            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            searchBar.setOnItemClickListener(mAutoCompleteClickListener);

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                    .build();

            mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, currentBounds, typeFilter);

            searchBar.setAdapter(mPlaceAutoCompleteAdapter);

            searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.KEYCODE_SEARCH ){
                        //execute locating function
                        getLocationOfSearch();
                        searchBar.setText("");
                    }
                    return false;
                }
            });

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentLocation();
                    mPlace = null;
                }
            });

            passLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passMealLocation();
                    try{
                    if(mMarker.isInfoWindowShown()) {
                        mMarker.hideInfoWindow();
                    }
                     }catch (NullPointerException e){
                    Log.e("map act", "onClick: info button " + e.getMessage());
                 }
                }
            });

            nearbyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findNearbyPlaces();
                }
            });

            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(mMarker.isInfoWindowShown()){
                            mMarker.hideInfoWindow();
                        }else{
                            Log.d("map activity", "onClick: info button" + mPlace.toString());
                            mMarker.showInfoWindow();
                        }
                    }catch (NullPointerException e){
                        Log.e("map act", "onClick: info button " + e.getMessage());
                    }
                }
            });

            ArrayList<LatLng> latLngs = fillArrayFromDatabase();
            fillMapFromArray(latLngs);
            hideKeyboard();
        }

    private void findNearbyPlaces(){
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(MapActivity.this), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesNotAvailableException e){
            Log.e("map activity", "onClick: not available exception " + e.getMessage());
        }catch (GooglePlayServicesRepairableException e){
            Log.e("map activity", "onClick: repairable exception " + e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("%s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(updatePlaceDetailsCallback);
            }
        }
    }

    private void fillMapFromArray(ArrayList<LatLng> mealLocations){
        for(int i = 0; i < mealLocations.size(); i++){
            MarkerOptions mOptions = new MarkerOptions().position(mealLocations.get(i)).title("Green Meal").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            map.addMarker(mOptions);
        }
    }

    private ArrayList<LatLng> fillArrayFromDatabase(){
        ArrayList<LatLng> mealLocationsDatabase = new ArrayList<>();
        return mealLocationsDatabase;
    }

    private void passMealLocation(){
        //either to grab location of device or place longitude and latitude that you have chosen
        Double longitude, latitude = 0d;
        if(mPlace != null) {
            Toast.makeText(this, "Location For Meal Saved", Toast.LENGTH_SHORT).show();
            LatLng mealLocation = mPlace.getLatLng();
            longitude = mealLocation.longitude;
            latitude = mealLocation.latitude;

            String[] locationData = {mPlace.getName(), latitude.toString(), longitude.toString()};

            // The name of the file to open.
            String fileName = "locationdata.txt";

            try {
                FileWriter fileWriter =
                        new FileWriter(fileName);

                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write(locationData[0]+ "," +locationData[1]+ "," +locationData[2]);
                // close file
                bufferedWriter.close();
            }
            catch(IOException ex) {
                 ex.printStackTrace();
            }

           /*
            Bundle bundle = new Bundle();
            bundle.putStringArray("location_data", locationData );

            Fragment mealFragment = new MakeMealFragment();
            mealFragment.setArguments( bundle );

            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add( R.id.frame_activity_map, mealFragment );
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }
        //here is where we can either pass meal location to the meal plan screen or pass the latitude and longitude so we can display the entire database of green meals on the map.
    }


        private void getLocationOfSearch(){
            hideKeyboard();
            String searchInput = searchBar.getText().toString();

            Geocoder geocoder = new Geocoder(MapActivity.this);
            List<Address> list = new ArrayList<>();
            try{
                list = geocoder.getFromLocationName(searchInput, 1);
            }catch(IOException e){
                Log.e("map activity", "getLocationOfSearch: error on input string" + e.getMessage() );
            }

            if(list.size() > 0){
                Address address = list.get(0);
                moveCameraToLatLng(new LatLng(address.getLatitude(), address.getLongitude()), MAP_ZOOM, address.getAddressLine(0));

            }
        }

        private void getLocationPermission(){
            Log.d("map activity", "getLocationPermission: called");
            String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        permissionFlag = true;
                        initializeMap();
                }else{
                    ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_PERMISSION);
                }
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_REQUEST_PERMISSION);
            }
        }

    private LatLngBounds findBoundsForAutoComplete(LatLng latLng){
            LatLngBounds bounds = new LatLngBounds(new LatLng((latLng.latitude - 2), latLng.longitude - 2),new LatLng((latLng.latitude + 2),latLng.longitude + 2));
            return bounds;
    }

        private void getCurrentLocation(){
            Log.d("map activity", "getCurrentLocation: gets the devices location if enabled");
            Toast.makeText(this, "Loading Current Location", Toast.LENGTH_SHORT).show();
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
            try{
                if(permissionFlag){
                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                Log.d("map activity", "getCurrentLocation: found location");
                                Location currentLocation = (Location) task.getResult();
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                currentBounds = findBoundsForAutoComplete(latLng);
                                moveCameraToLatLng(latLng, MAP_ZOOM, "Your Location");
                            }else{
                                Log.d("map activity", "getCurrentLocation: location null");
                                Toast.makeText(MapActivity.this, "unable to find current location",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }catch (SecurityException e){
                Log.e("map activity", "getCurrentLocation: exception caught" + e.getMessage());
            }
        }

        private void moveCameraToLatLng(LatLng latLng, float zoomAmt, String placeName){
            Log.d("map activity", "moveCameraToLatLng: moving camera to lat / lng" + latLng.latitude + " " + latLng.longitude );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomAmt));

            if(!placeName.equals("Your Location")){
                MarkerOptions options = new MarkerOptions().position(latLng).title(placeName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                map.addMarker(options);
            }
            hideKeyboard();
            searchBar.setText("");
        }

        private void moveCameraToLatLng(LatLng latLng, float zoomAmt, PlaceInfo placeInfo){
            Log.d("map activity", "moveCameraToLatLng: moving camera to lat / lng" + latLng.latitude + " " + latLng.longitude );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomAmt));

            map.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapActivity.this));

            if(placeInfo != null){
                try{
                    String description = "Address: " + placeInfo.getAddress() + "\n" +
                            "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                            "Rating: " + placeInfo.getRating() + "\n" +
                            "Website: " + placeInfo.getWebsiteUri() + "\n";

                    MarkerOptions options = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    mMarker = map.addMarker(options);

                }catch (NullPointerException e){
                    Log.e("map act", "moveCameraLATLNG: null pointer" + e.getMessage());
                }
            }else{
                map.addMarker(new MarkerOptions().position(latLng));
            }

            hideKeyboard();
            searchBar.setText("");
        }

        private void initializeMap(){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("map activity", "onRequestPermissionResult: called");
        permissionFlag = false;

        switch(requestCode){
            case LOCATION_REQUEST_PERMISSION:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            permissionFlag = false;
                            return;
                        }
                    }
                    Log.d("map activity", "Permission Granted");
                    permissionFlag = true;
                    //initialize map as permissions have been granted
                    initializeMap();
                }
            }
        }
    }
    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();
            final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(position);

            final String placeID = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeID);
            placeResult.setResultCallback(updatePlaceDetailsCallback);

        }
    };

    private ResultCallback<PlaceBuffer> updatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                //failed to get query
                places.release();
                return;
            }
            final Place place = places.get(0);
            //custom place object

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setId(place.getId());
                mPlace.setLatLng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
            }catch (NullPointerException e){
                Log.e("map activity", "onResult: null pointer on place object creation" + e.getMessage());
            }
            moveCameraToLatLng(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude), MAP_ZOOM, mPlace);
            places.release();
        }
    };

}
