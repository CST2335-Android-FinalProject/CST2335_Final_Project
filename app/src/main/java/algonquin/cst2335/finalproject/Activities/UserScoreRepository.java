package algonquin.cst2335.finalproject.Activities;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserScoreRepository {
    UserScoreDatabase userScoreDatabase;
    UserScoreDao userDao;

    private LiveData<List<UserScore>> userScores;

    /**
     * Initializes the UserScoreRepository with the provided application context.
     *
     * @param application The application context.
     */
    public UserScoreRepository(Application application) {
        userScoreDatabase = UserScoreDatabase.getDatabase(application);
        userDao = userScoreDatabase.getUserScoreDao();
        userScores = userDao.getLeaderboardUsers();
    }

    /**
     * Inserts a new user score into the database.
     *
     * @param userScore The user score to be inserted.
     */
    public void insert(UserScore userScore) {
        userDao.save(userScore);
    }

    /**
     * Retrieves the LiveData list of user scores.
     *
     * @return The LiveData list of user scores.
     */
    public LiveData<List<UserScore>> getUserScores() {
        return userScores;
    }

}
