package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.math.BigDecimal;

import br.org.unesco.appesca.model.LocalizacaoUsuario;
import br.org.unesco.appesca.model.Usuario;

import static br.org.unesco.appesca.dao.AppescaHelper.COL_LOCALIZACAO_USUARIO_ID;
import static br.org.unesco.appesca.dao.AppescaHelper.TABLE_LOCALIZACAO_USUARIO;

public class LocalizacaoUsuarioDAO extends BaseDAO<LocalizacaoUsuario>  {

    public LocalizacaoUsuarioDAO(Context context){
        super(context, TABLE_LOCALIZACAO_USUARIO, COL_LOCALIZACAO_USUARIO_ID);
    }

    @NonNull
    protected ContentValues factoryContentValues(LocalizacaoUsuario localizacaoUsuario) {
        ContentValues values = new ContentValues();

        if(localizacaoUsuario.getId()!=null) {
            values.put(AppescaHelper.COL_LOCALIZACAO_USUARIO_ID, localizacaoUsuario.getId());
        }

        if(localizacaoUsuario.getUsuario()!=null && localizacaoUsuario.getUsuario().getId()!=null) {
            values.put(AppescaHelper.COL_USUARIO_ID, localizacaoUsuario.getUsuario().getId());
        }

        values.put(AppescaHelper.COL_DATA_REGISTRO, getDataFormatada(localizacaoUsuario.getDataRegistro()));

        if(localizacaoUsuario.getLatitude()!=null) {
            values.put(AppescaHelper.COL_LATITUDE, localizacaoUsuario.getLatitude().toString());
        }

        if(localizacaoUsuario.getLongitude()!=null) {
            values.put(AppescaHelper.COL_LONGITUDE, localizacaoUsuario.getLongitude().toString());
        }

        values.put(AppescaHelper.COL_PROVIDED, localizacaoUsuario.getProvided());

        return values;
    }

    @Override
    protected LocalizacaoUsuario factoryEntity(Cursor cursor) {
        LocalizacaoUsuario localizacaoUsuario = new LocalizacaoUsuario();

        localizacaoUsuario.setId(getInt(cursor, AppescaHelper.COL_LOCALIZACAO_USUARIO_ID));

        Integer idUsuario = getInt(cursor, AppescaHelper.COL_USUARIO_ID);
        if(idUsuario!=null){
            Usuario usr = new Usuario();
            usr.setId(idUsuario);
            localizacaoUsuario.setUsuario(usr);
        }

        localizacaoUsuario.setDataRegistro(getData(cursor, AppescaHelper.COL_DATA_REGISTRO));

        String latitude = getString(cursor, AppescaHelper.COL_LATITUDE);
        if(latitude!=null && !latitude.isEmpty()){
            BigDecimal bDLat = new BigDecimal(latitude);
            localizacaoUsuario.setLatitude(bDLat);
        }
        String longitude = getString(cursor, AppescaHelper.COL_LONGITUDE);
        if(longitude!=null && !longitude.isEmpty()){
            BigDecimal bDLong = new BigDecimal(longitude);
            localizacaoUsuario.setLongitude(bDLong);
        }

        localizacaoUsuario.setProvided(getString(cursor, AppescaHelper.COL_PROVIDED));

        return localizacaoUsuario;
    }

    public void deleteAll(){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();
        db.delete(TABLE_LOCALIZACAO_USUARIO,null,null);
        if(db!=null && db.isOpen()){
            db.close();
        }
    }

}
