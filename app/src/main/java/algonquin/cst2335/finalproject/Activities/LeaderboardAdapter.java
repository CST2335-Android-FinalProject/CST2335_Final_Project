package algonquin.cst2335.finalproject.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;

/**
 * The LeaderboardAdapter class is responsible for displaying user scores in a RecyclerView.
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserScore> userScores;
    private UserScoreDao userDao;
    private RecyclerView.Adapter<ViewHolder> userAdapter;


    /**
     * Constructor to initialize the LeaderboardAdapter with user scores and necessary components.
     *
     * @param userScores  The list of user scores to be displayed.
     * @param userDao     The DAO interface for interacting with the user score database.
     * @param userAdapter The RecyclerView adapter for user scores.
     */
    public LeaderboardAdapter(List<UserScore> userScores, UserScoreDao userDao, RecyclerView.Adapter<ViewHolder> userAdapter) {
        this.userScores = userScores;
        this.userDao = userDao;
        this.userAdapter = userAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserScore score = userScores.get(position);
        holder.usernameTextView.setText(score.getName());
        holder.scoreTextView.setText(String.format("%s/%s", score.getScore(), score.getTotalQuestions()));
    }

    @Override
    public int getItemCount() {
        return userScores.size();
    }


    /**
     * ViewHolder class for holding the views of each user score item in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView scoreTextView;
        TextView usernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            usernameTextView = itemView.findViewById(R.id.nameTextView);
            itemView.setOnClickListener(click -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showDeleteDialog(itemView.getContext(), position);
                }
            });
        }

        /**
         * Display a confirmation dialog for deleting a user score.
         *
         * @param context  The context of the activity.
         * @param position The position of the user score in the list.
         */
        private void showDeleteDialog(Context context, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Score")
                    .setMessage("Do you want to delete this score?")
                    .setPositiveButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setNegativeButton("Yes", (dialog, which) -> {
                        Log.d("LeaderboardAdapter", "showDeleteDialog: Yes clicked");
                        UserScore score = userScores.get(position);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            userDao.delete(score);
                        });
                        userScores.remove(position);
                        userAdapter.notifyItemRemoved(position);
                        Log.d("LeaderboardAdapter", "showDeleteDialog: Score removed");
                    })
                    .create()
                    .show();
        }
    }
}
