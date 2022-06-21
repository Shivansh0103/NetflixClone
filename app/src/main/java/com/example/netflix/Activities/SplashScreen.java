package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.netflix.MainScreens.MainScreen;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    Date today,validityDate;
    String uID,fireFirstName,fireLastName,fireContactNumber,fireEmail;
    static int counter=0,duration=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar calendar=Calendar.getInstance();
        today= calendar.getTime();
        progress();
        start();
    }
    public void progress()
    {
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);
                if(counter==5000)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask,0,100);
    }
    public void start()
    {
        new Handler().postDelayed(() -> {
            if(firebaseAuth.getCurrentUser()!=null)
            {
                uID=firebaseAuth.getCurrentUser().getUid();
                documentReference=firebaseFirestore.collection("Users ").document(uID);
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        validityDate=documentSnapshot.getDate("Valid Date");
                        fireFirstName=documentSnapshot.getString("First Name");
                        fireLastName=documentSnapshot.getString("Last Name");
                        fireContactNumber=documentSnapshot.getString("Contact Number");
                        fireEmail=documentSnapshot.getString("Email");
                        if(validityDate.compareTo(today)>=0)
                        {
                            Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                            startActivity(intent);
                        }
                        else {
                            startActivity(new Intent(SplashScreen.this, PaymentOverdue.class).putExtra("firstName", fireFirstName).putExtra("lastname", fireLastName).putExtra("Contact number", fireContactNumber).putExtra("Email", fireEmail).putExtra("uid", uID));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseNetworkException)
                            Toast.makeText(SplashScreen.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SplashScreen.this, "Data not fetched", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
            startActivity(new Intent(SplashScreen.this, SignInActivity.class));
            finish();
            }
        },duration);
    }
}