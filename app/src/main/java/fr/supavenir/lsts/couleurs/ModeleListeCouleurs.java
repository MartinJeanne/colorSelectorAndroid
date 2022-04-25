package fr.supavenir.lsts.couleurs;

import android.util.Log;

import java.util.ArrayList;

public class ModeleListeCouleurs {

    private ArrayList<Couleur> lesCouleurs;

    public  ModeleListeCouleurs() {
        lesCouleurs = new ArrayList<Couleur>();
    }

    public ArrayList<Couleur> getLesCouleurs() { return lesCouleurs; }

    public void setLesCouleurs(ArrayList<Couleur> lesCouleurs) {
        this.lesCouleurs = lesCouleurs;
    }

    public void ajouterCouleur(Couleur couleur) { lesCouleurs.add(couleur); }

    public void retirerCouleurEnPosition(Couleur couleur) {
        int index = lesCouleurs.indexOf(couleur);
        Log.i("--INDEX", "" + index);
        lesCouleurs.remove(index);
    }

    public void modifierCouleur(Couleur couleur) { lesCouleurs.set(lesCouleurs.indexOf(couleur), couleur); }
}
