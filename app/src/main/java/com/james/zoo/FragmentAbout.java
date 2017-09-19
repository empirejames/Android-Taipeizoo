package com.james.zoo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentAbout extends AppCompatActivity {
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    String TAG = FragmentAbout.class.getSimpleName();
    TextView tvContener;
    Button btn_evalution, btn_attention, btn_share, btn_feedback;
    ImageView iv_aboutLog;
    boolean mIsPremium = false;
    public String returnString;

    //about_logo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_about);
        btn_evalution = (Button) findViewById(R.id.btn_eva);
        btn_attention = (Button) findViewById(R.id.btn_attent);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_feedback = (Button) findViewById(R.id.btn_feedback);
        tvContener = (TextView) findViewById(R.id.tvContent);
        tvContener.setMovementMethod(new ScrollingMovementMethod());
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvContener.setText(getString(R.string.stockFYI));
            }
        });
        btn_evalution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDL = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.james.zoo"));
                startActivity(intentDL);
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "";
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFriend();
            }
        });
    }
    @Override
    public void onBackPressed() {
        FragmentAbout.this.finish();
        overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public void shareFriend(){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "台北動物園");
        share_intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_content));
        share_intent = Intent.createChooser(share_intent, "選擇分享");
        startActivity(share_intent);
    }

}
