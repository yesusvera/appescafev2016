package br.org.unesco.appesca.model;

public class LocResidencia implements java.io.Serializable {

	private static final long serialVersionUID = 3251866057864092677L;
	
	private Integer id;
	private Integer localResid;
	private Boolean residUnConserv;
	private int idFormulario;

	public LocResidencia() {
	}

	public LocResidencia(int idFormulario) {
		this.idFormulario = idFormulario;
	}

	public LocResidencia(Integer localResid, Boolean residUnConserv, int idFormulario) {
		this.localResid = localResid;
		this.residUnConserv = residUnConserv;
		this.idFormulario = idFormulario;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLocalResid() {
		return this.localResid;
	}

	public void setLocalResid(Integer localResid) {
		this.localResid = localResid;
	}

	public Boolean getResidUnConserv() {
		return this.residUnConserv;
	}

	public void setResidUnConserv(Boolean residUnConserv) {
		this.residUnConserv = residUnConserv;
	}

	public int getIdFormulario() {
		return this.idFormulario;
	}

	public void setIdFormulario(int idFormulario) {
		this.idFormulario = idFormulario;
	}

}
