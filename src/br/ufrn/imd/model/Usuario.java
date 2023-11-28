package br.ufrn.imd.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Usuario {
	protected SimpleStringProperty nome;
	protected SimpleIntegerProperty id;
	protected SimpleStringProperty senha;
	
	public SimpleStringProperty getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = new SimpleStringProperty(nome);
	}
	public SimpleIntegerProperty getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = new SimpleIntegerProperty(id);
	}
	public SimpleStringProperty getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = new SimpleStringProperty(senha);
	}
}
