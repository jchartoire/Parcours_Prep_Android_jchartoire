package com.jchartoire.mareu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.viewSwitcher1)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.textviewView)
    View textviewView;
    @BindView(R.id.edittextView)
    View edittextView;

    MenuItem ok_settings;
    MenuItem edit_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
    }

    /**
     * Override onSupportNavigateUp is set to get back to previous tab, not the home tab, when touching the back button in appbar layout
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_detail, menu);
        ok_settings = menu.findItem(R.id.ok_settings);
        edit_settings = menu.findItem(R.id.edit_settings);
        setEditionMode (false);
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
                    setEditionMode (true);
                }
                return true;

            case R.id.ok_settings:
                if (viewSwitcher.getCurrentView() == edittextView) {
                    //todo: ajouter fonction pour mettre à jour les textview et l'API ici
                    setEditionMode (false);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setEditionMode (boolean editionMode)
    {
        if (editionMode == true)
        {
            // On transforme les champs en edittext
            viewSwitcher.setDisplayedChild(viewSwitcher.indexOfChild(edittextView));
            // paramétrage des boutons menu d'édition
            ok_settings.setVisible(true);
            edit_settings.setVisible(false);
        }
        else
        {
            // On transforme les champs en textview
            viewSwitcher.setDisplayedChild(viewSwitcher.indexOfChild(textviewView));
            // paramétrage des boutons menu d'édition
            ok_settings.setVisible(false);
            edit_settings.setVisible(true);
        }
    }

}
