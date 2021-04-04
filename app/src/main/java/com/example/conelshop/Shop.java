package com.example.conelshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Shop extends AppCompatActivity {

    private static final String PRODUCT_URL = "http://192.168.1.15/AndroidAppDatabaseConnection/products.php";

    RecyclerView mRecyclerView;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private RequestQueue mRequestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJson();



        return view;
    }

    private void parseJson() {
        String url = "http://192.168.1.15/AndroidAppDatabaseConnection/products.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray products = response.getJSONArray("products");
                    productList = new ArrayList<>();

                    for(int i = 0; i < products.length(); i++) {
                        JSONObject productObject = products.getJSONObject(i);

                        int productID = productObject.getInt("product_id");
                        String name = productObject.getString("name");
                        String description = productObject.getString("description");
                        String photo = productObject.getString("photo");
                        double price = productObject.getDouble("price");

                        Product product = new Product(productID, name, description, photo, price);
                        productList.add(product);
                    }

                    adapter = new ProductAdapter(Shop.this, productList);
                    mRecyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }
}