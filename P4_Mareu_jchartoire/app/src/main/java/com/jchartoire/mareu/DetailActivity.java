package com.jchartoire.mareu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.jchartoire.mareu.model.Meeting;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    // layout viewSwitcher
    @BindView(R.id.viewSwitcher1)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.textviewView)
    View textviewView;
    @BindView(R.id.edittextView)
    View edittextView;

    // Detail layout content
    @BindView(R.id.TextView_title1)
    TextView TextViewTitle1;
    @BindView(R.id.TextView_leader1)
    TextView TextViewLeader1;
    @BindView(R.id.agenda_debut1)
    ImageView agendaDebut1;
    @BindView(R.id.horloge_debut1)
    ImageView horlogeDebut1;
    @BindView(R.id.agenda_fin1)
    ImageView agendaFin1;
    @BindView(R.id.horloge_fin1)
    ImageView horlogeFin1;
    @BindView(R.id.TextView_participants1)
    TextView TextViewParticipants1;
    @BindView(R.id.TextView_description1)
    TextView TextViewDescription1;
    @BindView(R.id.EditText_leader2)
    EditText EditText_leader2;
    @BindView(R.id.agenda_debut2)
    ImageView agendaDebut2;
    @BindView(R.id.horloge_debut2)
    ImageView horlogeDebut2;
    @BindView(R.id.agenda_fin2)
    ImageView agendaFin2;
    @BindView(R.id.horloge_fin2)
    ImageView horlogeFin2;

    MenuItem ok_settings;
    MenuItem edit_settings;

    Meeting meeting;

    //dummy emails
    String[] emails = {"employé_1@boite.fr", "employé_2@boite.fr", "employé_3@boite.fr", "employé_4@boite.fr", "employé_5@boite.fr",
            "employé_6@boite.fr"};


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
            new Meeting(5, "Réunion budgétaire", "Julie", "13:00",
                    "13:00", "08/04/2020", "08/04/2020", "#8EFCF0", "Salle 5", "premier@boite.fr, troisième@boite.fr, cinquieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(6, "Réunion vacances d'été", "Bernard", "8:20",
                    "13:00", "08/04/2020", "08/04/2020", "#8EC7FC", "Salle 6", "premier@boite.fr, quatrieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(7, "Rappel de consignes de sécurité", "Leonie", "10:00",
                    "13:00", "08/04/2020", "08/04/2020", "#8EC7FC", "Salle 6", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(8, "Réunion tuperware", "Andrea", "12:40",
                    "13:00", "08/04/2020", "08/04/2020", "#AF8EFC", "Salle 7", "premier@boite.fr, deuxieme@boite.fr, quatrieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(9, "Réunion stagiaire", "Louis", "15:30",
                    "13:00", "08/04/2020", "08/04/2020", "#AF8EFC", "Salle 7", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(10, "Téléconférence avec la norvège", "Arthur", "16:10",
                    "13:00", "08/04/2020", "08/04/2020", "#FC8EEE", "Salle 8", "deuxieme@boite.fr, troisième@boite.fr, cinquieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(11, "Fixation d'objectifs", "Morgan", "8:50",
                    "13:00", "08/04/2020", "08/04/2020", "#D2D2D2", "Salle 9", "troisième@boite.fr, quatrieme@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552"),
            new Meeting(12, "Formation ERP", "Luca", "14:00",
                    "13:00", "08/04/2020", "08/04/2020", "#5A5A5A", "Salle 10", "premier@boite.fr, deuxieme@boite.fr, troisième@boite.fr",
                    "Réunion pour faire le point sur les avancé du projet X552")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        // initiate a MultiAutoCompleteTextView
        MultiAutoCompleteTextView simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.EditText_participants);
        // set adapter to fill the data in suggestion list
        ArrayAdapter<String> versionNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emails);
        simpleMultiAutoCompleteTextView.setAdapter(versionNames);
        // set threshold value 1 that help us to start the searching from first character
        simpleMultiAutoCompleteTextView.setThreshold(1);
        // set tokenizer that distinguish the various substrings by comma
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        /* get the intent of the meeting sent from ItemListActivity */
        Intent intent = getIntent();
        int id = intent.getIntExtra("meetingId", 0);
        meeting = mMeetings.get(id-1); //todo: remplacer par une vrai methode de service getMeetinById(id)

        updateMeetingDetails();
    }

    void updateMeetingDetails() {
        TextViewTitle1.setText(meeting.getTitle());
        TextViewLeader1.setText(meeting.getLeader());
        TextViewParticipants1.setText(meeting.getParticipant());
        TextViewDescription1.setText(meeting.getDescription());

    }

    /**
     * Override onSupportNavigateUp is set to get back to previous tab, not the home tab, when touching the back button in appbar layout
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_detail, menu);
        ok_settings = menu.findItem(R.id.ok_settings);
        edit_settings = menu.findItem(R.id.edit_settings);
        setEditionMode(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.edit_settings:
                if (viewSwitcher.getCurrentView() == textviewView) {
                    //todo: ajouter fonction pour mettre à jour les textview et l'API ici
                    setEditionMode(true);
                }
                return true;

            case R.id.ok_settings:
                if (viewSwitcher.getCurrentView() == edittextView) {
                    //todo: ajouter fonction pour mettre à jour les textview et l'API ici
                    setEditionMode(false);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setEditionMode(boolean editionMode) {
        if (editionMode == true) {
            // On transforme les champs en edittext
            viewSwitcher.setDisplayedChild(viewSwitcher.indexOfChild(edittextView));
            // paramétrage des boutons menu d'édition
            ok_settings.setVisible(true);
            edit_settings.setVisible(false);
        } else {
            // On transforme les champs en textview
            viewSwitcher.setDisplayedChild(viewSwitcher.indexOfChild(textviewView));
            // paramétrage des boutons menu d'édition
            ok_settings.setVisible(false);
            edit_settings.setVisible(true);
        }
    }

}
