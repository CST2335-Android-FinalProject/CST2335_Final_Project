package algonquin.cst2335.finalproject.Activities;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * The UserScoreDatabase class represents the Room database for managing user scores.
 */
@Database(entities = {UserScore.class}, version = 1)
public abstract class UserScoreDatabase extends RoomDatabase {

    /**
     * Get the UserScoreDao instance to access user score data.
     *
     * @return The UserScoreDao instance.
     */
    public abstract UserScoreDao getUserScoreDao();

    /**
     * Get the singleton instance of the UserScoreDatabase.
     *
     * @param context The application context.
     * @return The singleton UserScoreDatabase instance.
     */
    private static volatile UserScoreDatabase userScoreDatabase;

    public static UserScoreDatabase getDatabase(final Context context) {
        if (userScoreDatabase == null) {
            synchronized (UserScoreDatabase.class) {
                if (userScoreDatabase == null) {
                    userScoreDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    UserScoreDatabase.class, "user_score_database")
                            .build();
                }
            }
        }
        return userScoreDatabase;
    }

}
