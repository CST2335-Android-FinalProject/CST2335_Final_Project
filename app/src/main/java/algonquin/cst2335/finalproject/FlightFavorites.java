package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityFlightFavoritesBinding;


public class FlightFavorites extends AppCompatActivity {

    ActivityFlightFavoritesBinding binding;
    ArrayList<FlightResult> flightResults;
    RecyclerView.Adapter<FlightFavorites.MyRowHolder> myAdapter;
    FlightDatabase myDB ;
    FlightResultDAO myDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlightFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Access the database:
        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "MyFlightDB").build();
        myDAO = myDB.frDAO(); //the only function in MessageDatabase;

        // Sets LayoutManager
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

    }


    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView airline;
        TextView flightNumber;
        TextView arrivalAirport;
        TextView status;
        TextView departureTime;
        TextView arrivalTime;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener( click ->{
                int position = getAbsoluteAdapterPosition();
//                FlightResult selected = flightResults.get(position);
//                flightModel.selectedFlight.postValue(selected);

                AlertDialog.Builder builder = new AlertDialog.Builder( FlightFavorites.this );
                builder.setTitle( "Question:" )
                        .setMessage( "Do you want to delete this flight: " + flightNumber.getText() )
                        .setPositiveButton( "No" , (dialog, cl) -> {})
                        .setNegativeButton( "Yes" , (dialog, cl) -> {
                            FlightResult removeFlight = flightResults.get(position);
                            // Deletes the chatMessage in the Database and runs in another thread
                            Executors.newSingleThreadExecutor().execute(() -> {
                                myDAO.deleteFlight(removeFlight);
                            });
                            flightResults.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            //position starts from 0
                            Snackbar.make( flightNumber, "You deleted message #" + (position + 1), Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        // Re-inserts the chatMessage into the Database
                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            myDAO.insertFlight(removeFlight);
                                        });
                                        flightResults.add(position, removeFlight);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show(); //actually make the window appear
            });

            airline = itemView.findViewById(R.id.airline);
            flightNumber = itemView.findViewById(R.id.flightNumber);
            arrivalAirport = itemView.findViewById(R.id.arrivalAirport);
            status = itemView.findViewById(R.id.status);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
        }
    }

}