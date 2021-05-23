package com.example.conelshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    String remove_cart_url = "http://192.168.1.15/AndroidAppDatabaseConnection/remove_from_cart.php";

    private double total = 0;
    private String mTotal;
    private CartAdapter adapter;
    private String mEmail;
    private Context mContext;
    private ArrayList<Product> mProducts;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CartAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public CartAdapter(Context context, ArrayList<Product> products, String email, String total) {
        mTotal = total;
        mEmail = email;
        mContext = context;
        mProducts = products;
        this.adapter = this;
    }
    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_product, parent, false);
        return new CartAdapter.CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Product currentProduct = mProducts.get(position);
        String name = currentProduct.getName();
        String photo = currentProduct.getPhoto();
        int productID = currentProduct.getProductID();
        double price = currentProduct.getPrice();

        double total = 0;
        
        total = total + price;
        holder.textViewName.setText(name);
        holder.textViewPrice.setText(price + "€");
        holder.position = position;
        holder.productID = productID;


        int id;

        if(photo.equals("mens1")) {
            id = R.drawable.mens1;

        } else if(photo.equals("mens4")) {
            id = R.drawable.mens4;
        } else {
            id = R.drawable.mens1;
        }

        Picasso.get().load(id).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewPrice;
        public TextView total_amount;
        public int position;
        public int productID;
        public Button remove_from_cart;
        public Button increase;
        public Button decrease;
        public TextView integer_number;


        public CartViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            remove_from_cart = itemView.findViewById(R.id.remove_cart);
            total_amount = itemView.findViewById(R.id.total_amount);
            imageView = itemView.findViewById(R.id.image_view);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });



            remove_from_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_to_cart(mEmail, productID);
                }

                private void add_to_cart(final String e, final int id) {
                    JSONObject parameters = new JSONObject();
                    try {
                        parameters.put("user", e);
                        parameters.put("product", id);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, remove_cart_url, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(itemView.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(itemView.getContext(), "Could not remove item from cart", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getParams() {

                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("user", e);
                            params.put("product", String.valueOf(id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                    requestQueue.add(request);
                }
            });
        }
    }
}
