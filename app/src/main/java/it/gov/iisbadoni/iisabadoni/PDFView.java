package it.gov.iisbadoni.iisabadoni;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PDFView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        Intent myIntent = getIntent();
        String url = myIntent.getStringExtra("url");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        WebView webView = (WebView) findViewById(R.id.pdf_view);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+url);
    }
}
