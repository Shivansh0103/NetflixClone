package com.example.netflix.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.Activities.PaymentGateway;
import com.example.netflix.Activities.PaymentOverdue;
import com.example.netflix.Activities.SignInActivity;
import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Settings extends AppCompatActivity {
    EditText newPassword;
    TextView email,plan,date;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    DocumentReference userReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;
    Button resetPassword,signOut;
    String userID,emailString,planString;
    Date validityDate;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Settings.this,MainScreen.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        newPassword=findViewById(R.id.resetpasswordedittext);
        resetPassword=findViewById(R.id.resetpasswordbutton);
        signOut=findViewById(R.id.signoutbutton);
        email=findViewById(R.id.emailsettings);
        plan=findViewById(R.id.plansettings);
        date=findViewById(R.id.datesettings);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        progressDialog=new ProgressDialog(Settings.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            userID=firebaseAuth.getCurrentUser().getUid();
            userReference=firebaseFirestore.collection("Users ").document(userID);
            userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    validityDate=documentSnapshot.getDate("Valid Date");
                    emailString=documentSnapshot.getString("Email");
                    planString=documentSnapshot.getString("Plan");
                    email.setText(emailString);
                    plan.setText(planString);
                    date.setText(validityDate.toString());
                    progressDialog.cancel();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException)
                        Toast.makeText(Settings.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Settings.this, "Error! Data not fetched", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homeicon:
                        startActivity(new Intent(Settings.this,MainScreen.class));
                        break;
                    case R.id.searchicon:
                        startActivity(new Intent(Settings.this,Search.class));
                        break;
                    case R.id.settingsicon:
                        break;
                }
                return false;
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder signout=new AlertDialog.Builder(view.getContext());
                signout.setTitle("Do you really want to sign out");
                signout.setMessage("Press Yes to sign out");
                signout.setCancelable(false);
                signout.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Settings.this,SignInActivity.class));
                        finish();
                    }
                });
                signout.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                signout.create().show();
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(Settings.this);
                progressDialog.setMessage("Loading..");
                progressDialog.show();
                progressDialog.setCancelable(false);
                if(newPassword.getText().toString().length()>7)
                {
                    firebaseAuth.signInWithEmailAndPassword(emailString,newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                            EditText changePassowrd = new EditText(view.getContext());
                            AlertDialog.Builder passwordUpdate = new AlertDialog.Builder(view.getContext());
                            passwordUpdate.setTitle("Update Password");
                            passwordUpdate.setCancelable(false);
                            passwordUpdate.setView(changePassowrd);
                            changePassowrd.setHint("New Password");
                            changePassowrd.setSingleLine();
                            passwordUpdate.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressDialog.show();
                                    String newpassword = changePassowrd.getText().toString();
                                    if (newpassword.length() > 7) {
                                        firebaseUser.updatePassword(newpassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(Settings.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                                newPassword.setText("");
                                                progressDialog.cancel();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if (e instanceof FirebaseNetworkException)
                                                    Toast.makeText(Settings.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(Settings.this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                                                progressDialog.cancel();
                                            }
                                        });
                                    } else {
                                        progressDialog.cancel();
                                        Toast.makeText(Settings.this, "Password too short", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            passwordUpdate.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    newPassword.setText("");
                                    progressDialog.cancel();
                                }
                            });
                            passwordUpdate.create().show();
                        }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e instanceof FirebaseNetworkException)
                                Toast.makeText(Settings.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            else if(e instanceof FirebaseAuthInvalidUserException)
                                newPassword.setError("Invalid Credentials");
                            else
                                Toast.makeText(Settings.this, "Error", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
                else {
                    newPassword.setError("Enter valid password");
                    progressDialog.cancel();
                }
            }
        });
    }
}