package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.model.Formulario;

import static br.org.unesco.appesca.util.ConstantesUNESCO.FORMATO_DATA;


public class FormularioDAO extends BaseDAO<Formulario> {

    public FormularioDAO(Context context){
        super(context, AppescaHelper.TABLE_FORMULARIO, AppescaHelper.COL_FORMULARIO_ID);
    }



    public List<Formulario> listarTodosPorUsuario(int idUsuario) {
        return  listByRawQuery(SELECT_FROM_TABLE()
                                + " WHERE " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ?", new String[]{String.valueOf(idUsuario)});
    }


    public List<Formulario> getFormulariosByUsuario(int idUsuario) {
        return listByRawQuery(SELECT_FROM_TABLE() +
                                    " WHERE " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ?", new String[]{String.valueOf(idUsuario)});
    }

    public List<Formulario> listarPorSituacaoUsuario(int situacao, int idUsuario) {
        return listByRawQuery(SELECT_FROM_TABLE()
                        +   " WHERE " + AppescaHelper.COL_FORMULARIO_SITUACAO + " = ? " +
                            " AND " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ? " +
                            " ORDER BY " + AppescaHelper.COL_FORMULARIO_DATA_APLICACAO +  " DESC " ,
                                 new String[]{String.valueOf(situacao),
                                    String.valueOf(idUsuario)});
    }

    public List<Formulario> listarTodosOffLine(int idUsuario) {
        return listByRawQuery(SELECT_FROM_TABLE()
                        +   " WHERE " + AppescaHelper.COL_FORMULARIO_SITUACAO + " in (-1, 0) " +
                        " AND " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ? " +
                        " ORDER BY " + AppescaHelper.COL_FORMULARIO_SITUACAO +  " ASC " ,
                new String[]{String.valueOf(idUsuario)});
    }


    public Formulario getFormularioPorIdSincronizacao(String idSincronizacao) {
        List<Formulario> lstTmp = listByRawQuery(SELECT_FROM_TABLE()
                        +   " WHERE " + AppescaHelper.COL_FORMULARIO_ID_SINCRONIZACAO + " = ? ",
                new String[]{idSincronizacao});

        if(lstTmp!=null && lstTmp.size()>0){
            return lstTmp.get(0);
        }

        return null;
    }


    public List<Formulario> listarPorSituacaoTipoUsuario(int situacao, int tipoFormulario, int idUsuario) {

       return listByRawQuery(SELECT_FROM_TABLE()
                +       " WHERE " + AppescaHelper.COL_FORMULARIO_SITUACAO + " = ? " +
                        " AND " + AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO + " = ? " +
                        " AND " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ? " ,
                        new String[]{String.valueOf(situacao),
                        String.valueOf(tipoFormulario),
                        String.valueOf(idUsuario)});
    }

    public List<Formulario> getFormulariosByTipoFormulario(int idTipoFormulariosuario) {
       return listByRawQuery(SELECT_FROM_TABLE()
                                                 +  " WHERE " + AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO + " = ?",
                                                     new String[]{String.valueOf(idTipoFormulariosuario)});
    }

    public List<Formulario> getFormulariosByUsuarioAndTipoFormulario(int idUsuario, int idTipoFormulario) {
        return  listByRawQuery(SELECT_FROM_TABLE()
                                    + " WHERE " + AppescaHelper.COL_FORMULARIO_ID_USUARIO + " = ?"
                                    + " AND "+ AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO + " = ?",
                                        new String[]{String.valueOf(idUsuario), String.valueOf(idTipoFormulario)});
    }


    public ContentValues factoryContentValues(Formulario formulario){
        ContentValues values = new ContentValues();
        if(formulario.getId()!=null) {
            values.put(AppescaHelper.COL_FORMULARIO_ID, formulario.getId());
        }
        values.put(AppescaHelper.COL_FORMULARIO_ID_SINCRONIZACAO, formulario.getIdSincronizacao());
        values.put(AppescaHelper.COL_FORMULARIO_NOME, formulario.getNome());
        values.put(AppescaHelper.COL_FORMULARIO_DATA_APLICACAO, getDataFormatada(formulario.getDataAplicacao()));
        values.put(AppescaHelper.COL_FORMULARIO_ID_USUARIO, formulario.getIdUsuario());
        values.put(AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO, formulario.getIdTipoFormulario());
        values.put(AppescaHelper.COL_FORMULARIO_SITUACAO, formulario.getSituacao());

        if(formulario.getLatitude()!=null) {
            values.put(AppescaHelper.COL_LATITUDE, formulario.getLatitude().toString());
        }

        if(formulario.getLongitude()!=null) {
            values.put(AppescaHelper.COL_LONGITUDE, formulario.getLatitude().toString());
        }
        return values;
    }

    public Formulario factoryEntity(Cursor cursor){
        Formulario formulario = new Formulario();
        formulario.setId(getInt(cursor, AppescaHelper.COL_FORMULARIO_ID));
        formulario.setIdSincronizacao(getString(cursor, AppescaHelper.COL_FORMULARIO_ID_SINCRONIZACAO));
        formulario.setNome(getString(cursor, AppescaHelper.COL_FORMULARIO_NOME));
        formulario.setDataAplicacao(getData(cursor, AppescaHelper.COL_FORMULARIO_DATA_APLICACAO));
        formulario.setIdUsuario(getInt(cursor, AppescaHelper.COL_FORMULARIO_ID_USUARIO));
        formulario.setIdTipoFormulario(getInt(cursor, AppescaHelper.COL_FORMULARIO_ID_TIPO_FORMULARIO));
        formulario.setSituacao(getInt(cursor, AppescaHelper.COL_FORMULARIO_SITUACAO));

        String latitude = getString(cursor, AppescaHelper.COL_LATITUDE);
        if(latitude!=null && !latitude.isEmpty()){
            BigDecimal bDLat = new BigDecimal(latitude);
            formulario.setLatitude(bDLat);
        }
        String longitude = getString(cursor, AppescaHelper.COL_LONGITUDE);
        if(longitude!=null && !longitude.isEmpty()){
            BigDecimal bDLong = new BigDecimal(longitude);
            formulario.setLongitude(bDLong);
        }

        return formulario;
    }
}