package net.simplifiedcoding.workerattendance1;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//implementing onclicklistener
public class ScanQR extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private Button buttonLogout;
    private TextView textViewUserEmail;
    String workerUID;
    //qr code scanner object
    private IntentIntegrator qrScan;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        firebaseAuth = FirebaseAuth.getInstance();


        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        workerUID = user.getUid();
        textViewUserEmail.setText("Welcome "+user.getEmail());




        //attaching onclick listener
        buttonScan.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    String id = myRef.push().getKey();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    String strTime = mdformat.format(calendar.getTime());
                    String strDate = dateFormat.format(calendar.getTime());
                    AttendanceData attendanceData = new AttendanceData(obj.getString("bathroom_id"), obj.getString("floor_no"), strTime, strDate);
                    myRef.child("attendance").child(workerUID).child(id).setValue(attendanceData);
                    Toast.makeText(this,"Attendance successful",Toast.LENGTH_LONG).show();
                    AttendanceTime attendanceTime = new AttendanceTime(workerUID,strTime, strDate);
                    myRef.child("bathrooms").child(obj.getString("bathroom_id")).setValue(attendanceTime);
                    myRef.child("Workers").child(workerUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRef.child("Workers").child(workerUID).child("Real").setValue(dataSnapshot.child("Real").getValue(Integer.class) + 1);
                            float x = ((float)dataSnapshot.child("Real").getValue(float.class)+1)/((float)dataSnapshot.child("Ideal").getValue(float.class))*100;
                            myRef.child("Workers").child(workerUID).child("Attendance%").setValue(x);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    startActivity(new Intent(getApplicationContext(), Geolocation.class));

                    //setting values to textviews
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        if(view == buttonScan){
            qrScan.initiateScan();
        }

        if(view == buttonLogout){
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}