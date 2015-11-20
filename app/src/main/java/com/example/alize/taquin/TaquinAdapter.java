package com.example.alize.taquin;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Alizée on 12/11/2015.
 */
public class TaquinAdapter extends BaseAdapter {

    private Context mContext;
    private Bitmap[] bouts;

    public TaquinAdapter(Context c, Bitmap b, int w, int h) {
        mContext = c;
        decoupe(b, w, h);
    }

    private void decoupe(Bitmap img, int w, int h) {
        bouts = new Bitmap[9];

        int iw = img.getWidth();
        int ih = img.getHeight();

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(i == 2 && j == 2){
                    bouts[(i*3)+j] = Bitmap.createBitmap(iw/3, ih/3, Bitmap.Config.ALPHA_8);
                }else{
                    bouts[(i*3)+j] = Bitmap.createBitmap(img, j*iw/3, i*ih/3, iw/3, ih/3);
                    //bouts[(i*3)+j] = Bitmap.createScaledBitmap(bouts[(i*3)+j], w/3, h/3, true);
                }
            }
        }
        // mélange des bouts
        
    }

    @Override
    public int getCount() {return bouts.length;}

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
        imageView.setImageBitmap(bouts[position]);
        imageView.setAdjustViewBounds(true);
        return imageView;
    }
}
