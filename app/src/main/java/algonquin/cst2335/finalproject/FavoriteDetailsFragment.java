package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.FavoriteDetailBinding;

/**
 * A fragment to display detailed information about a selected favorite flight.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
public class FavoriteDetailsFragment extends Fragment {

    /** The selected FlightResult object containing flight details. */
    FlightResult selected;

    /**
     * Constructs a new FavoriteDetailsFragment with the selected FlightResult.
     *
     * @param fr The FlightResult object representing the selected flight.
     */
    public FavoriteDetailsFragment(FlightResult fr){
        selected = fr;
    }

    /**
     * Inflates the layout for the fragment and populates it with flight details.
     *
     * @param inflater           The LayoutInflater to inflate the layout.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state.
     * @return The root View of the inflated layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout using view binding
        FavoriteDetailBinding binding = FavoriteDetailBinding.inflate(inflater);

        // Populate the UI with flight details
        binding.favFlightDate.setText(selected.getFlightDate());
        binding.favDepTime.setText(selected.getDepartureTime());
        binding.favDepAirport.setText(selected.getDepartureAirportName());
        binding.favDepAirportNum.setText(selected.getDepartureAirport());
        binding.favDepTerminal.setText(selected.getDepartureTerminal());
        binding.favDepGate.setText(selected.getDepartureGate());
        binding.favAirline.setText(selected.getAirline());
        binding.favAirlineNum.setText(selected.getFlightNumber());
        binding.favArrTime.setText(selected.getArrivalTime());
        binding.favArrAirport.setText(selected.getArrivalAirportName());
        binding.favArrAirportNum.setText(selected.getArrivalAirport());
        binding.favArrTerminal.setText(selected.getArrivalTerminal());
        binding.favArrGate.setText(selected.getArrivalGate());

        // Set up click listeners for UI elements
        binding.closeButton.setOnClickListener( click -> {
        });

        binding.removeButton.setOnClickListener( click -> {
        });

        return binding.getRoot();
    }

}
