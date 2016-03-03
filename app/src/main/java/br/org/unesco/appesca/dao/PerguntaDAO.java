package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.model.Pergunta;

/**
 * Created by marcosmagalhaes on 09/01/16.
 */
public class PerguntaDAO {

    private Context context;
    private AppescaHelper appescaHelper;

    public PerguntaDAO(Context context){
        this.context = context;
        appescaHelper = new AppescaHelper(this.context);
    }

    public Pergunta insertPergunta(Pergunta pergunta){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_PERGUNTA_BOOLEANA, pergunta.getBooleana());
        values.put(AppescaHelper.COL_PERGUNTA_RESP_BOOLEANA, pergunta.getRespBooleana());
        values.put(AppescaHelper.COL_PERGUNTA_ORDEM, pergunta.getOrdem());
        values.put(AppescaHelper.COL_PERGUNTA_ID_QUESTAO, pergunta.getIdQuestao());

        long id = db.insert(AppescaHelper.TABLE_PERGUNTA, null, values);

        if (db != null && db.isOpen()) {
            db.close();
        }

        return findPerguntaByQuestao((int) id);
    }

    public Pergunta findPerguntaByQuestao(int idPergunta) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_PERGUNTA_ID + " , " +
                        AppescaHelper.COL_PERGUNTA_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_RESP_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_ORDEM + " , " +
                        AppescaHelper.COL_PERGUNTA_ID_QUESTAO +
                        " FROM " + AppescaHelper.TABLE_PERGUNTA +
                        " WHERE " + AppescaHelper.COL_PERGUNTA_ID + " = ?", new String[]{String.valueOf(idPergunta)});

        Pergunta pergunta = null;
        if(cursor.moveToFirst()){
            pergunta = new Pergunta();
            pergunta.setId(cursor.getInt(0));
            if(cursor.getInt(1) == 1)
                pergunta.setBooleana(true);
            if(cursor.getInt(2) == 2)
                pergunta.setRespBooleana(true);
            pergunta.setOrdem(cursor.getInt(3));
            pergunta.setIdQuestao(cursor.getInt(4));

            RespostaDAO respostaDAO = new RespostaDAO(context);
            pergunta.setRespostas(respostaDAO.findRespostasByPergunta(pergunta.getId()));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return pergunta;
    }

    public void updatePergunta(Pergunta pergunta){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_PERGUNTA_ID, pergunta.getId());
        values.put(AppescaHelper.COL_PERGUNTA_BOOLEANA, pergunta.getBooleana());
        values.put(AppescaHelper.COL_PERGUNTA_RESP_BOOLEANA, pergunta.getRespBooleana());
        values.put(AppescaHelper.COL_PERGUNTA_ORDEM, pergunta.getOrdem());
        values.put(AppescaHelper.COL_PERGUNTA_ID_QUESTAO, pergunta.getIdQuestao());

        db.update(AppescaHelper.TABLE_PERGUNTA, values,
                AppescaHelper.COL_PERGUNTA_ID + " = ?",
                new String[]{String.valueOf(pergunta.getId())});

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void deletePerguntaById(int idPergunta){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        db.delete(AppescaHelper.TABLE_PERGUNTA, AppescaHelper.COL_PERGUNTA_ID + " = ?",
                new String[]{String.valueOf(idPergunta)});

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public List<Pergunta> findAllPerguntas() {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_PERGUNTA_ID + " , " +
                        AppescaHelper.COL_PERGUNTA_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_RESP_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_ORDEM + " , " +
                        AppescaHelper.COL_PERGUNTA_ID_QUESTAO +
                        " FROM " + AppescaHelper.TABLE_PERGUNTA, null);
        cursor.moveToFirst();

        List<Pergunta> perguntaList = new ArrayList<Pergunta>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Pergunta pergunta = new Pergunta();
            pergunta.setId(cursor.getInt(0));
            if(cursor.getInt(1) == 1)
                pergunta.setBooleana(true);
            if(cursor.getInt(2) == 2)
                pergunta.setRespBooleana(true);
            pergunta.setOrdem(cursor.getInt(3));
            pergunta.setIdQuestao(cursor.getInt(4));
            perguntaList.add(pergunta);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return perguntaList;
    }

    public List<Pergunta> findPerguntasByQuestao(int idQuestao) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_PERGUNTA_ID + " , " +
                        AppescaHelper.COL_PERGUNTA_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_RESP_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_ORDEM + " , " +
                        AppescaHelper.COL_PERGUNTA_ID_QUESTAO +
                        " FROM " + AppescaHelper.TABLE_PERGUNTA +
                        " WHERE " + AppescaHelper.COL_PERGUNTA_ID_QUESTAO + " = ?", new String[]{String.valueOf(idQuestao)});
        cursor.moveToFirst();

        List<Pergunta> perguntaList = new ArrayList<Pergunta>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Pergunta pergunta = new Pergunta();
            pergunta.setId(cursor.getInt(0));
            if(cursor.getInt(1) == 1)
                pergunta.setBooleana(true);
            if(cursor.getInt(2) == 2)
                pergunta.setRespBooleana(true);
            pergunta.setOrdem(cursor.getInt(3));
            pergunta.setIdQuestao(cursor.getInt(4));

            RespostaDAO respostaDAO = new RespostaDAO(context);
            pergunta.setRespostas(respostaDAO.findRespostasByPergunta(pergunta.getId()));

            perguntaList.add(pergunta);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return perguntaList;
    }

    public Pergunta findPerguntaByOrdemIdQuestao(int ordem, int idQuestao) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_PERGUNTA_ID + " , " +
                        AppescaHelper.COL_PERGUNTA_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_RESP_BOOLEANA + " , " +
                        AppescaHelper.COL_PERGUNTA_ORDEM + " , " +
                        AppescaHelper.COL_PERGUNTA_ID_QUESTAO +
                        " FROM " + AppescaHelper.TABLE_PERGUNTA +
                        " WHERE " + AppescaHelper.COL_PERGUNTA_ORDEM + " = ?"+
                        " AND " + AppescaHelper.COL_PERGUNTA_ID_QUESTAO + " = ?", new String[]{String.valueOf(ordem), String.valueOf(idQuestao)});

        Pergunta pergunta = null;
        if(cursor.moveToFirst()){
            pergunta = new Pergunta();
            pergunta.setId(cursor.getInt(0));
            if(cursor.getInt(1) == 1)
                pergunta.setBooleana(true);
            if(cursor.getInt(2) == 2)
                pergunta.setRespBooleana(true);
            pergunta.setOrdem(cursor.getInt(3));
            pergunta.setIdQuestao(cursor.getInt(4));

            RespostaDAO respostaDAO = new RespostaDAO(context);
            pergunta.setRespostas(respostaDAO.findRespostasByPergunta(pergunta.getId()));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return pergunta;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        appescaHelper.close();
    }
}
