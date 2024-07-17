package dev.lab.fnffoodtruckmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NearMeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    LatLng centerlocation;

    private String URL_FOODTRUCK = "http://192.168.152.1/information/get_foodtruck_details.php";
    private String URL_INFORMATION = "http://192.168.152.1/information/all.php";
    RequestQueue requestQueue;
    Gson gson;
    FoodTruck[] foodtrucks;
    Information[] informations;

    public NearMeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_me, container, false);

        gson = new GsonBuilder().create();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerlocation = new LatLng(3.0, 101.0);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation, 8));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 0, 0, 150);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
        mMap.setOnInfoWindowClickListener(this);

        enableMyLocation();
        sendRequest(URL_FOODTRUCK, BitmapDescriptorFactory.HUE_RED, true); // Fetch data for food trucks
        sendRequest(URL_INFORMATION, BitmapDescriptorFactory.HUE_VIOLET, false); // Fetch data for other information
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    public void sendRequest(String url, final float markerColor, final boolean isFoodTruck) {
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JSONResponse", response);
                if (isFoodTruck) {
                    foodtrucks = gson.fromJson(response, FoodTruck[].class);

                    if (foodtrucks.length < 1) {
                        Toast.makeText(getContext(), "Problem retrieving JSON data", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (FoodTruck foodtruck : foodtrucks) {
                        LatLng position = new LatLng(foodtruck.getLat(), foodtruck.getLng());
                        String title = foodtruck.getName();
                        String snippet = foodtruck.getSchedule();

                        MarkerOptions markerOptions = new MarkerOptions().title(title)
                                .position(position)
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor));

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(foodtruck); // Set the tag for food truck markers
                    }
                } else {
                    informations = gson.fromJson(response, Information[].class);

                    if (informations.length < 1) {
                        Toast.makeText(getContext(), "Problem retrieving JSON data", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (Information info : informations) {
                        LatLng position = new LatLng(info.getLat(), info.getLng());
                        String title = info.getName();
                        String snippet = info.getDescription();

                        MarkerOptions markerOptions = new MarkerOptions().title(title)
                                .position(position)
                                .snippet(snippet)
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor));

                        mMap.addMarker(markerOptions); // No tag needed for other markers
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag() != null && marker.getTag() instanceof FoodTruck) {
            FoodTruck foodTruck = (FoodTruck) marker.getTag();
            showFoodTruckDetails(foodTruck);
        }
    }
    private void showFoodTruckDetails(FoodTruck foodTruck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_foodtruck_details, null);
        dialogView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dialog_background));
        builder.setView(dialogView);
        TextView tvFoodTruckName = dialogView.findViewById(R.id.tvFoodTruckName);
        TextView tvOperator = dialogView.findViewById(R.id.tvOperator);
        TextView tvSchedule = dialogView.findViewById(R.id.tvSchedule);
        TextView tvMenuLabel = dialogView.findViewById(R.id.tvMenuLabel);
        LinearLayout layoutMenu = dialogView.findViewById(R.id.layoutMenu);

        tvFoodTruckName.setText(foodTruck.getName());
        tvOperator.setText("Operator: " + foodTruck.getOperatorName());

        String schedule = foodTruck.getSchedule().replace(",", ",\n");
        tvSchedule.setText("Schedule:\n" + schedule);
        tvMenuLabel.setTypeface(tvMenuLabel.getTypeface(), Typeface.BOLD);
        tvMenuLabel.setPaintFlags(tvMenuLabel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String[] menuItems = foodTruck.getMenu().split(",");
        for (String menuItem : menuItems) {
            TextView tvMenuItem = new TextView(getContext());
            tvMenuItem.setText("• " + menuItem.trim());
            tvMenuItem.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            layoutMenu.addView(tvMenuItem);
        }

        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        // Set the dialog background to transparent to ensure the custom background is visible
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }
}
