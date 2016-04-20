package br.org.unesco.appesca.util;

/**
 * Created by yesus on 1/11/16.
 */
public class ConstantesREST {

//     public static String HOST = "http://172.20.10.2:8080/appesca-web/";
      public static String HOST = "http://sauber03.unesco.org.br/appesca-web/";
     //private final static String HOST = "http://macbookyesus.local:8080/appesca-web/";

     /**
     * SERVICOS REST
     */
    public final static String AUTHENTICATION_SERVICE = "rest/usuario/autenticacao";
    public final static String USUARIO_IMAGEM = "rest/usuario/imag  em";
    public final static String FORMULARIO_LISTA = "rest/formulario/lista";
    public final static String LOCALIZACAO_INSERIR = "rest/localizacao/inserir";

    public final static String FORMULARIO_INSERIR = "rest/formulario/inserir";
    public final static String FORMULARIO_INSERIR_VS2 = "InsertFormulario"; //Inserido para não causar incompatibilidade com as versões anteriores que já estão em prod.

    public final static String IMAGEM_USUARIO = "rest/usuario/imagem";

    public static String getURLService(String SERVICE_REST){
        return HOST + SERVICE_REST;
    }

    public static void main(String[] args) {
        ConstantesREST.getURLService(AUTHENTICATION_SERVICE);
    }
}
