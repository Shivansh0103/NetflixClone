package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentOverdue extends AppCompatActivity {

    TextView signin;
    Button payButton;
    RadioButton radioBasic, radioStandard, radioPremium;
    String planName, planCost, planFormatOfCost,iFirstName,iLastName,iUID,iEmail,iContact,TAG="Payment Error";
    Date today,validityDate;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        getSupportActionBar().hide();
        Intent i=getIntent();
        iFirstName=i.getStringExtra("firstName");
        iLastName=i.getStringExtra("lastname");
        iUID=i.getStringExtra("uid");
        iEmail=i.getStringExtra("Email");
        iContact=i.getStringExtra("Contact number");
        signin = findViewById(R.id.signinstepone);
        payButton = findViewById(R.id.paybuttonoverdue);
        radioBasic = findViewById(R.id.radiobuttonforbasic);
        radioStandard = findViewById(R.id.radiobuttonforstandard);
        radioPremium = findViewById(R.id.radiobuttonforpremium);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar calendar=Calendar.getInstance();
        today= calendar.getTime();
        calendar.add(Calendar.MONTH,1);
        validityDate= calendar.getTime();
        radioBasic.setOnCheckedChangeListener(new PaymentOverdue.Radio_Check());
        radioStandard.setOnCheckedChangeListener(new PaymentOverdue.Radio_Check());
        radioPremium.setOnCheckedChangeListener(new PaymentOverdue.Radio_Check());
        radioPremium.setChecked(true);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentOverdue.this, SignInActivity.class));
            }
        });
    }

    class Radio_Check implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                if (compoundButton.getId() == R.id.radiobuttonforbasic) {
                    planName = "Basic";
                    planCost = "349";
                    planFormatOfCost = "₹349/month";
                    radioStandard.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if (compoundButton.getId() == R.id.radiobuttonforstandard) {
                    planName = "Standard";
                    planCost = "649";
                    planFormatOfCost = "₹649/month";
                    radioBasic.setChecked(false);
                    radioPremium.setChecked(false);
                }
                if (compoundButton.getId() == R.id.radiobuttonforpremium) {
                    planName = "Premium";
                    planCost = "899";
                    planFormatOfCost = "₹899/month";
                    radioStandard.setChecked(false);
                    radioBasic.setChecked(false);
                }
            }
        }
    }
    public void startPayment()
    {
        progressDialog=new ProgressDialog(PaymentOverdue.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        Checkout checkout=new Checkout();
        checkout.setKeyID("rzp_test_jej0hZXpNtgWgZ");
        final Activity activity=this;
        String name=iFirstName+iLastName;
        try {
            JSONObject options=new JSONObject();
            options.put("name",name);
            options.put("Description","APP PAYMENT");
            options.put("Currency","INR");
            String payment=planCost;
            double total=Double.parseDouble(payment);
            total=total*100;
            options.put("Amount",total);
            options.put("prefill.email",iEmail);
            options.put("prefill.contact",iContact);
            checkout.open(activity,options);
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error Occurred",e);
        }
    }

    public void onPaymentSuccess(String s) {
                    DocumentReference documentReference=firebaseFirestore.collection("Users ").document(iUID);
                    Map<String, Object> user=new HashMap<String,Object>();
                    user.put("Email",iEmail);
                    user.put("First Name",iFirstName);
                    user.put("Last Name",iLastName);
                    user.put("Plan",planName);
                    user.put("Contact Number",iContact);
                    user.put("Valid Date",validityDate);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(PaymentOverdue.this, MainScreen.class));
                            progressDialog.cancel();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e instanceof FirebaseNetworkException) {
                                Toast.makeText(PaymentOverdue.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            }
                            Toast.makeText(PaymentOverdue.this, "Values Not Stored", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
    }

    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }
}