package com.example.jonathan.imageed;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**L'activité principale de l'application.
 *
 *
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Assignation de la layout à l'activité
        setContentView(R.layout.activity_main);

        //Création de l'image de départ dans une view de type: MonImage
        final Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.test);
        _img = new MonImage(getBaseContext(),bmp);
        final int idImg = View.generateViewId();
        _img.setId(idImg);
        FrameLayout lay  = (FrameLayout) findViewById(R.id.lay_img);
        //Ajout de la view à la layout.
        lay.addView(_img);


        //Connexion des boutons.

        //Le bouton amène à l'activité de choix de modifications à apporter à l'image.
        Button bout = (Button) findViewById(R.id.bout_mod);
        bout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(),ChoixModif.class);
                startActivityForResult(intent,MODIF_IMG);
            }


        });


        //Bouton qui ramène à l'image de départ
        bout = (Button) findViewById(R.id.bout_ori);
        bout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _img.origine();
            }
        });

        //Bouton pour obtenir une photo de l'appareil photo.
        bout = (Button) findViewById(R.id.bout_phot);
        bout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendrePhoto();
            }
        });

        //Bouton pour obtenir une image de la gallerie.
        bout = (Button) findViewById(R.id.bout_gal);
        bout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallerie();

            }
        });





    }

    /**Fonction de gestion des resultats des activités tieces.
     *
     *
     * @param requestCode le code de requête caractérisant les modifications à apporter à l'image.
     * @param resultCode le code resultat: juste une vérification que c'est bien RESULT_OK
     * @param data l'intent
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MODIF_IMG && resultCode == RESULT_OK)
        {
            int modif =  data.getIntExtra("modif",0);
            switch(modif)
            {
                //Griser l'image
                case GRISER:
                    _img.set_bmp(ImageEdit.griserScr(_img.get_bmp(),getApplicationContext()));
                    break;
                //Egaliser l'hitogramme de l'image.
                case EGALISER:
                    _img.set_bmp(ImageEdit.egaliser(_img.get_bmp()));
                    break;
                //diminuer le contrates de l'image
                case DIM_CONTRASTE:
                    _img.set_bmp(ImageEdit.diminutionContraste(_img.get_bmp()));
                    break;
                //étendre dynamiquement le contratste de l'image.
                case EXT_DYN_CONTRASTE:
                    _img.set_bmp(ImageEdit.extensionContraste(_img.get_bmp()));
                    break;
                //test des nouvelles modification n'ayant pas encore de boutons attitrés

                case TEST:
                    float matrice[][] = new float[3][3];
                    for(int i =0;i<3;i++)
                    {
                        for(int j = 0;j<3;j++)
                        {
                            matrice[i][j] = 1;
                        }
                    }
                    /*matrice[0][0] = -1;
                    matrice[0][1] = -1;
                    matrice[0][2] = -1;
                    matrice[1][0] = -1;
                    matrice[1][1] = 8;
                    matrice[1][2] = -1;
                    matrice[2][0] = -1;
                    matrice[2][1] = -1;
                    matrice[2][2] = -1;*/
                    _img.set_bmp(ImageEdit.convolutionScr(_img.get_bmp(),matrice,getApplicationContext()));
                    break;
            }


        }

        //récupération de la photo à partir du chemin fourni par l'activité appareil photo.
        else if (requestCode == PRENDRE_PHOTO && resultCode == RESULT_OK)
        {


            BitmapFactory.Options opt = new BitmapFactory.Options();

            opt.inScaled = false;
            opt.inMutable = true;
            _img.set_bmpBase(BitmapFactory.decodeFile(_path,opt));


        }

        //récupération d'un image à partir de la gallerie du téléphone.
        //code inspiré de : http://stackoverflow.com/questions/28530332/how-to-get-image-from-gallery
        else if (requestCode == GALLERIE && resultCode == RESULT_OK)
        {
            Uri uriImg = data.getData();
            String[] colFich = { MediaStore.Images.Media.DATA };

            // obtenir le curseur de choix de fichier.
            Cursor curseur = getContentResolver().query(uriImg, colFich, null, null, null);
            // Initialisation au premier item
            curseur.moveToFirst();

            int index = curseur.getColumnIndex(colFich[0]);
            String imgDecodableString = curseur.getString(index);
            curseur.close();

            //décodage de l'image et création du bitmap.
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inScaled = false;
            opt.inMutable = true;


            _img.set_bmpBase(BitmapFactory.decodeFile(imgDecodableString,opt));

        }


    }


    /**Fonction pour prendre une photo à partir de l'appareil photo du téléphone
     * inspiré de :https://developer.android.com/training/camera/photobasics.html
     *
     */


    private void prendrePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File fichPhoto = null;
            try
            {
                fichPhoto= createFichImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if(fichPhoto != null)
            {
                Log.i("photo",fichPhoto.getPath());
                Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                        "com.example.jonathan.imageed.fileprovider",
                        fichPhoto);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent, PRENDRE_PHOTO);
            }

        }
    }


    /** Fonction pour séléectioner une photo à partir de la gallerie du téléphone.
     *
     *
     */

    private void gallerie()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERIE);


    }



    //Création d'un fichier pour la photo stockée
    private File createFichImage() throws IOException {
        // On creer un fichier pour la photo
        String tps = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String nomFich = "JPEG_" + tps + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                nomFich,  /* prefixe */
                ".jpg",         /* extension */
                storageDir      /* dossier */
        );
        _path =  image.getAbsolutePath();
        return image;
    }

    //l'image view sur laquelle on travaille
    protected MonImage _img;

    //chemin d'acces à la photo s'il y en a
    protected String _path;


    //les code des request code des intents.

    public static final int GRISER = 0;
    public static final int EGALISER = 1;
    public static final int CHG_TEINTE = 2;
    public static final int DIM_CONTRASTE = 3;
    public static final int EXT_DYN_CONTRASTE = 4;
    public static final int PRENDRE_PHOTO = 5;
    public static final int GALLERIE = 7;

    public static final int TEST = 9;
    public static final int MODIF_IMG = 6;
}
