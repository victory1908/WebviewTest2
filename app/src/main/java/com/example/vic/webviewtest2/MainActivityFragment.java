package com.example.vic.webviewtest2;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    WebView myWebView;
    Handler mIncomingHandler = new Handler(new IncomingHandlerCallback());
    ProgressBar progressBar;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myWebView.saveState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragment = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        myWebView = (WebView)viewFragment.findViewById(R.id.webview);

        progressBar = (ProgressBar)viewFragment.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        // Reload the old WebView content
        if (savedInstanceState != null) {
            myWebView.restoreState(savedInstanceState);
        }else // Create the WebView
        {
            myWebView.loadUrl("https://www.nlb.gov.sg");
            //Webview Setting
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            myWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(false);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }
            });

            myWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(progress);

                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);

                    } else {
                        progressBar.setVisibility(View.VISIBLE);

                    }
                }
            });

            myWebView.setOnKeyListener(new View.OnKeyListener() {

                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
                        mIncomingHandler.sendEmptyMessage(1);
                        return true;
                    }
                    return false;
                }

            });
        }

        return viewFragment;
    }


    class IncomingHandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case 1: {
                    myWebView.goBack();
                }
                break;
            }

            return true;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//    //this method is used for adding menu items to the Activity
//    // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_firm_detail, menu);
//        return true;
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    //this method is used for handling menu items' events
    // Handle item selection
        switch (item.getItemId()) {

            case R.id.goBack:
                if(myWebView.canGoBack()) {
                    myWebView.goBack();
                }
                return true;

            case R.id.goForward:
                if(myWebView.canGoForward()) {
                    myWebView.goForward();
                }
                return true;

            case R.id.reLoad:
                myWebView.reload();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
