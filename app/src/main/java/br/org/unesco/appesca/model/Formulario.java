package br.org.unesco.appesca.model;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Formulario extends BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = 2165508619825487958L;

	private Integer id;
	private String idSincronizacao;
	private String nome;
	private int idTipoFormulario;
	private int idUsuario;
	private Date dataAplicacao;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private int situacao;
	private List<Questao> listaQuestoes;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdSincronizacao() {
		return idSincronizacao;
	}

	public void setIdSincronizacao(String idSincronizacao) {
		this.idSincronizacao = idSincronizacao;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdTipoFormulario() {
		return this.idTipoFormulario;
	}

	public void setIdTipoFormulario(int idTipoFormulario) {
		this.idTipoFormulario = idTipoFormulario;
	}

	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getDataAplicacao() {
		return this.dataAplicacao;
	}

	public void setDataAplicacao(Date dataAplicacao) {
		this.dataAplicacao = dataAplicacao;
	}

	public List<Questao> getListaQuestoes() {
		return listaQuestoes;
	}

	public void setListaQuestoes(List<Questao> listQuestoes) {
		this.listaQuestoes = listQuestoes;
	}

	public int getSituacao() {
		return situacao;
	}

	public void setSituacao(int situacao) {
		this.situacao = situacao;
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
