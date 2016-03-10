package br.org.unesco.appesca.model;

import java.util.List;

public class Pergunta implements java.io.Serializable {

	private static final long serialVersionUID = -8806813412918825907L;

	private Integer id;
	private Boolean booleana;
	private Boolean respBooleana;
	private Integer ordem;
	private Questao questao = new Questao();
	private List<Resposta> respostas;


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getBooleana() {
		return this.booleana;
	}

	public void setBooleana(Boolean booleana) {
		this.booleana = booleana;
	}

	public Boolean getRespBooleana() {
		return this.respBooleana;
	}

	public void setRespBooleana(Boolean respBooleana) {
		this.respBooleana = respBooleana;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Questao getQuestao() {
		return questao;
	}

	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
	}
}
