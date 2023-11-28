package br.ufrn.imd.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Playlist {
	private SimpleStringProperty nome;
	private ArrayList<Musica> musicas;
	
	public Playlist() {
		musicas = new ArrayList<Musica>();
	}
	
	public SimpleStringProperty getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = new SimpleStringProperty(nome);
	}

	public void addMusica(Musica m) {
		musicas.add(m);
	}

	public ArrayList<Musica> getMusicas() {
		return musicas;
	}

	public void setMusicas(ArrayList<Musica> musicas) {
		this.musicas = musicas;
	}
}
