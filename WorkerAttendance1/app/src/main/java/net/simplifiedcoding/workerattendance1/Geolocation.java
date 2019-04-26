package net.simplifiedcoding.workerattendance1;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Geolocation extends AppCompatActivity {

    double lat1 = 28.547338;
    double long1 = 77.183123;
    double Distance;
    Button btnLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);
        btnLoc = (Button) findViewById(R.id.btnGetLoc);
        ActivityCompat.requestPermissions(Geolocation.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GpsTracker gt = new GpsTracker(getApplicationContext());
                Location l = gt.getLocation();
                if( l == null){
                    Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                }else {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    Distance = distance(lat, lon, lat1, long1);
                    if(Distance < 0.06){
                        finish();
                        startActivity(new Intent(getApplicationContext(), ScanQR.class));
                        Toast.makeText(getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Go to your workplace",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}