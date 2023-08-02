package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Represents a Room database for storing flight-related data.
 * This class defines the database configuration and provides access to the DAO.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
@Database(entities = {FlightResult.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase{

    /**
     * Returns an instance of the FlightResultDAO for accessing flight data operations.
     *
     * @return The FlightResultDAO instance.
     */
    public abstract FlightResultDAO frDAO();
}
