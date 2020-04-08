package com.jchartoire.mareu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jchartoire.mareu.model.Meeting;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemListActivity extends AppCompatActivity {

    // dummy generator
    private List<Meeting> mMeetings = Arrays.asList(
            new Meeting(1, "Réunion R&D", "Thomas", "12:00",
                    "13:00", "08/04/2020", "08/04/2020", "#FC8E8E", "Salle 1", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(2, "Réunion développement", "Marion", "9:00",
                    "13:00", "08/04/2020", "08/04/2020", "#FCCB93", "Salle 2", "quatrieme@boite.fr, cinquieme@boite.fr, sixieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(3, "Réunion inventaire", "Sabrina", "10:00",
                    "13:00", "08/04/2020", "08/04/2020", "#F1FC8E", "Salle 3", "quatrieme@boite.fr, deuxieme@boite.fr, premier@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion marketing", "Thomas", "12:00",
                    "13:00", "08/04/2020", "08/04/2020", "#ABFC8E", "Salle 4", "troisième@boite.fr, quatrieme@boite.fr, cinquieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion budgétaire", "Julie", "13:00",
                    "13:00", "08/04/2020", "08/04/2020", "#8EFCF0", "Salle 5", "premier@boite.fr, troisième@boite.fr, cinquieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion vacances d'été", "Bernard", "8:20",
                    "13:00", "08/04/2020", "08/04/2020", "#8EC7FC", "Salle 6", "premier@boite.fr, quatrieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion marketing", "Leonie", "10:00",
                    "13:00", "08/04/2020", "08/04/2020", "#8EC7FC", "Salle 6", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion tuperware", "Andrea", "12:40",
                    "13:00", "08/04/2020", "08/04/2020", "#AF8EFC", "Salle 7", "premier@boite.fr, deuxieme@boite.fr, quatrieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion stagiaire", "Louis", "15:30",
                    "13:00", "08/04/2020", "08/04/2020", "#AF8EFC", "Salle 7", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion marketing", "Arthur", "16:10",
                    "13:00", "08/04/2020", "08/04/2020", "#FC8EEE", "Salle 8", "deuxieme@boite.fr, troisième@boite.fr, cinquieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion marketing", "Morgan", "8:50",
                    "13:00", "08/04/2020", "08/04/2020", "#D2D2D2", "Salle 9", "troisième@boite.fr, quatrieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(4, "Réunion marketing", "Luca", "14:00",
                    "13:00", "08/04/2020", "08/04/2020", "#5A5A5A", "Salle 10", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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

        initRecyclerView();
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(mMeetings);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
