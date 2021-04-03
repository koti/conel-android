package com.example.conelshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class CategoryFragment extends Fragment {

    private static final String PRODUCT_URL = "http://192.168.1.15/AndroidAppDatabaseConnection/categories.php";

    RecyclerView mRecyclerView;
    private CategoryAdapter adapter;
    private ArrayList<Category> categoryList;
    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        parseJson();



        return view;
    }

    private void parseJson() {
        String url = "http://192.168.1.15/AndroidAppDatabaseConnection/categories.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray categories = response.getJSONArray("categories");
                    categoryList = new ArrayList<>();

                    for(int i = 0; i < categories.length(); i++) {
                        JSONObject productObject = categories.getJSONObject(i);

                        String name = productObject.getString("name");
                        Category category = new Category(name);
                        categoryList.add(category);
                    }

                    adapter = new CategoryAdapter(getActivity(), categoryList);
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
