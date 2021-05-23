package com.example.conelshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.conelshop.MainActivity.EXTRA_DESCRIPTION;
import static com.example.conelshop.MainActivity.EXTRA_ID;
import static com.example.conelshop.MainActivity.EXTRA_NAME;
import static com.example.conelshop.MainActivity.EXTRA_PRICE;
import static com.example.conelshop.MainActivity.EXTRA_URL;

public class ProductDetailActivity extends AppCompatActivity {



    String cart_url = "http://192.168.1.15/AndroidAppDatabaseConnection/add_to_cart.php";
    String favorites_url = "http://192.168.1.15/AndroidAppDatabaseConnection/add_to_wishlist.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        String photo = intent.getStringExtra(EXTRA_URL);
        String email = intent.getStringExtra("user");
        final int product_id = intent.getIntExtra(EXTRA_ID, 0);
        String name = intent.getStringExtra(EXTRA_NAME);
        Double price = intent.getDoubleExtra(EXTRA_PRICE, 0);
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewName = findViewById(R.id.text_view_name);
        TextView textViewPrice = findViewById(R.id.text_view_price);
        TextView textViewDescription = findViewById(R.id.text_view_description);
        Button add_cart = findViewById(R.id.add_cart);
        Button add_favorites = findViewById(R.id.add_wishlist);

        add_cart.setTag(email);
        add_favorites.setTag(email);
        textViewName.setText(name);
        textViewPrice.setText(price + "€");
        int id;

        if(photo.equals("mens1")) {
            id = R.drawable.mens1;

        } else if(photo.equals("mens4")) {
            id = R.drawable.mens4;
        } else if(photo.equals("shop8")) {
            id = R.drawable.shop8;
        }
        else {
            id = R.drawable.mens1;
        }

        Picasso.get().load(id).into(imageView);
        textViewDescription.setText(description);

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = view.getTag().toString();
                add_to_cart(email, product_id);
            }
        });

        add_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = view.getTag().toString();
                add_to_favorites(email, product_id);
            }
        });

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
                Toast.makeText(ProductDetailActivity.this, "Item added into cart", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailActivity.this, "Could not add item into cart", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void add_to_favorites(final String e, final int id) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("user", e);
            parameters.put("product", id);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, favorites_url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ProductDetailActivity.this, "Item added into wishlist", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductDetailActivity.this, "Could not add item into wishlist", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}