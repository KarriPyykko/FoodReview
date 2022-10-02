package com.example.k.htyo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

// RecyclerView adapter for comment field
public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.ItemViewHolder>{

    private List<Comment> mData;
    private LayoutInflater mInflater;
    private static RecyclerViewClickListener itemListener;
    private Context context;

    // default constructor
    MyRecyclerViewAdapter2(Context context, List<Comment> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context =  context;
    }

    // Overriding the default onCreateViewHolder method and inflating the Recycler row
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ItemViewHolder(view);
    }

    // Overriding the default onBindViewHolder method and inserting Food-object data into the row fields
    @Override
    public void onBindViewHolder(final MyRecyclerViewAdapter2.ItemViewHolder holder, final int position) {
        final Comment comment = mData.get(position);
        holder.myTextView.setText(comment.getFeedback());
        holder.myHeaderView.setText(comment.getAuthor());

        // Click listener for remove comment ImageButton
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // times in milliseconds
                long now = new Date().getTime();
                long diff = now - mData.get(position).getTime();

                // checking if comment age is less than 5 minutes (300 seconds) and removing if true
                if((diff/1000)<300) {
                     mData.remove(position);
                     notifyItemRemoved(position);
                     notifyItemChanged(position);
                 }
            }
        });

        // Click listener for edit comment ImageButton
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // times in milliseconds
                long now = new Date().getTime();
                long diff = now - mData.get(position).getTime();

                // checking if comment age is less than 5 minutes (300 seconds) and focusing if true
                if((diff/1000)<300) {
                    holder.myTextView.setFocusableInTouchMode(true);
                    holder.myTextView.requestFocus();
                }
            }
        });

        // Key listener for recycler row EditText field (the comments). Updates comment feedback attribute on enter key press
        holder.myTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                    //hiding soft keyboard
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.myTextView.getWindowToken(), 0);

                    // Update data and clear focus
                    holder.myTextView.clearFocus();
                    mData.get(position).setFeedback(holder.myTextView.getText().toString());
                    notifyItemChanged(position);
                    holder.myTextView.setFocusableInTouchMode(false);
                    return true;
                }
                return false;
            }
        });

        // Editor action listener for recycler row EditText field (the comments). Updates comment feedback attribute on IME_ACTION_DONE
        holder.myTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    // Update data and clear focus
                    holder.myTextView.clearFocus();
                    mData.get(position).setFeedback(holder.myTextView.getText().toString());
                    notifyItemChanged(position);
                    return true;
                }
                return false;
            }
        });
    }

    // Method returns number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // Class for storing views
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText myTextView;
        TextView myHeaderView;
        ImageButton remove;
        ImageButton edit;

        public ItemViewHolder(View itemView) {
            super(itemView);
            myHeaderView = itemView.findViewById(R.id.commentHeader);
            myTextView = itemView.findViewById(R.id.commentContent);
            myTextView.setFocusableInTouchMode(false);
            remove = itemView.findViewById(R.id.imageButton3);
            edit = itemView.findViewById(R.id.imageButtonEdit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.onItemClick(v, this.getAdapterPosition());
        }
    }

    // Method that returns object at click position
    Comment getItem(int id) {
        return mData.get(id);
    }

    // Method for enabling click listener from parent activity
    void setClickListener(RecyclerViewClickListener itemClickListener) {
        this.itemListener = itemClickListener;
    }

    // Interface for parent activity
    public interface RecyclerViewClickListener {
        void onItemClick(View view, int position);
    }


}
