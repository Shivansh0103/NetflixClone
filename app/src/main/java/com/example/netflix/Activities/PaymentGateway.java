package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.MainScreens.MainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import com.example.netflix.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {
    String planName, planCost, planFormatOfCost,userEmailId,userPassword,userID;
    EditText firstNameEditText,lastNameEditText,contactNumberEditText;
    Button startYourMembership;
    CheckBox agree;
    TextView change,textUrl,textView,costToSet,planNameToSet;
    String firstName,lastName,contactNumber,TAG="Payment Error";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date today,validityDate;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        getSupportActionBar().hide();
        Checkout.preload(this);
        Intent intent=getIntent();
        planName=intent.getStringExtra("Plan Name");
        planCost=intent.getStringExtra("Plan Cost");
        planFormatOfCost=intent.getStringExtra("Plan Cost Format");
        userEmailId=intent.getStringExtra("Email ID");
        userPassword=intent.getStringExtra("Password");
        firstNameEditText=findViewById(R.id.firstnamedittext);
        lastNameEditText=findViewById(R.id.lastnameedittext);
        contactNumberEditText=findViewById(R.id.contactNumberEditText);
        costToSet=findViewById(R.id.costToSet);
        planNameToSet=findViewById(R.id.planNameToSet);
        agree=findViewById(R.id.agree);
        startYourMembership=findViewById(R.id.startmem);
        textUrl=findViewById(R.id.toPutUrlText);
        textView=findViewById(R.id.step3of3);
        change=findViewById(R.id.change);
        costToSet.setText(planFormatOfCost);
        planNameToSet.setText(planName);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Calendar calendar=Calendar.getInstance();
        today= calendar.getTime();
        calendar.add(Calendar.MONTH,1);
        validityDate= calendar.getTime();
        firstName=firstNameEditText.getText().toString();
        lastName=lastNameEditText.getText().toString();
        contactNumber=contactNumberEditText.getText().toString();
        SpannableString st=new SpannableString("Step 3 of 3");
        StyleSpan boldSpan1=new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan2=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldSpan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldSpan2,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(st);
        SpannableString ss=new SpannableString("By checking the checkbox below, you agree to our Terms of Use, Privacy Statement, and that you are over 18. Netflix will automatically continue your membership and charge the monthly membership fee to your payment method until you cancel. You may cancel at any time to avoid future charges.");
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/termsofuse")));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        ClickableSpan clickableSpan1=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy")));
            }
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };
        ss.setSpan(clickableSpan,49,61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan1,63,80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textUrl.setText(ss);
        textUrl.setMovementMethod(LinkMovementMethod.getInstance());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentGateway.this,ChooseYourPlan.class));
            }
        });
        startYourMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName=firstNameEditText.getText().toString();
                lastName=lastNameEditText.getText().toString();
                contactNumber=contactNumberEditText.getText().toString();
                if(firstName.length()>0 && firstName.matches("[a-z A-Z]+") && contactNumber.length()==10 && agree.isChecked()) {
                    progressDialog=new ProgressDialog(PaymentGateway.this);
                    progressDialog.setMessage("Loading..");
                    progressDialog.show();
                    startPayment();
                }else
                {
                    if(firstName.length()==0 || !firstName.matches("[a-z A-Z]+"))
                        firstNameEditText.setError("Enter a valid first name");
                    if(lastName.length()>0 && !lastName.matches("[a-z A-Z]+"))
                        lastNameEditText.setError("Enter a valid last name");
                    if(contactNumber.length()!=10)
                        contactNumberEditText.setError("Enter a valid contact number");
                    if(!agree.isChecked())
                        Toast.makeText(PaymentGateway.this, "Please agree the policy", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(PaymentGateway.this, "Error", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }
        });
    }
    public void startPayment()
    {
        firstName=firstNameEditText.getText().toString();
        lastName=lastNameEditText.getText().toString();
        contactNumber=contactNumberEditText.getText().toString();
        Checkout checkout=new Checkout();
        checkout.setKeyID("rzp_test_jej0hZXpNtgWgZ");
        final Activity activity=this;
        String name=firstName+lastName;
        try {
            JSONObject options=new JSONObject();
            options.put("name",name);
            options.put("Description","APP PAYMENT");
            options.put("Currency","INR");
            String payment=planCost;
            double total=Double.parseDouble(payment);
            total=total*100;
            options.put("Amount",total);
            options.put("prefill.email",userEmailId);
            options.put("prefill.contact",contactNumber);
            checkout.open(activity,options);
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error Occurred",e);
        }
    }
    @Override
    public void onPaymentSuccess(String s) {
        firebaseAuth.createUserWithEmailAndPassword(userEmailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    userID=firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference=firebaseFirestore.collection("Users ").document(userID);
                    Map<String, Object> user=new HashMap<String,Object>();
                    user.put("Email",userEmailId);
                    user.put("First Name",firstName);
                    user.put("Last Name",lastName);
                    user.put("Plan",planName);
                    user.put("Contact Number",contactNumber);
                    user.put("Valid Date",validityDate);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(PaymentGateway.this, MainScreen.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e instanceof FirebaseNetworkException) {
                                Toast.makeText(PaymentGateway.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                            Toast.makeText(PaymentGateway.this, "Values Not Stored", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Unsuccessful", Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }
}