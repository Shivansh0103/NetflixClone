package com.example.netflix.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.netflix.Adapters.ViewPagerAdapter;
import com.example.netflix.R;

public class SwipeScreen extends AppCompatActivity {
    TextView signin,help,privacy;
    Button getStarted;
    ViewPager swipe;
    LinearLayout sliderdots;
    private int dotcount;
    private ImageView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_screen);
        getSupportActionBar().hide();
        help=findViewById(R.id.helptextview);
        signin=findViewById(R.id.signintextview);
        privacy=findViewById(R.id.privacytextview);
        getStarted=findViewById(R.id.getstarted);
        swipe=findViewById(R.id.viewpagerswipescreen);
        sliderdots=findViewById(R.id.sliderdots);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        swipe.setAdapter(viewPagerAdapter);
        dotcount=viewPagerAdapter.getCount();
        dots=new ImageView[dotcount];
        for(int i=0;i<dotcount;i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.inactivedots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            sliderdots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.activedots));
            swipe.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    for(int i=0;i<dotcount;i++)
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.inactivedots));
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.activedots));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SwipeScreen.this,SignInActivity.class);
                    startActivity(intent);
                }
            });
            privacy.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/legal/privacy"));
                startActivity(intent);
                }
            });
            help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.netflix.com/en/"));
                startActivity(intent);
            }
            });
            getStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SwipeScreen.this,StepOne.class);
                    startActivity(intent);
                }
            });
    }
}