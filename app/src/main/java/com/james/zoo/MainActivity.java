package com.james.zoo;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Animals> mGridData;
    private ArrayList<EquipmentItem> mEquipData;
    private EquipmentItem equItem;
    private Animals animals;
    private GridView mGridView;
    private ListView listView;
    private ImageAdapterGridView mGridAdapter;
    private EquipmentAdapter mEquipmentAdapter;
    protected ProgressDialog dialogSMS;
    private Bundle bundle = new Bundle();
    private String types;
    TinyDB tinydb;
    String alreadyGj, result;
    Double longitude, latitude;
    private LocationManager lms;
    final private int REQUEST_CODE_ASK_ALL = 122;
    private String Zoo_URL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=a3e2b221-75e0-45c1-8f97-75acbd43d613";
    private String equit_URL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=5048d475-7642-43ee-ac6f-af0a368d63bf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinydb = new TinyDB(MainActivity.this);
        alreadyGj = tinydb.getString("GJ");
        types = getActivityValue();
        //Log.e(TAG, "Types ... " + types);
        if (alreadyGj.equals("")) {
            alreadyGj = "true";
        }
        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setDisplay(Display.DIALOG)
                .showAppUpdated(false)  // 若已是最新版本, 則 true: 仍會提示之, false: 不會提示之
                .start();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("F618803C89E1614E3394A55D5E7A756B").build(); //Nexus 5
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.lv_data);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridData = new ArrayList<>();
        mEquipData = new ArrayList<>();
        mGridAdapter = new ImageAdapterGridView(this, R.layout.grid_item, mGridData);
        mEquipmentAdapter = new EquipmentAdapter(this, R.layout.activity_equiment_layout, mEquipData);

        mGridView.setAdapter(mGridAdapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String[] gps = getGpsLocation(mEquipData.get(position).getS_gps());
                String vDirectionUrl = "http://maps.google.com/?q=" + gps[1] + "," + gps[0];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(vDirectionUrl));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });


        mGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                animals = mGridData.get(position);
                bundle.putString("Location", animals.getLocation());
                bundle.putString("Geo", animals.getGeo());
                bundle.putString("Video", animals.getVideo());
                bundle.putString("imgURL", animals.getPic1_URL());
                bundle.putString("NameCh", animals.getName_Ch());
                bundle.putString("Class", animals.getPhylum() + " / " + animals.getClasses()
                        + " / " + animals.getOrder() + " / " + animals.getFamily());
                bundle.putString("Distribution", animals.getDistribution());
                bundle.putString("Habitat", animals.getHabitat());
                bundle.putString("Feature", animals.getFeature());
                bundle.putString("Diet", animals.getDiet());
                Intent i = new Intent(MainActivity.this, AnimalsDetail.class);
                i.putExtras(bundle);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_in_left_2);
                //Toast.makeText(AnimalActivity.this, animals.getTid()+" . " + animals.getWebId(), Toast.LENGTH_SHORT).show();
            }
        });
        if (types == null) {
            startDialog();
            new AsyncHttpTask().execute(Zoo_URL + "&q=企鵝館");
        } else {
            checkPermission();
        }

        //drawer.openDrawer(R.id.drawer_layout);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), FragmentAbout.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_in_left_2);
            return true;
        } else if (id == R.id.action_services) {
            Intent i = new Intent(getApplicationContext(), GenderActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_in_left_2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mGridAdapter.clear();
        startDialog();
        types = null;
        if (id == R.id.nav_penguin) {// 企鵝館
            new AsyncHttpTask().execute(Zoo_URL + "&q=企鵝館");
        } else if (id == R.id.nav_bird) {// 鳥園區
            new AsyncHttpTask().execute(Zoo_URL + "&q=鳥園區");
        } else if (id == R.id.nav_NoBear) {// 無尾熊館
            new AsyncHttpTask().execute(Zoo_URL + "&q=無尾熊館");
        } else if (id == R.id.nav_panda) {// 大貓熊館
            new AsyncHttpTask().execute(Zoo_URL + "&q=大貓熊館");
        } else if (id == R.id.nav_childAnmals) { // 兒童動物區
            new AsyncHttpTask().execute(Zoo_URL + "&q=兒童動物區");
        } else if (id == R.id.nav_desert) { // 沙漠動物區
            new AsyncHttpTask().execute(Zoo_URL + "&q=沙漠動物區");
        } else if (id == R.id.nav_warmAnimals) { // 溫帶動物區
            new AsyncHttpTask().execute(Zoo_URL + "&q=溫帶動物區");
        } else if (id == R.id.nav_australia) { // 澳洲動物區
            new AsyncHttpTask().execute(Zoo_URL + "&q=澳洲動物區");
        } else if (id == R.id.nav_afreca) { // 非洲動物區
            new AsyncHttpTask().execute(Zoo_URL + "&q=非洲動物區");
        } else if (id == R.id.nav_TaiwanAnimals) { // 台灣動物區
            new AsyncHttpTask().execute(Zoo_URL + "&q=台灣動物區");
        } else if (id == R.id.nav_Asia) { // 亞洲熱帶雨林區
            new AsyncHttpTask().execute(Zoo_URL + "&q=亞洲熱帶雨林區");
        } else if (id == R.id.nav_insect) { // 兩棲爬蟲動物館
            new AsyncHttpTask().execute(Zoo_URL + "&q=兩棲爬蟲動物館");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... urls) {
            Integer result = 0;
            if (types != null) {
                getEquit(urls[0]);
            } else {
                getData(urls[0]);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (types != null) {
                DistanceSort(mEquipData);
                listView.setAdapter(mEquipmentAdapter);
                mGridView.setVisibility(View.GONE);
            } else {
                mGridAdapter.setGridData(mGridData);
                listView.setVisibility(View.GONE);
            }
            //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            dialogSMS.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mGridData.clear();
            mEquipData.clear();
            mGridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);

        }
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

    public void getData(String url) {
        try {
            String jsonStr = Jsoup.connect(url).ignoreContentType(true).execute().body();
            if (jsonStr.indexOf("{") != -1) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject userDetails = jsonObj.getJSONObject("result");
                JSONArray data = userDetails.getJSONArray("results");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    String A_Name_Ch = jsonObject.getString("A_Name_Ch");
                    String A_Geo = jsonObject.getString("A_Geo");
                    String A_Location = jsonObject.getString("A_Location");
                    String A_Phylum = jsonObject.getString("A_Phylum");
                    String A_Class = jsonObject.getString("A_Class");
                    String A_Order = jsonObject.getString("A_Order");
                    String A_Family = jsonObject.getString("A_Family");
                    String A_Distribution = jsonObject.getString("A_Distribution");
                    String A_Habitat = jsonObject.getString("A_Habitat");
                    String A_Feature = jsonObject.getString("A_Feature");
                    String A_Diet = jsonObject.getString("A_Diet");
                    String A_Interpretation = jsonObject.getString("A_Interpretation");
                    String A_Pic01_ALT = jsonObject.getString("A_Pic01_ALT");
                    String A_Pic01_URL = jsonObject.getString("A_Pic01_URL");
                    String A_Pic02_ALT = jsonObject.getString("A_Pic02_ALT");
                    String A_Pic02_URL = jsonObject.getString("A_Pic02_URL");
                    String A_Pic03_ALT = jsonObject.getString("A_Pic03_ALT");
                    String A_Pic03_URL = jsonObject.getString("A_Pic03_URL");
                    String A_Pic04_ALT = jsonObject.getString("A_Pic04_ALT");
                    String A_Pic04_URL = jsonObject.getString("A_Pic04_URL");
                    String A_Voice01_ALT = jsonObject.getString("A_Voice01_ALT");
                    String A_Voice01_URl = jsonObject.getString("A_Voice01_URL");
                    String A_Voice02_ALT = jsonObject.getString("A_Voice02_ALT");
                    String A_Voice02_URL = jsonObject.getString("A_Voice02_URL");
                    String A_Voice03_ALT = jsonObject.getString("A_Voice03_ALT");
                    String A_Voice03_URL = jsonObject.getString("A_Voice03_URL");
                    String A_Vedio_URL = jsonObject.getString("A_Vedio_URL");
                    mGridData.add(new Animals(A_Name_Ch, A_Location, A_Geo, A_Phylum, A_Class, A_Order, A_Family, A_Distribution
                            , A_Habitat, A_Feature, A_Diet, A_Interpretation, A_Pic01_ALT, A_Pic01_URL, A_Pic02_ALT, A_Pic02_URL, A_Pic03_ALT, A_Pic03_URL
                            , A_Pic04_ALT, A_Pic04_URL, A_Voice01_ALT, A_Voice01_URl, A_Voice02_ALT, A_Voice02_URL, A_Voice03_ALT, A_Voice03_URL, A_Vedio_URL));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getEquit(String url) {
        try {
            String jsonStr = Jsoup.connect(url).ignoreContentType(true).execute().body();
            if (jsonStr.indexOf("{") != -1) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject userDetails = jsonObj.getJSONObject("result");
                JSONArray data = userDetails.getJSONArray("results");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    String S_Title = jsonObject.getString("S_Title");
                    String S_Summary = jsonObject.getString("S_Summary");
                    String S_Location = jsonObject.getString("S_Location");
                    String S_Geo = jsonObject.getString("S_Geo");
                    String S_Pic01_URL = jsonObject.getString("S_Pic01_URL");
                    String distanceFin = DistanceText(Distance(Double.parseDouble(getGpsLocation(S_Geo)[0]), Double.parseDouble(getGpsLocation(S_Geo)[1]), longitude, latitude));
                    String[] distanceKil = distanceFin.split("公");
                    mEquipData.add(new EquipmentItem(S_Title, S_Summary, S_Location, S_Geo, distanceKil[0], distanceKil[1], S_Pic01_URL));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void DistanceSort(ArrayList<EquipmentItem> equip) {
        //Log.e(TAG, "Sort");
        Collections.sort(equip, new Comparator<EquipmentItem>() {

            @Override
            public int compare(EquipmentItem equip1, EquipmentItem equip2) {

                double a = Double.parseDouble(equip1.getS_geo());
                double b = Double.parseDouble(equip2.getS_geo());
                //Log.e(TAG,a + " V.S " + b);
                return a < b ? -1 : 1;
            }
        });
    }

    public void checkPermission() {
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
        }
    }

    private void locationServiceInitial() {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);    //取得系統定位服務
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    || (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    ) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                }, REQUEST_CODE_ASK_ALL);
                finish();
            } else {
                Location location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);    //使用GPS定位座標
                getLocation(location);
                startDialog();
                Log.e(TAG, "ELSE : " + equit_URL + "&q=" + types);
                new AsyncHttpTask().execute(equit_URL + "&q=" + types);
            }
        }
    }

    private void getLocation(Location location) {    //將定位資訊顯示在畫面中
        if (location != null) {
            longitude = location.getLongitude();    //取得經度
            latitude = location.getLatitude();    //取得緯度
            //Log.e(TAG, longitude + " .. " + latitude);
        } else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }

    public double Distance(double longitude1, double latitude1, double longitude2, double latitude2) {
        //Log.e(TAG, longitude1 + "." + latitude1 + " V.S. " + longitude2 + "." + latitude2);
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;
        DistanceText(distance);
        return distance;
    }

    private String DistanceText(double distance) {
        if (distance < 1000) {
            //Log.e(TAG, String.valueOf((int) distance) + "公尺");
            return String.valueOf((int) distance) + "公尺";
        } else {
            //Log.e(TAG, new DecimalFormat("#.00").format(distance / 1000) + "公里");
            return new DecimalFormat("#.00").format(distance / 1000) + "公里";
        }
    }

    public void startDialog() {
        dialogSMS = ProgressDialog.show(this, null, null, false, true);
        dialogSMS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSMS.setContentView(R.layout.progressbar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Thread.sleep(30 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void openWebView(String url, String name) {
        Intent intentWV = new Intent(MainActivity.this, WebViewActivity.class);
        intentWV.putExtra("URL", url);
        startActivity(intentWV);
        overridePendingTransition(R.anim.slide_in_left_1, R.anim.slide_in_left_2);
    }

    public String getActivityValue() {
        Intent i = getIntent();
        result = i.getStringExtra("types");
        Log.e(TAG, "result:  " + result);
        return result;
    }

    public String[] getGpsLocation(String Geo) {
        String result = ""; //MULTIPOINT ((121.5831587 24.9971109))
        String gps[];
        String temp[];
        result = Geo.substring(Geo.indexOf("(") + 2, Geo.indexOf(")") - 1);
        temp = result.split(" ");
        gps = temp;
        //gps = temp[1] + ","+temp[0];
        return gps;
    }

    public void exitDialog() {
        if (alreadyGj.toString().equals("true")) {
            if (result == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("◎ 給個 5 星好評\n◎ 一同愛護動物\n◎ 可回饋問題讓我們知道")
                        .setTitle("感恩您的使用")
                        .setCancelable(false)
                        .setPositiveButton("讚", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intentDL = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.james.zoo"));
                                startActivity(intentDL);
                            }
                        })
                        .setNegativeButton("已經讚囉", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNeutralButton("不再提示", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tinydb.putString("GJ", "false");
                                MainActivity.this.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                MainActivity.this.finish();
            }
        }else{
            MainActivity.this.finish();
        }
    }
}
