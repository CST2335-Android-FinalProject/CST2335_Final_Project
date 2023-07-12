package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FlightResult.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase{
    public abstract FlightResultDAO frDAO();
}
