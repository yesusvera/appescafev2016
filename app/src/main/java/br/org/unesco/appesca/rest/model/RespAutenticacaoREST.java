package br.org.unesco.appesca.rest.model;

import java.io.Serializable;

import br.org.unesco.appesca.model.Usuario;

/**
 * @author yesus
 */
public class RespAutenticacaoREST extends RespostaBaseREST implements Serializable {

	private static final long serialVersionUID = 7332729555176339254L;

	private Usuario usuario;

	public RespAutenticacaoREST(){
		
	}
	
	public RespAutenticacaoREST(Usuario usuario, boolean erro, String mensagemErro) {
		this.usuario = usuario;
		this.erro = erro;
		this.mensagemErro = mensagemErro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
