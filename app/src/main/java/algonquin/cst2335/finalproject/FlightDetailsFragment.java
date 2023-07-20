package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.FlightDetailBinding;

public class FlightDetailsFragment extends Fragment {

    FlightResult selected;

    public FlightDetailsFragment(FlightResult fr){
        selected = fr;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        FlightDetailBinding binding = FlightDetailBinding.inflate(inflater);

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

        return binding.getRoot();
    }
}
