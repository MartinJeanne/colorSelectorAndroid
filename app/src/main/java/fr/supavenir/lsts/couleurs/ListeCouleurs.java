package fr.supavenir.lsts.couleurs;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import fr.supavenir.lsts.couleurs.db.DbHelper;


// TODO pouvoir modifier et supprimer la couleur sélectionnée dans la liste
// TODO en ajoutant deux éléments visibles comportant des îcones de type delete et edit


public class ListeCouleurs extends AppCompatActivity {

    private ListView lvListeCouleurs;
    private Button btnAjouterCouleur;
    private AdaptateurCouleur adaptateur;

    ActivityResultLauncher<Intent> lanceurActiviteChoixCouleur = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        int a = result.getData().getIntExtra("a", 255);
                        int r = result.getData().getIntExtra("r", 255);
                        int v = result.getData().getIntExtra("v", 255);
                        int b = result.getData().getIntExtra("b", 255);
                        String nomCouleur = result.getData().getStringExtra("nom");
                        if (TextUtils.isEmpty(nomCouleur)) nomCouleur = "Couleur";
                        Log.i("--ListeCouleur",a + ", " + r + ", " + v +", " + b + ", " + nomCouleur);
                        String requete = result.getData().getStringExtra("requete");
                        Couleur couleur = new Couleur(a, r, v, b, nomCouleur);

                        if(requete.equals("AJOUTER")) adaptateur.ajouterCouleur(couleur);
                        else if (requete.equals("MODIFIER")) adaptateur.changerCouleur(couleur);
                        else if (requete.equals("SUPPRIMER")) adaptateur.supprimerCouleur(couleur);

                    } else if( result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(ListeCouleurs.this, "Opération annulée", Toast.LENGTH_SHORT).show();
                    }
                    getCouleursFromDB();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activite_liste_couleurs );

        boolean dbUpToDate = checkDbState();
        if(!dbUpToDate) {
            //createAndPopulateDb();
            writeDbState();
        }

        getCouleursFromDB();

        btnAjouterCouleur = findViewById(R.id.btnAjouterCouleur);
        btnAjouterCouleur.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionChoixCouleur = new Intent(ListeCouleurs.this , ActiviteChoixCouleur.class);
                intentionChoixCouleur.putExtra("requete", "AJOUTER");
                lanceurActiviteChoixCouleur.launch(intentionChoixCouleur);
            }
        });
        //deleteInDb();
    }

    public void getLanceurActiviteChoixCouleur(Intent intent) { lanceurActiviteChoixCouleur.launch(intent); }

    private void createAndPopulateDb() {
        DbHelper dbHelper = new DbHelper(ListeCouleurs.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        for(int i =0;i<3;i++) {
            String nom = String.format("couleur%d", i+1);
            int a = (int) (Math.random()*255);
            int r = (int) (Math.random()*255);
            int g = (int) (Math.random()*255);
            int b = (int) (Math.random()*255);

            ContentValues contentValues = new ContentValues();
            contentValues.put("nom",nom);
            contentValues.put("a",a);
            contentValues.put("r",r);
            contentValues.put("g",g);
            contentValues.put("b",b);

            db.insert("CouleurARGB", null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void deleteInDb() {
        DbHelper dbHelper = new DbHelper(ListeCouleurs.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        db.execSQL("delete from CouleurARGB");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @SuppressLint("Range")
    public void getCouleursFromDB() {
        String sqlQuery = "Select * from CouleurARGB";
        DbHelper dbHelper = new DbHelper(ListeCouleurs.this);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sqlQuery, null);
        ArrayList<Couleur> listCouleur = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            String nom = cursor.getString(cursor.getColumnIndex("nom"));
            int a = cursor.getInt(cursor.getColumnIndex("a"));
            int r = cursor.getInt(cursor.getColumnIndex("r"));
            int g = cursor.getInt(cursor.getColumnIndex("g"));
            int b = cursor.getInt(cursor.getColumnIndex("b"));

            Couleur couleur = new Couleur(a, r, g, b, nom);
            listCouleur.add(couleur);
        }
        cursor.close();

        adaptateur = new AdaptateurCouleur(this, listCouleur);
        lvListeCouleurs = findViewById(R.id.lvCouleurs);
        lvListeCouleurs.setAdapter(adaptateur);
    }

    private boolean checkDbState() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getBoolean("dbUpToDate", false);
    }


    private void writeDbState() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dbUpToDate", true);
        editor.apply();
    }
}