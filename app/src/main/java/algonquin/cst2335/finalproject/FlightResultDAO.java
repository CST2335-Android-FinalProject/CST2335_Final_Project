package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlightResultDAO {

    @Insert // after inserting, the database returns id as int. The return type could be void.
    long insertFlight(FlightResult fr);

    @Query("Select * from FlightResult")
    List<FlightResult> getAllFlightResults();

//    @Update
//    int updateFlight(FlightResult fr);

    @Delete // number of rows deleted, should be 1 if id matches. The return type could be void.
    int deleteFlight(FlightResult fr);

}
