package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.model.TipoFormulario;

/**
 * Created by marcosmagalhaes on 06/01/2015.
 */
public class TipoFormularioDAO {

    private Context context;
    private AppescaHelper appescaHelper;

    public TipoFormularioDAO(Context context){
        this.context = context;
        appescaHelper = new AppescaHelper(this.context);
    }

    public void insertTipoFormulario(TipoFormulario tipoFormulario){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_NOME, tipoFormulario.getNome());
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_DESCRICAO, tipoFormulario.getDescricao());
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_ORDEM, tipoFormulario.getOrdem());

        db.insert(AppescaHelper.TABLE_TIPO_FORMULARIO, null,values);

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void updateTipoFormulario(TipoFormulario tipoFormulario){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_ID, tipoFormulario.getId());
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_NOME, tipoFormulario.getNome());
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_DESCRICAO, tipoFormulario.getDescricao());
        values.put(AppescaHelper.COL_TIPO_FORMULARIO_ORDEM, tipoFormulario.getOrdem());

        db.update(AppescaHelper.TABLE_TIPO_FORMULARIO, values,
                AppescaHelper.COL_TIPO_FORMULARIO_ID + " = ?",
                new String[]{String.valueOf(tipoFormulario.getId())});

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void deleteTipoFormularioById(int idTipoFormulario){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        db.delete(AppescaHelper.TABLE_TIPO_FORMULARIO, AppescaHelper.COL_TIPO_FORMULARIO_ID + " = ?",
                new String[]{String.valueOf(idTipoFormulario)});

        if (db!=null && db.isOpen()) {
            db.close();
        }
    }

    public List<TipoFormulario> getAllTipoFormulario() {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_TIPO_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_TIPO_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_TIPO_FORMULARIO_DESCRICAO + " , " +
                        AppescaHelper.COL_TIPO_FORMULARIO_ORDEM+
                        " FROM " + AppescaHelper.TABLE_TIPO_FORMULARIO, null);
        cursor.moveToFirst();

        List<TipoFormulario> tipoFormularioList = new ArrayList<TipoFormulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            TipoFormulario tipoFormulario = new TipoFormulario();
            tipoFormulario.setId(cursor.getInt(0));
            tipoFormulario.setNome(cursor.getString(1));
            tipoFormulario.setDescricao(cursor.getString(2));
            tipoFormulario.setOrdem(cursor.getInt(3));

            tipoFormularioList.add(tipoFormulario);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }


        return tipoFormularioList;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        appescaHelper.close();
    }
}