package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalproject.databinding.FavoriteDetailBinding;

public class FavoriteDetailsFragment extends Fragment {

    FlightResult selected;

    public FavoriteDetailsFragment(FlightResult fr){
        selected = fr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        FavoriteDetailBinding binding = FavoriteDetailBinding.inflate(inflater);

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

        binding.closeButton.setOnClickListener( click -> {
        });

        binding.removeButton.setOnClickListener( click -> {
        });

        return binding.getRoot();
    }

}
