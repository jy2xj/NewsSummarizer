package com.example.jycoders.newssummarizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ArticlesActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    String urlA;
    public final static String nURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String caller = intent.getStringExtra(SearchActivity.CALLER);
        if(caller.equals("SearchActivity")) {
            urlA = intent.getStringExtra(SearchActivity.URL);
        }
        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        if(caller.equals("SearchActivity")) {
            itemsAdapter.add(urlA);
            //itemsAdapter.add("https://www.washingtonpost.com/politics/sanders-hopes-to-notch-three-victories-in-democratic-contests-on-saturday/2016/03/26/bba69156-f367-11e5-a61f-e9c95c06edca_story.html?hpid=hp_rhp-top-table-main_democratsweb-310pm%3Ahomepage%2Fstory");
            //itemsAdapter.add("https://www.washingtonpost.com/politics/why-some-republicans-are-feeling-shame/2016/03/27/b4e0ac00-f2de-11e5-85a6-2132cf446d0a_story.html?hpid=hp_rhp-top-table-main_gopshame640p%3Ahomepage%2Fstory");
            writeItems();
        }
        setupListViewListener();

        if (caller.equals("SearchActivity")) {
            Intent intent2 = new Intent(this, OptionsActivity.class);
            intent2.putExtra(nURL, urlA);
            startActivity(intent2);
        }

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
                onClick(view);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (!Patterns.WEB_URL.matcher(itemText).matches()) {
            Toast.makeText(this, "Invalid URL. Please enter again", Toast.LENGTH_SHORT).show();
        }
        else {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();

            Intent intent2 = new Intent(this, OptionsActivity.class);
            intent2.putExtra(nURL, urlA);
            startActivity(intent2);
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }

    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        urlA = (String) ((TextView) view).getText();
        Intent intent2 = new Intent(this, OptionsActivity.class);
        intent2.putExtra(nURL, urlA);
        startActivity(intent2);

    }

    public void clearArt(View view) {

        itemsAdapter.clear();
        setupListViewListener();
    }
}
