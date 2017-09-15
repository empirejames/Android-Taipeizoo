package com.james.zoo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Animals> mGridData;
    private ArrayList<String> mDetailData;
    private Animals animals;
    private GridView mGridView;
    private ImageAdapterGridView mGridAdapter;
    protected ProgressDialog dialogSMS;
    private Bundle bundle = new Bundle();
    TinyDB tinydb;
    String alreadyGj;
    private String Zoo_URL = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=a3e2b221-75e0-45c1-8f97-75acbd43d613";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinydb = new TinyDB(MainActivity.this);
        alreadyGj = tinydb.getString("GJ");
        if(alreadyGj.equals("")){
            alreadyGj="true";
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridData = new ArrayList<>();
        mGridAdapter = new ImageAdapterGridView(this, R.layout.grid_item, mGridData);
        mGridView.setAdapter(mGridAdapter);
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
        startDialog();
        new AsyncHttpTask().execute(Zoo_URL + "&q=企鵝館");
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            getData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.e(TAG, "result: " + result);
            mGridAdapter.setGridData(mGridData);
            //Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            dialogSMS.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mGridData.clear();
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
                    //Log.e(TAG, " A_Interpretation : " + A_Interpretation);
                    mGridData.add(new Animals(A_Name_Ch, A_Location, A_Geo, A_Phylum, A_Class, A_Order, A_Family, A_Distribution
                            , A_Habitat, A_Feature, A_Diet, A_Interpretation, A_Pic01_ALT, A_Pic01_URL, A_Pic02_ALT, A_Pic02_URL, A_Pic03_ALT, A_Pic03_URL
                            , A_Pic04_ALT, A_Pic04_URL, A_Voice01_ALT, A_Voice01_URl, A_Voice02_ALT, A_Voice02_URL, A_Voice03_ALT, A_Voice03_URL, A_Vedio_URL));
                }
                //Log.e(TAG, " name : " + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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

    public void exitDialog(){
        if(alreadyGj.toString().equals("true")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("◎ 給個 5 星好評\n◎ 一同愛護動物\n◎ 可回饋問題讓我們知道")
                    .setTitle("感恩您的使用")
                    .setCancelable(false)
                    .setPositiveButton("讚", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intentDL = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.james.animalshome"));
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
        }else{
            MainActivity.this.finish();
        }
    }
}
