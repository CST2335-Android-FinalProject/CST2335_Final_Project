package algonquin.cst2335.finalproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import algonquin.cst2335.finalproject.R;

/**
 * The SplashScreenActivity displays a splash screen for a brief period before transitioning to the TriviaActivity.
 */
public class SplashScreenActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Hide the ActionBar
            actionBar.hide();
        }

        // Delayed transition to the TriviaActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreenActivity.this, TriviaActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}
