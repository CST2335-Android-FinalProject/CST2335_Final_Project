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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.finalproject.databinding.ActivityFlightTrackerBinding;
import algonquin.cst2335.finalproject.databinding.FlightResultBinding;

/**
 * Represents the Flight Tracker page, where users can track flights and view details.
 * This class extends AppCompatActivity to provide the UI functionality.
 * It allows users to search for flights, view search results, and see flight details.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
public class FlightTracker extends AppCompatActivity {

    /** View binding instance for the FlightTracker activity. */
    ActivityFlightTrackerBinding binding;
    /** ViewModel for managing flight-related data. */
    FlightTrackerViewModel flightModel;
    /** Adapter for populating the RecyclerView with flight result items. */
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    /** List of flight results displayed in the RecyclerView. */
    ArrayList<FlightResult> flightResults;
    /** Database instance for storing flight data. */
    FlightDatabase myDB ;
    /** Data Access Object for interacting with the flight result table in the database. */
    FlightResultDAO myDAO;
    /** RequestQueue for managing network requests using Volley library. */
    RequestQueue queue = null;

    /**
     * Called when the activity is first created. Initializes the user interface, sets up
     * the RecyclerView, handles user interactions, and manages flight tracking functionality.
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
        binding = ActivityFlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sets history search from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("mySearch", MODE_PRIVATE);
        binding.date.setText(prefs.getString("Date", ""));
        binding.code.setText(prefs.getString("Airport",""));

        // Set up the toolbar
        setSupportActionBar(binding.toolbar);

        // Initialize Volley RequestQueue for network requests
        queue = Volley.newRequestQueue(this);

        // Initialize the local database and DAO to Access the database:
        myDB = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "MyFlightDB").build();
        myDAO = myDB.frDAO(); //the only function in MessageDatabase;

        // Initialize FlightTracker View Model
        flightModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        flightResults = flightModel.getFlights().getValue();
        if (flightResults == null){
            flightModel.getFlights().postValue( flightResults = new ArrayList<>() );
        }

        //register as a listener to the MutableLiveData object
        flightModel.selectedFlight.observe( this, selectedFlight -> {

            if(selectedFlight != null) {
                //This is a Singleton object
                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();
                //What to show:
                FlightDetailsFragment flightFragment = new FlightDetailsFragment(selectedFlight, this);
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

        // Sets LayoutManager for RecyclerView
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        //Search Button
        binding.searchButton.setOnClickListener( click -> {

            String airportCode = binding.code.getText().toString();
            String url = null;
            try {
                url = "http://api.aviationstack.com/v1/flights?access_key=3eca0d55b29990d3c80897ed53c69136&dep_iata="
                        + URLEncoder.encode(airportCode, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            // Perform network request using Volley
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (successfulResponse) -> {
                        boolean success = true;
                        try {
                            JSONArray data = successfulResponse.getJSONArray( "data");

                            for ( int i = 0 ; i < 10; i++) {
                                JSONObject flight1 = data.getJSONObject(i);

                                String flightDate = flight1.getString("flight_date");
                                String flightStatus = flight1.getString("flight_status");

                                JSONObject departure = flight1.getJSONObject("departure");
                                String depAirport = departure.getString("airport");
                                String depTimezone = departure.getString("timezone");
                                String depAirportCode = departure.getString("iata");
                                String depTerminal = departure.getString("terminal");
                                String depGate = departure.getString("gate");
//                                int depDelay = departure.getInt("delay");
                                String depTime = departure.getString("scheduled");
                                String depTimeSub = depTime.substring(11,16);
                                String depEstimated = departure.getString("estimated");

                                JSONObject arrival = flight1.getJSONObject("arrival");
                                String arrAirport = arrival.getString("airport");
                                String arrTimezone = arrival.getString("timezone");
                                String arrAirportCode = arrival.getString("iata");
                                String arrTerminal = arrival.getString("terminal");
                                String arrGate = arrival.getString("gate");
//                                int arrDelay = arrival.getInt("delay");
                                String arrTime = arrival.getString("scheduled");
                                String arrTimeSub = arrTime.substring(11,16);
                                String arrEstimated = arrival.getString("estimated");

                                JSONObject airline = flight1.getJSONObject("airline");
                                String airlineName = airline.getString("name");

                                JSONObject flight = flight1.getJSONObject("flight");
                                String flightNumber = flight.getString("iata");


                                FlightResult fr = new FlightResult();
                                fr.setFlightDate(flightDate);
                                fr.setStatus(flightStatus);
                                fr.setAirline(airlineName);
                                fr.setFlightNumber(flightNumber);

                                fr.setDepartureAirport(depAirportCode);
                                fr.setDepartureAirportName(depAirport);
                                fr.setDepartureTimezone(depTimezone);
                                fr.setDepartureTerminal(depTerminal);
                                fr.setDepartureGate(depGate);
                                fr.setDepartureTime(depTimeSub);
                                fr.setDepartureEstimated(depEstimated);
//                                fr.setDepartureDelay(depDelay);

                                fr.setArrivalAirport(arrAirportCode);
                                fr.setArrivalAirportName(arrAirport);
                                fr.setArrivalTimezone(arrTimezone);
                                fr.setArrivalTerminal(arrTerminal);
                                fr.setArrivalGate(arrGate);
                                fr.setArrivalTime(arrTimeSub);
                                fr.setArrivalEstimated(arrEstimated);
//                                fr.setArrivalDelay(arrDelay);

                                // insert into ArrayList
                                flightResults.add(fr);

                                // notify the adapter: tells the Adapter which row has to be redrawn
                                myAdapter.notifyItemInserted(flightResults.size()-1);
                            }
//                            // notify the adapter: redraw the whole screen
//                            myAdapter.notifyDataSetChanged();
                            Toast.makeText(this, "Your search is successful!", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },//gets called if it is successful
                    (vError) -> {
                        int i = 0;
                    });//gets called if there is an error
            queue.add(request);//run the web query

            // Saves the search history into Shared Preferences
            SharedPreferences.Editor editor = getSharedPreferences("mySearch", Context.MODE_PRIVATE).edit();
            editor.putString("Date", binding.date.getText().toString());
            editor.putString("Airport", binding.code.getText().toString());
            editor.apply();
        });
    }

    /**
     * Represents a custom ViewHolder for RecyclerView items in the flight result list.
     * This ViewHolder is responsible for displaying flight information in each row of the list.
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

            // Set up click listener for selecting a flight result
            itemView.setOnClickListener( click ->{
                int position = getAbsoluteAdapterPosition();
                FlightResult selected = flightResults.get(position);
                flightModel.selectedFlight.postValue(selected);
            });

            // Initialize TextViews for flight information
            airline = itemView.findViewById(R.id.airline);
            flightNumber = itemView.findViewById(R.id.flightNumber);
            arrivalAirport = itemView.findViewById(R.id.arrivalAirport);
            status = itemView.findViewById(R.id.status);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
        }
    }

    /**
     * Initialize the options menu for the FlightTracker activity.
     *
     * @param menu The options menu in which items are placed.
     * @return True if the options menu is successfully initialized.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate( R.menu.my_menu, menu );
        return true;
    }

    /**
     * Handle options menu item selection.
     *
     * @param item The menu item that was selected.
     * @return True if the item selection was handled successfully.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_about) {
            // Show an instructional dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(FlightTracker.this);
            builder.setTitle("Instruction:")
                    .setMessage("Step 1: Enter your departure date and airport code, you can search the flights.\n\n"
                            + "Step 2: Click one of the search results, you can check the detail of flight.\n\n"
                            + "Step 3: Click the \"Add to Favorite Lists\" button, you can keep the flight information in your lists.")
                    .setPositiveButton("OK", (dialog, cl) -> {
                    })
                    .create().show(); //actually make the window appear
        } else if (item.getItemId() == R.id.item_myFavorite) {
            // Open the Favorite Page activity
            Intent flightFavorite = new Intent( this, FlightFavorites.class);
            startActivity( flightFavorite );
        }

        return true;
    }
}

