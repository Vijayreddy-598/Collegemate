package com.batch16.collegemate.Functions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.batch16.collegemate.MainActivity;
import com.batch16.collegemate.R;
import com.batch16.collegemate.ui.HomeFragment;

import java.util.List;

public class HomeNotificationAdapter extends RecyclerView.Adapter<HomeNotificationAdapter.ViewInfo> {
    Context ctx;
    List<String> Title,Sub;
    public HomeNotificationAdapter(Context ct, List<String> Ti, List<String>Sub){
        ctx=ct;
        Title=Ti;
        this.Sub=Sub;
    }

    @NonNull
    @Override
    public ViewInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.home_recycler_adapter,parent,false);
        return new ViewInfo(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInfo holder, int position) {
        holder.titletxt.setText(Title.get(position));
        holder.subtitletxt.setText(Sub.get(position));
    }

    @Override
    public int getItemCount() {
        return Title.size();
    }

    public class ViewInfo extends RecyclerView.ViewHolder {
        TextView titletxt,subtitletxt;
        public ViewInfo(@NonNull View itemView) {
            super(itemView);
            titletxt=itemView.findViewById(R.id.title);
            subtitletxt=itemView.findViewById(R.id.subtitle);
        }
    }
}
