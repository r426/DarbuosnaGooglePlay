package com.ryeslim.darbuosna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.ryeslim.darbuosna.model.WorkWithProverbs;

public class ListOfProverbsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_proverbs);

        // 1) Create an ArrayAdapter, whose data source is a list of Proverb objects.
        com.ryeslim.darbuosna.controller.ItemArrayAdapter adapter = new com.ryeslim.darbuosna.controller.ItemArrayAdapter(this,
                R.layout.one_line, WorkWithProverbs.getInstance().readBookmarks());

        // 2) Find the ListView object (the location where to drop the whole list)
        ListView listView = findViewById(R.id.list_view);

        // 3) Make the ListView use the ArrayAdapter
        listView.setAdapter(adapter);
    }


    //When clicked, returns to MainActivity
    public void carryOn(View view) {
        this.finish();
    }
}
