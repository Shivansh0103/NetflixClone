package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.MainScreens.MainScreen;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SignInActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView signup,forgotpass;
    Button signIn;
    EditText email,password;
    String resetEmail,authEmail,authPassword,userID,fireFirstName,fireLastName,fireContactNumber;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date today,validityDate;
    DocumentReference userReference;
    static int counter=0,duration=500;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();
        progressBar=findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        signup=findViewById(R.id.signuptextview);
        forgotpass=findViewById(R.id.forgotpasswordtextview);
        signIn=findViewById(R.id.buttonsignin);
        email=findViewById(R.id.emailedittext);
        password=findViewById(R.id.passwordtext);
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar calendar=Calendar.getInstance();
        today= calendar.getTime();
        firebaseAuth=FirebaseAuth.getInstance();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authEmail=email.getText().toString();
                authPassword=password.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(email.getText().toString().length()>8 && email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") && password.getText().toString().length()>7) {
                    firebaseAuth.signInWithEmailAndPassword(authEmail, authPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userID=firebaseAuth.getCurrentUser().getUid();
                            userReference=firebaseFirestore.collection("Users ").document(userID);
                            userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    validityDate=documentSnapshot.getDate("Valid Date");
                                    fireFirstName=documentSnapshot.getString("First Name");
                                    fireLastName=documentSnapshot.getString("Last Name");
                                    fireContactNumber=documentSnapshot.getString("Contact Number");

                                    if(validityDate.compareTo(today)>=0)
                                    {
                                        Intent intent = new Intent(SignInActivity.this, MainScreen.class);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                    else {
                                        startActivity(new Intent(SignInActivity.this, PaymentOverdue.class).putExtra("firstName", fireFirstName).putExtra("lastname", fireLastName).putExtra("Contact number", fireContactNumber).putExtra("Email", authEmail).putExtra("uid", userID));
                                        progressBar.setVisibility(View.GONE);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                }
                            });
                        } else {
                            if(task.getException() instanceof FirebaseNetworkException)
                                Toast.makeText(SignInActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            if(task.getException() instanceof FirebaseAuthInvalidUserException)
                                Toast.makeText(SignInActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                Toast.makeText(SignInActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
                }
                else
                {
                    if(email.getText().toString().length()<=7||!email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                        email.setError("Enter a valid email ID");
                    if(password.getText().toString().length()<=7)
                        password.setError("Wrong Password");
                    else
                        Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this, SwipeScreen.class);
                startActivity(intent);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length()>8 && email.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
                {
                    AlertDialog.Builder passwordReset=new AlertDialog.Builder(view.getContext());
                    passwordReset.setTitle("Reset Password");
                    passwordReset.setMessage("Press Yes to receive reset link");
                    passwordReset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resetEmail=email.getText().toString();
                            firebaseAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SignInActivity.this, "Reset Link Sent", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this, "Reset link not sent as no user exists by this email", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    passwordReset.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    passwordReset.create().show();
                }
                else
                {
                    email.setError("Enter Valid Email");
                }
            }
        });
    }
    public void progress()
    {
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);
                if(counter==500)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask,0,100);
    }
}