package dev.lab.fnffoodtruckmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

import dev.lab.fnffoodtruckmap.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    MarkerOptions marker;
    LatLng centerlocation;
    Vector<MarkerOptions> markerOptions;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerlocation = new LatLng(3.0, 101.0);
        markerOptions = new Vector<>();

        markerOptions.add(new MarkerOptions().title("Nazs Chicken")
                .position(new LatLng(6.17, 100.37))
                .snippet("Open : 8am - 6pm"));
        markerOptions.add(new MarkerOptions().title("Raja Gulai Foodtruck")
                .position(new LatLng(6.14, 100.34))
                .snippet("Open : 8am - 6pm"));
        markerOptions.add(new MarkerOptions().title("Food Truck Kuala Perlis")
                .position(new LatLng(6.41, 100.12))
                .snippet("Open : 8am - 6pm"));
        markerOptions.add(new MarkerOptions().title("Chapee Kopi Claypot Perlis HQ Foodtruck")
                .position(new LatLng(6.47, 100.20))
                .snippet("Open : 8am - 6pm"));
        markerOptions.add(new MarkerOptions().title("Kak Yah Corner Food Truck")
                .position(new LatLng(6.27, 100.41))
                .snippet("Open : 8am - 6pm"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (MarkerOptions mark : markerOptions) {
            mMap.addMarker(mark);
        }

        enableMyLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerlocation, 8));
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }
}
