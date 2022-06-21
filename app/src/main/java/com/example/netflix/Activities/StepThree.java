package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.R;

public class StepThree extends AppCompatActivity {
    String planName, planCost, planFormatOfCost,userEmailId,userPassword;
    TextView signOut,textView;
    LinearLayout paymentlinearlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_step_three);
        signOut=findViewById(R.id.signoutstep3);
        textView=findViewById(R.id.step3);
        paymentlinearlayout=findViewById(R.id.paymentlinearlayout);
        Intent intent=getIntent();
        planName=intent.getStringExtra("Plan Name");
        planCost=intent.getStringExtra("Plan Cost");
        planFormatOfCost=intent.getStringExtra("Plan Cost Format");
        userEmailId=intent.getStringExtra("Email ID");
        userPassword=intent.getStringExtra("Password");
        SpannableString st=new SpannableString("Step 3 of 3");
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(st);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(StepThree.this,SignInActivity.class);
                startActivity(intent1);
            }
        });
        paymentlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StepThree.this, PaymentGateway.class);
                intent.putExtra("Plan Name",planName);
                intent.putExtra("Plan Cost",planCost);
                intent.putExtra("Plan Cost Format",planFormatOfCost);
                intent.putExtra("Email ID",userEmailId);
                intent.putExtra("Password",userPassword);
                startActivity(intent);
            }
        });
    }
}