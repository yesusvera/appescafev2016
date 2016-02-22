package br.org.unesco.appesca.model;

import java.util.List;

public class Questao implements java.io.Serializable {

	private static final long serialVersionUID = -7214392109110721089L;

	private Integer id;
	private String titulo;
	private Integer ordem;
	private int idFormulario;
	private List<Pergunta> perguntas;

	public Questao() {
	}

	public Questao(int idFormulario) {
		this.idFormulario = idFormulario;
	}

	public Questao(String titulo, Integer ordem, int idFormulario) {
		this.titulo = titulo;
		this.ordem = ordem;
		this.idFormulario = idFormulario;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public int getIdFormulario() {
		return this.idFormulario;
	}

	public void setIdFormulario(int idFormulario) {
		this.idFormulario = idFormulario;
	}

	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

}
