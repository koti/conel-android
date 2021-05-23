package com.example.conelshop;

import android.content.Context;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishlistAdapter  extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    String cart_url = "http://192.168.1.15/AndroidAppDatabaseConnection/add_to_cart.php";
    String remove_wishlist_url = "http://192.168.1.15/AndroidAppDatabaseConnection/remove_from_wishlist.php";

    private String mEmail;
    private Context mContext;
    private ArrayList<Product> mProducts;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public WishlistAdapter(Context context, ArrayList<Product> products, String email) {
        mEmail = email;
        mContext = context;
        mProducts = products;
    }
    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.wishlist_product, parent, false);
        return new WishlistViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Product currentProduct = mProducts.get(position);
        int productID = currentProduct.getProductID();
        String photo = currentProduct.getPhoto();
        String name = currentProduct.getName();
        double price = currentProduct.getPrice();


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

    public class WishlistViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textViewName;
        public TextView textViewPrice;
        public Button add_to_cart;
        public int position;
        public int productID;
        public Button remove_from_wishlist;


        public WishlistViewHolder(@NonNull final View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            add_to_cart = itemView.findViewById(R.id.add_cart);
            remove_from_wishlist = itemView.findViewById(R.id.remove_wishlist);
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

            add_to_cart.setOnClickListener(new View.OnClickListener() {
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

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, cart_url, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(itemView.getContext(), "Item added into cart", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(itemView.getContext(), "Could not add item into cart", Toast.LENGTH_SHORT).show();
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

            remove_from_wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove_from_wishlist(mEmail, productID);
                }

                private void remove_from_wishlist(final String e, final int id) {
                    JSONObject parameters = new JSONObject();
                    try {
                        parameters.put("user", e);
                        parameters.put("product", id);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, remove_wishlist_url, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(itemView.getContext(), "Item removed from wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(itemView.getContext(), "Could not remove item from wishlist", Toast.LENGTH_SHORT).show();
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
