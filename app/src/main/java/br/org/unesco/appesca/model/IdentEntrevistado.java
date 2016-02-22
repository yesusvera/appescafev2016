package br.org.unesco.appesca.model;

public class IdentEntrevistado implements java.io.Serializable {

	private static final long serialVersionUID = -4061574933952985453L;
	
	private Integer id;
	private String nomeCompleto;
	private String apelido;
	private Integer idMunicipio;
	private String comunidade;
	private Boolean possuiCertNasc;
	private Boolean possuiCartIdent;
	private Boolean possuiCpf;
	private Boolean possuiCartTrab;
	private Boolean possuiRgp;
	private Boolean possuiDap;
	private String possuiOutro;
	private Integer idade;
	private String sexo;
	private String telefone;
	private Integer acessoComunidade;
	private Integer distMunProx;
	private Integer estCivil;
	private Integer escolaridade;
	private Boolean responsUnFam;
	private int idFormulario;

	public IdentEntrevistado() {
	}

	public IdentEntrevistado(int idFormulario) {
		this.idFormulario = idFormulario;
	}

	public IdentEntrevistado(String nomeCompleto, String apelido, Integer idMunicipio, String comunidade,
			Boolean possuiCertNasc, Boolean possuiCartIdent, Boolean possuiCpf, Boolean possuiCartTrab,
			Boolean possuiRgp, Boolean possuiDap, String possuiOutro, Integer idade, String sexo, String telefone,
			Integer acessoComunidade, Integer distMunProx, Integer estCivil, Integer escolaridade, Boolean responsUnFam,
			int idFormulario) {
		this.nomeCompleto = nomeCompleto;
		this.apelido = apelido;
		this.idMunicipio = idMunicipio;
		this.comunidade = comunidade;
		this.possuiCertNasc = possuiCertNasc;
		this.possuiCartIdent = possuiCartIdent;
		this.possuiCpf = possuiCpf;
		this.possuiCartTrab = possuiCartTrab;
		this.possuiRgp = possuiRgp;
		this.possuiDap = possuiDap;
		this.possuiOutro = possuiOutro;
		this.idade = idade;
		this.sexo = sexo;
		this.telefone = telefone;
		this.acessoComunidade = acessoComunidade;
		this.distMunProx = distMunProx;
		this.estCivil = estCivil;
		this.escolaridade = escolaridade;
		this.responsUnFam = responsUnFam;
		this.idFormulario = idFormulario;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return this.nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getApelido() {
		return this.apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public Integer getIdMunicipio() {
		return this.idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getComunidade() {
		return this.comunidade;
	}

	public void setComunidade(String comunidade) {
		this.comunidade = comunidade;
	}

	public Boolean getPossuiCertNasc() {
		return this.possuiCertNasc;
	}

	public void setPossuiCertNasc(Boolean possuiCertNasc) {
		this.possuiCertNasc = possuiCertNasc;
	}

	public Boolean getPossuiCartIdent() {
		return this.possuiCartIdent;
	}

	public void setPossuiCartIdent(Boolean possuiCartIdent) {
		this.possuiCartIdent = possuiCartIdent;
	}

	public Boolean getPossuiCpf() {
		return this.possuiCpf;
	}

	public void setPossuiCpf(Boolean possuiCpf) {
		this.possuiCpf = possuiCpf;
	}

	public Boolean getPossuiCartTrab() {
		return this.possuiCartTrab;
	}

	public void setPossuiCartTrab(Boolean possuiCartTrab) {
		this.possuiCartTrab = possuiCartTrab;
	}

	public Boolean getPossuiRgp() {
		return this.possuiRgp;
	}

	public void setPossuiRgp(Boolean possuiRgp) {
		this.possuiRgp = possuiRgp;
	}

	public Boolean getPossuiDap() {
		return this.possuiDap;
	}

	public void setPossuiDap(Boolean possuiDap) {
		this.possuiDap = possuiDap;
	}

	public String getPossuiOutro() {
		return this.possuiOutro;
	}

	public void setPossuiOutro(String possuiOutro) {
		this.possuiOutro = possuiOutro;
	}

	public Integer getIdade() {
		return this.idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Integer getAcessoComunidade() {
		return this.acessoComunidade;
	}

	public void setAcessoComunidade(Integer acessoComunidade) {
		this.acessoComunidade = acessoComunidade;
	}

	public Integer getDistMunProx() {
		return this.distMunProx;
	}

	public void setDistMunProx(Integer distMunProx) {
		this.distMunProx = distMunProx;
	}

	public Integer getEstCivil() {
		return this.estCivil;
	}

	public void setEstCivil(Integer estCivil) {
		this.estCivil = estCivil;
	}

	public Integer getEscolaridade() {
		return this.escolaridade;
	}

	public void setEscolaridade(Integer escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Boolean getResponsUnFam() {
		return this.responsUnFam;
	}

	public void setResponsUnFam(Boolean responsUnFam) {
		this.responsUnFam = responsUnFam;
	}

	public int getIdFormulario() {
		return this.idFormulario;
	}

	public void setIdFormulario(int idFormulario) {
		this.idFormulario = idFormulario;
	}

}
