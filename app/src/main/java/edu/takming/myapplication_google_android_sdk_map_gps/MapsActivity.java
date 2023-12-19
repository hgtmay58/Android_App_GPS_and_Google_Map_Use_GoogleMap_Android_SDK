package edu.takming.myapplication_google_android_sdk_map_gps;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.takming.myapplication_google_android_sdk_map_gps.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locationManager;
    Location location;
    double mLatitude = 0;
    double mLongitude = 0;
    String provider;
    MapsActivity mactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mactivity=this;
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(mactivity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(mactivity, new String[]{ACCESS_FINE_LOCATION}, 12);

        }
        else
            initlocation();
    }
//
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 12) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initlocation();
        } else
            return;
    }
}
    //
    void initlocation() {
        try {
            // Getting LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            provider = locationManager.getBestProvider(criteria, true);
            // Enabling  get My Location
            // Getting Current Location From GPS
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 12000, 0, mactivity);
            onLocationChanged(location);
        }
        catch(SecurityException e)
        {
            ActivityCompat.requestPermissions(mactivity, new String[]{ACCESS_FINE_LOCATION}, 12);
        }

    }
    //
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location==null)
        {

        }
        else {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();


           //
            // Add a marker
            LatLng myloc = new LatLng( location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myloc).title("Marker My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myloc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //精細程度：
            //
            //1：全世界
            //5：自然景觀/大陸
            //10：城市
            //15：街道
            //20：建築


        }
    }
}