package edu.weber.cs.w01402128.bathroomfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

public class MainActivity extends AppCompatActivity implements bathroomCreateionFragment.onButtonListener, BathroomRecyclerAdapter.OnClickListener,MapsFragment.OnClickListener {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private editBathroomFragment editBathroomFrag;
    private bathroomCreateionFragment bathroomCreationFrag;
    private MapsFragment mapFrag;
    private BathroomListFragment bathroomListFrag;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MenuItem addBathroomBtn, deleteBathroomBtn;


    //location services
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0, longitude = 0.0;
    private LocationRequest locationRequest;
    private int defaultUpdateInterval = 30;
    private int fastUpdateInterval = 5;
    private LocationCallback locationCallback;
    private Location currentLocation;

    private Bathroom tempBathroom;
    private List<Bathroom> bathroomList = new ArrayList<Bathroom>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fm = getSupportFragmentManager();
        ft = getFragmentManager().beginTransaction();

        bathroomCreationFrag = (bathroomCreateionFragment) fm.findFragmentById(R.id.createBathroomFrag);
        editBathroomFrag = (editBathroomFragment) fm.findFragmentById(R.id.editBathroomFrag);
        mapFrag = (MapsFragment) fm.findFragmentById(R.id.mapFrag);
        bathroomListFrag = (BathroomListFragment) fm.findFragmentById(R.id.bathroomListFrag);

        fm.beginTransaction().show(mapFrag).commit();
        fm.beginTransaction().hide(bathroomCreationFrag).commit();
        fm.beginTransaction().hide(editBathroomFrag).commit();
        fm.beginTransaction().hide(bathroomListFrag).commit();

        locationRequest = new LocationRequest();

        locationRequest.setInterval(1000 * defaultUpdateInterval);
        locationRequest.setFastestInterval(1000 * fastUpdateInterval);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);




        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                updateMap(locationResult.getLastLocation());
            }
        };


        updateGPS();


    }

    public void updateGPS() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user provided permission
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions. Put the values of location.
                    //latitude = location.getLatitude();
                    //longitude = location.getLongitude();
                    currentLocation = location;
                    mapFrag.setLastLocationPlaced(location);
//                    Log.d("test", "location: " + location);
                }
            });
        } else {
            // user hasn't provided permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();
    }

    public void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.removeLocationUpdates(locationCallback);

    }

    public void updateMap(Location location){

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
//        bathroomList.addAll(bathroomListFrag.getBathroomList());
//        mapFrag.updateList(bathroomList);

        for (Bathroom bathroom : bathroomList) {
            Log.d("test", "bathroom: " + bathroom.toString());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }else{
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main, menu);
        addBathroomBtn = menu.findItem(R.id.action_add);
        deleteBathroomBtn = menu.findItem(R.id.action_delete);

        deleteBathroomBtn.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                // It's easier to create a method to call here otherwise
                // it get's bloated and hard to read.
                fm.beginTransaction().show(bathroomCreationFrag).commit();
                fm.beginTransaction().hide(editBathroomFrag).commit();
                fm.beginTransaction().hide(bathroomListFrag).commit();
                fm.beginTransaction().hide(mapFrag).commit();
                return true;
            case R.id.action_listView:
                fm.beginTransaction().hide(bathroomCreationFrag).commit();
                fm.beginTransaction().hide(mapFrag).commit();
                fm.beginTransaction().hide(editBathroomFrag).commit();
                fm.beginTransaction().show(bathroomListFrag).commit();
                if(deleteBathroomBtn.isVisible()){
                    updateOptionMenu();
                }
                return true;
            case R.id.action_mapView:
                fm.beginTransaction().hide(bathroomCreationFrag).commit();
                fm.beginTransaction().show(mapFrag).commit();
                fm.beginTransaction().hide(editBathroomFrag).commit();
                fm.beginTransaction().hide(bathroomListFrag).commit();
                if(deleteBathroomBtn.isVisible()){
                    updateOptionMenu();
                }
                return true;
            case R.id.action_delete:

                DeleteDialogFragment dialogFrag = new DeleteDialogFragment();
                dialogFrag.setCancelable(false);
                dialogFrag.show(fm, "delete_dialog");
                dialogFrag.getBathroom(tempBathroom);
                fm.beginTransaction().hide(editBathroomFrag).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void submitButtonClicked() {
        bathroomCreationFrag.getCurrentLocation(currentLocation);
        bathroomCreationFrag.addBathroom();
        fm.beginTransaction().hide(bathroomCreationFrag).commit();
        fm.beginTransaction().show(mapFrag).commit();
    }

    @Override
    public void listButtonClicked(Bathroom bathroom) {
        fm.beginTransaction().hide(bathroomCreationFrag).commit();
        fm.beginTransaction().hide(mapFrag).commit();
        fm.beginTransaction().show(editBathroomFrag).commit();
        fm.beginTransaction().hide(bathroomListFrag).commit();
        editBathroomFrag.addBathroomInfo(bathroom);
        tempBathroom = bathroom;
        updateOptionMenu();

    }

    public void updateOptionMenu(){
        if(deleteBathroomBtn.isVisible()){
            deleteBathroomBtn.setVisible(false);
            addBathroomBtn.setVisible(true);
        }else{
            deleteBathroomBtn.setVisible(true);
            addBathroomBtn.setVisible(false);
        }
    }

    @Override
    public void mapMarkerClicked(Bathroom bathroom) {
        fm.beginTransaction().hide(bathroomCreationFrag).commit();
        fm.beginTransaction().hide(mapFrag).commit();
        fm.beginTransaction().show(editBathroomFrag).commit();
        fm.beginTransaction().hide(bathroomListFrag).commit();
        editBathroomFrag.addBathroomInfo(bathroom);
        tempBathroom = bathroom;
        updateOptionMenu();

    }
}
