package br.ufrn.imd.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioVIP extends Usuario {
	private ArrayList<Playlist> playlists;
	
	public UsuarioVIP() {
		playlists = new ArrayList<Playlist>();
	}
	
	public ArrayList<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(ArrayList<Playlist> playlists) {
		this.playlists = playlists;
	}
	
	public void addPlaylist(Playlist p) {
		playlists.add(p);
	}
	
	
}
