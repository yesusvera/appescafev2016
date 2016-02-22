package br.org.unesco.appesca.model;

public class TipoFormulario implements java.io.Serializable {

	private static final long serialVersionUID = -1968974358816603465L;
	
	private Integer id;
	private String nome;
	private String descricao;
	private Integer ordem;

	public TipoFormulario() {
	}

	public TipoFormulario(String nome) {
		this.nome = nome;
	}

	public TipoFormulario(String nome, String descricao, Integer ordem) {
		this.nome = nome;
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
