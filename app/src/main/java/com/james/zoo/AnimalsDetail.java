package com.james.zoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 101716 on 2017/9/13.
 */

public class AnimalsDetail extends AppCompatActivity {
    private ArrayList<String> getMedData = new ArrayList<String>();
    private String imgURL, name ,classes, distribution, habitat, feature, diet;
    private String TAG = AnimalsDetail.class.getSimpleName();
    private TextView tv_name, tv_classes,tv_distribution,tv_habitat,tv_feature,tv_diet;
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        initView();


        Intent intent = getIntent();
        Bundle buldle = intent.getExtras();
        imgURL = buldle.getString("imgURL");
        name = buldle.getString("NameCh");
        classes = buldle.getString("Class");
        distribution = buldle.getString("Distribution");
        habitat = buldle.getString("Habitat");
        feature = buldle.getString("Feature");
        diet = buldle.getString("Diet");
       // Log.e(TAG,name +"... "+ classes);
        tv_name.setText(name);
        tv_classes.setText(classes);
        if(distribution.toString().equals("")){
            tv_distribution.setText("無資料");
        }else{
            tv_distribution.setText(distribution);
        }
        if(habitat.toString().equals("")){
            tv_habitat.setText("無資料");
        }else{
            tv_habitat.setText(habitat);
        }
        if(feature.toString().equals("")){
            tv_feature.setText("無資料");
        }else{
            tv_feature.setText(feature);
        }
        if(diet.toString().equals("")){
            tv_diet.setText("無資料");
        }else{
            tv_diet.setText(diet);
        }






        new DownloadImageTask((ImageView) findViewById(R.id.img_pic))
                .execute(imgURL);
    }

    public void initView(){
        imgView = (ImageView)findViewById(R.id.img_pic);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_classes = (TextView)findViewById(R.id.tv_classes);
        tv_distribution = (TextView)findViewById(R.id.tv_distribution);
        tv_habitat = (TextView)findViewById(R.id.tv_habitat);
        tv_feature = (TextView)findViewById(R.id.tv_feature);
        tv_diet = (TextView)findViewById(R.id.tv_diet);

    }


    public void backClick(View view){

        this.finish();
        overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
