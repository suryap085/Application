package com.india.viralindia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class HomeActivity extends AppCompatActivity {

    private WebView webView;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    //  private CoordinatorLayout coordinatorLayout;
    private MyWebChromeClient mWebChromeClient = null;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            showAlertDialog(this, "No Internet Connection",
                    "You don't have internet connection.", false);
            return;
        }

        webView = (WebView) findViewById(R.id.webView1);
        WebSettings settings = webView.getSettings();
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.setWebViewClient(new WebViewClient());
        settings.setJavaScriptEnabled(true);


       mAdView = (AdView) findViewById(R.id.startAppBanner);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

                AdRequest adRequest = new AdRequest.Builder()
                         .build();
                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest);
                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });

            }
        }, 300000);

        mWebChromeClient = new MyWebChromeClient();
        webView.setWebChromeClient(mWebChromeClient);
        startWebView("http://viralvideindia.blogspot.in/");


    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);


    }


    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                if (url.toLowerCase().startsWith("http")
                        || url.toLowerCase().startsWith("https")
                        || url.toLowerCase().startsWith("file")) {
                    view.loadUrl(url);
                } else {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.d("JSLogs", "WebView Error:" + e.getMessage());
                        ;
                    }
                }
                return (true);
            }

            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setMessage("Loading...");

                    progressDialog.setIndeterminateDrawable(getResources()
                            .getDrawable(R.drawable.customprogrssbar));
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();

                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        webView.loadUrl(url);


    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }

    }


    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        boolean returnValue = true;

        switch (itemId) {

            case R.id.menu_rate_us:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id="
                                        + "com.india.viralindia")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + "com.india.viralindia")));
                    }
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;
            case R.id.share:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    Intent sharingIntent = new Intent(
                            android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                            "Viral in India");

                    sharingIntent
                            .putExtra(
                                    android.content.Intent.EXTRA_TEXT,
                                    "https://play.google.com/store/apps/details?id=com.india.viralindia&hl=en");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;

            case R.id.more:
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://search?q=Surya+Prakash+Maurya")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + "com.india.viralindia")));
                    }
                } else {
                    showAlertDialog(this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

                break;

            default:
                returnValue = super.onOptionsItemSelected(item);
                break;
        }
        return returnValue;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        webView.onPause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);


        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
        {
            getSupportActionBar().hide();
        }
        else
        {
            getSupportActionBar().show();
        }

    }
}
