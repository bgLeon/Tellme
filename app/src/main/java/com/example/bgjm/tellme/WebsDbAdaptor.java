package com.example.bgjm.tellme; /**
 * @author hefesto
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WebsDbAdaptor {
    public static final String COL_TITULO = "titulo";
    public static final String COL_URL = "url";
    public static final String COL_ID = "_id";
    private static final String TAG = WebsDbAdaptor.class.getSimpleName();

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "coleccion_webs";
    private static final String DATABASE_TABLE = "webs";

    private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
            + " (_id integer primary key autoincrement, "
            + COL_TITULO + " text not null, "
            + COL_URL + " text not null"
            + ");";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;


    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creando base de datos");
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
            Log.w(TAG, "Actualizando base de datos de la version " + versionAnterior + " a "
                    + versionNueva + ", lo que destruira todos los datos existentes");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db); // en desarrollo, vuelvo a crear BD en vez de cambiar de version
        }
    }

    /**
     * Constructor - recibe el contexto de la base de datos que va a ser
     * abierta o creada. Abre la base de datos. Si no se puede abrir, intenta crear una nueva
     * instancia de la base de datos. Si no se puede crear, lanza una excepción
     * para alertar del fallo
     *
     * @param ctx contexto con el que trabajar
     * @throws SQLException si la base de datos no estuviera ni abierta ni creada
     */
    public WebsDbAdaptor(Context ctx) throws SQLException {
        this.mCtx = ctx;
        dbHelper = new DBHelper(mCtx);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private int boolean2int(boolean b) {
        return b ? 0 : 1;
    }

    public long creaWeb(Web web) {
        ContentValues valoresIniciales = new ContentValues();
        valoresIniciales.put(COL_TITULO, web.getTitulo());
        valoresIniciales.put(COL_URL, web.getUrl());

        return db.insert(DATABASE_TABLE, null, valoresIniciales);
    }

    public boolean borraWeb(long id) {
        Log.i(TAG, "borra web id " + id);
        return db.delete(DATABASE_TABLE, COL_ID + "=" + id, null) > 0;
    }

    public Cursor recuperaTodasLasWebs() {

        return db.query(DATABASE_TABLE, new String[]{COL_ID, COL_TITULO,
                COL_URL}, null, null, null, null, null);
    }

    public Cursor recuperaWeb(long id) throws SQLException {

        Cursor mCursor =

                db.query(true, DATABASE_TABLE, new String[]{COL_ID,
                                COL_TITULO, COL_URL}, COL_ID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public void borraTodasLasWebs() {
        db.delete(DATABASE_TABLE, null, null);
    }

    public boolean actualizaWeb(long id, Web web) {
        Log.i(TAG, "Actualiza web " + id + " " + web);
        ContentValues args = new ContentValues();
        args.put(COL_TITULO, web.getTitulo());
        args.put(COL_URL, web.getUrl());
        return db.update(DATABASE_TABLE, args, COL_ID + "=" + id, null) > 0;
    }
}