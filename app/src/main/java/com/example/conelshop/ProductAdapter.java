package com.example.conelshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private ArrayList<Product> mProducts;

    public ProductAdapter(FragmentActivity context, ArrayList<Product> products) {
        mContext = context;
        mProducts = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product currentProduct = mProducts.get(position);
        String name = currentProduct.getName();
        double price = currentProduct.getPrice();

        holder.textViewName.setText(name);
        holder.textViewPrice.setText(price + "€");
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);

        }
    }
}
