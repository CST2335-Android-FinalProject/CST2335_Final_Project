package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class FlightTrackerViewModel extends ViewModel {

    public MutableLiveData<ArrayList<FlightResult>> flightResults = new MutableLiveData< >();
    public MutableLiveData<FlightResult> selectedFlight = new MutableLiveData< >();
    public MutableLiveData<ArrayList<FlightResult>> getFlights() {
        return flightResults;
    }

}
