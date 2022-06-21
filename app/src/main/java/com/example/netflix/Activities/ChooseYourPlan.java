package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.netflix.R;

public class ChooseYourPlan extends AppCompatActivity {
    TextView signin;
    Button continueButton;
    RadioButton radioBasic, radioStandard, radioPremium;
    String planName, planCost, planFormatOfCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_plan);
        getSupportActionBar().hide();
        signin = findViewById(R.id.signinstepone);
        continueButton = findViewById(R.id.continuebuttonchooseplan);
        radioBasic = findViewById(R.id.radiobuttonforbasic);
        radioStandard = findViewById(R.id.radiobuttonforstandard);
        radioPremium = findViewById(R.id.radiobuttonforpremium);
        radioBasic.setOnCheckedChangeListener(new Radio_Check());
        radioStandard.setOnCheckedChangeListener(new Radio_Check());
        radioPremium.setOnCheckedChangeListener(new Radio_Check());
        radioPremium.setChecked(true);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseYourPlan.this, FinishUpAccount.class);
                intent.putExtra("Plan Name",planName);
                intent.putExtra("Plan Cost",planCost);
                intent.putExtra("Plan Cost Format",planFormatOfCost);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseYourPlan.this, SignInActivity.class));
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
}