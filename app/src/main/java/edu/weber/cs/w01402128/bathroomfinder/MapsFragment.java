package edu.weber.cs.w01402128.bathroomfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.AppDatabase;
import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

public class MapsFragment extends Fragment {

    private View root;
    private Bathroom bathroom;
    private OnClickListener mCallBack;
    private LatLng lastLocationPlaced;

    public interface OnClickListener{
        void mapMarkerClicked(Bathroom bathroom);
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
           //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            //lastLocationPlaced = sydney;

            new ViewModelProvider(MapsFragment.this)
                    .get(AllBathroomViewModel.class)
                    .getAllBathrooms(getContext())
                    .observe(MapsFragment.this, new Observer<List<Bathroom>>() {
                        @Override
                        public void onChanged(@Nullable List<Bathroom> bathrooms) {
                            if( bathrooms != null ){
                                // updateList(bathrooms);
                                for (Bathroom bathroom : bathrooms) {
                                    LatLng latLng = new LatLng(bathroom.getLat(), bathroom.getLon());
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title("" + bathroom.getBathroom_id());
                                    googleMap.addMarker(markerOptions);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

                                }
                            }
                        }
                    });
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            String bName = marker.getTitle();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    AppDatabase db = AppDatabase.getInstance(getContext());

                                    bathroom = db.bathroomDAO().getBathroomByID(Integer.parseInt(bName));
                                    Log.d("test", "Bathroom: "  + bathroom);
                                    mCallBack.mapMarkerClicked(bathroom);
                                }
                            }).start();



                            return false;
                        }
                    });

                    if(lastLocationPlaced != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced, 12.0f));
                    }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        bathroomLocations.add(0, new Bathroom("Name", -111.0,40.0,false,false,false,false,""));


    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try{
            mCallBack = (OnClickListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " Must implement onButtonListener");
        }
    }

    public void setLastLocationPlaced(Location currentLocation){
        this.lastLocationPlaced = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }



}