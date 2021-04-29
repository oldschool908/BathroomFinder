package edu.weber.cs.w01402128.bathroomfinder.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BathroomDAO {

    @Query("SELECT * FROM Bathroom")
    LiveData<List<Bathroom>> getAll();

    @Query("SELECT * FROM Bathroom WHERE bathroom_id IN(:bathroomId)")
    Bathroom getBathroomByID(int bathroomId);

    @Query("SELECT * FROM Bathroom WHERE bathroom_name IN (:bathroomName)")
    Bathroom findByName(String bathroomName);

    @Insert
    void insertAll(Bathroom... bathrooms);

    @Delete
    void deleteBathroom(Bathroom bathroom);

    @Update
    void editBathroom(Bathroom bathroom);
}
