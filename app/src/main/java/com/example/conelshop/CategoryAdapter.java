package com.example.conelshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private ArrayList<Category> mCategories;

    public CategoryAdapter(FragmentActivity context, ArrayList<Category> categories) {
        mContext = context;
        mCategories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category currentCategory = mCategories.get(position);
        String name = currentCategory.getName();

        holder.textViewName.setText(name);
    }


    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
