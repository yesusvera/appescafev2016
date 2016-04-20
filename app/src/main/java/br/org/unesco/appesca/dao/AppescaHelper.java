package br.org.unesco.appesca.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppescaHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "appescadb";
    private static final int DATABASE_VERSION = 2;


    /********************* TABELA LOCALIZACAO USUARIO***********************/
    public static final String TABLE_LOCALIZACAO_USUARIO = "LOCALIZACAO_USUARIO";
    public static final String COL_LOCALIZACAO_USUARIO_ID = "ID_LOCALIZACAO_USUARIO";
    public static final String COL_DATA_REGISTRO = "DATA_REGISTRO";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";
    public static final String COL_PROVIDED = "PROVIDED";

    /********************* TABELA TIPO FORMULARIO ***********************/
    public static final String TABLE_TIPO_FORMULARIO = "TIPO_FORMULARIO";
    public static final String COL_TIPO_FORMULARIO_ID = "ID_TIPO_FORMULARIO";
    public static final String COL_TIPO_FORMULARIO_NOME = "NOME";
    public static final String COL_TIPO_FORMULARIO_DESCRICAO = "DESCRICAO";
    public static final String COL_TIPO_FORMULARIO_ORDEM = "ORDEM";

    /********************* TABELA FORMULARIO ***********************/
    public static final String TABLE_FORMULARIO = "FORMULARIO";
    public static final String COL_FORMULARIO_ID = "ID_FORMULARIO";
    public static final String COL_FORMULARIO_ID_SINCRONIZACAO = "ID_SINCRONIZACAO";
    public static final String COL_FORMULARIO_NOME = "NOME";
    public static final String COL_FORMULARIO_ID_TIPO_FORMULARIO = "ID_TIPO_FORMULARIO";
    public static final String COL_FORMULARIO_ID_USUARIO = "ID_USUARIO";
    public static final String COL_FORMULARIO_DATA_APLICACAO = "DATA_APLICACAO";
    public static final String COL_FORMULARIO_SITUACAO = "SITUACAO";

    /********************* TABELA QUESTAO ***********************/
    public static final String TABLE_QUESTAO = "QUESTAO";
    public static final String COL_QUESTAO_ID = "ID_QUESTAO";
    public static final String COL_QUESTAO_TITULO = "TITULO";
    public static final String COL_QUESTAO_ORDEM = "ORDEM";
    public static final String COL_QUESTAO_ID_FORMULARIO = "ID_FORMULARIO";

    /********************* TABELA PERGUNTA ***********************/
    public static final String TABLE_PERGUNTA = "PERGUNTA";
    public static final String COL_PERGUNTA_ID = "ID_PERGUNTA";
    public static final String COL_PERGUNTA_BOOLEANA = "BOOLEANA";
    public static final String COL_PERGUNTA_RESP_BOOLEANA = "RESP_BOOLEANA";
    public static final String COL_PERGUNTA_ORDEM = "ORDEM";
    public static final String COL_PERGUNTA_ID_QUESTAO = "ID_QUESTAO";

    /********************* TABELA RESPOSTA ***********************/
    public static final String TABLE_RESPOSTA = "RESPOSTA";
    public static final String COL_RESPOSTA_ID = "ID_RESPOSTA";
    public static final String COL_RESPOSTA_OPCAO = "OPCAO";
    public static final String COL_RESPOSTA_TEXTO = "TEXTO";
    public static final String COL_RESPOSTA_AUDIO = "AUDIO";
    public static final String COL_RESPOSTA_ORDEM = "ORDEM";
    public static final String COL_RESPOSTA_ID_PERGUNTA = "ID_PERGUNTA";

    /********************* TABELA USUARIO ***********************/
    public static final String TABLE_USUARIO = "USUARIO";
    public static final String COL_USUARIO_ID = "ID_USUARIO";
    public static final String COL_USUARIO_NOME = "NOME";
    public static final String COL_USUARIO_ENDERECO = "ENDERECO";
    public static final String COL_USUARIO_LOGIN = "LOGIN";
    public static final String COL_USUARIO_SENHA = "SENHA";
    public static final String COL_USUARIO_PERFIL = "PERFIL";
    public static final String COL_USUARIO_IMAGEM = "IMAGEM";
    public static final String COL_USUARIO_EMAIL = "EMAIL";

    /********************* TABELA LOCALIZACAO RESIDENCIA ***********************/
    public static final String TABLE_LOC_RESIDENCIA = "LOC_RESIDENCIA";
    public static final String COL_LOC_RESIDENCIA_ID = "ID_LOC_RESIDENCIA";
    public static final String COL_LOC_RESIDENCIA_LOCAL_RESID = "LOCAL_RESID";
    public static final String COL_LOC_RESIDENCIA_RESID_UN_CONSERV = "RESID_UN_CONSERV";
    public static final String COL_LOC_RESIDENCIA_ID_FORMULARIO = "ID_FORMULARIO";

    /********************* TABELA ENTREVISTADO ***********************/
    public static final String TABLE_ENTREVISTADO = "IDENT_ENTREVISTADO";
    public static final String COL_ENTREVISTADO_ID = "ID_IDENT_ENTREVISTADO";
    public static final String COL_ENTREVISTADO_NOME_COMPLETO = "NOME_COMPLETO";
    public static final String COL_ENTREVISTADO_APELIDO = "APELIDO";
    public static final String COL_ENTREVISTADO_UF = "UF";
    public static final String COL_ENTREVISTADO_MUNICIPIO = "MUNICIPIO";
    public static final String COL_ENTREVISTADO_COMUNIDADE = "COMUNIDADE";
    public static final String COL_ENTREVISTADO_POSSUI_CERT_NASC = "POSSUI_CERT_NASC";
    public static final String COL_ENTREVISTADO_POSSUI_CART_IDENT = "POSSUI_CART_IDENT";
    public static final String COL_ENTREVISTADO_POSSUI_CPF = "POSSUI_CPF";
    public static final String COL_ENTREVISTADO_POSSUI_CART_TRAB = "POSSUI_CART_TRAB";
    public static final String COL_ENTREVISTADO_POSSUI_RGP = "POSSUI_RGP";
    public static final String COL_ENTREVISTADO_POSSUI_DAP = "POSSUI_DAP";
    public static final String COL_ENTREVISTADO_POSSUI_OUTRO = "POSSUI_OUTRO";
    public static final String COL_ENTREVISTADO_IDADE = "IDADE";
    public static final String COL_ENTREVISTADO_SEXO = "SEXO";
    public static final String COL_ENTREVISTADO_TELEFONE = "TELEFONE";
    public static final String COL_ENTREVISTADO_ACESSO_COMUNIDADE = "ACESSO_COMUNIDADE";
    public static final String COL_ENTREVISTADO_DIST_MUN_PROX = "DIST_MUN_PROX";
    public static final String COL_ENTREVISTADO_EST_CIVIL = "EST_CIVIL";
    public static final String COL_ENTREVISTADO_ESCOLARIDADE = "ESCOLARIDADE";
    public static final String COL_ENTREVISTADO_REPONS_UN_FAM = "REPONS_UN_FAM";
    public static final String COL_ENTREVISTADO_ID_FORMULARIO = "ID_FORMULARIO";


    /********************* TABELA EQUIPE ***********************/
    public static final String TABLE_EQUIPE = "EQUIPE";
    public static final String COL_EQUIPE_ID = "ID_EQUIPE";
    public static final String COL_EQUIPE_NOME = "NOME";
    public static final String COL_EQUIPE_ULTIMOS_AVISOS = "ULTIMOS_AVISOS";
    public static final String COL_EQUIPE_REGULAMENTO = "REGULAMENTO";
    public static final String COL_EQUIPE_COORDENADOR_ID = "ID_COORDENADOR";
    public static final String COL_EQUIPE_DATA_CRIACAO = "DATA_CRIACAO";

    /********************* TABELA MEMBROS_EQUIPE ***********************/
    public static final String TABLE_MEMBROS_EQUIPE = "MEMBROS_EQUIPE";
    public static final String COL_MEMBRO_EQUIPE_ID = "ID_MEMBRO_EQUIPE";
    public static final String COL_MEMBROS_EQUIPE_DATA_CRIACAO = "DATA_CRIACAO";

    private Context mContext;
