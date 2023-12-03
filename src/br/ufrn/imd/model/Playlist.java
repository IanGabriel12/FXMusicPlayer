package br.ufrn.imd.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Playlist {
	private SimpleStringProperty name;
	/**
	 * Posição da música que está tocando na playlist
	 */
	private SimpleIntegerProperty currentSongIndex;
	private ObservableList<Song> songs;
	
	public Playlist() {
		songs = FXCollections.observableArrayList();
		currentSongIndex = new SimpleIntegerProperty(0);
	}
	
	public SimpleIntegerProperty getCurrentSongIndex() {
		return currentSongIndex;
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
	
	/**
	 * 
	 * @return Retorna a música inicial da playlist
	 */
	public Song start() {
		currentSongIndex.setValue(0);
		return songs.get(currentSongIndex.getValue());
	}
	
	/**
	 * Calcula qual música é a próxima da playlist em relação à música atual.
	 * @return A próxima música da playlist em relação à música atual
	 */
	public Song next() {
		currentSongIndex.setValue((currentSongIndex.getValue() + 1) % songs.size());;
		return songs.get(currentSongIndex.getValue());
	}
	
	/**
	 * Calcula qual foi a música anterior à música atual da playlist
	 * @return A instância da música anterior à atual.
	 */
	public Song previous() {
		currentSongIndex.setValue((currentSongIndex.getValue() - 1 + songs.size()) % songs.size());;
		return songs.get(currentSongIndex.getValue());
	}
	
	/**
	 * Define qual música será selecionada para ser tocada
	 * @param index Índice da música selecionada na playlist
	 * @return Instância da música selecionada
	 */
	public Song setSong(Integer index) {
		currentSongIndex.setValue(index);
		return songs.get(currentSongIndex.getValue());
	}
	
	/**
	 * Adiciona uma música na playlist
	 * @param s Música a ser adicionada
	 */
	public void addSong(Song s) {
		songs.add(s);
	}
	
}
