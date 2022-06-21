package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.netflix.R;

public class FinishUpAccount extends AppCompatActivity {
    String planName, planCost, planFormatOfCost;
    TextView textView,signin;
    Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up_account);
        getSupportActionBar().hide();
        textView=findViewById(R.id.step1of3finish);
        signin=findViewById(R.id.signinstepone);
        continueButton=findViewById(R.id.continuebuttonfinish);
        Intent intent=getIntent();
        planName=intent.getStringExtra("Plan Name");
        planCost=intent.getStringExtra("Plan Cost");
        planFormatOfCost=intent.getStringExtra("Plan Cost Format");
        SpannableString st=new SpannableString("Step 1 of 3");
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(st);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinishUpAccount.this,SignInActivity.class));
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinishUpAccount.this, StepTwo.class);
                intent.putExtra("Plan Name",planName);
                intent.putExtra("Plan Cost",planCost);
                intent.putExtra("Plan Cost Format",planFormatOfCost);
                startActivity(intent);
            }
        });
    }
}