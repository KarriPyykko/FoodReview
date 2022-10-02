package com.example.k.htyo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

// RecyclerView adapter for menu field
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Food> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // default constructor
    MyRecyclerViewAdapter(Context context, List<Food> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // Overriding the default onCreateViewHolder method and inflating the Recycler row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_menu, parent, false);
        return new ViewHolder(view);
    }

    // Overriding the default onBindViewHolder method and inserting Food-object data into the row fields
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food food = mData.get(position);
        holder.myTextView.setText(food.getTitle_fi());
        holder.myHeaderView.setText(food.getCategory());
    }

    // Method returns number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // Class for storing views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        TextView myHeaderView;

        ViewHolder(View itemView) {
            super(itemView);
            myHeaderView = itemView.findViewById(R.id.foodHeader);
            myTextView = itemView.findViewById(R.id.foodContent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Method that returns object at click position
    Food getItem(int id) {
        return mData.get(id);
    }

    // Method for enabling click listener from parent activity
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Interface for parent activity
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
