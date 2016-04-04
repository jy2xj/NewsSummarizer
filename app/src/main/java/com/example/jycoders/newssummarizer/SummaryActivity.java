package com.example.jycoders.newssummarizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

public class SummaryActivity extends AppCompatActivity {

    String url;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
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
            progressDialog = new ProgressDialog(SummaryActivity.this);
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

            TextView txtTitle = (TextView) findViewById(R.id.sumTitle);
            txtTitle.setText(title);
            //progressDialog.dismiss();
        }
    }


    //Returns the article summary
    private class Article extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SummaryActivity.this);
            progressDialog.setTitle("Description");
            progressDialog.setMessage("Loading... This might take a bit...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();

                Elements description = document.select("article");
                desc = description.text();

                String keywords = document.select("meta[name=keywords]").first().attr("content");

                List<String> keyList = Arrays.asList(keywords.split(","));

                String sentences[] = SentenceDetect2(desc);
                int[] count = new int[sentences.length];
                for (int i = 0; i < count.length; i++) {
                    count[i] = Tokenize2(sentences, keyList, i);
                }
                int number = sentences.length*3/10;
                int current = 0;
                ArrayList<String> summed = new ArrayList<String>();
                for (int j = 0; j < count.length; j++) {
                    if (current <= number && count[j] > 0) {
                        summed.add(sentences[j]);
                        current++;
                    }
                }

                String summedString = "";
                for (String s : summed)
                {
                    summedString += s + "\t";
                }

                desc = summedString;

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TextView txtDesc = (TextView) findViewById(R.id.sumArt);
            txtDesc.setText(desc);
            txtDesc.setMovementMethod(new ScrollingMovementMethod());
            progressDialog.dismiss();
        }
    }

    public String[] SentenceDetect2(String paragraph) throws InvalidFormatException,
            IOException {

        // always start with a model, a model is learned from training data
        AssetManager assetManager = getResources().getAssets();
        InputStream is = assetManager.open("en-sent.bin");
        SentenceModel model = new SentenceModel(is);
        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String sentences[] = sdetector.sentDetect(paragraph);

        is.close();
        return sentences;
    }

    public int Tokenize2(String[] sentences, List<String> Keys, int s) throws InvalidFormatException, IOException {
        AssetManager assetManager = getResources().getAssets();
        InputStream is = assetManager.open("en-token.bin");

        TokenizerModel model = new TokenizerModel(is);

        Tokenizer tokenizer = new TokenizerME(model);

        String tokens[] = tokenizer.tokenize(sentences[s]);

        int count = 0;

        for (String a : tokens) {
            for (int i = 0; i < Keys.size(); i++) {
                if (a.contains(Keys.get(i)) || (Keys.get(i)).contains(a)) {
                    count++;
                }
            }
        }
        is.close();
        return count;
    }

}
