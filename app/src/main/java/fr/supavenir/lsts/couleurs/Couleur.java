package fr.supavenir.lsts.couleurs;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Couleur implements Parcelable {

    int a;
    int r;
    int v;
    int b;
    String nom;

    public Couleur() { }

    public Couleur(int a, int r, int v, int b, String nom) {
        this.a = a;
        this.r = r;
        this.v = v;
        this.b = b;
        this.nom = nom;
    }

    public Couleur(int a, int r, int v, int b) {
        this.a = a;
        this.r = r;
        this.v = v;
        this.b = b;
    }

    public Couleur(Parcel parcel) {
        this.a = parcel.readInt();
        this.r = parcel.readInt();
        this.v = parcel.readInt();
        this.b = parcel.readInt();
        this.nom = parcel.readString();
    }

    public static final Creator<Couleur> CREATOR = new Creator<Couleur>() {
        @Override
        public Couleur createFromParcel(Parcel in) {
            return new Couleur(in);
        }

        @Override
        public Couleur[] newArray(int size) {
            return new Couleur[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(a);
        parcel.writeInt(r);
        parcel.writeInt(v);
        parcel.writeInt(b);
        parcel.writeString(nom);
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Couleur{" +
                "a=" + a +
                ", r=" + r +
                ", v=" + v +
                ", b=" + b +
                ", nom='" + nom + '\'' +
                '}';
    }
}
