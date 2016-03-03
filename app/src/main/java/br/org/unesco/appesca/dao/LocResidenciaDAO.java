package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.org.unesco.appesca.model.LocResidencia;

/**
 * Created by yesus
 */
public class LocResidenciaDAO {

    private Context context;
    private AppescaHelper appescaHelper;


    public LocResidenciaDAO(Context context){
        this.context = context;
        appescaHelper = new AppescaHelper(this.context);
    }

    public LocResidencia save(LocResidencia locResidencia){
        if(locResidencia.getId()!=null){
            return updateLocResidencia(locResidencia);
        }else{
           return insertLocResidencia(locResidencia);
        }
    }

    private LocResidencia insertLocResidencia(LocResidencia locResidencia){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_LOCAL_RESID, locResidencia.getLocalResid());
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_RESID_UN_CONSERV, locResidencia.getResidUnConserv());
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_ID_FORMULARIO, locResidencia.getIdFormulario());

        long id = db.insert(AppescaHelper.TABLE_LOC_RESIDENCIA, null, values);

        locResidencia.setId((int)id);

        if (db != null && db.isOpen()) {
            db.close();
        }

        return locResidencia;
    }


    private LocResidencia updateLocResidencia(LocResidencia locResidencia){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_ID, locResidencia.getId());
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_LOCAL_RESID, locResidencia.getLocalResid());
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_RESID_UN_CONSERV, locResidencia.getResidUnConserv());
        values.put(AppescaHelper.COL_LOC_RESIDENCIA_ID_FORMULARIO, locResidencia.getIdFormulario());

        db.update(AppescaHelper.TABLE_LOC_RESIDENCIA, values,
                AppescaHelper.COL_LOC_RESIDENCIA_ID + " = ?",
                new String[]{String.valueOf(locResidencia.getId())});

        if (db != null && db.isOpen()) {
            db.close();
        }

        return locResidencia;
    }


    public LocResidencia findByIdFormulario(int idFormulario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_LOC_RESIDENCIA_ID + " , " +
                        AppescaHelper.COL_LOC_RESIDENCIA_LOCAL_RESID + " , " +
                        AppescaHelper.COL_LOC_RESIDENCIA_RESID_UN_CONSERV + " , " +
                        AppescaHelper.COL_LOC_RESIDENCIA_ID_FORMULARIO +
                        " FROM " + AppescaHelper.TABLE_LOC_RESIDENCIA +
                        " WHERE " + AppescaHelper.COL_LOC_RESIDENCIA_ID_FORMULARIO + " = ?", new String[]{String.valueOf(idFormulario)});
       if(cursor.moveToFirst()) {
           LocResidencia locResidencia = new LocResidencia();
           locResidencia.setId(cursor.getInt(0));
           locResidencia.setLocalResid(cursor.getInt(1));
           locResidencia.setResidUnConserv(cursor.getInt(2) == 1);
           locResidencia.setIdFormulario(cursor.getInt(3));

           if (cursor != null && !cursor.isClosed()) {
               cursor.close();
               db.close();
           }

           return locResidencia;
       }else{

           if (cursor != null && !cursor.isClosed()) {
               cursor.close();
               db.close();
           }

           return null;
       }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        appescaHelper.close();
    }
}