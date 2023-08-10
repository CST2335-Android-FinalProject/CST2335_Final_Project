package algonquin.cst2335.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LoadImageAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private Context context;
    private List<LoadImage> dataList;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public LoadImageAdapter(Context context, List<LoadImage> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recHeight.setText("Height: "+dataList.get(position).getHeight());
        holder.recWidth.setText("Width: "+dataList.get(position).getWidth());
        Glide.with(context)
                .load(dataList.get(position).getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.baseline_image_24)
                .into(holder.recImage);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            }
        });

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadImage imageDetails = dataList.get(position);
                openImageDetailsFragment(imageDetails);
            }
        });

    }

    private void openImageDetailsFragment(LoadImage imageDetails) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        ImageDetailsFragment fragment = ImageDetailsFragment.newInstance(imageDetails);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder
{
    ImageView recImage;
    TextView recHeight,recWidth;

    CardView recCard;
    ImageButton deleteButton;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        deleteButton = itemView.findViewById(R.id.deleteButton);
        recCard = itemView.findViewById(R.id.recCard);
        recImage=itemView.findViewById(R.id.item_Image);
        recWidth=itemView.findViewById(R.id.item_width);
        recHeight=itemView.findViewById(R.id.item_height);
    }

}
