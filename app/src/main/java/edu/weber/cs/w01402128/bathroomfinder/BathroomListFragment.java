package edu.weber.cs.w01402128.bathroomfinder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BathroomListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BathroomListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View root;
    private RecyclerView recyclerView;
    private BathroomRecyclerAdapter adapter;
    private BathroomRecyclerAdapter.OnClickListener mCallBack;
    private List<Bathroom> bathroomList = new ArrayList<Bathroom>();


    public BathroomListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BathroomListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BathroomListFragment newInstance(String param1, String param2) {
        BathroomListFragment fragment = new BathroomListFragment();
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
        return root = inflater.inflate(R.layout.fragment_bathroom_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
         Context context = getContext();


         recyclerView = root.findViewById(R.id.bathroomRV);
         adapter = new BathroomRecyclerAdapter(new ArrayList<Bathroom>(), mCallBack);

         recyclerView.setLayoutManager(new LinearLayoutManager(context));
         recyclerView.setAdapter(adapter);
         recyclerView.setHasFixedSize(false);

        new ViewModelProvider(this)
                .get(AllBathroomViewModel.class)
                .getAllBathrooms(context)
                .observe(this, new Observer<List<Bathroom>>() {
                    @Override
                    public void onChanged(@Nullable List<Bathroom> bathrooms) {
                        if( bathrooms != null ){
                            adapter.setBathroomList(bathrooms);
                            //bathroomList.addAll(bathrooms);

//                            for(Bathroom bathroom: bathroomList){
//                                Log.d("test", "bathroom: " + bathroom.toString());
//                            }
                        }
                    }
                });


    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try{
            mCallBack = (BathroomRecyclerAdapter.OnClickListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " Must implement onButtonListener");
        }
    }

    public List<Bathroom> getBathroomList(){

        return bathroomList;
    }
}