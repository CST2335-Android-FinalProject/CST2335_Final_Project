package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.FlightDetailBinding;

public class FlightDetailsFragment extends Fragment {

    FlightResult selected;
    FlightTracker tracker;

    public FlightDetailsFragment(FlightResult fr, FlightTracker ft){
        selected = fr;
        tracker = ft;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        FlightDetailBinding binding = FlightDetailBinding.inflate(inflater);

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

        binding.closeButton.setOnClickListener( click -> {
            // Back to Previous Main Page
            tracker.onBackPressed();
        });

        binding.addButton.setOnClickListener( click -> {
             // insert into database
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
