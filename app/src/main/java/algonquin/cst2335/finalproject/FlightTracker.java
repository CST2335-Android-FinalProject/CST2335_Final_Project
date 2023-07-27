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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

        // adds your toolbar
        setSupportActionBar(binding.toolbar);

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
            String url = null;
            try {
                url = "http://api.aviationstack.com/v1/flights?access_key=3eca0d55b29990d3c80897ed53c69136&dep_iata="
                        + URLEncoder.encode(airportCode, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

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
                                int depDelay = departure.getInt("delay");
                                String depTime = departure.getString("scheduled");
                                String depEstimated = departure.getString("estimated");

                                JSONObject arrival = flight1.getJSONObject("arrival");
                                String arrAirport = arrival.getString("airport");
                                String arrTimezone = arrival.getString("timezone");
                                String arrAirportCode = arrival.getString("iata");
                                String arrTerminal = arrival.getString("terminal");
                                String arrGate = arrival.getString("gate");
                                int arrDelay = arrival.getInt("delay");
                                String arrTime = arrival.getString("scheduled");
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
                                fr.setDepartureTime(depTime);
                                fr.setDepartureEstimated(depEstimated);
                                fr.setDepartureDelay(depDelay);

                                fr.setArrivalAirport(arrAirportCode);
                                fr.setArrivalAirportName(arrAirport);
                                fr.setArrivalTimezone(arrTimezone);
                                fr.setArrivalTerminal(arrTerminal);
                                fr.setArrivalGate(arrGate);
                                fr.setArrivalTime(arrTime);
                                fr.setArrivalEstimated(arrEstimated);
                                fr.setArrivalDelay(arrDelay);
                                // insert into ArrayList
                                flightResults.add(fr);
                                // notify the adapter:
                                myAdapter.notifyDataSetChanged(); //redraw the whole screen
                                Toast.makeText(this, "Your search is successful!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },//gets called if it is successful
                    (vError) -> {
                        int i = 0;
                    });//gets called if there is an error
            queue.add(request);//run the web query

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
//            fr.setDepartureDelay(10);
//
//            fr.setArrivalAirport("LAX");
//            fr.setArrivalAirportName("Los Angeles International");
//            fr.setArrivalTimezone("America/Los_Angeles");
//            fr.setArrivalTerminal("");
//            fr.setArrivalGate("");
//            fr.setArrivalTime("02:58");
//            fr.setArrivalEstimated("02:58");
//            fr.setArrivalDelay(0);
//            // insert into ArrayList
//            flightResults.add(fr);
//            // insert into database
//            Executors.newSingleThreadExecutor().execute(() -> {
//                myDAO.insertFlight(fr);
//            });
//            // notify the adapter:
//            myAdapter.notifyDataSetChanged(); //redraw the whole screen
////            myAdapter.notifyItemInserted(flightResults.size()-1); //tells the Adapter which row has to be redrawn
//            Toast.makeText(this, "Your search is successful!", Toast.LENGTH_LONG).show();

            // Saves the search history into Shared Preferences
            SharedPreferences.Editor editor = getSharedPreferences("mySearch", Context.MODE_PRIVATE).edit();
            editor.putString("Date", binding.date.getText().toString());
            editor.putString("Airport", binding.code.getText().toString());
            editor.apply();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate( R.menu.my_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FlightTracker.this);
            builder.setTitle("Instruction:")
                    .setMessage("Step 1: Enter your departure date and airport code, you can search the flights.\n\n"
                            + "Step 2: Click one of the search results, you can check the detail of flight.\n\n"
                            + "Step 3: Click the \"Add to Favorite Lists\" button, you can keep the flight information in your lists.")
                    .setPositiveButton("OK", (dialog, cl) -> {
                    })
                    .create().show(); //actually make the window appear
        } else if (item.getItemId() == R.id.item_myFavorite) {
            // Go to Favorite Page:
            Intent flightFavorite = new Intent( this, FlightFavorites.class);
            startActivity( flightFavorite );
        }

        return true;
    }
}

