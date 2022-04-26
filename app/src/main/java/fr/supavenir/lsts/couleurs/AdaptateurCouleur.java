package fr.supavenir.lsts.couleurs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.supavenir.lsts.couleurs.db.DbHelper;

/**
 *  L'adaptateur pour visualiser le modèle : une liste d'objets Couleur
 */

public class AdaptateurCouleur extends BaseAdapter {

    private Context context;
    private ArrayList<Couleur> couleurs;

    public AdaptateurCouleur(Context context, ArrayList<Couleur> couleurs) {
        this.context = context;
        this.couleurs = couleurs;
    }

    public void ajouterCouleur(Couleur couleur) {
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        String[] columnNames = new String[]{"nom"};
        Cursor cursor = db.query("CouleurARGB", columnNames, "nom = '" + couleur.nom + "'", null, null, null, null, null);
        if (cursor.getCount() > 0) {
            //Toast.makeText(context, "Nom déjà utilisé", Toast.LENGTH_LONG).show();
            cursor = db.query("CouleurARGB", columnNames, null, null, null, null, null, null);
            couleur.nom += " " + cursor.getCount();
        }
        db.execSQL("INSERT INTO CouleurARGB (nom, a, r ,g ,b) VALUES ('" + couleur.nom + "'," + couleur.a + "," + couleur.r + "," + couleur.v + "," + couleur.b + ");");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        this.notifyDataSetChanged();
    }

    public void changerCouleur(Couleur  couleur, String ancienNom) {
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("UPDATE CouleurARGB SET nom = '"+ couleur.nom +"', a = " + couleur.a + ", r = " + couleur.r + ", g = " + couleur.v + ", b = " + couleur.b +
                " WHERE nom = '" + ancienNom+ "'");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        this.notifyDataSetChanged();
    }

    public void supprimerCouleur(Couleur couleur) {
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE FROM CouleurARGB WHERE nom = '" + couleur.nom +"'");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        this.notifyDataSetChanged();
    }


    /** On adapte les methodes pour visualiser le modèle en mémoire. */
    @Override
    public int getCount() { return couleurs.size(); }

    @Override
    public Object getItem(int position) {
        return couleurs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Important : le code pour recupérer la vue de l'item par son layout
        View itemView = LayoutInflater.from(context).inflate(R.layout.liste_couleur_item, parent , false );

        TextView tvCouleur = itemView.findViewById(R.id.tvCouleur);
        TextView tvNomCouleur = itemView.findViewById( R.id.tvNomCouleur);

        Button btnSuppr = itemView.findViewById(R.id.btn_suppr);
        Button btnModif = itemView.findViewById(R.id.btn_modif);

        int a = couleurs.get(position).getA();
        int r = couleurs.get(position).getR();
        int v = couleurs.get(position).getV();
        int b = couleurs.get(position).getB();
        String nom = couleurs.get(position).getNom();
        Couleur couleur = new Couleur(a, r, v, b, nom);

        btnModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActiviteChoixCouleur.class);
                Log.i("--couleurBeforeIntent", couleur.toString());
                intent.putExtra("couleur", couleur);
                intent.putExtra("requete", "MODIFIER");
                ((ListeCouleurs)context).getLanceurActiviteChoixCouleur(intent);
            }
        });

        btnSuppr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActiviteChoixCouleur.class);
                Log.i("--couleurBeforeIntent", couleur.toString());
                intent.putExtra("couleur", couleur);
                intent.putExtra("requete", "SUPPRIMER");
                ((ListeCouleurs)context).getLanceurActiviteChoixCouleur(intent);
            }
        });

        tvCouleur.setBackgroundColor(Color.argb(a, r, v, b));
        tvNomCouleur.setText(((Couleur)getItem(position)).getNom());

        return itemView;
    }
}
