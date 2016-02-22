package br.org.unesco.appesca.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalizacaoUsuario implements java.io.Serializable {

	private static final long serialVersionUID = 2165508619825487958L;

	private Integer id;
	private Usuario usuario;
	private Date dataRegistro;
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	public LocalizacaoUsuario() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario idUsuario) {
		this.usuario = idUsuario;
	}


	public Date getDataRegistro() {
		return this.dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getData(){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(getDataRegistro());
	}

	public String getHora(){
		SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
		return sd.format(getDataRegistro());
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
}