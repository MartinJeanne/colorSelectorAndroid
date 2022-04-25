package fr.supavenir.lsts.couleurs;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


//TODO  Empêcher la validation par OK tant que le champ nom de la couleur est vide ou ne
//TODO  contient que des blancs
//TODO  Passer directement un objet Couleur dans l'intent de retour de resultat


public class ActiviteChoixCouleur extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sbAlpha;
    private SeekBar sbRouge;
    private SeekBar sbVert;
    private SeekBar sbBleu;
    private TextView tvARGB;
    private TextView tvCouleur;
    private EditText etNomCouleur;

    private int a = 255;
    private int r = 0;
    private int v = 0;
    private int b = 0;
    private int resultat = RESULT_OK;
    private String actionToperform = "AJOUTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activite_choix_couleur);

        sbAlpha = findViewById( R.id.sbAlpha );
        sbAlpha.setOnSeekBarChangeListener( this );

        sbRouge = findViewById( R.id.sbRouge );
        sbRouge.setOnSeekBarChangeListener( this );

        sbVert = findViewById( R.id.sbVert );
        sbVert.setOnSeekBarChangeListener( this );

        sbBleu = findViewById( R.id.sbBleu );
        sbBleu.setOnSeekBarChangeListener( this );

        tvARGB = findViewById( R.id.tvARGB );
        tvCouleur = findViewById( R.id.tvCouleur);

        etNomCouleur = findViewById( R.id.etNomCouleur );

        Intent intent = getIntent();
        Log.i("--COULEUR", intent.getStringExtra("requete"));
        if(intent.getStringExtra("requete").equals("MODIFIER")) {
            actionToperform = "MODIFIER";
            Couleur couleur = (Couleur) intent.getParcelableExtra("couleur");
            Log.i("--After Intent","couleur : " + couleur.toString());
            if(couleur != null) {
                putInitialValue(couleur);
            }
        }
        else if (intent.getStringExtra("requete").equals("SUPPRIMER")) {
            actionToperform = "SUPPRIMER";
            Couleur couleur = (Couleur) intent.getParcelableExtra("couleur");
            finish();
        }

        Button btnOk = findViewById( R.id.btnOk );
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActiviteChoixCouleur.this.finish();
            }
        });

        Button btnAnnuler = findViewById( R.id.btnAnnuler );
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On annule : utiliser RESULT_CANCELED
                resultat = RESULT_CANCELED;
                ActiviteChoixCouleur.this.finish();
            }
        });
    }

    private void putInitialValue(Couleur couleur) {
        sbAlpha.setProgress(couleur.getA());
        sbRouge.setProgress(couleur.getR());
        sbVert.setProgress(couleur.getV());
        sbBleu.setProgress(couleur.getB());
        etNomCouleur.setText(couleur.getNom());
        miseAjour();
    }

    public void miseAjour( ) {
        a = sbAlpha.getProgress();
        r = sbRouge.getProgress();
        v = sbVert.getProgress();
        b = sbBleu.getProgress();
        String texteCouleur = "ARGB( " + a +" , " + r + " , " + v + " , " + b +" )";
        tvARGB.setText( texteCouleur);
        tvCouleur.setBackgroundColor(Color.argb( a , r, v ,b ));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        miseAjour();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    // Utiliser la methode finish pour transmettre des resultats a l'activite appelante
    @Override
    public void finish() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("a" , a);
        resultIntent.putExtra("r" , r);
        resultIntent.putExtra("v" , v);
        resultIntent.putExtra("b" , b);
        resultIntent.putExtra( "nom" , etNomCouleur.getText().toString());
        resultIntent.putExtra("requete", actionToperform);

        setResult(resultat , resultIntent);
        super.finish();
    }

}