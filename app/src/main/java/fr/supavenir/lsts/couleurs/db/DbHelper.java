package fr.supavenir.lsts.couleurs.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;


public class DbHelper extends SQLiteOpenHelper {
    private final static int dbVersion = 2;
    private final static String dbName="mainDB";

    public DbHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Le code ci-dessous correspond à la version 2 : la colonne description est ajoutée à la table
        db.execSQL("CREATE TABLE CouleurARGB (id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", nom TEXT" +
                ", a INTEGER" +
                ", r INTEGER" +
                ", g INTEGER" +
                ", b INTEGER" +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int indexVersion = oldVersion; indexVersion < newVersion;
             indexVersion++) {
            int nextVersion = indexVersion + 1;
            switch (nextVersion) {
                case 2:
                    upgrapdeToVersion2(db);
                    break;
                case 3:
                    // mise à jour future pour la version 3
                    break;
            }
        }
    }

    private void upgrapdeToVersion2(SQLiteDatabase db) {
        //db.execSQL("ALTER TABLE DummyItems  ADD COLUMN alpha INTEGER");
    }

    public void deleteColorByNom(String nom) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] nomArray = {nom};
        db.execSQL("DELETE FROM CouleurARGB WHERE nom=?;",nomArray);
    }
}

