package com.example.conelshop;

import androidx.annotation.NonNull;
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
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends AppCompatActivity implements CartAdapter.OnItemClickListener {

    TextView total_amount;
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_ID = "product_id";
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_DESCRIPTION = "description";
    double total = 0;
    public final String totalCost = this.getTotal();

    DrawerLayout drawerLayout;

    RecyclerView mRecyclerView;
    private CartAdapter adapter;
    private ArrayList<Product> productList;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        drawerLayout = findViewById(R.id.drawer_layout);

        String email = getIntent().getStringExtra("user");
        total_amount = findViewById(R.id.total_amount);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        parseJson(email);
    }

    private void parseJson(final String email) {
        String url = "http://192.168.1.15/AndroidAppDatabaseConnection/cart.php";
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

                        total = total + price;



                        Product product = new Product(productID, name, description, photo, price);
                        productList.add(product);
                    }

                    total_amount.setText(Double.toString(total) + "€");
                    adapter = new CartAdapter(Cart.this, productList, email, totalCost);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(Cart.this);
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

        mRequestQueue.add(request);
    }

    public String getTotal() {
        return Double.toString(total);
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
        redirectActivity(this, Favorites.class);
    }

    public void ClickCart(View view) {
        recreate();
    }

    public void ClickLogout(View view) {
        logout(this);
    }

    public void ClickCheckout(View view) {
        Intent detailIntent = new Intent(this, Checkout.class);
        String total = total_amount.getText().toString();
        detailIntent.putExtra("total", total);
        startActivity(detailIntent);
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