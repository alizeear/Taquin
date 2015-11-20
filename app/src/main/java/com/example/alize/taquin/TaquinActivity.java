package com.example.alize.taquin;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class TaquinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_taquin);

        // taille de l'écran
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new TaquinAdapter(this, BitmapFactory.decodeResource(this.getResources(), R.drawable.jina), m.widthPixels, m.heightPixels));

        // affichage de la petite bulle de numéro de la case au clique
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(TaquinActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
