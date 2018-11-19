package ca.cmpt276.greengoblins.emission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_PERMISSION = 1234;
    private static final float MAP_ZOOM = 15f;

    private Boolean permissionFlag = false;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("map activity", "onMapReady: called");
        Toast.makeText(this, "Loading Current Location", Toast.LENGTH_SHORT).show();
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

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
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

        private void getCurrentLocation(){
            Log.d("map activity", "getCurrentLocation: gets the devices location if enabled");

            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
            try{
                if(permissionFlag){
                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.d("map activity", "getCurrentLocation: found location");
                                Location currentLocation = (Location) task.getResult();

                                moveCameraToLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLatitude()), MAP_ZOOM);
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

        private void moveCameraToLatLng(LatLng latLng, float zoomAmt){
            Log.d("map activity", "moveCameraToLatLng: moving camera to lat / lng" + latLng.latitude + " " + latLng.longitude );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomAmt));

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


}
