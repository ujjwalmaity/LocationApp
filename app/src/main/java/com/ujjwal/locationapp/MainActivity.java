package com.ujjwal.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView, textView2;

    LocationManager locationManager;
    LocationListener locationListener;

    Double latitude;
    Double longitude;

    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.main_location);
        textView2 = findViewById(R.id.main_address);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                textView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress = geocoder.getFromLocation(latitude, longitude, 1);
                    if (listAddress != null && listAddress.size() > 0) {
                        //Toast.makeText(MainActivity.this, listAddress.get(0).toString(), Toast.LENGTH_SHORT).show();
                        if (listAddress.get(0).getSubThoroughfare() != null) {
                            address += listAddress.get(0).getSubThoroughfare() + "\n";
                        }
                        if (listAddress.get(0).getThoroughfare() != null) {
                            address += listAddress.get(0).getThoroughfare() + "\n";
                        }
                        if (listAddress.get(0).getLocality() != null) {
                            address += listAddress.get(0).getLocality() + "\n";
                        }
                        if (listAddress.get(0).getPostalCode() != null) {
                            address += listAddress.get(0).getPostalCode() + "\n";
                        }
                        if (listAddress.get(0).getCountryName() != null) {
                            address += listAddress.get(0).getCountryName() + "\n";
                        }
                        textView2.setText(address);
                        address = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            //Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //latitude = lastKnownLocation.getLatitude();
            //longitude = lastKnownLocation.getLongitude();
            //textView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}
