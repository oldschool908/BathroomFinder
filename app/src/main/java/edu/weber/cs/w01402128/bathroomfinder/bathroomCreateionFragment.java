package edu.weber.cs.w01402128.bathroomfinder;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.AppDatabase;
import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bathroomCreateionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bathroomCreateionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View root;

    //UI textViews and btns
    private TextInputEditText nameTV;
    private RadioButton keyYesRB, keyNoRB, handiYesRB, handiNoRB;
    private Button submitBtn;
    private MapView map;

    //database variables
    private AppDatabase db;
    private LiveData<List<Bathroom>> bathrooms;
    private Location currentLocation;

    //On button listener
    private onButtonListener mCallBack;

    public interface onButtonListener{
        void submitButtonClicked();
    }

    public bathroomCreateionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment bathroomCreateionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static bathroomCreateionFragment newInstance(String param1, String param2) {
        bathroomCreateionFragment fragment = new bathroomCreateionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_bathroom_createion, container, false);
    }



    @Override
    public void onResume() {
        super.onResume();

        nameTV = root.findViewById(R.id.nameTextInput);
        keyYesRB = root.findViewById(R.id.keyYesRB);
        keyNoRB = root.findViewById(R.id.keyNoRB);
        handiYesRB = root.findViewById(R.id.handicapYestRB);
        handiNoRB = root.findViewById(R.id.handicapNoRB);

        new Thread(new Runnable() {
            @Override
            public void run() {
                db = AppDatabase.getInstance(getContext());

                bathrooms = db.bathroomDAO().getAll();
                //Log.d("test", "Bathrooms: "  + bathrooms);
            }
        }).start();

    }

    @Override
    public void onStart() {
        super.onStart();

        submitBtn = root.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.submitButtonClicked();
            }
        });

    }

    public void addBathroom(){
        final String bathroomName = nameTV.getText().toString();
        boolean hasKey = false;
        boolean isHandi = true;
        final Double lat = currentLocation.getLatitude(), lon = currentLocation.getLongitude();
        final boolean longWait = false;
        final boolean unavailable = false;
        final String unavailableNote = "Available";

        if (keyYesRB.isChecked()){
            hasKey = true;
        }else if(keyNoRB.isChecked()){
            hasKey = false;
        }

        if(handiYesRB.isChecked()){
            isHandi = true;
        }else if(handiNoRB.isChecked()){
            isHandi = false;
        }

        final boolean hasKeyFinal = hasKey;
        final boolean isHandiFinal = isHandi;

        new Thread(new Runnable() {
            @Override
            public void run() {
                db = AppDatabase.getInstance(getContext());
                db.bathroomDAO().insertAll(new Bathroom(bathroomName,lon,lat,longWait,hasKeyFinal,isHandiFinal,unavailable,unavailableNote));
                //bathrooms = db.bathroomDAO().getAll();

                //Log.d("test", "Bathrooms: "  + bathrooms.toString());
            }
        }).start();

        //testing

        nameTV.setText("");
        keyYesRB.setChecked(false);
        handiYesRB.setChecked(true);
    }

    public void getCurrentLocation(Location location){
        currentLocation = location;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        try{
            mCallBack = (onButtonListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " Must implement onButtonListener");
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {





        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}