package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import br.org.unesco.appesca.model.Usuario;

import static br.org.unesco.appesca.dao.AppescaHelper.COL_EQUIPE_ID;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_EMAIL;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_ENDERECO;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_ID;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_IMAGEM;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_LOGIN;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_NOME;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_PERFIL;
import static br.org.unesco.appesca.dao.AppescaHelper.COL_USUARIO_SENHA;
import static br.org.unesco.appesca.dao.AppescaHelper.TABLE_EQUIPE;
import static br.org.unesco.appesca.dao.AppescaHelper.TABLE_MEMBROS_EQUIPE;
import static br.org.unesco.appesca.dao.AppescaHelper.TABLE_USUARIO;

/**
 * author yesus
 */
public class UsuarioDAO extends BaseDAO<Usuario> {

    private Context context;
    private AppescaHelper appescaHelper;

    private String SELECT_USUARIO_JOIN_EQUIPE =
                SELECT_FROM_TABLE()
                            + " INNER JOIN "
                            + TABLE_MEMBROS_EQUIPE + " ON " +
                                TABLE_USUARIO + "." + COL_USUARIO_ID + "=" +  TABLE_MEMBROS_EQUIPE + "." + COL_USUARIO_ID
                            + " INNER JOIN "
                            + TABLE_EQUIPE + " ON " +
                                TABLE_EQUIPE + "." + COL_EQUIPE_ID + "=" +  TABLE_MEMBROS_EQUIPE + "." + COL_EQUIPE_ID
                            + " WHERE " + TABLE_MEMBROS_EQUIPE + "." + COL_EQUIPE_ID + " = ?";

    public UsuarioDAO(Context context) {
        super(context, TABLE_USUARIO, COL_USUARIO_ID);
    }

    @NonNull
    protected ContentValues factoryContentValues(Usuario usuario) {
        ContentValues values = new ContentValues();
        if(usuario.getId()!=null) {
            values.put(COL_USUARIO_ID, usuario.getId());
        }
        values.put(COL_USUARIO_NOME, usuario.getNome());
        values.put(COL_USUARIO_ENDERECO, usuario.getEndereco());
        values.put(COL_USUARIO_LOGIN, usuario.getLogin());
        values.put(COL_USUARIO_SENHA, usuario.getSenha());
        values.put(COL_USUARIO_PERFIL, usuario.getPerfil());
        values.put(COL_USUARIO_IMAGEM, usuario.getImagem());
        values.put(COL_USUARIO_EMAIL, usuario.getEmail());
        return values;
    }

    @Override
    protected Usuario factoryEntity(Cursor cursor) {
        Usuario u = new Usuario();
        u.setId(getInt(cursor, COL_USUARIO_ID));
        u.setNome(getString(cursor, COL_USUARIO_NOME));
        u.setEndereco(getString(cursor, COL_USUARIO_ENDERECO));
        u.setLogin(getString(cursor, COL_USUARIO_LOGIN));
        u.setSenha(getString(cursor, COL_USUARIO_SENHA));
        u.setPerfil(getString(cursor, COL_USUARIO_PERFIL));
        u.setImagem(getBlob(cursor, COL_USUARIO_IMAGEM));
        u.setEmail(getString(cursor, COL_USUARIO_EMAIL));
        return u;
    }

    public List<Usuario> listUsuariosByEquipe(Integer idEquipe) {
        if(idEquipe==null) return null;
        return listByRawQuery(SELECT_USUARIO_JOIN_EQUIPE,  new String[]{idEquipe.toString()});
    }

}
