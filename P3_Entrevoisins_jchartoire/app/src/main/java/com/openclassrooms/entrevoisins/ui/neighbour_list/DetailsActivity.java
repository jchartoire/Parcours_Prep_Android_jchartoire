package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.app_bar_image)
    ImageView app_bar_image;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewPhone)
    TextView textViewPhone;
    @BindView(R.id.textViewAdress)
    TextView textViewAdress;
    @BindView(R.id.textViewAbout)
    TextView textViewAbout;
    @BindView(R.id.textViewLink)
    TextView textViewLink;

    private NeighbourApiService mApiService;
    private String mNeighbourImageFull;
    private Neighbour mNeighbour;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.favoriteFAB);

        mApiService = DI.getNeighbourApiService();

        Intent intent = getIntent();
        long id = intent.getLongExtra("neighbourId", 0);
        Neighbour neighbour = mApiService.getNeighbourById(id);
        mNeighbour = neighbour;

        init();
        mNeighbourImageFull = mNeighbour.getAvatarUrl();
        mNeighbourImageFull = mNeighbourImageFull.replace("https://i.pravatar.cc/100","https://i.pravatar.cc/500"); //better quality image url tranform for view in detail
        ImageView app_bar_image = (ImageView) this.findViewById(R.id.app_bar_image);

        app_bar_image.setImageBitmap((null));
        Glide.with(this).load(mNeighbourImageFull).into(app_bar_image);

//      (TextView) this.findViewById(R.id.textViewName)).setText(neighbour.getName());
        textViewName.setText(neighbour.getName());

        textViewAdress.setText(mNeighbour.getAddress());
        textViewPhone.setText(mNeighbour.getPhoneNumber());
        textViewLink.setText("www.facebook.fr/" + (mNeighbour.getName().toLowerCase()));
        textViewAbout.setText(mNeighbour.getAboutMe());
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(neighbour.getName());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (neighbour.getFavoriteStatus())
                {
                    neighbour.setAsFavorite(false);
                    Snackbar.make(view, "Favoris retiré", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Drawable newStar = getResources().getDrawable(R.drawable.ic_star_border_black_24dp, getTheme());
                    fab.setImageDrawable(newStar);
                }
                else
                {
                    neighbour.setAsFavorite(true);
                    Snackbar.make(view, "Voisin ajouté aux favoris", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Drawable newStar = getResources().getDrawable(R.drawable.ic_star_black_24dp, getTheme());
                    fab.setImageDrawable(newStar);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void init() {
        if (mNeighbour.getFavoriteStatus())
        {
            Drawable newStar = getResources().getDrawable(R.drawable.ic_star_black_24dp, getTheme());
            fab.setImageDrawable(newStar);
        }
        else
        {
            Drawable newStar = getResources().getDrawable(R.drawable.ic_star_border_black_24dp, getTheme());
            fab.setImageDrawable(newStar);
        }
    }

    /**
     * Override onSupportNavigateUp is set to get back to previous tab, not the home tab when touching the back button in appbar layout
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}