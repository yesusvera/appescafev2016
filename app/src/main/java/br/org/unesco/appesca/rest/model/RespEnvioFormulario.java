package br.org.unesco.appesca.rest.model;

import java.io.Serializable;

import br.org.unesco.appesca.model.Usuario;

/**
 * @author yesus
 */
public class RespEnvioFormulario extends RespostaBaseREST implements Serializable {

	private static final long serialVersionUID = 5786739347672878227L;

	private String idSincronizacao;

	public RespEnvioFormulario(){
		
	}
	
	public RespEnvioFormulario(boolean erro, String mensagemErro) {
		this.erro = erro;
		this.mensagemErro = mensagemErro;
	}

	public String getIdSincronizacao() {
		return idSincronizacao;
	}

	public void setIdSincronizacao(String idSincronizacao) {
		this.idSincronizacao = idSincronizacao;
	}
}
