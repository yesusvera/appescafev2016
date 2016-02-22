package br.org.unesco.appesca.rest.model;

import java.io.Serializable;
import java.util.List;

import br.org.unesco.appesca.model.Formulario;

/**
 * @author yesus
 */
public class RespFormularioREST extends RespostaBaseREST implements Serializable {

	private static final long serialVersionUID = 7332729555176339254L;

	private List<Formulario> listaFormularios;

	public RespFormularioREST(){
		
	}
	
	public RespFormularioREST(List<Formulario> listaFormularios) {
		super();
		this.listaFormularios = listaFormularios;
	}

	public List<Formulario> getListaFormularios() {
		return listaFormularios;
	}

	public void setListaFormularios(List<Formulario> listaFormularios) {
		this.listaFormularios = listaFormularios;
	}

}
