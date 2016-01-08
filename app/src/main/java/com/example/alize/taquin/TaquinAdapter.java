package com.example.alize.taquin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Alizée on 12/11/2015.
 */
public class TaquinAdapter extends BaseAdapter {

    private Context mContext;
    private Bitmap bouts;
    private Bitmap vide;
    private int level;
    private ArrayList<Bitmap> imgBouts;
    private ArrayList<Bitmap> boutBonOrdre;
    private Animation animation;

    public TaquinAdapter(Context c, int l, Bitmap b, int w, int h) {
        mContext = c;
        level = l;
        decoupe(b, w, h);
        melange();
    }

    private void decoupe(Bitmap img, int w, int h) {
        imgBouts = new ArrayList<>(); // on met les bouts dans une ArrayList pour pouvoir les mélanger par la suite
        boutBonOrdre = new ArrayList<>(); // Tableau qui contiendra les morceaux dans l'ordre

        // Récupération des tailles de la bitmap
        int iw = img.getWidth();
        int ih = img.getHeight();

        // Découpe de l'image
        for(int i=0; i<level; i++){
            for(int j=0; j<level; j++){
                if(i == level-1 && j == level-1){
                    vide = Bitmap.createBitmap(iw/level, ih/level, Bitmap.Config.ALPHA_8);
                    Bitmap bout = vide;
                    imgBouts.add(bout);
                    boutBonOrdre.add(bout);
                }else{
                    Bitmap bout = Bitmap.createBitmap(img, j*iw/level, i*ih/level, iw/level, ih/level);
                    imgBouts.add(bout);
                    boutBonOrdre.add(bout);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return level*level;
    }

    @Override
    public Object getItem(int position) {
        return imgBouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return boutBonOrdre.indexOf(imgBouts.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        // on place tous les morceaux dans la grille
        imageView.setImageBitmap(imgBouts.get(position));
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    // permutation des morceaux avec la position du morceau cliqué en paramètre
    public Animation permutation(int position){
        int caseVide = imgBouts.indexOf(vide); // récupération de la position du morceau vide
        int[] testCase = {0,0,0,0};
        float translateX = 0;
        float translateY = 0;

        if(position >=0 && position < imgBouts.size()){
            // on rempli le tableau de cases à tester
            if(position+1 != 0){
                testCase[0] = position+1;
            }
            if(position+level != 0){
                testCase[1] = position+level;
            }
            if(position-1 != 0){
                testCase[2] = position-1;
            }
            if(position-level != 0){
                testCase[3] = position-level;
            }
            // on va tester les 4 cotés du bout d'image pour savoir si on a la case vide
            for(int i=0; i<4; i++){
                if(testCase[i] == caseVide){
                    if(i==0){
                        translateX = 1;
                    }else if(i==1){
                        translateY = 1;
                    }else if(i==2){
                        translateX = -1;
                    }else if(i==3){
                        translateY = -1;
                    }

                    // si on tombe sur la case vide on permute les cases
                    Bitmap temp = imgBouts.get(position);
                    imgBouts.set(position, vide);
                    imgBouts.set(caseVide, temp);
                }
            }
        }
        animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, (float)0, TranslateAnimation.RELATIVE_TO_SELF, translateX,
                TranslateAnimation.RELATIVE_TO_SELF, (float)0, TranslateAnimation.RELATIVE_TO_SELF, translateY);
        return animation;
    }

    //1000 fois nous allons fair ejouer aléatoirement le jeu tout seul pour le mélanger, ceci évite de tomber sur un problème non résolvable
    public void melange(){
        for(int i=0; i<1000; i++){ // Nb de fois que l'on va jouer aléatoirement pour mélanger le jeu
            int currentPosition = imgBouts.indexOf(vide); // on récupère la position courante de la case vide
            int random = (int) (Math.random()*level); // valeur de random en fonction du nombre de cases du jeu

            // on effectue la permutation en fonction du chiffre retourné par random
            if(random == 0){
                permutation(currentPosition+1);
            }
            if(random == 1){
                permutation(currentPosition+level);
            }
            if(random == 2){
                permutation(currentPosition-1);
            }
            if(random == 3){
                permutation(currentPosition-level);
            }
        }
    }

    // Test si le jeu est terminé
    public boolean bonOrdre(){
        // pour chaque morceaux de l'image
        for(int i=0; i<boutBonOrdre.size(); i++){
            // si le morceau n'est pas le morceau vide
            if(boutBonOrdre.get(i) != null){
                // si les deux bouts sont différent on retourne false, le jeu n'est pas terminé
                if(!boutBonOrdre.get(i).sameAs(imgBouts.get(i))){
                    return false;
                }
            }
        }
        // à la fin des itérations si aucune différence n'est détecté on retourne true, le jeu est dans le bon ordre
        return true;
    }
}
