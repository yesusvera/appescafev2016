package br.org.unesco.appesca.rest.model;

import java.io.Serializable;

/**
 * @author yesus
 */
public class RespEnvioLocalizacao extends RespostaBaseREST implements Serializable {

	private static final long serialVersionUID = 5786739347672878227L;

	public RespEnvioLocalizacao(){
		
	}
	
	public RespEnvioLocalizacao(boolean erro, String mensagemErro) {
		this.erro = erro;
		this.mensagemErro = mensagemErro;
	}
}
