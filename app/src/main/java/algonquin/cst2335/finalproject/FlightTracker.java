package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import algonquin.cst2335.finalproject.databinding.ActivityFlightTrackerBinding;
import algonquin.cst2335.finalproject.databinding.FlightResultBinding;

public class FlightTracker extends AppCompatActivity {

    ActivityFlightTrackerBinding binding;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    ArrayList<String> flightResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recycleView.setAdapter( myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                FlightResultBinding frBinding = FlightResultBinding.inflate(getLayoutInflater());
                return new MyRowHolder( frBinding.getRoot() );
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.airline.setText("");
                holder.flightNumber.setText("");
                holder.arrivalAirport.setText("");
                holder.status.setText("");
                holder.departureTime.setText("");
                holder.arrivalTime.setText("");
            }

            @Override
            public int getItemCount() {
                return 0;
            }
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
            airline = itemView.findViewById(R.id.airline);
            flightNumber = itemView.findViewById(R.id.flightNumber);
            arrivalAirport = itemView.findViewById(R.id.arrivalAirport);
            status = itemView.findViewById(R.id.status);
            departureTime = itemView.findViewById(R.id.departureTime);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
        }
    }
}