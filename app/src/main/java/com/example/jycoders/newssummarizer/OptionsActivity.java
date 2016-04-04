package com.example.jycoders.newssummarizer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class OptionsActivity extends AppCompatActivity {

    String url;
    public final static String URLS = "";
    public final static String CALLER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        url = intent.getStringExtra(ArticlesActivity.nURL);
    }

    public void toWeb (View v) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void toFull (View v) {
        Intent intent = new Intent(this, FullArticle.class);
        intent.putExtra(URLS, url);
        startActivity(intent);
    }

    public void toSearch (View v) {
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
    }

    public void toArticles (View v) {
        finish();
    }

    public void toSum (View v) {
        Intent intent = new Intent(this, SummaryActivity.class);
        intent.putExtra(URLS, url);
        startActivity(intent);
    }
}
