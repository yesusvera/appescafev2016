package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import br.org.unesco.appesca.model.Equipe;
import br.org.unesco.appesca.model.Usuario;

import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_COORDENADOR_ID;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_DATA_CRIACAO;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_ID;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_NOME;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_REGULAMENTO;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_ULTIMOS_AVISOS;

/**
 * author yesus
 */
public class EquipeDAO extends BaseDAO<Equipe>{

    public EquipeDAO(Context context){
        super(context, AppescaHelper.TABLE_EQUIPE, AppescaHelper.COL_EQUIPE_ID);
    }

    @NonNull
    protected Equipe factoryEntity(Cursor cursor) {
        Equipe equipe = new Equipe();
        equipe.setId(getInt(cursor, COL_EQUIPE_ID));
        equipe.setNome(getString(cursor, COL_EQUIPE_NOME));
        equipe.setUltimosAvisos(getString(cursor, COL_EQUIPE_ULTIMOS_AVISOS));
        equipe.setRegulamento(getString(cursor, COL_EQUIPE_REGULAMENTO));
        equipe.setData(getData(cursor, COL_EQUIPE_DATA_CRIACAO));

        //TABELAS RELACIONADAS
        UsuarioDAO usuarioDao = new UsuarioDAO(getContext());
        Integer idCoordenador = getInt(cursor, COL_EQUIPE_COORDENADOR_ID);
        Usuario coordenador = usuarioDao.findById(idCoordenador);
        equipe.setCoordenador(coordenador);

        List<Usuario> listaMembros= usuarioDao.listUsuariosByEquipe(equipe.getId());
        equipe.setListaMembrosEquipe(listaMembros);

        return equipe;
    }

    @NonNull
    protected ContentValues factoryContentValues(Equipe equipe) {
        ContentValues values = new ContentValues();
        if(equipe.getId()!=null) {
            values.put(COL_EQUIPE_ID, equipe.getId());
        }
        values.put(COL_EQUIPE_NOME, equipe.getNome());
        values.put(COL_EQUIPE_ULTIMOS_AVISOS, equipe.getUltimosAvisos());
        values.put(COL_EQUIPE_REGULAMENTO, equipe.getRegulamento());
        values.put(COL_EQUIPE_COORDENADOR_ID, equipe.getCoordenador().getId());
        values.put(COL_EQUIPE_DATA_CRIACAO, getDataFormatada(equipe.getData()));
        return values;
    }
}