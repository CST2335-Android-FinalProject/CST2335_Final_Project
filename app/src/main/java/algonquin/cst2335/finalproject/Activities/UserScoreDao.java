package algonquin.cst2335.finalproject.Activities;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserScoreDao {
    /**
     * Retrieve a list of all user scores.
     *
     * @return A list of all user scores.
     */
    @Query("SELECT * FROM user_score")
    List<UserScore> getAllUsers();

//    @Query("SELECT * FROM user_score order by score DESC")
//    List<UserScore> getLeaderboardUsers();

    /**
     * Retrieve a live data list of user scores ordered by name in ascending order.
     *
     * @return A LiveData list of user scores.
     */
    @Query("SELECT * from user_score ORDER By name Asc")
    LiveData<List<UserScore>> getLeaderboardUsers();
    /**
     * Save a user score to the database.
     *
     * @param userScore The user score to save.
     */
    @Insert
    void save(UserScore userScore);

    /**
     * Delete a user score from the database.
     *
     * @param userScore The user score to delete.
     */
    @Delete
    void delete(UserScore userScore);
}

