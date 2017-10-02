package com.james.zoo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class WebViewActivity extends Activity {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    WebView webView;
    String webViewUrl, webName, webAcceptnum, webType;
    WebSettings webSettings;
    String mailUrl = "tcapoa8@mail.taipei.gov.tw";
    String vDirectionUrl;
    TextView txt_Title;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        txt_Title = (TextView) findViewById(R.id.tv_toptitlebar_name);
        Bundle extras = getIntent().getExtras();
        webViewUrl = extras.getString("URL");
        webName = extras.getString("Location");
        txt_Title.setText(webName);
        String data = "<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width=" + width + ", initial-scale=0.65 \" /></head>";
        data = data + "<body><center><img width=\"" + width + "\" src=\"" + webViewUrl + "\" /></center></body></html>";
        webView = (WebView) findViewById(R.id.webview);
        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //setZoomControlGone(webView);

        webView.loadData(data, "text/html", null);
        //webView.setBackgroundColor(0x00000000);
        // webView.setInitialScale(180);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        this.webView.setWebChromeClient(new WebChromeClient());
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Hashtable<String, String> map = new Hashtable<String, String>();
                view.loadUrl(url, map);
                return true;
            }
        });
    }
    public void backClick(View view) {
        this.finish();
        overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.slide_in_right_1, R.anim.slide_in_right_2);
    }

    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
