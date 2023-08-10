package algonquin.cst2335.finalproject.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaBinding;

/**
 * The QuestionsActivity displays a list of trivia questions for the user to answer.
 * It calculates the user's score and allows them to submit their name along with the score.
 */
public class QuestionsActivity extends AppCompatActivity {

    private ActivityTriviaBinding binding;
    private List<QuestionDetails> questionList;
    private RecyclerView recyclerView;

    UserScoreDao userScoreDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_questions);
        String category = getIntent().getStringExtra("category");

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);


        EditText numOfQuestions = binding.editTextNumber;
        numOfQuestions.setText(sharedPreferences.getString("numOfQuestions", "2"));



        int numberOfQuestions = 0;
        String numOfQuestionsText = numOfQuestions.getText().toString();

        if (!numOfQuestionsText.isEmpty()) {
            try {
                 numberOfQuestions = Integer.parseInt(numOfQuestionsText);
                if (numberOfQuestions >= 1 && numberOfQuestions <= 50) {
                    // Rest of your code
                    fetchQuestions(category, numberOfQuestions);
                } else {
                    // Display a Snackbar to inform the user about the invalid range
                    Snackbar.make(findViewById(android.R.id.content), "Please enter a number between 1 and 50.", Snackbar.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                // Display a Snackbar to inform the user about the invalid input
                Snackbar.make(findViewById(android.R.id.content), "Please enter a valid integer.", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            // Display a Snackbar to inform the user about the empty input
            Snackbar.make(findViewById(android.R.id.content), "Please enter a valid integer.", Snackbar.LENGTH_SHORT).show();
        }



        Button submitButton = findViewById(R.id.submit_result);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int score = calculateScore(questionList);
                createDialog(score);
            }
        });

    }

    /**
     * Fetches trivia questions from the API based on the selected category and number of questions.
     *
     * @param category          The selected trivia category.
     * @param numberOfQuestions The number of trivia questions to fetch.
     */
    private void fetchQuestions(String category, int numberOfQuestions) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://the-trivia-api.com/api/questions?categories=" + category + "&limit=" + numberOfQuestions;
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                questionList =
                        gson.fromJson(response, new TypeToken<List<QuestionDetails>>() {
                        }.getType());

                recyclerView = findViewById(R.id.question_data);
                QuestionAdapter questionAdapter = new QuestionAdapter(questionList);
                recyclerView.setAdapter(questionAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
            }
        }, error -> System.out.println("Unable to find questions"));
        queue.add(stringRequest);
    }

    /**
     * Calculates the user's score based on the correctness of their answers.
     *
     * @param questions The list of trivia questions.
     * @return The calculated score.
     */
    private int calculateScore(List<QuestionDetails> questions) {
        AtomicInteger score = new AtomicInteger();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            questions.forEach(question -> {
                if (question.isCorrectAnswer){
                    score.getAndIncrement();
                }
            });
        }
        return score.get();
    }

    /**
     * Creates a dialog for the user to enter their name and submit their score.
     *
     * @param score The user's score.
     */
    private void createDialog(int score) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QuestionsActivity.this);

        EditText editText = new EditText(QuestionsActivity.this);
        editText.setHint("Enter Name");
        dialogBuilder.setTitle("Score");
        dialogBuilder.setMessage("Your score: " + score);
        dialogBuilder.setView(editText);
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UserScoreDatabase userScoreDatabase = UserScoreDatabase.getDatabase(QuestionsActivity.this);
                userScoreDao = userScoreDatabase.getUserScoreDao();

                Executor executor = Executors.newSingleThreadExecutor();

                executor.execute(() -> {
                    UserScore userScore = new UserScore();
                    userScore.setName(editText.getText().toString());
                    userScore.setScore(score);
                    userScore.setTotalQuestions(questionList.size());
                    userScoreDao.save(userScore);
                });

                Toast.makeText(QuestionsActivity.this, "Name : " + editText.getText().toString() + " | Your score: " + score, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestionsActivity.this, TriviaActivity.class);
                startActivity(intent);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(QuestionsActivity.this, TriviaActivity.class);
                startActivity(intent);
            }
        });
        dialogBuilder.show();
    }

}