//    private SQLiteDatabase mDataBase;

    public AppescaHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

//        mContext = context;
//        mDataBase = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(criarTabelaTipoFormulario());
        sqLiteDatabase.execSQL(criarTabelaUsuario());
        sqLiteDatabase.execSQL(criarTabelaFormulario());
        sqLiteDatabase.execSQL(criarTabelaQuestao());
        sqLiteDatabase.execSQL(criarTabelaPergunta());
        sqLiteDatabase.execSQL(criarTabelaResposta());
        sqLiteDatabase.execSQL(criarTabelaLocResidencia());
        sqLiteDatabase.execSQL(criarTabelaEntrevistado());
        sqLiteDatabase.execSQL(criarTabelaEquipe());
        sqLiteDatabase.execSQL(criarTabelaMembrosEquipe());
        sqLiteDatabase.execSQL(criarTabelaLocalizacaoUsuario());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    private static String criarTabelaTipoFormulario() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_TIPO_FORMULARIO);
        sql.append(" ( ");
        sql.append(COL_TIPO_FORMULARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_TIPO_FORMULARIO_NOME + " TEXT , ");
        sql.append(COL_TIPO_FORMULARIO_DESCRICAO + " TEXT , ");
        sql.append(COL_TIPO_FORMULARIO_ORDEM + " INTEGER ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaUsuario() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_USUARIO);
        sql.append(" ( ");
//        sql.append(COL_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_USUARIO_ID + " INTEGER PRIMARY KEY NOT NULL , ");
        sql.append(COL_USUARIO_NOME + " TEXT , ");
        sql.append(COL_USUARIO_ENDERECO + " TEXT , ");
        sql.append(COL_USUARIO_LOGIN + " TEXT , ");
        sql.append(COL_USUARIO_SENHA + " TEXT , ");
        sql.append(COL_USUARIO_PERFIL + " TEXT, ");
        sql.append(COL_USUARIO_IMAGEM + " BLOB, ");
        sql.append(COL_USUARIO_EMAIL + " TEXT ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaFormulario() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_FORMULARIO);
        sql.append(" ( ");
        sql.append(COL_FORMULARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_FORMULARIO_ID_SINCRONIZACAO + " TEXT , ");
        sql.append(COL_FORMULARIO_NOME + " TEXT , ");
        sql.append(COL_FORMULARIO_DATA_APLICACAO + " DATETIME , ");
        sql.append(COL_FORMULARIO_ID_USUARIO + " INTEGER , ");
        sql.append(COL_LATITUDE + " TEXT , ");
        sql.append(COL_LONGITUDE + " TEXT , ");
        sql.append(COL_FORMULARIO_ID_TIPO_FORMULARIO + " INTEGER , ");
        sql.append(COL_FORMULARIO_SITUACAO + " INTEGER , ");
        sql.append(" FOREIGN KEY ("+ COL_FORMULARIO_ID_USUARIO +") REFERENCES "+ TABLE_USUARIO+" ("+ COL_USUARIO_ID +") ");
        sql.append(" FOREIGN KEY ("+ COL_FORMULARIO_ID_TIPO_FORMULARIO +") REFERENCES "+ TABLE_TIPO_FORMULARIO+" ("+ COL_TIPO_FORMULARIO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }


    private static String criarTabelaLocalizacaoUsuario() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_LOCALIZACAO_USUARIO);
        sql.append(" ( ");
        sql.append(COL_LOCALIZACAO_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_USUARIO_ID + " INTEGER , ");
        sql.append(COL_DATA_REGISTRO + " DATETIME , ");
        sql.append(COL_LATITUDE + " TEXT , ");
        sql.append(COL_LONGITUDE + " TEXT , ");
        sql.append(COL_PROVIDED + " TEXT , ");
        sql.append(" FOREIGN KEY ("+ COL_USUARIO_ID +") REFERENCES "+ TABLE_USUARIO+" ("+ COL_USUARIO_ID +") ");
        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaQuestao() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_QUESTAO);
        sql.append(" ( ");
        sql.append(COL_QUESTAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_QUESTAO_TITULO + " TEXT , ");
        sql.append(COL_QUESTAO_ORDEM + " INTEGER , ");
        sql.append(COL_QUESTAO_ID_FORMULARIO + " INTEGER , ");
        sql.append(" FOREIGN KEY ("+ COL_QUESTAO_ID_FORMULARIO +") REFERENCES "+ TABLE_FORMULARIO+" ("+ COL_FORMULARIO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaPergunta() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_PERGUNTA);
        sql.append(" ( ");
        sql.append(COL_PERGUNTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_PERGUNTA_BOOLEANA + " BOOLEAN , ");
        sql.append(COL_PERGUNTA_RESP_BOOLEANA + " BOOLEAN , ");
        sql.append(COL_PERGUNTA_ORDEM + " INTEGER , ");
        sql.append(COL_PERGUNTA_ID_QUESTAO + " INTEGER , ");
        sql.append(" FOREIGN KEY ("+ COL_PERGUNTA_ID_QUESTAO +") REFERENCES "+ TABLE_QUESTAO+" ("+ COL_QUESTAO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaResposta() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_RESPOSTA);
        sql.append(" ( ");
        sql.append(COL_RESPOSTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_RESPOSTA_OPCAO + " INTEGER , ");
        sql.append(COL_RESPOSTA_TEXTO + " TEXT , ");
        sql.append(COL_RESPOSTA_AUDIO + " TEXT , ");
        sql.append(COL_RESPOSTA_ORDEM + " INTEGER , ");
        sql.append(COL_RESPOSTA_ID_PERGUNTA + " INTEGER , ");
        sql.append(" FOREIGN KEY ("+ COL_RESPOSTA_ID_PERGUNTA +") REFERENCES "+ TABLE_PERGUNTA+" ("+ COL_PERGUNTA_ID +") ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaLocResidencia() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_LOC_RESIDENCIA);
        sql.append(" ( ");
        sql.append(COL_LOC_RESIDENCIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_LOC_RESIDENCIA_LOCAL_RESID + " INTEGER , ");
        sql.append(COL_LOC_RESIDENCIA_RESID_UN_CONSERV + " BOOLEAN , ");
        sql.append(COL_LOC_RESIDENCIA_ID_FORMULARIO + " INTEGER , ");
        sql.append(" FOREIGN KEY ("+ COL_LOC_RESIDENCIA_ID_FORMULARIO +") REFERENCES "+ TABLE_FORMULARIO+" ("+ COL_FORMULARIO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaEntrevistado() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_ENTREVISTADO);
        sql.append(" ( ");
        sql.append(COL_ENTREVISTADO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , ");
        sql.append(COL_ENTREVISTADO_NOME_COMPLETO + " TEXT , ");
        sql.append(COL_ENTREVISTADO_APELIDO + " TEXT , ");
        sql.append(COL_ENTREVISTADO_UF + " TEXT , ");
        sql.append(COL_ENTREVISTADO_MUNICIPIO + " TEXT , ");
        sql.append(COL_ENTREVISTADO_COMUNIDADE + " TEXT , ");
        sql.append(COL_ENTREVISTADO_POSSUI_CERT_NASC + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_POSSUI_CART_IDENT + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_POSSUI_CPF + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_POSSUI_CART_TRAB + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_POSSUI_RGP + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_POSSUI_DAP + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_POSSUI_OUTRO + " TEXT , ");
        sql.append(COL_ENTREVISTADO_IDADE + " INTEGER , ");
        sql.append(COL_ENTREVISTADO_SEXO + " TEXT , ");
        sql.append(COL_ENTREVISTADO_TELEFONE + " TEXT , ");
        sql.append(COL_ENTREVISTADO_ACESSO_COMUNIDADE + " INTEGER , ");
        sql.append(COL_ENTREVISTADO_DIST_MUN_PROX + " INTEGER , ");
        sql.append(COL_ENTREVISTADO_EST_CIVIL + " INTEGER , ");
        sql.append(COL_ENTREVISTADO_ESCOLARIDADE + " INTEGER , ");
        sql.append(COL_ENTREVISTADO_REPONS_UN_FAM + " BOOLEAN , ");
        sql.append(COL_ENTREVISTADO_ID_FORMULARIO + " INTEGER , ");
        sql.append(" FOREIGN KEY ("+ COL_ENTREVISTADO_ID_FORMULARIO +") REFERENCES "+ TABLE_FORMULARIO+" ("+ COL_FORMULARIO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }

    private static String criarTabelaEquipe() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_EQUIPE);
        sql.append(" ( ");
        sql.append(COL_EQUIPE_ID + " INTEGER PRIMARY KEY NOT NULL , ");
        sql.append(COL_EQUIPE_NOME + " TEXT , ");
        sql.append(COL_EQUIPE_ULTIMOS_AVISOS + " TEXT , ");
        sql.append(COL_EQUIPE_REGULAMENTO + " TEXT , ");
        sql.append(COL_EQUIPE_COORDENADOR_ID + " INTEGER , ");
        sql.append(COL_EQUIPE_DATA_CRIACAO + " DATETIME , ");
        sql.append(" FOREIGN KEY ("+ COL_EQUIPE_COORDENADOR_ID +") REFERENCES "+ TABLE_USUARIO+" ("+ COL_USUARIO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }


    private static String criarTabelaMembrosEquipe() {
        final StringBuffer sql = new StringBuffer();

        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_MEMBROS_EQUIPE);
        sql.append(" ( ");
        sql.append(COL_MEMBRO_EQUIPE_ID + " INTEGER PRIMARY KEY NOT NULL , ");
        sql.append(COL_EQUIPE_ID + " INTEGER , ");
        sql.append(COL_USUARIO_ID + " INTEGER , ");
        sql.append(COL_MEMBROS_EQUIPE_DATA_CRIACAO + " DATETIME , ");
        sql.append(" FOREIGN KEY ("+ COL_EQUIPE_ID +") REFERENCES "+ TABLE_EQUIPE+" ("+ COL_EQUIPE_ID +") ");
        sql.append(" FOREIGN KEY ("+ COL_USUARIO_ID +") REFERENCES "+ TABLE_USUARIO+" ("+ COL_USUARIO_ID +") ");

        sql.append("); ");

        return sql.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        try{
            super.close();
        }catch(Exception e){

        }
    }
}