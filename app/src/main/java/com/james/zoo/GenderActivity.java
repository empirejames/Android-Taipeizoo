package com.james.zoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by James on 2017/8/30.
 */

public class GenderActivity extends AppCompatActivity {
    private ImageView img_question, img_secure, img_trafic, img_info, img_food;
    private String TAG = GenderActivity.class.getSimpleName();
    private String result;
    private String count;
    String animal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gender);
        img_question = (ImageView) findViewById(R.id.img_question);
        img_secure = (ImageView) findViewById(R.id.img_Secure);
        img_trafic = (ImageView) findViewById(R.id.img_trafic);
        img_info = (ImageView) findViewById(R.id.img_info);
        img_food = (ImageView) findViewById(R.id.img_food);

        result = getActivityValue();
        img_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("question");

            }
        });
        img_secure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("secure");
            }
        });
        img_trafic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("trafic");
            }
        });
        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("info");
            }
        });
        img_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity("food");
            }
        });
    }

    public void startActivity(String type) {
        Intent i = new Intent(GenderActivity.this, MainActivity.class);
        i.putExtra("type", result);
        if (type.equals("question")) {
            i.putExtra("types", "諮詢售票");
        } else if(type.equals("secure")){
            i.putExtra("types", "醫護急救");
        }  else if(type.equals("trafic")){
            i.putExtra("types", "交通服務");
        } else if(type.equals("info")){
            i.putExtra("types", "設施服務");
        }else{
            i.putExtra("types", "餐飲服務");
        }
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_in_left_2);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
    }

    public String getActivityValue() {
        Intent i = getIntent();
        String result = i.getStringExtra("type");
        return result;
    }


    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap ToChangeColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }
}
