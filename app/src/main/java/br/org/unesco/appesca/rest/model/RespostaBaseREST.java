package br.org.unesco.appesca.rest.model;

import java.io.Serializable;

/**
 * @author yesus
 */
public abstract class RespostaBaseREST implements Serializable {

	private static final long serialVersionUID = 7332729555176339254L;

	protected boolean erro;
	protected String mensagemErro;

	public boolean isErro() {
		return erro;
	}

	public void setErro(boolean erro) {
		this.erro = erro;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;

	}
}
