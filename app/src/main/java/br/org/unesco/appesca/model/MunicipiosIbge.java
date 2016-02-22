package br.org.unesco.appesca.model;

public class MunicipiosIbge implements java.io.Serializable {

	private static final long serialVersionUID = 8297494606786626460L;

	private int codigo;
	private String municipio;
	private String uf;

	public MunicipiosIbge() {
	}

	public MunicipiosIbge(int codigo) {
		this.codigo = codigo;
	}

	public MunicipiosIbge(int codigo, String municipio, String uf) {
		this.codigo = codigo;
		this.municipio = municipio;
		this.uf = uf;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return this.uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
