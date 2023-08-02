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

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityFlightFavoritesBinding;
import algonquin.cst2335.finalproject.databinding.FlightResultBinding;

/**
 * An activity that displays a list of favorite flight results.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
public class FlightFavorites extends AppCompatActivity {

    /** View binding instance for the FlightFavorites activity. */
    ActivityFlightFavoritesBinding binding;
    /** ViewModel for managing favorite flight data. */
    FlightTrackerViewModel favoriteModel;
    /** List of favorite flight results displayed in the RecyclerView. */
    ArrayList<FlightResult> favoriteResults;
    /** Adapter for populating the RecyclerView with favorite flight items. */
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    /** Database instance for storing flight data. */
    FlightDatabase myDB ;
    /** Data Access Object for interacting with the flight result table in the database. */
    FlightResultDAO myDAO;

    /**
     * Called when the activity is first created. Initializes the user interface, sets up
     * the RecyclerView, handles user interactions, and manages favorite flight functionality.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Otherwise,
     *                           it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityFlightFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Access the database:
        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "MyFlightDB").build();
        myDAO = myDB.frDAO(); //the only function in MessageDatabase;

        // Initialize FlightFavorites View Model
        favoriteModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        favoriteResults = favoriteModel.getFlights().getValue();
        if (favoriteResults == null){
            favoriteModel.getFavorites().postValue( favoriteResults = new ArrayList<>() );
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                List<FlightResult> fromDatabase = myDAO.getAllFlightResults();
                //Once you get the data from database
                favoriteResults.addAll(fromDatabase);
                //You can then load the RecyclerView (must be done on the main UI thread)
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter ));
            });
        }

        //register as a listener to the MutableLiveData object
        favoriteModel.selectedFavorite.observe( this, selectedFavorite -> {

            if(selectedFavorite != null) {
                //This is a Singleton object
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                //What to show:
                FavoriteDetailsFragment favoriteFragment = new FavoriteDetailsFragment(selectedFavorite);
                //Where to load:
                // This line actually loads the fragment into the specified FrameLayout
                tx.replace(R.id.fraFavoriteLocation, favoriteFragment);
                // Back to previous step
                tx.addToBackStack("anything here");
                tx.commit();
                // Another written codes using Builder Pattern:
                // getSupportFragmentManager().beginTransaction().add(R.id.fragmentLocation, chatFragment).commit();
            }
        });

        // Set up RecyclerView and adapter
        binding.recycleView.setAdapter( myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                FlightResultBinding frBinding = FlightResultBinding.inflate(getLayoutInflater());
                return new MyRowHolder( frBinding.getRoot() );
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                FlightResult fr = favoriteResults.get(position);

                holder.airline.setText(fr.getAirline());
                holder.flightNumber.setText(fr.getFlightNumber());
                holder.arrivalAirport.setText(fr.getArrivalAirport());
                holder.status.setText(fr.getStatus());
                holder.departureTime.setText(fr.getDepartureTime());
                holder.arrivalTime.setText(fr.getArrivalTime());
            }

            @Override
            public int getItemCount() { return favoriteResults.size(); }
        });
        // Set LayoutManager for RecyclerView
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        // Handle back button click
        binding.backButton.setOnClickListener( click -> {
            onBackPressed();
        });
    }

    /**
     * Custom ViewHolder for RecyclerView items in the favorite flight list.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {

        /** TextView displaying the airline name. */
        TextView airline;
        /** TextView displaying the flight number. */
        TextView flightNumber;
        /** TextView displaying the arrival airport code. */
        TextView arrivalAirport;
        /** TextView displaying the flight status. */
        TextView status;
        /** TextView displaying the departure time. */
        TextView departureTime;
        /** TextView displaying the arrival time. */
        TextView arrivalTime;

        /**
         * Constructs a new MyRowHolder instance.
         *
         * @param itemView The view representing a single item in the RecyclerView.
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            // Set up click listener for selecting a favorite flight
            itemView.setOnClickListener( click ->{
                int position = getAbsoluteAdapterPosition();
//                FlightResult selected = favoriteResults.get(position);
//                favoriteModel.selectedFavorite.postValue(selected);

                // Show a confirmation dialog for deleting the selected flight
                AlertDialog.Builder builder = new AlertDialog.Builder( FlightFavorites.this );
                builder.setTitle( "Question:" )
                        .setMessage( "Do you want to delete this flight: " + flightNumber.getText() )
                        .setPositiveButton( "No" , (dialog, cl) -> {})
                        .setNegativeButton( "Yes" , (dialog, cl) -> {
                            FlightResult removeFlight = favoriteResults.get(position);
                            // Deletes the chatMessage in the Database and runs in another thread
                            Executors.newSingleThreadExecutor().execute(() -> {
                                myDAO.deleteFlight(removeFlight);
                            });
                            favoriteResults.remove(position);
                            myAdapter.notifyItemRemoved(position);
                            //position starts from 0
                            Snackbar.make( flightNumber, "You deleted message #" + (position + 1), Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        // Re-inserts the chatMessage into the Database
                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            myDAO.insertFlight(removeFlight);
                                        });
                                        favoriteResults.add(position, removeFlight);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        })
                        .create().show(); //actually make the window appear
            });

            // Initialize TextViews for favorite flight information
            airline = itemView.findViewById(R.id.airline);
            flightNumber = itemView.findViewById(R.id.flightNumber);
            arrivalAirport = itemView.findViewById(R.id.arrivalAirport);
            status = itemView.findViewById(R.id.status);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
        }
    }
}