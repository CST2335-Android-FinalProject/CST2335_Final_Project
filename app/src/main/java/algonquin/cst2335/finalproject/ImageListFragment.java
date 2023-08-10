package algonquin.cst2335.finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageListFragment extends Fragment {

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<LoadImage> dataList;
    LoadImageAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_image_list, container, false);

        recyclerView = fragmentView.findViewById(R.id.imageListRecycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();

        adapter = new LoadImageAdapter(getContext(), dataList);
        recyclerView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(new LoadImageAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteItem(position);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("images");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String imageId = itemSnapshot.getKey();
                    String image = itemSnapshot.child("imageUrl").getValue(String.class);
                    int height = itemSnapshot.child("height").getValue(Integer.class);
                    int width = itemSnapshot.child("width").getValue(Integer.class);
                    LoadImage loadImage = new LoadImage(imageId, image, height, width);
                    dataList.add(loadImage);
                }

                Collections.reverse(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });

        return fragmentView;
    }


    private Dialog generatingDialog;
    private void deleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Deleting Image, please wait...");
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

        String imageId = dataList.get(position).getImageId();

        // Remove the item from the dataList and notify the adapter
        dataList.remove(position);
        adapter.notifyItemRemoved(position);

        // Remove the item from Firebase database
        DatabaseReference itemRef = databaseReference.child(imageId);
        itemRef.removeValue();
        showToast("Image Deleted Successfully!!");
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}