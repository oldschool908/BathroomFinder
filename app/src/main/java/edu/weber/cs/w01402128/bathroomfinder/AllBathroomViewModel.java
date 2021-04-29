package edu.weber.cs.w01402128.bathroomfinder;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.weber.cs.w01402128.bathroomfinder.db.AppDatabase;
import edu.weber.cs.w01402128.bathroomfinder.db.Bathroom;

public class AllBathroomViewModel extends ViewModel {
    private LiveData<List<Bathroom>> bathroomList;

    public LiveData<List<Bathroom>> getAllBathrooms(Context context){

        AppDatabase db = AppDatabase.getInstance(context);

        bathroomList = db.bathroomDAO().getAll();


        return bathroomList;
    }

}
