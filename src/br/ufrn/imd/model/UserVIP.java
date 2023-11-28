package br.ufrn.imd.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserVIP extends User {
	private ObservableList<Playlist> playlists;
	
	public UserVIP() {
		playlists = FXCollections.observableArrayList();
	}

	public ObservableList<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(ObservableList<Playlist> playlists) {
		this.playlists = playlists;
	}

	public void addPlaylist(Playlist p) {
		playlists.add(p);
	}
}
