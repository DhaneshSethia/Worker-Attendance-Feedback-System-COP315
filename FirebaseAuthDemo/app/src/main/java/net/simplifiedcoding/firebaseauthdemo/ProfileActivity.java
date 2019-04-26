package net.simplifiedcoding.firebaseauthdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonScan;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    private Spinner selectBathroomId;
    private UserFeedback userFeedback;
    private NotificationData notificationData;
    private String id;
    private String ID;
    private String workerUID;
    private String workerName;
    private String AdminUID;
    private String bathroomId;
    private StorageReference storageReference;

    private IntentIntegrator qrScan;
    FirebaseUser User;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference myRef =  firebaseDatabase.getReference();

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private RatingBar StarRating;
    private Button buttonSubmit;
    float stars;
    Spinner selectFloor;
    EditText utilityCode;
    EditText custom;
    CheckBox cleanliness;
    CheckBox repair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        storageReference = FirebaseStorage.getInstance().getReference();
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonScan = (Button) findViewById(R.id.buttonScan);

        imageView = (ImageView) findViewById(R.id.image_view);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonScan.setOnClickListener(this);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();


        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        User = user;
        qrScan = new IntentIntegrator(this);

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        StarRating = (RatingBar) findViewById(R.id.simpleRatingBar);
        buttonSubmit = (Button) findViewById(R.id.submit_button);
        selectFloor = (Spinner) findViewById(R.id.selectFloor);
        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());
        utilityCode = (EditText) findViewById(R.id.utilityCode);
        custom = (EditText) findViewById(R.id.Custom);
        cleanliness = (CheckBox) findViewById(R.id.cleanliness);
        repair = (CheckBox) findViewById(R.id.repair);
        selectBathroomId = (Spinner) findViewById(R.id.selectBathroomId);



        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendStarRating();
            }
        });

        StarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                stars = StarRating.getRating();
            }
        });

    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/pic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
//                //if qr contains data
//                try {
//                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
////                    AttendanceData attendanceData = new AttendanceData(obj.getString("bathroom_id"), obj.getString("floor_no"), strTime, strDate);
//                    QRData qrData = new QRData(obj.getString("bathroom_id"), obj.getString("utilityCode"));
////                    myRef.child("attendance").child(workerUID).child(id).setValue(attendanceData);
////                    Toast.makeText(this,"Attendance successful",Toast.LENGTH_LONG).show();
////                    AttendanceTime attendanceTime = new AttendanceTime(workerUID,strTime, strDate);
////                    myRef.child("bathrooms").child(obj.getString("bathroom_id")).setValue(attendanceTime);
////                    startActivity(new Intent(getApplicationContext(), Geolocation.class));
//
//                    //setting values to textviews
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View view) {

        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }

        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(view == buttonSubmit){
            SendStarRating();
        }
        else if(view == buttonScan){
            qrScan.initiateScan();
        }
    }

    private void SendStarRating(){
        Calendar calendar = Calendar.getInstance();
        String floor = selectFloor.getSelectedItem().toString();
        bathroomId = selectBathroomId.getSelectedItem().toString();
        Boolean unclean = cleanliness.isChecked();
        Boolean unrepaired = repair.isChecked();
        String uCode = utilityCode.getText().toString();
        String customFeedback = custom.getText().toString();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = "Current Time : " + mdformat.format(calendar.getTime());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        id = myRef.push().getKey();
        userFeedback = new UserFeedback(stars, strDate, floor, bathroomId, uCode, unclean, unrepaired, customFeedback);

        myRef.child("bathrooms").child(bathroomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                AttendanceTime attendanceTime = dataSnapshot1.getValue(AttendanceTime.class);
                workerUID = attendanceTime.uid;
                myRef.child("Rating_Table").child(workerUID).child(id).setValue(userFeedback);
                myRef.child("Workers").child(workerUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        workerName = dataSnapshot.child("name").getValue(String.class);
                        myRef.child("Rating_Table").child(workerUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                TotalRating tot_rating = dataSnapshot2.getValue(TotalRating.class);
                                float net_rate = tot_rating.net_rate + stars;
                                float count = tot_rating.count + 1;
                                float avg = net_rate/count;
                                myRef.child("Rating_Table").child(workerUID).child("net_rate").setValue(net_rate);
                                myRef.child("Rating_Table").child(workerUID).child("count").setValue(count);
                                myRef.child("Rating_Table").child(workerUID).child("avg").setValue(avg);
                                if(stars == 1 || avg < 1){
                                    final NotificationData notificationData = new NotificationData(stars, avg, workerName, bathroomId);
                                    myRef.child("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                                AdminUID = child.getKey();
                                            }

                                            myRef.child("notifications").child(AdminUID).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(ProfileActivity.this, "thank you for feedback", Toast.LENGTH_LONG).show();
    }

//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }
}