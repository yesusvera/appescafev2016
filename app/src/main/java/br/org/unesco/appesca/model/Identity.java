package br.org.unesco.appesca.model;

import java.io.Serializable;

/**
 * Created by yesus on 11/29/15.
 */
public class Identity implements Serializable{

    public static Usuario usuarioLogado;


    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado) {
        Identity.usuarioLogado = usuarioLogado;
    }
}
