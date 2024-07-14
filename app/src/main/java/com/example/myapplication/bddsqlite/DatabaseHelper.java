package com.example.myapplication.bddsqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.myapplication.model.VilleModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "database.db";
    public static String DBLOCATION="/data/data/com.example.myapplication/databases/";
    public final Context context;
    public SQLiteDatabase mdatabese;
    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_VILLES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "lat REAL,"
                + "lon REAL,"
                + "nomVille TEXT" + ")";
        db.execSQL(sql);*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_VILLES);
        onCreate(db);*/
    }
    public void ajoutVille(VilleModel ville) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO ville (lat, lon, nomVille) VALUES (?, ?, ?)",
                new Object[]{ville.getLat(), ville.getLon(), ville.getNomVille()});
        db.close();
    }
    public List<VilleModel> rechercheVille(String nom) {
        List<VilleModel> villes = new ArrayList<>();
        openDatabase();
        Cursor cursor = mdatabese.rawQuery("SELECT * FROM ville WHERE nomVille LIKE ?", new String[]{"%" + nom + "%"});
        if (cursor.moveToFirst()) {
            do {
                VilleModel ville = new VilleModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                villes.add(ville);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDataBase();
        return villes;
    }
    public List<VilleModel> test(String nom) {
        List<VilleModel> villes = new ArrayList<>();
        openDatabase();
        String[] columns = {"id", "lat", "lon", "nomVille"};
        String selection = "id = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = mdatabese.query("ville", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                VilleModel ville = new VilleModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                villes.add(ville);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDataBase();
        return villes;
    }
    public void openDatabase(){
        String path = context.getDatabasePath(DBNAME).getPath();
        if(mdatabese!=null && mdatabese.isOpen()){
            return;
        }
        mdatabese=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
    }
    public void closeDataBase(){
        if(mdatabese!=null){
            mdatabese.close();
        }
    }

}
