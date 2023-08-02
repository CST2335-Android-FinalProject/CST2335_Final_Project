package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.FlightDetailBinding;

/**
 * A fragment to display detailed information about a selected flight result.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
public class FlightDetailsFragment extends Fragment {

    /** The selected FlightResult object containing flight details. */
    FlightResult selected;
    /** The parent FlightTracker activity for navigation. */
    FlightTracker tracker;

    /**
     * Constructs a new FlightDetailsFragment with the selected FlightResult and parent activity.
     *
     * @param fr The FlightResult object representing the selected flight.
     * @param ft The parent FlightTracker activity for navigation.
     */
    public FlightDetailsFragment(FlightResult fr, FlightTracker ft){
        selected = fr;
        tracker = ft;
    }

    /**
     * Inflates the layout for the fragment and populates it with flight details.
     * Handles the behavior of buttons for closing the fragment and adding to favorites.
     *
     * @param inflater           The LayoutInflater to inflate the layout.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state.
     * @return The root View of the inflated layout.
     */
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout using view binding
        FlightDetailBinding binding = FlightDetailBinding.inflate(inflater);

        // Populate the UI with flight details
        binding.fraFlightDate.setText(selected.getFlightDate());
        binding.fraDepTime.setText(selected.getDepartureTime());
        binding.fraDepAirport.setText(selected.getDepartureAirportName());
        binding.fraDepAirportNum.setText(selected.getDepartureAirport());
        binding.fraDepTerminal.setText(selected.getDepartureTerminal());
        binding.fraDepGate.setText(selected.getDepartureGate());
        binding.fraAirline.setText(selected.getAirline());
        binding.fraAirlineNum.setText(selected.getFlightNumber());
        binding.fraArrTime.setText(selected.getArrivalTime());
        binding.fraArrAirport.setText(selected.getArrivalAirportName());
        binding.fraArrAirportNum.setText(selected.getArrivalAirport());
        binding.fraArrTerminal.setText(selected.getArrivalTerminal());
        binding.fraArrGate.setText(selected.getArrivalGate());

        // Set up click listeners for UI elements
        binding.closeButton.setOnClickListener( click -> {
            // Navigate back to the previous main page
            tracker.onBackPressed();
        });

        binding.addButton.setOnClickListener( click -> {
            // Insert selected flight into the database
            Executors.newSingleThreadExecutor().execute(() -> {
                tracker.myDAO.insertFlight(selected);
                tracker.runOnUiThread( () ->
                    Toast.makeText(tracker, "Added successfully!", Toast.LENGTH_LONG).show()
                );
            });
        });

        return binding.getRoot();
    }

}
