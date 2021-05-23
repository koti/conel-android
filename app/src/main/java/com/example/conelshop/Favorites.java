package com.example.conelshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.conelshop.data.model.LoggedInUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.conelshop.MainActivity.EXTRA_ID;
import static com.example.conelshop.MainActivity.EXTRA_NAME;
import static com.example.conelshop.MainActivity.EXTRA_PRICE;

public class Favorites extends AppCompatActivity implements WishlistAdapter.OnItemClickListener {


    String cart_url = "http://192.168.1.15/AndroidAppDatabaseConnection/add_to_cart.php";
    String favorites_url = "http://192.168.1.15/AndroidAppDatabaseConnection/add_to_wishlist.php";

    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_ID = "product_id";
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_DESCRIPTION = "description";
    DrawerLayout drawerLayout;

    RecyclerView mRecyclerView;
    private WishlistAdapter adapter;
    private ArrayList<Product> productList;
    private RequestQueue mRequestsQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        String email = intent.getStringExtra("user");


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final int product_id = intent.getIntExtra(EXTRA_ID, 0);
        String name = intent.getStringExtra(EXTRA_NAME);
        Double price = intent.getDoubleExtra(EXTRA_PRICE, 0);

        TextView textViewName = findViewById(R.id.text_view_name);
        TextView textViewPrice = findViewById(R.id.text_view_price);



        productList = new ArrayList<>();
        mRequestsQueue = Volley.newRequestQueue(this);
        parseJson(email);
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
                Toast.makeText(Favorites.this, "Item added into cart", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Favorites.this, "Could not add item into cart", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Favorites.this, "Item added into wishlist", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Favorites.this, "Could not add item into wishlist", Toast.LENGTH_SHORT).show();
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

    private void parseJson(final String email) {
        String url = "http://192.168.1.15/AndroidAppDatabaseConnection/wishlist.php";
        final JSONObject parameters = new JSONObject();
        try {
            parameters.put("user", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
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

                    adapter = new WishlistAdapter(Favorites.this, productList, email);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(Favorites.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    parameters.put("Content-Type", "application/json");
                    parameters.put("user", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        mRequestsQueue.add(request);
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        redirectActivity(this, MainActivity.class);
    }

    public void ClickFavorites(View view) {
        recreate();
    }

    public void ClickCart(View view) {
        redirectActivity(this, Cart.class);
    }

    public void ClickLogout(View view) {
        logout(this);
    }

    private static void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finishAffinity();
                System.exit(0);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
    private void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        String email = getIntent().getStringExtra("user");
        intent.putExtra("user", email);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, ProductDetailActivity.class);

        Product clickedItem = productList.get(position);
        String email = getIntent().getStringExtra("user");
        detailIntent.putExtra("user", email);
        detailIntent.putExtra(EXTRA_ID, clickedItem.getProductID());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getPhoto());
        detailIntent.putExtra(EXTRA_NAME, clickedItem.getName());
        detailIntent.putExtra(EXTRA_PRICE, clickedItem.getPrice());
        detailIntent.putExtra(EXTRA_DESCRIPTION, clickedItem.getDescription());

        startActivity(detailIntent);
    }
}