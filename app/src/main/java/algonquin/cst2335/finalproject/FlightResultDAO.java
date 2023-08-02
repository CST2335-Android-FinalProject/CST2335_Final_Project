package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with the FlightResult entity in the database.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
@Dao
public interface FlightResultDAO {

    /**
     * Inserts a new flight result record into the database.
     *
     * @param fr The FlightResult object to be inserted.
     * @return The ID of the newly inserted flight result record.
     */
    @Insert // after inserting, the database returns id as int. The return type could be void.
    long insertFlight(FlightResult fr);

    /**
     * Retrieves all flight result records from the database.
     *
     * @return A list of FlightResult objects representing all flight results in the database.
     */
    @Query("Select * from FlightResult")
    List<FlightResult> getAllFlightResults();

//    @Update
//    int updateFlight(FlightResult fr);

    /**
     * Deletes a flight result record from the database.
     *
     * @param fr The FlightResult object to be deleted.
     * @return The number of rows deleted (should be 1 if the ID matches).
     */
    @Delete // number of rows deleted, should be 1 if id matches. The return type could be void.
    int deleteFlight(FlightResult fr);

}
