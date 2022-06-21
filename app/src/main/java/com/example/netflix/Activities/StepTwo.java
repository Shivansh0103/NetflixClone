package com.example.netflix.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.util.Timer;
import java.util.TimerTask;

public class StepTwo extends AppCompatActivity {
    String planName, planCost, planFormatOfCost,userEmailId,userPassword;
    TextView signin,textView;
    Button continueButton;
    ProgressBar progressBar;
    EditText emailIdUser,passwordUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    Boolean x;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        planName=intent.getStringExtra("Plan Name");
        planCost=intent.getStringExtra("Plan Cost");
        planFormatOfCost=intent.getStringExtra("Plan Cost Format");
        signin=findViewById(R.id.signinstepone);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBarstep2);
        continueButton=findViewById(R.id.buttonstep2);
        emailIdUser=findViewById(R.id.emailedittextstep2);
        passwordUser=findViewById(R.id.passwordtextstep2);
        textView=findViewById(R.id.step2of3);
        progressBar.setVisibility(View.GONE);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StepTwo.this,SignInActivity.class));
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmailId=emailIdUser.getText().toString();
                userPassword=passwordUser.getText().toString();
                x=true;
                if(userEmailId.length()<10||!userEmailId.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
                    emailIdUser.setError("Enter a valid Email Id");
                    x=false;
                }
                if(userPassword.length()<8)
                {
                    passwordUser.setError("Password too short");
                    x=false;
                }
                if(x)
                {
                    progressDialog=new ProgressDialog(StepTwo.this);
                    progressDialog.setMessage("Loading..");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    firebaseAuth.signInWithEmailAndPassword(userEmailId,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                x=false;
                                Toast.makeText(StepTwo.this, "Please Enter via main login screen", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(StepTwo.this,SignInActivity.class));
                                finish();
                            }
                            else
                            {
                                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                {
                                    emailIdUser.setError("Email ID is already registered");
                                    x=false;
                                    progressDialog.cancel();
                                }
                                if(task.getException() instanceof FirebaseNetworkException) {
                                    Toast.makeText(StepTwo.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    x=false;
                                    progressDialog.cancel();
                                }
                            }
                            if(userEmailId.length()>9&&userPassword.length()>7&&userEmailId.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")&&x)
                            {
                                Intent intent=new Intent(StepTwo.this, StepThree.class);
                                intent.putExtra("Plan Name",planName);
                                intent.putExtra("Plan Cost",planCost);
                                intent.putExtra("Plan Cost Format",planFormatOfCost);
                                intent.putExtra("Email ID",userEmailId);
                                intent.putExtra("Password",userPassword);
                                startActivity(intent);
                                progressDialog.cancel();
                            }
                        }
                    });
                }
            }
        });
        SpannableString st=new SpannableString("Step 2 of 3");
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(st);
    }
    public void progress()
    {
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                counter++;
                progressBar.setProgress(counter);
                if(counter==1000)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask,0,100);
    }
}