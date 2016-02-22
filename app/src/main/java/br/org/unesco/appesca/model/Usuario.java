package br.org.unesco.appesca.model;

import java.util.List;

public class Usuario extends BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = -8637469199477374376L;

	private Integer id;
	private String nome;
	private String email;
	private String endereco;
	private String login;
	private String senha;
	private String perfil;
	private byte[] imagem;
	private String uf;

	private List<Equipe> listaEquipes;

	private List<LocalizacaoUsuario> listaLocalizacoes;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public List<Equipe> getListaEquipes() {
		return listaEquipes;
	}

	public void setListaEquipes(List<Equipe> listaEquipes) {
		this.listaEquipes = listaEquipes;
	}

	public List<LocalizacaoUsuario> getListaLocalizacoes() {
		return listaLocalizacoes;
	}

	public void setListaLocalizacoes(List<LocalizacaoUsuario> listaLocalizacoes) {
		this.listaLocalizacoes = listaLocalizacoes;
	}
}
