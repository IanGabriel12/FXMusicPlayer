package br.ufrn.imd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import br.ufrn.imd.model.Playlist;
import br.ufrn.imd.model.Song;
import br.ufrn.imd.model.UserVIP;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaylistDAO {
	private String playlistFolderPath = "src/br/ufrn/imd/data/playlists";
	private static PlaylistDAO instance;
	
	public static PlaylistDAO getInstance() {
		if(instance == null) {
			instance = new PlaylistDAO();
		}
		
		return instance;
	}
	
	public void loadPlaylistsFromUser(UserVIP user) {
		try {
			String userName = user.getName().getValue();
			user.setPlaylists(FXCollections.observableArrayList());
			
			File playlistsFolder = new File(playlistFolderPath);
			for(File playlist : playlistsFolder.listFiles()) {
				String playlistFileName = playlist.getName();
				if(!playlistFileName.startsWith(userName + "_")) {
					continue;
				}
				
				String playlistName = playlistFileName.substring(
						userName.length() + 1, playlistFileName.length() - 4
				);
				Playlist p = new Playlist();
				p.setName(new SimpleStringProperty(playlistName));
				
				ObservableList<Song> songs = FXCollections.observableArrayList();
				
				FileReader fr = new FileReader(playlist);
				try (BufferedReader br = new BufferedReader(fr)) {
					String songPath = br.readLine();
					while(songPath != null) {
						Song s = new Song();
						s.setAbsolutePath(new SimpleStringProperty(songPath));
						songs.add(s);
						songPath = br.readLine();
					}
					p.setSongs(songs);
				}
				
				user.addPlaylist(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void savePlaylistsFromUser(UserVIP user) {
		try {
			String userName = user.getName().getValue();
			
			for(Playlist p : user.getPlaylists()) {
				String playlistName = p.getName().getValue();
				String fileName = String.format("%s_%s.txt", userName, playlistName);
				File playlistFile = new File(playlistFolderPath + "/" + fileName);
				
				if(!playlistFile.exists()) {
					playlistFile.createNewFile();
				}
				
				FileWriter fw = new FileWriter(playlistFile);
				try (BufferedWriter bw = new BufferedWriter(fw)) {
					for(Song s : p.getSongs()) {
						bw.write(s.getAbsolutePath().getValue());
						bw.newLine();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
