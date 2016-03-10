package br.org.unesco.appesca.rest.model;

import java.io.Serializable;

import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Usuario;

/**
 * @author yesus
 */
public class FormularioREST implements Serializable {

	private static final long serialVersionUID = -332913217851652370L;

	private Formulario formulario;

	public FormularioREST(){

	}

	public FormularioREST(Formulario formulario) {
		this.formulario = formulario;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	@Override
	public String toString() {
		return "FormularioREST [formulario=" + formulario + "]";
	}
}
