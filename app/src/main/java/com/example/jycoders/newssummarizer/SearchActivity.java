package com.example.jycoders.newssummarizer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    public final static String URL = "asdas";
    public final static String CALLER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView txtTitle = (TextView) findViewById(R.id.titletxt);
        txtTitle.setTextColor(Color.RED);

    }

    //Goes to OptionsActivity.class if valid url. If not, an message will pop out.
    public void goURL (View view) {
        //Intent intent = new Intent(this, );
        //startActivity(intent);
        EditText editText = (EditText) findViewById(R.id.edit_url);
        String url = editText.getText().toString();
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            Toast.makeText(this, "Invalid URL. Please enter again", Toast.LENGTH_SHORT).show();
        }
        else {
           /* Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);*/
            Intent intent = new Intent(this,ArticlesActivity.class);
            /*Bundle extras = new Bundle();
            extras.putString(URL,url);
            extras.putString("caller", "SearchActivity");*/
            intent.putExtra(URL, url);
            intent.putExtra(CALLER, "SearchActivity");
            //intent.putExtras(extras);
            startActivity(intent);
        }
    }

    public void goURL2 (View view) {
        Intent intent = new Intent(this,ArticlesActivity.class);
        //intent.putExtra(URL, url);
        intent.putExtra(CALLER, "SearchActivity2");
        startActivity(intent);

    }




}
