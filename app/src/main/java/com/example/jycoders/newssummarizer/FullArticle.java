package com.example.jycoders.newssummarizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class FullArticle extends AppCompatActivity {

    String url;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        url = intent.getStringExtra(OptionsActivity.URLS);
        new Title().execute();
        new Article().execute();
    }

    //Returns the Title of the article
    private class Title extends AsyncTask<Void, Void, Void> {
        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FullArticle.this);
            progressDialog.setTitle("Title");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document document = Jsoup.connect(url).get();
                title = document.title();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView txtTitle = (TextView) findViewById(R.id.titletxt);
            txtTitle.setText(title);
            //progressDialog.dismiss();
        }
    }

    //Returns the article text
    private class Article extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FullArticle.this);
            progressDialog.setTitle("Description");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements description = document.select("article");
                desc = description.text();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView txtDesc = (TextView) findViewById(R.id.arttxt);
            txtDesc.setText(desc);
            txtDesc.setMovementMethod(new ScrollingMovementMethod());
            progressDialog.dismiss();
        }
    }

}
