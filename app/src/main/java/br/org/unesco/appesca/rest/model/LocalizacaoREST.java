package br.org.unesco.appesca.rest.model;

import java.io.Serializable;
import java.util.List;

import br.org.unesco.appesca.model.LocalizacaoUsuario;


/**
 * @author yesus
 */
public class LocalizacaoREST implements Serializable {

	
	private static final long serialVersionUID = -4811355033518315284L;
	
	private List<LocalizacaoUsuario> listaLocalizacaoUsuario;

	public List<LocalizacaoUsuario> getListaLocalizacaoUsuario() {
		return listaLocalizacaoUsuario;
	}

	public void setListaLocalizacaoUsuario(List<LocalizacaoUsuario> listaLocalizacaoUsuario) {
		this.listaLocalizacaoUsuario = listaLocalizacaoUsuario;
	}

	@Override
	public String toString() {
		return "LocalizacaoREST [listaLocalizacaoUsuario=" + listaLocalizacaoUsuario + "]";
	}

}
