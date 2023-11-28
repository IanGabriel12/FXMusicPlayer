package br.ufrn.imd.model;

import java.io.File;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	protected SimpleStringProperty name;
	protected SimpleIntegerProperty id;
	protected SimpleStringProperty password;
	protected ObservableList<Song> songs;
	protected ObservableList<Folder> folders;
	protected Playlist defaultPlaylist;
	
	public User() {
		songs = FXCollections.observableArrayList();
		folders = FXCollections.observableArrayList();
		defaultPlaylist = new Playlist();
	}

	public SimpleStringProperty getName() {
		return name;
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public SimpleIntegerProperty getId() {
		return id;
	}

	public void setId(SimpleIntegerProperty id) {
		this.id = id;
	}

	public SimpleStringProperty getPassword() {
		return password;
	}

	public void setPassword(SimpleStringProperty password) {
		this.password = password;
	}

	public ObservableList<Song> getSongs() {
		return songs;
	}

	public void setSongs(ObservableList<Song> songs) {
		this.songs = songs;
	}

	public ObservableList<Folder> getFolders() {
		return folders;
	}

	public void setFolders(ObservableList<Folder> folders) {
		this.folders = folders;
	}
	
	public void addSong(Song s) {
		songs.add(s);
		defaultPlaylist.addSong(s);
	}
	
	public void addFolder(Folder f) {
		folders.add(f);
		File folder = new File(f.getPath().getValue());
		for(File file : folder.listFiles()) {
			if(isAudioFile(file)) {
				Song s = new Song();
				s.setAbsolutePath(new SimpleStringProperty(file.getAbsolutePath()));
				defaultPlaylist.addSong(s);
			}
		}
	}
	
	public Playlist getDefaultPlaylist() {
		return defaultPlaylist;
	}

	public void setDefaultPlaylist(Playlist defaultPlaylist) {
		this.defaultPlaylist = defaultPlaylist;
	}

	private boolean isAudioFile(File file) {
		return file.getName().endsWith(".mp3");
	}
	
}
