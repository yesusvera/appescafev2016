package br.org.unesco.appesca.util;

/**
 * Created by yesus on 1/11/16.
 */
public class ConstantesREST {
    public static String HOST = "http://10.0.0.108:8080/appesca-web/";
//    public static String HOST = "http://sauber03.unesco.org.br/appesca-web/";

    //private final static String HOST = "http://macbookyesus.local:8080/appesca-web/";

    /**
     * SERVICOS REST
     */
    public final static String AUTHENTICATION_SERVICE = "rest/usuario/autenticacao";
    public final static String USUARIO_IMAGEM = "rest/usuario/imagem";
    public final static String FORMULARIO_LISTA = "rest/formulario/lista";
    public final static String IMAGEM_USUARIO = "rest/usuario/imagem";


    public static String getURLService(String SERVICE_REST){
        return HOST + SERVICE_REST;
    }

    public static void main(String[] args) {
        ConstantesREST.getURLService(AUTHENTICATION_SERVICE);
    }
}
