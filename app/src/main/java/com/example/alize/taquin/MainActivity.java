package com.example.alize.taquin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    public static Uri uriString;
    private int level;
    private ImageView imageView;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // chargement du layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // on bloque l'application en mode portrait
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView); // définition de la zone où se trouvera l'image d'aperçu avant de démarrer le jeu

        // Définition du bouton pour lancer l'appareil photo
        Button capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Définition du bouton de lancement du jeu, au clique on enregistre la photo
        // et on lance la seconde activité en envoyant le lien de la photo et le niveau de la grille
        Button btnJouer = (Button) findViewById(R.id.BtnJouer);
        btnJouer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                galleryAddPic();
                Intent intent = new Intent(MainActivity.this, TaquinActivity.class);
                intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });
    }

    // Lancement de l'appareil photo
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // création du fichier
            } catch (IOException ex) {
                System.err.println(ex);
            }
            if (photoFile != null) {
                // si le fichier a bien été créé on enregistre la photo dedans
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                setUriString(Uri.fromFile(photoFile)); // enregistrement de l'uri
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO); // on retourne sur la vue d'accueil du jeu (l'activité qui à fait la requête)
            }
        }
    }

    // Une fois la photo prise
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            // on redimentionne l'image et on l'affiche dans imageView
            Bitmap bmp = setPic();
            imageView.setImageBitmap(bmp);
        }
    }

    // Création de l'image
    private File createImageFile() throws IOException {
        // Création d'un nom pour l'image grâce à la date courante
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // enregistrement du chemin absolu de l'image
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap setPic() {
        // on récupère la taille de l'écran
        Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        d.getMetrics(m);

        // on créé des variables de tailles, celle que doit faire l'image une fois redimentionnée
        int targetW = m.widthPixels;
        int targetH = m.heightPixels;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }

    // Enregistrement de l'image en local
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    // Au clique sur une checkbox
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // on test quel checkbox a été cliqué puis si elle a aussi été coché on attribut une valeur a notre variable de niveau de  jeu
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked) {
                    level = 2;
                }
                break;
            case R.id.checkBox2:
                if (checked) {
                    level = 3;
                }
                break;
            case R.id.checkBox3:
                if (checked) {
                    level = 4;
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setUriString(Uri uriString) {
        MainActivity.uriString = uriString;
    }
}
