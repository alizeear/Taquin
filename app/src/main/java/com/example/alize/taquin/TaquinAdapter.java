package com.example.alize.taquin;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayList<Bitmap> imgBouts;

    public TaquinAdapter(Context c, Bitmap b, int w, int h) {
        mContext = c;
        decoupe(b, w, h);
        // mélange des bouts
        melange();
    }

    private void decoupe(Bitmap img, int w, int h) {
        imgBouts = new ArrayList<>();

        int iw = img.getWidth();
        int ih = img.getHeight();

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(i == 2 && j == 2){
//                    Bitmap bout = Bitmap.createBitmap(iw/3, ih/3, Bitmap.Config.ALPHA_8);
                    Bitmap bout = vide;
                    imgBouts.add(bout);
                }else{
                    Bitmap bout = Bitmap.createBitmap(img, j*iw/3, i*ih/3, iw/3, ih/3);
                    imgBouts.add(bout);
                    //bouts[(i*3)+j] = Bitmap.createScaledBitmap(bouts[(i*3)+j], w/3, h/3, true);
                }
            }
        }
    }

    @Override
    public int getCount() {return 9;}

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
//        imageView.setImageBitmap(bouts[position]);
        imageView.setImageBitmap(imgBouts.get(position));
        imageView.setAdjustViewBounds(true);
        return imageView;
    }

    public boolean permutation(int position){
        boolean permutable = false;
        int caseVide = imgBouts.indexOf(vide);
        int[] testCase = {0,0,0,0};

        if(position >=0 && position < imgBouts.size()){
            if(position+1 != 0){
                testCase[0] = position+1;
            }
            if(position+3 != 0){
                testCase[1] = position+3;
            }
            if(position-1 != 0){
                testCase[2] = position-1;
            }
            if(position-3 != 0){
                testCase[3] = position-3;
            }
            for(int i=0; i<4; i++){
                if(testCase[i] == caseVide){
                    Bitmap temp = imgBouts.get(position);
                    imgBouts.set(position, vide);
                    imgBouts.set(caseVide, temp);
                    permutable = true;
                }
            }
        }
        return permutable;
    }
    public void melange(){
//        Collections.shuffle(imgBouts);
        for(int i=0; i<5; i++){ // Nb de fois que l'on va jouer aléatoirement pour mélanger le jeu
            int currentPosition = imgBouts.indexOf(vide);
            int random = (int) (Math.random()*3); // valeur en fonction du nombre de case du jeu
            if(random == 0){
                permutation(currentPosition+1);
            }
            if(random == 1){
                permutation(currentPosition+3);
            }
            if(random == 2){
                permutation(currentPosition-1);
            }
            if(random == 3){
                permutation(currentPosition-3);
            }
        }
    }

}
