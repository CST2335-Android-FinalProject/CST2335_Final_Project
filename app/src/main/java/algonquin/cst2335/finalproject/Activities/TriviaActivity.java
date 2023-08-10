package algonquin.cst2335.finalproject.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import algonquin.cst2335.finalproject.databinding.ActivityTriviaBinding;

/**
 * The TriviaActivity provides options to start a trivia game, view the leaderboard, and get help.
 */
public class TriviaActivity extends AppCompatActivity {
    private ActivityTriviaBinding binding;
    private EditText numberOfQuestions;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        numberOfQuestions = binding.editTextNumber;
        Button foodDrink = binding.button1;
        Button filmTv = binding.button2;
        FloatingActionButton leaderboard = binding.leaderboardButton;
        FloatingActionButton help = binding.helpButton;

        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        foodDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String category = "Music";
                @SuppressLint("CommitPrefEdits")
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String numOfQuestions = numberOfQuestions.getText().toString();
                editor.putString("numOfQuestions", numOfQuestions);
                editor.apply();
                Intent intent = new Intent(TriviaActivity.this, QuestionsActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        filmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String numOfQuestions = numberOfQuestions.getText().toString();
                String category = "Geography";
                Intent intent = new Intent(TriviaActivity.this, QuestionsActivity.class);
                intent.putExtra("numOfQuestions", numOfQuestions);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TriviaActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TriviaActivity.this);
                builder.setTitle("Help");
                builder.setMessage("Enter Number of Question in text Field and select the Category");
                builder.create().show();
            }
        });
    }
    /**
     * Hide the software keyboard.
     *
     * @param view The view from which the keyboard should be hidden.
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
