package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import br.org.unesco.appesca.model.Resposta;

import static br.org.unesco.appesca.dao.AppescaHelper.COL_RESPOSTA_ID;
import static br.org.unesco.appesca.dao.AppescaHelper.TABLE_RESPOSTA;

public class RespostaDAO extends BaseDAO<Resposta>  {

    public RespostaDAO(Context context){
        super(context, TABLE_RESPOSTA, COL_RESPOSTA_ID);
    }

    @NonNull
    protected ContentValues factoryContentValues(Resposta resposta) {
        ContentValues values = new ContentValues();

        if(resposta.getId()!=null) {
            values.put(AppescaHelper.COL_RESPOSTA_ID, resposta.getId());
        }

        values.put(AppescaHelper.COL_RESPOSTA_OPCAO, resposta.getOpcao());
        values.put(AppescaHelper.COL_RESPOSTA_TEXTO, resposta.getTexto());
        values.put(AppescaHelper.COL_RESPOSTA_AUDIO, resposta.getAudio());
        values.put(AppescaHelper.COL_RESPOSTA_ORDEM, resposta.getOrdem());
        values.put(AppescaHelper.COL_RESPOSTA_ID_PERGUNTA, resposta.getIdPergunta());
        return values;
    }

    @Override
    protected Resposta factoryEntity(Cursor cursor) {
        Resposta resposta = new Resposta();
        resposta.setId(getInt(cursor, AppescaHelper.COL_RESPOSTA_ID));
        resposta.setOpcao(getInt(cursor, AppescaHelper.COL_RESPOSTA_OPCAO));
        resposta.setTexto(getString(cursor, AppescaHelper.COL_RESPOSTA_TEXTO));
        resposta.setAudio(getBlob(cursor, AppescaHelper.COL_RESPOSTA_AUDIO));
        resposta.setOrdem(getInt(cursor, AppescaHelper.COL_RESPOSTA_ORDEM));
        resposta.setIdPergunta(getInt(cursor, AppescaHelper.COL_RESPOSTA_ID_PERGUNTA));

        return resposta;
    }

    public List<Resposta> findRespostasByPergunta(int idPergunta) {
        return listByRawQuery(
                SELECT_FROM_TABLE() +
                        " WHERE " + AppescaHelper.COL_RESPOSTA_ID_PERGUNTA + " = ?",
                    new String[]{String.valueOf(idPergunta)});
    }

    public Resposta findRespostaByOrdemIdPergunta(int ordem, int idPergunta) {
        List<Resposta> lst = listByRawQuery(
                SELECT_FROM_TABLE() +
                        " WHERE " + AppescaHelper.COL_RESPOSTA_ORDEM + " = ?" +
                        " AND " + AppescaHelper.COL_RESPOSTA_ID_PERGUNTA + " = ?", new String[]{String.valueOf(ordem), String.valueOf(idPergunta)});

        if(lst!=null && lst.size()>0){
            return lst.get(0);
        }else{
            return null;
        }
    }
}
