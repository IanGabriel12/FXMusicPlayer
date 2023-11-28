package br.ufrn.imd.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist {
	private SimpleStringProperty name;
	private SimpleIntegerProperty currentSongIndex;
	private ObservableList<Song> songs;
	
	public Playlist() {
		songs = FXCollections.observableArrayList();
		currentSongIndex = new SimpleIntegerProperty(0);
	}
	
	public SimpleStringProperty getName() {
		return name;
	}
	public void setName(SimpleStringProperty name) {
		this.name = name;
	}
	public ObservableList<Song> getSongs() {
		return songs;
	}
	public void setSongs(ObservableList<Song> songs) {
		this.songs = songs;
	}
	
	public Song start() {
		currentSongIndex.setValue(0);
		return songs.get(currentSongIndex.getValue());
	}
	
	public Song next() {
		currentSongIndex.setValue((currentSongIndex.getValue() + 1) % songs.size());;
		return songs.get(currentSongIndex.getValue());
	}
	
	public Song previous() {
		currentSongIndex.setValue((currentSongIndex.getValue() - 1 + songs.size()) % songs.size());;
		return songs.get(currentSongIndex.getValue());
	}
	
	public Song setSong(Integer index) {
		currentSongIndex.setValue(index);
		return songs.get(currentSongIndex.getValue());
	}

	public void addSong(Song s) {
		songs.add(s);
	}
	
}
