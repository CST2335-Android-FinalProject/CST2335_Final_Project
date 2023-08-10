package algonquin.cst2335.finalproject.Activities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The UserScore class represents the user's score and related information.
 */

@Entity(tableName = "user_score")
public class UserScore {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "score")
    private int score;

    @ColumnInfo(name = "total_questions")
    private int totalQuestions;
    /**
     * Get the ID of the user score.
     *
     * @return The ID of the user score.
     */
    public int getId() {
        return id;
    }
    /**
     * Set the ID of the user score.
     *
     * @param id The ID of the user score.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Get the name associated with the user score.
     *
     * @return The name associated with the user score.
     */
    public String getName() {
        return name;
    }
    /**
     * Set the name associated with the user score.
     *
     * @param name The name to set for the user score.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Get the score value.
     *
     * @return The score value.
     */
    public int getScore() {
        return score;
    }
    /**
     * Set the score value.
     *
     * @param score The score value to set.
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * Get the total number of questions.
     *
     * @return The total number of questions.
     */
    public int getTotalQuestions() {
        return totalQuestions;
    }
    /**
     * Set the total number of questions.
     *
     * @param totalQuestions The total number of questions to set.
     */
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    /**
     * Calculate the score as a ratio of score to total questions.
     *
     * @return The calculated score.
     */
    public double calculateScore() {
        return score / totalQuestions;
    }

}

