package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.model.Formulario;

import static br.org.unesco.appesca.util.ConstantesUNESCO.FORMATO_DATA;

/**
 * Created by marcosmagalhaes on 07/01/2015.
 */
public class FormularioDAO {

//    private Context context;
    private AppescaHelper appescaHelper;


    public FormularioDAO(Context context){
//        this.context = context;
        appescaHelper = new AppescaHelper(context);
    }

    public Formulario insertFormulario(Formulario formulario){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_FORMULARIO_NOME, formulario.getNome());
        String dateFormat = getDataFormatada(formulario);
        values.put(AppescaHelper.COL_FORMULARIO_DATA_APLICACAO, dateFormat);
        values.put(AppescaHelper.COL_FORMULARIO_ID_USUARIO, formulario.getIdUsuario());
        values.put(AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO, formulario.getIdTipoFormulario());
        values.put(AppescaHelper.COL_FORMULARIO_SITUACAO, formulario.getSituacao());

        long id = db.insert(AppescaHelper.TABLE_FORMULARIO, null, values);

        if (db != null && db.isOpen()) {
            db.close();
        }

        return getFormularioById((int)id);
    }


    public void updateFormulario(Formulario formulario){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_FORMULARIO_ID, formulario.getId());
        values.put(AppescaHelper.COL_FORMULARIO_NOME, formulario.getNome());
        String dateFormat = getDataFormatada(formulario);
        values.put(AppescaHelper.COL_FORMULARIO_DATA_APLICACAO, dateFormat);
        values.put(AppescaHelper.COL_FORMULARIO_ID_USUARIO, formulario.getIdUsuario());
        values.put(AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO, formulario.getIdTipoFormulario());
        values.put(AppescaHelper.COL_FORMULARIO_SITUACAO, formulario.getSituacao());

        db.update(AppescaHelper.TABLE_FORMULARIO, values,
                AppescaHelper.COL_FORMULARIO_ID + " = ?",
                new String[]{String.valueOf(formulario.getId())});


        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void deleteFormularioById(int idFormulario){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        db.delete(AppescaHelper.TABLE_FORMULARIO, AppescaHelper.COL_FORMULARIO_ID + " = ?",
                new String[]{String.valueOf(idFormulario)});


        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public List<Formulario> listarTodosPorUsuario(int idUsuario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ?", new String[]{String.valueOf(idUsuario)});
        cursor.moveToFirst();

        List<Formulario> formularioList = new ArrayList<Formulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Formulario formulario = new Formulario();
            formulario.setId(cursor.getInt(0));
            formulario.setNome(cursor.getString(1));
            try {
                setDataFormatada(cursor, formulario);
            } catch (ParseException e) {
                Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
            }
            formulario.setIdUsuario(cursor.getInt(3));
            formulario.setIdTipoFormulario(cursor.getInt(4));

            formularioList.add(formulario);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formularioList;
    }
    public Formulario getFormularioById(int idFormulario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_ID + " = ?", new String[]{String.valueOf(idFormulario)});
        cursor.moveToFirst();

        Formulario formulario = new Formulario();
        formulario.setId(cursor.getInt(0));
        formulario.setNome(cursor.getString(1));
        try {
            setDataFormatada(cursor, formulario);
        } catch (ParseException e) {
            Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
        }
        formulario.setIdUsuario(cursor.getInt(3));
        formulario.setIdTipoFormulario(cursor.getInt(4));


        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formulario;
    }


    public List<Formulario> getFormulariosByUsuario(int idUsuario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ?", new String[]{String.valueOf(idUsuario)});
        cursor.moveToFirst();

        List<Formulario> formularioList = new ArrayList<Formulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Formulario formulario = new Formulario();
            formulario.setId(cursor.getInt(0));
            formulario.setNome(cursor.getString(1));
            try {
                setDataFormatada(cursor, formulario);
            } catch (ParseException e) {
                Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
            }
            formulario.setIdUsuario(cursor.getInt(3));
            formulario.setIdTipoFormulario(cursor.getInt(4));

            formularioList.add(formulario);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formularioList;
    }


    /**
     * @author yesus
     * @param situacao
     * @param tipoFormulario
     * @param idUsuario
     * @return
     */
    public List<Formulario> listarPorSituacaoUsuario(int situacao, int idUsuario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_SITUACAO + " = ? " +
                        " AND " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ? " +
                        " ORDER BY " + AppescaHelper.COL_FORMULARIO_DATA_APLICACAO +  " DESC " ,
                new String[]{String.valueOf(situacao),
                        String.valueOf(idUsuario)});
        cursor.moveToFirst();

        List<Formulario> formularioList = new ArrayList<Formulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Formulario formulario = new Formulario();
            formulario.setId(cursor.getInt(0));
            formulario.setNome(cursor.getString(1));
            try {
                setDataFormatada(cursor, formulario);
            } catch (ParseException e) {
                Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
            }
            formulario.setIdUsuario(cursor.getInt(3));
            formulario.setIdTipoFormulario(cursor.getInt(4));

            formularioList.add(formulario);
            cursor.moveToNext();
        }


        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formularioList;
    }

    /**
     * @author yesus
     * @param situacao
     * @param tipoFormulario
     * @param idUsuario
     * @return
     */
    public List<Formulario> listarPorSituacaoTipoUsuario(int situacao, int tipoFormulario, int idUsuario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_SITUACAO + " = ? " +
                        " AND " + AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO + " = ? " +
                        " AND " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ? " ,
                                new String[]{String.valueOf(situacao),
                                             String.valueOf(tipoFormulario),
                                             String.valueOf(idUsuario)});
        cursor.moveToFirst();

        List<Formulario> formularioList = new ArrayList<Formulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Formulario formulario = new Formulario();
            formulario.setId(cursor.getInt(0));
            formulario.setNome(cursor.getString(1));
            try {
                setDataFormatada(cursor, formulario);
            } catch (ParseException e) {
                Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
            }
            formulario.setIdUsuario(cursor.getInt(3));
            formulario.setIdTipoFormulario(cursor.getInt(4));

            formularioList.add(formulario);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formularioList;
    }

    public List<Formulario> getFormulariosByTipoFormulario(int idTipoFormulariosuario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO + " = ?", new String[]{String.valueOf(idTipoFormulariosuario)});
        cursor.moveToFirst();

        List<Formulario> formularioList = new ArrayList<Formulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Formulario formulario = new Formulario();
            formulario.setId(cursor.getInt(0));
            formulario.setNome(cursor.getString(1));
            try {
                setDataFormatada(cursor, formulario);
            } catch (ParseException e) {
                Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
            }
            formulario.setIdUsuario(cursor.getInt(3));
            formulario.setIdTipoFormulario(cursor.getInt(4));

            formularioList.add(formulario);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formularioList;
    }

    public List<Formulario> getFormulariosByUsuarioAndTipoFormulario(int idUsuario, int idTipoFormulario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_FORMULARIO_ID + " , " +
                        AppescaHelper.COL_FORMULARIO_NOME + " , " +
                        AppescaHelper.COL_FORMULARIO_DATA_APLICACAO + " , " +
                        AppescaHelper.COL_FORMULARIO_ID_USUARIO+ " , " +
                        AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO+
                        " FROM " + AppescaHelper.TABLE_FORMULARIO +
                        " WHERE " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ?"+
                        " AND "+ AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO + " = ?", new String[]{String.valueOf(idUsuario), String.valueOf(idTipoFormulario)});
        cursor.moveToFirst();

        List<Formulario> formularioList = new ArrayList<Formulario>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Formulario formulario = new Formulario();
            formulario.setId(cursor.getInt(0));
            formulario.setNome(cursor.getString(1));
            try {
                setDataFormatada(cursor, formulario);
            } catch (ParseException e) {
                Log.e("FormularioDAO", "Erro ao efetuar o parse da data.");
            }
            formulario.setIdUsuario(cursor.getInt(3));
            formulario.setIdTipoFormulario(cursor.getInt(4));

            formularioList.add(formulario);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return formularioList;
    }

    private void setDataFormatada(Cursor cursor, Formulario formulario) throws ParseException {
        formulario.setDataAplicacao(new SimpleDateFormat(FORMATO_DATA).parse(cursor.getString(2)));
    }


    @NonNull
    private String getDataFormatada(Formulario formulario) {
        return new SimpleDateFormat(FORMATO_DATA).format(formulario.getDataAplicacao());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        appescaHelper.close();
    }
}