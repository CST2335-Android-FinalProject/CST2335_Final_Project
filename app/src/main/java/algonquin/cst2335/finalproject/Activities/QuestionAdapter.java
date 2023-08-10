package algonquin.cst2335.finalproject.Activities;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algonquin.cst2335.finalproject.R;

/**
 * The QuestionAdapter class is responsible for displaying trivia questions in a RecyclerView.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final List<QuestionDetails> questionList;
    /**
     * Constructor to initialize the QuestionAdapter with a list of trivia questions.
     *
     * @param questionList The list of trivia questions to be displayed.
     */
    public QuestionAdapter(List<QuestionDetails> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trivia_question_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.optionsRadioGroup.removeAllViews();

        QuestionDetails question = questionList.get(position);
        holder.questionTextView.setText(question.getQuestion());
        List<String> options = new ArrayList<>();
        options.addAll(question.getIncorrectAnswers());
        options.add(question.getCorrectAnswer());
        Collections.shuffle(options);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            options.forEach(option -> {
                RadioButton radioButton = new RadioButton(holder.optionsRadioGroup.getContext());
                radioButton.setText(option);
                holder.optionsRadioGroup.addView(radioButton);
            });
        }

        holder.optionsRadioGroup.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                    String selectedOption = radioButton.getText().toString();
                    question.setCorrectAnswer(selectedOption.equals(question.getCorrectAnswer()));
                }
        );
    }

    /**
     * ViewHolder class for holding the views of each trivia question item.
     */
    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioGroup optionsRadioGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            optionsRadioGroup = itemView.findViewById(R.id.answersRadioGroup);
        }
    }
}

