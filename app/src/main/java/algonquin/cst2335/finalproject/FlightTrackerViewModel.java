package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * ViewModel class that stores and manages the flight and favorite results data for the Flight Tracker app.
 *
 * @author Wan-Hsuan Lee
 * @version 1.0
 */
public class FlightTrackerViewModel extends ViewModel {

    /** MutableLiveData holding a list of flight results. */
    public MutableLiveData<ArrayList<FlightResult>> flightResults = new MutableLiveData< >();
    /** MutableLiveData holding the selected flight result. */
    public MutableLiveData<FlightResult> selectedFlight = new MutableLiveData< >();
    /** MutableLiveData holding a list of favorite flight results. */
    public MutableLiveData<ArrayList<FlightResult>> favoriteResults = new MutableLiveData< >();
    /** MutableLiveData holding the selected favorite flight result. */
    public MutableLiveData<FlightResult> selectedFavorite = new MutableLiveData< >();

    /**
     * Retrieves the MutableLiveData instance holding a list of flight results.
     *
     * @return The MutableLiveData instance for flight results.
     */
    public MutableLiveData<ArrayList<FlightResult>> getFlights() {
        return flightResults;
    }

    /**
     * Retrieves the MutableLiveData instance holding a list of favorite flight results.
     *
     * @return The MutableLiveData instance for favorite flight results.
     */
    public MutableLiveData<ArrayList<FlightResult>> getFavorites() { return favoriteResults; }
}
