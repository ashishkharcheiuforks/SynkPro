package com.perusudroid.synkpro.adapter;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.perusudroid.synkpro.R;
import com.perusudroid.synkpro.model.view.response.Data;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


    private List<Data> dataList;

    public NotesAdapter(List<Data> data) {
        this.dataList = data;
    }

    public void refresh(List<Data> data) {
        this.dataList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflater_notes, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(dataList.get(i));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvContent, tvDate, tvCreated;
        private View v1;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCreated = itemView.findViewById(R.id.tvCreated);
            v1 = itemView.findViewById(R.id.v1);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void bind(Data data) {
            v1.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), data.getNote_is_synced() == 1 ? R.color.color_synced : R.color.color_un_synced));
            tvTitle.setText(data.getNote_title());
            tvContent.setText(data.getNote_content());
            tvCreated.setText(data.getNote_by());
            tvDate.setText(data.getNote_created_on());
            ratingBar.setRating(data.getNote_rating());
        }
    }
}
