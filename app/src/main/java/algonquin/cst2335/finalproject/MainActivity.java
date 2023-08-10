package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalproject.Activities.TriviaActivity;
import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.flight.setOnClickListener( clckk-> {
            Intent flightTracker = new Intent( MainActivity.this, FlightTracker.class);
            startActivity( flightTracker );
        });
//
//        binding.exchange.setOnClickListener( clckk-> {
//            Intent currencyConverter = new Intent( MainActivity.this, CurrencyConverter.class);
//            startActivity( currencyConverter );
//        });

        binding.question.setOnClickListener( clckk-> {
            Intent triviaQuestion = new Intent( MainActivity.this, TriviaActivity.class);
            startActivity( triviaQuestion );
        });

        binding.bear.setOnClickListener( clckk-> {
            Intent imageGenerator = new Intent( MainActivity.this, ImageGenerator.class);
            startActivity( imageGenerator );
       });
    }
}