package com.jchartoire.mareu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jchartoire.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListActivity extends AppCompatActivity {

    // dummy
    private List<Meeting> mMeetings = Arrays.asList(
            new Meeting(1, "Réunion R&D", "Thomas", "12:00",
                    "13:00",  "08/04/2020", "08/04/2020", "#ffffff", "Salle 1", "premier@boite.fr", "Réunion pour faire le point sur les avancé du " +
                    "projet X552"),
            new Meeting(2, "Réunion développement", "Marion", "9:00",
                    "13:00",  "08/04/2020", "08/04/2020", "#ffffff", "Salle 2", "premier@boite.fr", "Réunion pour faire le point sur les avancé du " +
                    "projet X552"),
            new Meeting(3, "Réunion inventaire", "Sabrina", "10:00",
                    "13:00",  "08/04/2020", "08/04/2020", "#ffffff", "Salle 1", "premier@boite.fr", "Réunion pour faire le point sur les avancé du " +
                    "projet X552"),
            new Meeting(4, "Réunion marketing", "Thomas", "12:00",
                    "13:00",  "08/04/2020", "08/04/2020", "#ffffff", "Salle 3", "premier@boite.fr", "Réunion pour faire le point sur les avancé du " +
                    "projet X552")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add_fab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemListActivity.this, DetailActivity.class);
                ItemListActivity.this.startActivity(intent);
            }
        });

      //  initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filter_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.item_list);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(mMeetings);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
