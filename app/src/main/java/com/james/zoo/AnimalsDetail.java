package com.james.zoo;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.landptf.bean.BannerBean;
import com.landptf.view.BannerM;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 101716 on 2017/9/13.
 */

public class AnimalsDetail extends AppCompatActivity {
    private ArrayList<Animals> getAniData = new ArrayList<Animals>();
    private Animals animals;
    private String Location, Geo, Video, imgURL1, imgURL2, imgURL3, imgURL4, name, classes, distribution, habitat, feature, diet;
    private String TAG = AnimalsDetail.class.getSimpleName();
    private TextView tv_name, tv_classes, tv_distribution, tv_habitat, tv_feature, tv_diet, tv_toptitlebar_name;
    private Button tv_ZooMap, tv_voiceZoo, tv_voiceZoo1;
    private ImageView imgView , img_dialog;
    private Button button ;
    private List<BannerBean> bannerList;
    private TextToSpeech mTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("7EC78E2E44AAFFCB85B0B0A916A71418").build(); //Nexus 5
//        mAdView.loadAd(adRequest);
        getIntentData();
        initData();
        initView();
        initializeEngine();
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
        tv_voiceZoo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTts.speak(name +"，"+ habitat + feature, TextToSpeech.QUEUE_FLUSH, null);
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
    private void initializeEngine() {
        mTts = new TextToSpeech(this, mInitListener);
    }
    private final TextToSpeech.OnInitListener mInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = mTts.setLanguage(Locale.CHINESE);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Language is not available.");
                } else {
                    tv_voiceZoo1.setEnabled(true);
                }
            } else {
                // Initialization failed.
                Log.e(TAG, "Could not initialize TextToSpeech.");
            }
        }
    };

    public void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_classes = (TextView) findViewById(R.id.tv_classes);
        tv_distribution = (TextView) findViewById(R.id.tv_distribution);
        tv_habitat = (TextView) findViewById(R.id.tv_habitat);
        tv_feature = (TextView) findViewById(R.id.tv_feature);
        tv_diet = (TextView) findViewById(R.id.tv_diet);
        button = (Button) findViewById(R.id.btnReturn);
        tv_toptitlebar_name = (TextView) findViewById(R.id.tv_toptitlebar_name);
        tv_ZooMap = (Button) findViewById(R.id.btn_zooMap);
        tv_ZooMap.getBackground().setAlpha(0);
        tv_voiceZoo = (Button) findViewById(R.id.btn_voiceZoo);
        tv_voiceZoo.getBackground().setAlpha(0);
        tv_voiceZoo1 = (Button) findViewById(R.id.btn_voiceZoo1);
        tv_voiceZoo1.getBackground().setAlpha(0);
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
                            Log.e(TAG, "position = " + position);
                            String tmpUrl = "";
                            if (position == 0) {
                                tmpUrl = imgURL1;
                            } else if (position == 1) {
                                tmpUrl = imgURL2;
                            } else if (position == 2) {
                                tmpUrl = imgURL3;
                            } else if (position == 3) {
                                tmpUrl = imgURL4;
                            }
                            openWebView(tmpUrl);
                        }
                    })
                    .show();
        }
    }
    private void openWebView(String url) {
        Intent intentWV = new Intent(AnimalsDetail.this, WebViewActivity.class);
        intentWV.putExtra("URL", url);
        intentWV.putExtra("Location", Location);
        startActivity(intentWV);
        overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_in_left_2);
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
