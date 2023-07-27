package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityFlightTrackerBinding;
import algonquin.cst2335.finalproject.databinding.FlightResultBinding;

public class FlightTracker extends AppCompatActivity {

    ActivityFlightTrackerBinding binding;
    FlightTrackerViewModel flightModel;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    ArrayList<FlightResult> flightResults;

    FlightDatabase myDB ;
    FlightResultDAO myDAO;

    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sets history search
        SharedPreferences prefs = getSharedPreferences("mySearch", MODE_PRIVATE);
        binding.date.setText(prefs.getString("Date", ""));
        binding.code.setText(prefs.getString("Airport",""));

        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this);

        // Access the database:
        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "MyFlightDB").build();
        myDAO = myDB.frDAO(); //the only function in MessageDatabase;

        // FlightTracker View Model
        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        flightResults = flightModel.getFlights().getValue();
        if (flightResults == null){
            flightModel.getFlights().postValue( flightResults = new ArrayList<>() );
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                List<FlightResult> fromDatabase = myDAO.getAllFlightResults();
                //Once you get the data from database
                flightResults.addAll(fromDatabase);
                //You can then load the RecyclerView (must be done on the main UI thread)
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter ));
            });
        }

        //register as a listener to the MutableLiveData object
        flightModel.selectedFlight.observe( this, selectedFlight -> {

            if(selectedFlight != null) {
                //This is a Singleton object
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                //What to show:
                FlightDetailsFragment flightFragment = new FlightDetailsFragment(selectedFlight);
                //Where to load:
                // This line actually loads the fragment into the specified FrameLayout
                tx.replace(R.id.fragmentLocation, flightFragment);
                // Back to previous step
                tx.addToBackStack("anything here");
                tx.commit();
                // Another written codes using Builder Pattern:
                // getSupportFragmentManager().beginTransaction().add(R.id.fragmentLocation, chatFragment).commit();
            }
        });

        binding.recycleView.setAdapter( myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                FlightResultBinding frBinding = FlightResultBinding.inflate(getLayoutInflater());
                return new MyRowHolder( frBinding.getRoot() );
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                FlightResult fr = flightResults.get(position);

                holder.airline.setText(fr.getAirline());
                holder.flightNumber.setText(fr.getFlightNumber());
                holder.arrivalAirport.setText(fr.getArrivalAirport());
                holder.status.setText(fr.getStatus());
                holder.departureTime.setText(fr.getDepartureTime());
                holder.arrivalTime.setText(fr.getArrivalTime());
            }

            @Override
            public int getItemCount() {
                return flightResults.size();
            }
        });
        // Sets LayoutManager
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        //Search Button
        binding.searchButton.setOnClickListener( click -> {

            String airportCode = binding.code.getText().toString();

//            FlightResult fr = new FlightResult();
//
//            fr.setFlightDate("2023-06-24");
//            fr.setStatus("scheduled");
//            fr.setAirline("Polar Air Cargo");
//            fr.setFlightNumber("PO96");
//
//            fr.setDepartureAirport("HKG");
//            fr.setDepartureAirportName("Hong Kong International");
//            fr.setDepartureTimezone("Asia/Hong_Kong");
//            fr.setDepartureTerminal("");
//            fr.setDepartureGate("");
//            fr.setDepartureTime("06:00");
//            fr.setDepartureEstimated("06:00");
//            fr.setDepartureDelay("10");
//
//            fr.setArrivalAirport("LAX");
//            fr.setArrivalAirportName("Los Angeles International");
//            fr.setArrivalTimezone("America/Los_Angeles");
//            fr.setArrivalTerminal("");
//            fr.setArrivalGate("");
//            fr.setArrivalTime("02:58");
//            fr.setArrivalEstimated("02:58");
//            fr.setArrivalDelay("");
//            // insert into ArrayList
//            flightResults.add(fr);
//            // insert into database
//            Executors.newSingleThreadExecutor().execute(() -> {
//                myDAO.insertFlight(fr);
//            });

            // notify the adapter:
            myAdapter.notifyDataSetChanged(); //redraw the whole screen
//            myAdapter.notifyItemInserted(flightResults.size()-1); //tells the Adapter which row has to be redrawn
            Toast.makeText(this, "Your search is successful!", Toast.LENGTH_LONG).show();

            // Saves the search history into Shared Preferences
            SharedPreferences.Editor editor = getSharedPreferences("mySearch", Context.MODE_PRIVATE).edit();
            editor.putString("Date", binding.date.getText().toString());
            editor.putString("Airport", binding.code.getText().toString());
            editor.apply();
        });

        // Go to Favorite Page:
        binding.loveButton.setOnClickListener( click -> {
            Intent flightFavorite = new Intent( this, FlightFavorite.class);
            startActivity( flightFavorite );
        });

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
                FlightResult selected = flightResults.get(position);
                flightModel.selectedFlight.postValue(selected);

//                AlertDialog.Builder builder = new AlertDialog.Builder( FlightTracker.this );
//                builder.setTitle( "Question:" )
//                        .setMessage( "Do you want to delete this flight: " + flightNumber.getText() )
//                        .setPositiveButton( "No" , (dialog, cl) -> {})
//                        .setNegativeButton( "Yes" , (dialog, cl) -> {
//                            FlightResult removeFlight = flightResults.get(position);
//                            // Deletes the chatMessage in the Database and runs in another thread
//                            Executors.newSingleThreadExecutor().execute(() -> {
//                                myDAO.deleteFlight(removeFlight);
//                            });
//                            flightResults.remove(position);
//                            myAdapter.notifyItemRemoved(position);
//                                                                                    //position starts from 0
//                            Snackbar.make( flightNumber, "You deleted message #" + (position + 1), Snackbar.LENGTH_LONG)
//                                    .setAction("Undo", clk -> {
//                                        // Re-inserts the chatMessage into the Database
//                                        Executors.newSingleThreadExecutor().execute(() -> {
//                                            myDAO.insertFlight(removeFlight);
//                                        });
//                                        flightResults.add(position, removeFlight);
//                                        myAdapter.notifyItemInserted(position);
//                                    })
//                                    .show();
//                        })
//                        .create().show(); //actually make the window appear
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