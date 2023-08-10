package algonquin.cst2335.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ImageDetailsFragment extends Fragment {

    private static final String ARG_IMAGE_DETAILS = "image_details";

    private LoadImage imageDetails;

    private FloatingActionButton deleteButton;


    public ImageDetailsFragment() {

    }

    public static ImageDetailsFragment newInstance(LoadImage imageDetails) {
        ImageDetailsFragment fragment = new ImageDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE_DETAILS, (Parcelable) imageDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageDetails = getArguments().getParcelable(ARG_IMAGE_DETAILS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_image_details, container, false);
    }


    private Dialog generatingDialog;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Loading Data, please wait...");
        builder.setCancelable(false);
        generatingDialog = builder.create();
        generatingDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (generatingDialog != null && generatingDialog.isShowing()) {
                    generatingDialog.dismiss();
                }
            }
        }, 1000);

        ImageView imageView = view.findViewById(R.id.decImage);
        TextView heightTextView = view.findViewById(R.id.decHeight);
        TextView widthTextView = view.findViewById(R.id.desWidth);
        deleteButton=view.findViewById(R.id.deleteButton);

        showToast("Data Loaded Successfully");

        if (imageDetails != null) {
            Glide.with(requireContext()).load(imageDetails.getImage()).into(imageView);
            heightTextView.setText("Height: " + imageDetails.getHeight());
            widthTextView.setText("Width: " + imageDetails.getWidth());
        }
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}