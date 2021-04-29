package edu.weber.cs.w01402128.bathroomfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.AppDatabase;
import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editBathroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editBathroomFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View root;
    private Bathroom currentBathroom;

    // UI parts
    private TextView nameTV, longTV, latTV, keyTv, handicapTV, bathroomStatusTV;
    private Button unavailableBTN, navigateBTN;
    private RadioButton outOfOrderRB, closeRB, yesRB, noRB;
    private RadioGroup unavailableGroup, longWaitGroup;
    private FloatingActionButton saveFAB;

    private AppDatabase db;
    //private LiveData<List<Bathroom>> bathrooms;

    public editBathroomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editBathroomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editBathroomFragment newInstance(String param1, String param2) {
        editBathroomFragment fragment = new editBathroomFragment();
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
    public void onResume() {
        super.onResume();

        nameTV = root.findViewById(R.id.nameTextView);
        longTV = root.findViewById(R.id.longTextView);
        latTV = root.findViewById(R.id.latTextView);
        keyTv = root.findViewById(R.id.keyTextView);
        handicapTV = root.findViewById(R.id.handicapTextView);
        bathroomStatusTV = root.findViewById(R.id.bathroomStatusTV);

        unavailableBTN = root.findViewById(R.id.unavailableBtn);

        outOfOrderRB = root.findViewById(R.id.outOfOrderRB);
        closeRB = root.findViewById(R.id.closedRB);
        unavailableGroup = root.findViewById(R.id.unavailableGroupRB);
        navigateBTN = root.findViewById(R.id.navigateBtn);

        longWaitGroup = root.findViewById(R.id.longWaitGroup);
        yesRB = root.findViewById(R.id.longWaitYesRB);
        noRB = root.findViewById(R.id.longWaitNoRB);
        if(currentBathroom != null) {
            if (currentBathroom.isLong_wait()) {
                yesRB.setChecked(true);
            } else {
                noRB.setChecked(true);
            }

            if (currentBathroom.isUnavailable()) {
                unavailableGroup.setVisibility(root.VISIBLE);
                if (currentBathroom.getUnavailable_note().equals("Out of Order")) {
                    outOfOrderRB.setChecked(true);
                } else if (currentBathroom.getUnavailable_note().equals("Closed")) {
                    closeRB.setChecked(true);
                }
            } else {
                unavailableGroup.setVisibility(root.INVISIBLE);
            }
        }else{

            unavailableGroup.setVisibility(root.INVISIBLE);
        }

        unavailableBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unavailableGroup.getVisibility() != root.VISIBLE){
                    unavailableGroup.setVisibility(root.VISIBLE);
                }else{
                    unavailableGroup.setVisibility(root.INVISIBLE);
                    outOfOrderRB.setChecked(false);
                    closeRB.setChecked(false);

                }
            }
        });

        saveFAB = root.findViewById(R.id.saveFAB);
        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(v.getContext(), "Saving Update!", Toast.LENGTH_SHORT);
                toast.show();

                if(yesRB.isChecked()){
                    currentBathroom.setLong_wait(true);
                } else if(noRB.isChecked()){
                    currentBathroom.setLong_wait(false);
                }

                if(outOfOrderRB.isChecked()){
                    currentBathroom.setUnavailable(true);
                    currentBathroom.setUnavailable_note("Out of Order");
                }else if(closeRB.isChecked()){
                    currentBathroom.setUnavailable(true);
                    currentBathroom.setUnavailable_note("Closed");
                }else{
                    currentBathroom.setUnavailable(false);
                    currentBathroom.setUnavailable_note("Available");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db = AppDatabase.getInstance(getContext());
                        db.bathroomDAO().editBathroom(currentBathroom);

                    }
                }).start();

            }
        });

        navigateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+currentBathroom.getLat() + "," + currentBathroom.getLon()));
                intent.setPackage("com.google.android.apps.maps");

                    startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_edit_bathroom, container, false);
    }

    public void addBathroomInfo(Bathroom bathroom){
        nameTV.setText(bathroom.getBathroom_name());
        longTV.setText("" + bathroom.getLon());
        latTV.setText("" + bathroom.getLat());
        if(bathroom.isKey()){
            keyTv.setText("Yes");
        }else{
            keyTv.setText("No");
        }

        if(bathroom.isHandicap()){
            handicapTV.setText("Yes");
        }else {
            handicapTV.setText("No");
        }
        bathroomStatusTV.setText(bathroom.getUnavailable_note());

        currentBathroom = bathroom;

    }
}