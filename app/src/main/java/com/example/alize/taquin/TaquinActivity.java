package com.example.alize.taquin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;

public class TaquinActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "debuggg";
    private TaquinAdapter taquinAdapter;
    private Button btnRejouer;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_taquin); // chargement du layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Bloque l'application en mode portrait
        final GridView gridview = (GridView) findViewById(R.id.gridview);

        // taille de l'écran du téléphone
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        String imageUri = getIntent().getStringExtra("mCurrentPhotoPath"); // Récupère le lien de la photo envoyé en paramètre de Intent
        File image = new File(imageUri); // création d'une image à partir de ce lien
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions); // création d'une bitmap à partir du fichier image

        int level = getIntent().getIntExtra("level", 3); // récupère le niveau de la grille envoyé en paramètre de l'Intent
        gridview.setNumColumns(level); // Découpe en colonnes du gridView en fonction du niveau reçu

        // boutons restart et quitter
        btnRejouer = (Button) findViewById(R.id.rejouer);
        btnRejouer.setVisibility(View.GONE); // on cache le bouton quand le jeu est en cours
        // au clique sur le bouton on revient sur le premier écran de jeu (accueil)
        btnRejouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaquinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Envoi le niveau, la bitmap, et la taille de l'écran à l'adapteur pour traitement
        taquinAdapter = new TaquinAdapter(this, level, bitmap, m.widthPixels, m.heightPixels);
        gridview.setAdapter(taquinAdapter);

        // Au clique sur une case on effectue différents tests et traitements
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Permutation des pièces de l'image

                animation = taquinAdapter.permutation(position);
                View view = gridview.getChildAt(position);
                animation.setDuration(300);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Mise à jour de la grille pour voir la permutation
                        gridview.invalidateViews();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);

                if(taquinAdapter.bonOrdre()){
                    // Affichage d'un message pour féliciter le joueur
                    Toast toast = Toast.makeText(TaquinActivity.this, "Gagné ! Le jeu est terminé", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    gridview.setOnItemClickListener(null); // la partie est terminée, on stop le listner pour ne plus donner la possibilité de déplacer les cases
                    btnRejouer.setVisibility(View.VISIBLE); // le bouton pour revenir à l'accueil s'affiche
                }
            }
        });
    }
}
