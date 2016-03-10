package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.model.Questao;

public class QuestaoDAO {

    private Context context;
    private AppescaHelper appescaHelper;

    public QuestaoDAO(Context context){
        this.context = context;
        appescaHelper = new AppescaHelper(this.context);
    }

    public Questao insertQuestao(Questao questao){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_QUESTAO_TITULO, questao.getTitulo());
        values.put(AppescaHelper.COL_QUESTAO_ORDEM, questao.getOrdem());
        values.put(AppescaHelper.COL_QUESTAO_ID_FORMULARIO, questao.getIdFormulario());

        long id = db.insert(AppescaHelper.TABLE_QUESTAO, null,values);

        if (db != null && db.isOpen()) {
            db.close();
        }

        return getQuestaoById((int)id);
    }

    public void updateQuestao(Questao questao){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AppescaHelper.COL_QUESTAO_ID, questao.getId());
        values.put(AppescaHelper.COL_QUESTAO_TITULO, questao.getTitulo());
        values.put(AppescaHelper.COL_QUESTAO_ORDEM, questao.getOrdem());
        values.put(AppescaHelper.COL_QUESTAO_ID_FORMULARIO, questao.getIdFormulario());

        db.update(AppescaHelper.TABLE_QUESTAO, values,
                AppescaHelper.COL_QUESTAO_ID + " = ?",
                new String[]{String.valueOf(questao.getId())});

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void deleteQuestaoById(int idQuestao){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        db.delete(AppescaHelper.TABLE_QUESTAO, AppescaHelper.COL_QUESTAO_ID + " = ?",
                new String[]{String.valueOf(idQuestao)});

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public List<Questao> getAllQuestao() {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_QUESTAO_ID + " , " +
                        AppescaHelper.COL_QUESTAO_TITULO + " , " +
                        AppescaHelper.COL_QUESTAO_ORDEM + " , " +
                        AppescaHelper.COL_QUESTAO_ID_FORMULARIO +
                        " FROM " + AppescaHelper.TABLE_QUESTAO, null);
        cursor.moveToFirst();

        List<Questao> questaoList = new ArrayList<Questao>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Questao questao = new Questao();
            questao.setId(cursor.getInt(0));
            questao.setTitulo(cursor.getString(1));
            questao.setOrdem(cursor.getInt(2));
            questao.setIdFormulario(cursor.getInt(3));

            questaoList.add(questao);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return questaoList;
    }

    public Questao getQuestaoById(int idQuestao) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_QUESTAO_ID + " , " +
                        AppescaHelper.COL_QUESTAO_TITULO + " , " +
                        AppescaHelper.COL_QUESTAO_ORDEM + " , " +
                        AppescaHelper.COL_QUESTAO_ID_FORMULARIO +
                        " FROM " + AppescaHelper.TABLE_QUESTAO +
                        " WHERE " + AppescaHelper.COL_QUESTAO_ID + " = ?", new String[]{String.valueOf(idQuestao)});
        cursor.moveToFirst();

        Questao questao = new Questao();
        questao.setId(cursor.getInt(0));
        questao.setTitulo(cursor.getString(1));
        questao.setOrdem(cursor.getInt(2));
        questao.setIdFormulario(cursor.getInt(3));

        PerguntaDAO respostaDAO = new PerguntaDAO(context);
        questao.setListaPerguntas(respostaDAO.findPerguntasByQuestao(questao.getId()));

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return questao;
    }

    public List<Questao> getQuestoesByFormulario(int idFormulario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_QUESTAO_ID + " , " +
                        AppescaHelper.COL_QUESTAO_TITULO + " , " +
                        AppescaHelper.COL_QUESTAO_ORDEM + " , " +
                        AppescaHelper.COL_QUESTAO_ID_FORMULARIO +
                        " FROM " + AppescaHelper.TABLE_QUESTAO +
                        " WHERE " + AppescaHelper.COL_QUESTAO_ID_FORMULARIO + " = ?", new String[]{String.valueOf(idFormulario)});
        cursor.moveToFirst();

        List<Questao> questaoList = new ArrayList<Questao>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Questao questao = new Questao();
            questao.setId(cursor.getInt(0));
            questao.setTitulo(cursor.getString(1));
            questao.setOrdem(cursor.getInt(2));
            questao.setIdFormulario(cursor.getInt(3));

//            PerguntaDAO respostaDAO = new PerguntaDAO(context);
//            questao.setPerguntas(respostaDAO.findPerguntasByQuestao(questao.getId()));

            questaoList.add(questao);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }
        return questaoList;
    }


    public List<Questao> getQuestoesRespostasByFormulario(int idFormulario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_QUESTAO_ID + " , " +
                        AppescaHelper.COL_QUESTAO_TITULO + " , " +
                        AppescaHelper.COL_QUESTAO_ORDEM + " , " +
                        AppescaHelper.COL_QUESTAO_ID_FORMULARIO +
                        " FROM " + AppescaHelper.TABLE_QUESTAO +
                        " WHERE " + AppescaHelper.COL_QUESTAO_ID_FORMULARIO + " = ?", new String[]{String.valueOf(idFormulario)});
        cursor.moveToFirst();

        List<Questao> questaoList = new ArrayList<Questao>();

        PerguntaDAO respostaDAO = new PerguntaDAO(context);

        for (int i = 0; i < cursor.getCount(); i++) {
            Questao questao = new Questao();
            questao.setId(cursor.getInt(0));
            questao.setTitulo(cursor.getString(1));
            questao.setOrdem(cursor.getInt(2));
            questao.setIdFormulario(cursor.getInt(3));

            questao.setListaPerguntas(respostaDAO.findPerguntasByQuestao(questao.getId()));

            questaoList.add(questao);
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }
        return questaoList;
    }


    public Questao findQuestaoByOrdemIdFormulario(int ordem, int idFormulario) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + AppescaHelper.COL_QUESTAO_ID + " , " +
                        AppescaHelper.COL_QUESTAO_TITULO + " , " +
                        AppescaHelper.COL_QUESTAO_ORDEM + " , " +
                        AppescaHelper.COL_QUESTAO_ID_FORMULARIO +
                        " FROM " + AppescaHelper.TABLE_QUESTAO +
                        " WHERE " + AppescaHelper.COL_QUESTAO_ID_FORMULARIO + " = ?" +
                        " AND " + AppescaHelper.COL_QUESTAO_ORDEM + " = ?", new String[]{String.valueOf(idFormulario), String.valueOf(ordem)});

        Questao questao = null;
        if(cursor.moveToFirst()){
            questao = new Questao();
            questao.setId(cursor.getInt(0));
            questao.setTitulo(cursor.getString(1));
            questao.setOrdem(cursor.getInt(2));
            questao.setIdFormulario(cursor.getInt(3));

            PerguntaDAO respostaDAO = new PerguntaDAO(context);
            questao.setListaPerguntas(respostaDAO.findPerguntasByQuestao(questao.getId()));
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }


        return questao;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        appescaHelper.close();
    }
}
