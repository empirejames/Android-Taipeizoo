package com.james.zoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.landptf.bean.BannerBean;
import com.landptf.view.BannerM;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 101716 on 2017/9/13.
 */

public class AnimalsDetail extends AppCompatActivity {
    private ArrayList<String> getMedData = new ArrayList<String>();
    private String Location, Geo, Video, imgURL1, imgURL2, imgURL3, imgURL4, name, classes, distribution, habitat, feature, diet;
    private String TAG = AnimalsDetail.class.getSimpleName();
    private TextView tv_name, tv_classes, tv_distribution, tv_habitat, tv_feature, tv_diet, tv_toptitlebar_name, tv_ZooMap, tv_voiceZoo;
    private ImageView imgView;
    private Button button;
    private List<BannerBean> bannerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F618803C89E1614E3394A55D5E7A756B").build(); //Nexus 5
        mAdView.loadAd(adRequest);
        getIntentData();
        initData();
        initView();
        tv_toptitlebar_name.setText(Location);
        // Log.e(TAG,name +"... "+ classes);
        tv_name.setText(name);
        if (classes.toString().equals("")) {
            tv_classes.setText("無資料");
        } else {
            tv_classes.setText(classes);
        }

        if (distribution.toString().equals("")) {
            tv_distribution.setText("無資料");
        } else {
            tv_distribution.setText(goodToRead(distribution));
        }
        if (habitat.toString().equals("")) {
            tv_habitat.setText("無資料");
        } else {
            tv_habitat.setText(goodToRead(habitat));
        }
        if (feature.toString().equals("")) {
            tv_feature.setText("無資料");
        } else {
            tv_feature.setText(goodToRead(feature));
        }
        if (diet.toString().equals("")) {
            tv_diet.setText("無資料");
        } else {
            tv_diet.setText(goodToRead(diet));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
            }
        });
        tv_ZooMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vDirectionUrl = "https://maps.google.com/maps?q=" + getGpsLocation();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(vDirectionUrl));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
        tv_voiceZoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "Voice1 : " + Video);
                if (Video.equals("")) {

                    Toast.makeText(AnimalsDetail.this, "此動物沒有影片", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Video)));
                }
            }
        });
        //new DownloadImageTask((ImageView) findViewById(R.id.img_pic)).execute(imgURL1);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        Bundle buldle = intent.getExtras();
        Location = buldle.getString("Location");
        Geo = buldle.getString("Geo"); //MULTIPOINT ((121.5831587 24.9971109))
        Video = buldle.getString("Video");
        imgURL1 = buldle.getString("imgURL1");
        imgURL2 = buldle.getString("imgURL2");
        imgURL3 = buldle.getString("imgURL3");
        imgURL4 = buldle.getString("imgURL4");
        name = buldle.getString("NameCh");
        classes = buldle.getString("Class");
        distribution = buldle.getString("Distribution");
        habitat = buldle.getString("Habitat");
        feature = buldle.getString("Feature");
        diet = buldle.getString("Diet");
    }

    private void initData() {
        bannerList = new ArrayList<>(4);
        BannerBean banner1, banner2, banner3, banner4;
        Log.e(TAG, !imgURL1.equals("") + "");
        if (!imgURL1.equals("")) {
            banner1 = new BannerBean("", imgURL1, "");
            bannerList.add(banner1);
        }
        if (!imgURL2.equals("")) {
            banner2 = new BannerBean("", imgURL2, "");
            bannerList.add(banner2);
        }
        if (!imgURL3.equals("")) {
            banner3 = new BannerBean("", imgURL3, "");
            bannerList.add(banner3);
        }
        if (!imgURL4.equals("")) {
            banner4 = new BannerBean("", imgURL4, "");
            bannerList.add(banner4);
        }
    }

    public void initView() {
        //imgView = (ImageView) findViewById(R.id.img_pic);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_classes = (TextView) findViewById(R.id.tv_classes);
        tv_distribution = (TextView) findViewById(R.id.tv_distribution);
        tv_habitat = (TextView) findViewById(R.id.tv_habitat);
        tv_feature = (TextView) findViewById(R.id.tv_feature);
        tv_diet = (TextView) findViewById(R.id.tv_diet);
        button = (Button) findViewById(R.id.btnReturn);
        tv_toptitlebar_name = (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_ZooMap = (TextView) findViewById(R.id.zooMap);
        tv_voiceZoo = (TextView) findViewById(R.id.voiceZoo);
        BannerM banner = (BannerM) findViewById(R.id.bm_banner);
        if (banner != null) {
            banner.setBannerBeanList(bannerList)
                    .setDefaultImageResId(R.drawable.default_image)
                    .setIndexPosition(BannerM.INDEX_POSITION_BOTTOM)
                    .setIndexColor(getResources().getColor(R.color.white))
                    .setIntervalTime(100)
                    .setOnItemClickListener(new BannerM.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Log.e("landptf", "position = " + position);
                        }
                    })
                    .show();
        }
    }


    public void backClick(View view) {
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

    public String getGpsLocation() {
        String result = ""; //MULTIPOINT ((121.5831587 24.9971109))
        String gps;
        String temp[];
        result = Geo.substring(Geo.indexOf("(") + 2, Geo.indexOf(")") - 1);
        temp = result.split(" ");
        gps = temp[1] + "," + temp[0];
        return gps;
    }

    public String goodToRead(String str) {
        String result = "";
        result = str.replace("。", "。" + "\n");
        return result;
    }
}
