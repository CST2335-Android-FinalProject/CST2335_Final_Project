package algonquin.cst2335.finalproject.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaBinding;

/**
 * The LeaderboardActivity displays a list of user scores in descending order.
 */
public class LeaderboardActivity extends AppCompatActivity {
    private ActivityTriviaBinding binding;
    private LeaderboardAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(R.layout.leaderboard_layout);

        // Get the database and DAO instance
        UserScoreDatabase userScoreDatabase = UserScoreDatabase.getDatabase(LeaderboardActivity.this);
        UserScoreDao userScoreDao = userScoreDatabase.getUserScoreDao();


        // Create an executor for database operations
        Executor executor = Executors.newSingleThreadExecutor();


        // Get the RecyclerView and set its layout manager
        RecyclerView recyclerView = findViewById(R.id.leaderboard_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch user scores from the database, sort them, and set up the adapter
        executor.execute(() -> {
            List<UserScore> userScores = userScoreDao.getAllUsers();
            userScores.sort(Comparator.comparingDouble(UserScore::calculateScore).reversed());

            userAdapter = new LeaderboardAdapter(userScores, userScoreDao, recyclerView.getAdapter());
            recyclerView.setAdapter(userAdapter);
        });


    }


}
