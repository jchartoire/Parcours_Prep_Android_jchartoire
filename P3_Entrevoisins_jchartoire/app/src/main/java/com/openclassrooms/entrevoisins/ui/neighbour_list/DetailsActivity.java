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
    @BindView(R.id.favoriteFAB)
    FloatingActionButton favoriteFAB;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;

    private NeighbourApiService mApiService;
    private Neighbour mNeighbour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mApiService = DI.getNeighbourApiService();

        /* get the intent of the neighbour sent from MyNeighbourRecyclerViewAdapter */
        Intent intent = getIntent();
        long id = intent.getLongExtra("neighbourId", 0);
        Neighbour neighbour = mApiService.getNeighbourById(id);
        mNeighbour = neighbour;

        updateButton();

        /* better quality avatar image url tranform for view in detail */
        String mNeighbourImageFull = mNeighbour.getAvatarUrl();
        mNeighbourImageFull = mNeighbourImageFull.replace("https://i.pravatar.cc/100","https://i.pravatar.cc/500");

        /* set the new image avatar to the app_bar_image ImageView */
        app_bar_image.setImageBitmap((null));
        Glide.with(this).load(mNeighbourImageFull).into(app_bar_image);

        textViewName.setText(mNeighbour.getName());
        textViewAdress.setText(mNeighbour.getAddress());
        textViewPhone.setText(mNeighbour.getPhoneNumber());
        textViewLink.setText("www.facebook.fr/" + (mNeighbour.getName().toLowerCase()));
        textViewAbout.setText(mNeighbour.getAboutMe());
        toolbar_layout.setTitle(mNeighbour.getName());

        favoriteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mNeighbour.getFavoriteStatus())
                {
                    mNeighbour.setAsFavorite(false);
                    Snackbar.make(view, "Favoris retiré", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Drawable newStar = getResources().getDrawable(R.drawable.ic_star_empty_24dp, getTheme());
                    favoriteFAB.setImageDrawable(newStar);
                }
                else
                {
                    mNeighbour.setAsFavorite(true);
                    Snackbar.make(view, "Voisin ajouté aux favoris", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Drawable newStar = getResources().getDrawable(R.drawable.ic_star_filled_24dp, getTheme());
                    favoriteFAB.setImageDrawable(newStar);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    /* initialize favorite button state */
    private void updateButton() {
        if (mNeighbour.getFavoriteStatus())
        {
            // yellow filled star
            Drawable newStar = getResources().getDrawable(R.drawable.ic_star_filled_24dp, getTheme());
            favoriteFAB.setImageDrawable(newStar);
        }
        else
        {
            // yellow border star
            Drawable newStar = getResources().getDrawable(R.drawable.ic_star_empty_24dp, getTheme());
            favoriteFAB.setImageDrawable(newStar);
        }
    }

    /**
     * Override onSupportNavigateUp is set to get back to previous tab, not the home tab, when touching the back button in appbar layout
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}