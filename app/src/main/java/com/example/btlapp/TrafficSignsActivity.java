package com.example.btlapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TrafficSignsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_traffic_signs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView rvTrafficSigns = findViewById(R.id.rvTrafficSigns);
        rvTrafficSigns.setLayoutManager(new LinearLayoutManager(this));
        
        DatabaseHelper db = new DatabaseHelper(this);
        List<TrafficSign> signs = db.getAllTrafficSigns();
        
        Log.d("Signs", "Total signs loaded: " + signs.size());
        
        rvTrafficSigns.setAdapter(new TrafficSignAdapter(signs));
    }

    public static class TrafficSignAdapter extends RecyclerView.Adapter<TrafficSignAdapter.ViewHolder> {
        private final List<TrafficSign> signs;

        public TrafficSignAdapter(List<TrafficSign> signs) {
            this.signs = signs;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final ImageView ivSign;
            public final TextView tvName;
            public final TextView tvDesc;
            public final TextView tvCategory;

            public ViewHolder(View view) {
                super(view);
                ivSign = view.findViewById(R.id.ivSign);
                tvName = view.findViewById(R.id.tvSignName);
                tvDesc = view.findViewById(R.id.tvSignDescription);
                tvCategory = view.findViewById(R.id.tvSignCategory);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_traffic_sign, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TrafficSign sign = signs.get(position);
            
            // Load image from assets
            if (sign.getImageName() != null && !sign.getImageName().isEmpty()) {
                try {
                    InputStream is = holder.itemView.getContext().getAssets().open("images/Sign/" + sign.getImageName());
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    holder.ivSign.setImageBitmap(bitmap);
                    is.close();
                } catch (IOException e) {
                    Log.e("Signs", "Error loading sign image: " + sign.getImageName(), e);
                    holder.ivSign.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else if (sign.getImageResId() != 0) {
                holder.ivSign.setImageResource(sign.getImageResId());
            } else {
                holder.ivSign.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            holder.tvName.setText(sign.getName());
            holder.tvDesc.setText(sign.getDescription());
            holder.tvCategory.setText(sign.getCategory());
        }

        @Override
        public int getItemCount() {
            return signs.size();
        }
    }
}
