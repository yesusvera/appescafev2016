package br.org.unesco.appesca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.org.unesco.appesca.model.BaseModel;

/**
 * Created by yesus on 1/24/16.
 * Base de DAO com métodos padrão de save e update.
 */
public abstract class BaseDAO <T extends BaseModel>{

    public final static String FORMATO_DATA = "dd/MM/yyyy HH:mm:ss";



    private Context context;
    protected AppescaHelper appescaHelper;

    private String TABELA_PRINCIPAL;
    private String COLUNA_CHAVE_PRIMARIA;

    public BaseDAO(Context context, String TABELA_PRINCIPAL, String COLUNA_CHAVE_PRIMARIA) {
        this.context = context;
        appescaHelper = new AppescaHelper(this.context);
        this.TABELA_PRINCIPAL = TABELA_PRINCIPAL;
        this.COLUNA_CHAVE_PRIMARIA = COLUNA_CHAVE_PRIMARIA;
    }


    protected String SELECT_FROM_TABLE() {
        return "SELECT * FROM " + TABELA_PRINCIPAL;
    }

    public void save(T entidade){
        if(entidade.getId()!=null && findById(entidade.getId()) != null){
            update(entidade);
        }else{
            insert(entidade);
        }
    }

    public T findById(int id) {
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_FROM_TABLE() + " WHERE " + COLUNA_CHAVE_PRIMARIA + " = ?", new String[]{id + ""});
        if(cursor.moveToFirst()) {
            T t = factoryEntity(cursor);

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }

            return t;
        }else{

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }

            return null;
        }
    }

    private void insert(T entidade){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();
        ContentValues values = factoryContentValues(entidade);
        long id = db.insert(TABELA_PRINCIPAL, null, values);
        entidade = findById((int)id);

        if(db!=null && db.isOpen()){
            db.close();
        }
    }

    private void update(T entidade){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        ContentValues values = factoryContentValues(entidade);
        db.update(TABELA_PRINCIPAL, values,
                COLUNA_CHAVE_PRIMARIA + " = ?",
                new String[]{String.valueOf(entidade.getId())});

        if(db!=null && db.isOpen()){
            db.close();
        }
    }

    public void delete(int id){
        SQLiteDatabase db = appescaHelper.getWritableDatabase();

        db.delete(TABELA_PRINCIPAL, COLUNA_CHAVE_PRIMARIA + " = ?",
                new String[]{String.valueOf(id)});

        if(db!=null && db.isOpen()){
            db.close();
        }
    }

    public List<T> listAll() {
        return listByQuery(SELECT_FROM_TABLE());
    }

    protected List<T> listByQuery(String query){
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        List<T> list = new ArrayList<T>();

        for (int i = 0; i < cursor.getCount(); i++) {
            list.add(factoryEntity(cursor));
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return list;
    }

    protected List<T> listByRawQuery(String query, String[] selectionArgs){
        SQLiteDatabase db = appescaHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, selectionArgs);
        cursor.moveToFirst();

        List<T> list = new ArrayList<T>();

        for (int i = 0; i < cursor.getCount(); i++) {
            list.add(factoryEntity(cursor));
            cursor.moveToNext();
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            db.close();
        }

        return list;
    }


    abstract protected ContentValues factoryContentValues(T entidade);

    abstract protected T factoryEntity(Cursor cursor);

    protected Integer getInt(Cursor cursor, String nomeColuna){
        return cursor.getInt(cursor.getColumnIndex(nomeColuna));
    }

    protected String getString(Cursor cursor, String nomeColuna){
        return cursor.getString(cursor.getColumnIndex(nomeColuna));
    }

    protected Double getDouble(Cursor cursor, String nomeColuna){
        return cursor.getDouble(cursor.getColumnIndex(nomeColuna));
    }

    protected Float getFloat(Cursor cursor, String nomeColuna){
        return cursor.getFloat(cursor.getColumnIndex(nomeColuna));
    }

    protected byte[] getBlob(Cursor cursor, String nomeColuna){
        return cursor.getBlob(cursor.getColumnIndex(nomeColuna));
    }

    protected Date getData(Cursor cursor, String nomeColuna){
        try {
            String dt = getString(cursor, nomeColuna);
            return new SimpleDateFormat(FORMATO_DATA).parse(dt);
        }catch(ParseException pe){
            return null;
        }
    }

    public static String getDataFormatada(Date date) {
        return new SimpleDateFormat(FORMATO_DATA).format(date);
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        appescaHelper.close();
    }
}