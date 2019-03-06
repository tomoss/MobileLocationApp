package org.scd.mobiletrackingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.scd.mobiletrackingapp.model.ResLocation;
import org.scd.mobiletrackingapp.model.dto.LocationDTO;
import org.scd.mobiletrackingapp.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationListener locationListener;
    private LocationManager locationManager;

    Button btnSend;

   Double longitude = 0.0;
   Double latitude = 0.0;

   String latitudeS;
   String longitudeS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitudeS = latitude.toString();
                longitudeS = longitude.toString();

                if(validateSend(latitudeS, longitudeS)){
                    //do login
                    doSend(latitudeS, longitudeS);
                }
            }
        });

        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener=new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                LatLng loc = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(loc).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 1);

            return;
        } else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
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

    }

    private boolean validateSend(String latitude, String longitude){
        if(latitude == null || latitude.trim().length() == 0){
            Toast.makeText(this, "latitude is null", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(longitude == null || longitude.trim().length() == 0){
            Toast.makeText(this, "Longitude is null", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doSend(String latitude, String longitude){

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(latitude);
        locationDTO.setLongitude(longitude);

        String BASE_URL = "http://192.168.137.1:8080/";
        RetrofitClient client = new RetrofitClient(BASE_URL);

        Bundle extras = getIntent().getExtras();

        String credentials = extras.getString("credentials");;


        client.getServices().addLocation(locationDTO,credentials).enqueue(new Callback<ResLocation>(){
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        ResLocation resLocation = (ResLocation) response.body();
                        Toast.makeText(MapsActivity.this, "longitude: "+resLocation.getLongitude()+" latitude: "+resLocation.getLatitude()+
                                "\n"+resLocation.getUserEmail(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(MapsActivity.this, "The email or password are incorrect", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }


        });


    }
}
