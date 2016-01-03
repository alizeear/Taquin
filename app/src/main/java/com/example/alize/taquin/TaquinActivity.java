package com.example.alize.taquin;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.FileInputStream;

public class TaquinActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "debuggg";
    float initialX, initialY;
    private boolean permutable = false;
    private TaquinAdapter taquinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_taquin);

        // taille de l'écran
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final GridView gridview = (GridView) findViewById(R.id.gridview);
//        gridview.setAdapter(new TaquinAdapter(this, bmp, m.widthPixels, m.heightPixels));
        taquinAdapter = new TaquinAdapter(this, BitmapFactory.decodeResource(this.getResources(), R.drawable.jina), m.widthPixels, m.heightPixels);
        gridview.setAdapter(taquinAdapter);
        // affichage de la petite bulle de numéro de la case au clique
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                permutable = taquinAdapter.permutation(position);
                gridview.invalidateViews();
                Log.d("toto", permutable+"");
                Toast.makeText(TaquinActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(DEBUG_TAG, event.toString());
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        initialY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        float finalX = event.getX();
                        float finalY = event.getY();
                        String direction = "";

                        Log.d(DEBUG_TAG, "finalX: "+finalX+" initialX: "+initialX);
                        Log.d(DEBUG_TAG, "finalY: "+finalY+" initialY: "+initialY);

                        if (initialX < finalX) {
                            Log.d(DEBUG_TAG, "Left to Right swipe performed");
                            direction = "droite";
                        }

                        if (initialX > finalX) {
                            Log.d(DEBUG_TAG, "Right to Left swipe performed");
                            direction = "gauche";
                        }

                        if (initialY < finalY) {
                            Log.d(DEBUG_TAG, "Up to Down swipe performed");
                            direction = "bas";
                        }

                        if (initialY > finalY) {
                            Log.d(DEBUG_TAG, "Down to Up swipe performed");
                            direction = "haut";
                        }
                        Log.d(DEBUG_TAG, direction);
                        break;
                    case MotionEvent.ACTION_CANCEL:
//                        Log.d(DEBUG_TAG,"Action was CANCEL");
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
//                        Log.d(DEBUG_TAG, "Movement occurred outside bounds of current screen element");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
