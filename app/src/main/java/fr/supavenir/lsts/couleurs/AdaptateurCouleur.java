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
import java.util.ArrayList;

import fr.supavenir.lsts.couleurs.db.DbHelper;

/**
 *  L'adaptateur pour visualiser le modèle : une liste d'objets Couleur
 */

// TODO : Mettre en place un cache dans la methode getView()

public class AdaptateurCouleur extends BaseAdapter {

    private Context context;
    private ModeleListeCouleurs modele = new ModeleListeCouleurs();
    private int positionEnCours = 0;

    public AdaptateurCouleur( Context context , ArrayList<Couleur> couleurs ) {
        this.context = context;
        modele.setLesCouleurs(couleurs);
    }

    public void ajouterCouleur(Couleur couleur) {
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        String[] projection = new String[]{"id"};
        Cursor cursor = db.query("CouleurARGB", projection, null, null, null, null, null, null);
        int id = cursor.getCount() + 1;
        Log.i("--COULEUR","id : " + id);
        db.execSQL("INSERT INTO CouleurARGB (id, nom, a, r ,g ,b) VALUES (" + id + ",'" + couleur.nom + "'," + couleur.a + "," + couleur.r + "," + couleur.v + "," + couleur.b + ");");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        modele.ajouterCouleur(couleur);
        this.notifyDataSetChanged();
    }

    public void supprimerCouleur(int position) {
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int id = position + 1;
        db.execSQL("DELETE FROM CouleurARGB WHERE id = " + id);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        Couleur c = modele.getLesCouleurs().get(position);
        modele.retirerCouleurEnPosition(position);
        this.notifyDataSetChanged();
    }

    public void changerCouleur(int position, Couleur  couleur) {
        DbHelper dbHelper = new DbHelper(this.context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        int id = position + 1;
        Log.i("--COULEUR","id " + id);
        db.execSQL("UPDATE CouleurARGB SET nom = '"+ couleur.nom +"', a = " + couleur.a + ", r = " + couleur.r + ", g = " + couleur.v + ", b = " + couleur.b + " WHERE id = " + id);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        modele.modifierCouleur(position, couleur);
        this.notifyDataSetChanged();
    }

    /** On adapte les methodes pour visualiser le modèle en mémoire. */
    @Override
    public int getCount() {
        return modele.getLesCouleurs().size();
    }

    @Override
    public Object getItem(int position) {
        return modele.getLesCouleurs().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Important : le code pour recuperer la vue de l'item par son layout
        View itemView = LayoutInflater.from( context ).inflate(R.layout.liste_couleur_item,
                parent , false );

        TextView tvCouleur = itemView.findViewById( R.id.tvCouleur);
        TextView tvNomCouleur = itemView.findViewById( R.id.tvNomCouleur );

        Button btnSuppr = itemView.findViewById(R.id.btn_suppr);
        Button btnModif = itemView.findViewById(R.id.btn_modif);

        int a = modele.getLesCouleurs().get(position).getA();
        int r = modele.getLesCouleurs().get(position).getR();
        int v = modele.getLesCouleurs().get(position).getV();
        int b = modele.getLesCouleurs().get(position).getB();
        String nom = modele.getLesCouleurs().get( position ).getNom();
        Couleur couleur = new Couleur(a,r,v,b);
        couleur.setNom(nom);

        btnSuppr.setOnClickListener(view -> supprimerCouleur(position));

        btnModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActiviteChoixCouleur.class);
                Log.i("couleurBeforeIntent",couleur.toString());
                intent.putExtra("couleur",couleur);
                intent.putExtra("requete", "MODIFIER");
                setPositionEnCours(position);
                ((ListeCouleurs)context).getLanceurActiviteChoixCouleur(intent);

            }
        });

        tvCouleur.setBackgroundColor(Color.argb( a , r, v ,b ));
        tvNomCouleur.setText(((Couleur)getItem(position)).getNom());
        //itemView.setBackgroundColor(Color.argb(255, 200,200,200));
        return itemView;
    }

    public int getPositionEnCours() { return positionEnCours; }

    public void setPositionEnCours(int positionEnCours) {
        this.positionEnCours = positionEnCours;
    }
}
