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

import com.example.netflix.R;

public class StepOne extends AppCompatActivity {
    TextView signin,textView;
    Button seeYourPlans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);
        getSupportActionBar().hide();
        signin=findViewById(R.id.signinstepone);
        seeYourPlans=findViewById(R.id.seeyourplans);
        textView=findViewById(R.id.step1of3);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StepOne.this,SignInActivity.class));
            }
        });
        seeYourPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StepOne.this,ChooseYourPlan.class));
            }
        });
        SpannableString st=new SpannableString("Step 1 of 3");
        StyleSpan boldspan1=new StyleSpan(Typeface.BOLD);
        StyleSpan boldspan2=new StyleSpan(Typeface.BOLD);
        st.setSpan(boldspan1,5,6,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(boldspan2,10,11,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(st);
    }
}